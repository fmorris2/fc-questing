package scripts.fc.fcquester;

import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.fc.api.generic.FCConditions;
import scripts.fc.api.utils.DebugUtils;
import scripts.fc.fcquester.data.FCQuestingProfile;
import scripts.fc.fcquester.data.QuestLoader;
import scripts.fc.fcquester.gui.FCQuestingGUI;
import scripts.fc.framework.mission.Mission;
import scripts.fc.framework.paint.FCDetailedPaint;
import scripts.fc.framework.paint.FCPaintable;
import scripts.fc.framework.script.FCPremiumScript;
import scripts.fc.framework.statistic_tracking.StatTracking;

@ScriptManifest(
		authors     = { 
		    "Final Calibur",
		}, 
		category    = "Quests", 
		name        = "FC Quester", 
		version     = 1.19, 
		description = "Completes a variety of quests for you.", 
		gameMode    = 1)

public class FCQuester extends FCPremiumScript implements FCPaintable, Painting, Starting, Ending, EventBlockingOverride, Arguments, StatTracking
{		
	public QuestLoader questLoader;
	public boolean allQuests;
	public long currentQuestStart = Timing.currentTimeMillis();
	
	private int questsCompleted = 0;
	private int questPoints = -1;
	
	private FCQuestingGUI GUI;
	private Queue<Map.Entry<String, String>> accountQueue = new LinkedList<>();
	private boolean isUsingArgs;
	private HashMap<String,String> args;
	private Set<String> loggedInAccounts = new HashSet<>();
	
	@Override	
	protected int mainLogic()
	{			
		if(!accountQueue.isEmpty() && Login.getLoginState() != STATE.INGAME && Game.getGameState() != 30) {
			loginCurrentAccountInQueue();
			return 100;
		}
		
		if(!isUsingArgs)
		{
			GUI.init();
			this.paint.gui = GUI.getFrame();
			
			if(!GUI.hasFilledOut)
				return 100;
		}
		
		if(questPoints == -1)
			questPoints = getQuestPoints();
		
		if(currentMission != null && currentMission.hasReachedEndingCondition())
		{
			if(getQuestPoints() > questPoints)
				questsCompleted++;
			
			questPoints = -1;
			currentQuestStart = Timing.currentTimeMillis();
		}
		
		if(getSetMissions().isEmpty() && currentMission == null && !accountQueue.isEmpty()) {
			advanceAccountQueue();
			DebugUtils.debugOnInterval("Moving on to next account in queue...", 2000);
			return 100;
		}
		
		if(Login.getLoginState() == STATE.WELCOMESCREEN && !accountQueue.isEmpty()) {
			Login.login();
		}
			
		return super.mainLogic();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		setLoginBotState(isUsingArgs && accountQueue.isEmpty());
	}
	
	public void onEnd()
	{
		super.onEnd();
		GUI.dispose();
	}
	
	@Override
	protected Queue<Mission> getMissions()
	{
		return new LinkedList<>();
	}

	@Override
	protected String[] scriptSpecificPaint()
	{
		return new String[]{
				"Time ran: " + paint.getTimeRan(), 
				"Current quest time: " + Timing.msToString((Timing.timeFromMark(currentQuestStart))), 
				"Quest points: " + getQuestPoints(), 
				"Missions in queue: " + missions.size(), 
				"Next mission: " + (missions.isEmpty() ? "none" : missions.peek().getMissionName())};
	}
	
	private int getQuestPoints()
	{
		return Game.getSetting(101);
	}
	
	@Override
	public FCDetailedPaint getPaint()
	{
		GUI = new FCQuestingGUI(this);
		return new FCQuesterPaint(this, GUI.getFrame(), Color.GREEN, null);
	}
	
	@Override
	public void passArguments(HashMap<String, String> args)
	{
		this.args = args;
		String arguments = args.getOrDefault("custom_input", args.getOrDefault("autostart", ""));
		if(!arguments.isEmpty()) {
			isUsingArgs = true;
			FCQuestingProfile profile = FCQuestingProfile.get(arguments);
			if(profile != null) {
				GUI.useProfile(profile);
				General.println("Using profile: " + arguments);
			}
			else if(arguments.equals("all"))
				GUI.randomlyAddQuests();
			else if(arguments.equals("7qp"))
				GUI.add7qp();
			else if(arguments.equals("tut-ge"))
				GUI.addTutGe();
			else if(arguments.equals("tut"))
				GUI.addTut();
			else
				isUsingArgs = false;
		}
	}

	@Override
	public String getStatsArgs()
	{
		return "username="+General.getTRiBotUsername()+","
				+ "tableName=fc_questing,"
				+ "runtime="+(getRunningTime()/1000)+","
				+ "quests_completed="+questsCompleted;
	}
	
	private void advanceAccountQueue() {
		if(loggedInAccounts.contains(accountQueue.peek().getKey())) {
			println("Advancing account queue...");
			if(Login.getLoginState() == STATE.WELCOMESCREEN) {
				Login.login();
			}
			if(Login.logout()) {
				accountQueue.poll();
				BANK_OBSERVER.clear();
				if(isUsingArgs) {
					passArguments(args);
				} else {
					GUI.addSelectedQuestsToScript(true);
				}
			}
		} else {
			loginCurrentAccountInQueue();
		}
	}
	
	private boolean loginCurrentAccountInQueue() {
		if(accountQueue.isEmpty()) {
			return false;
		}
		
		Map.Entry<String, String> accountInfo = accountQueue.peek();
		General.println("Attempting to login to next account: " + accountInfo.getKey());
		boolean success = Login.login(accountInfo.getKey(), accountInfo.getValue())
				&& Timing.waitCondition(FCConditions.IN_GAME_CONDITION, 1200);
		
		if(success) {
			loggedInAccounts.add(accountInfo.getKey());
		} else if(isInvalidLoginResponse()) {
			General.println("Invalid account: " + accountInfo.getKey());
			clearLoginResponse();
			loggedInAccounts.add(accountInfo.getKey());
			accountQueue.poll();
		}
		
		return success;
	}
	
	private void clearLoginResponse() {
		Mouse.clickBox(394, 305, 530, 334, 1);
	}
	
	private boolean isInvalidLoginResponse() {
		final String RESP = Login.getLoginResponse();
		
		return RESP != null && (RESP.contains("invalid") || RESP.contains("disabled")
				|| RESP.contains("locked"));
	}
	
	public void addAccountToQueue(String user, String pass) {
		accountQueue.add(new SimpleEntry<>(user, pass));
	}
	
	public int getAccountQueueSize() {
		return accountQueue.size();
	}

}
