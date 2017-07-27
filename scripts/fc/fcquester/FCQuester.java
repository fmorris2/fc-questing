package scripts.fc.fcquester;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

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
		version     = 1.01, 
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
	
	@Override	
	protected int mainLogic()
	{
		if(!GUI.hasFilledOut && Login.getLoginState() != STATE.INGAME && Game.getGameState() != 30)
		{
			println("Waiting for login before displaying GUI...");
			return 1000;
		}
		
		GUI.init();
		this.paint.gui = GUI.getFrame();
		
		if(!GUI.hasFilledOut)
			return 100;
		
		if(questPoints == -1)
			questPoints = getQuestPoints();
		
		if(currentMission != null && currentMission.hasReachedEndingCondition())
		{
			if(getQuestPoints() > questPoints)
				questsCompleted++;
			
			questPoints = -1;
			currentQuestStart = Timing.currentTimeMillis();
		}
			
		return super.mainLogic();
	}

	@Override
	public void onStart()
	{
		super.onStart();
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
		String arguments = args.get("custom_input");
		if(arguments.equals("all"))
			GUI.randomlyAddQuests();
		else if(arguments.equals("7qp"))
			GUI.add7qp();
		else if(arguments.equals("tut-ge"))
			GUI.addTutGe();
		else if(arguments.equals("tut"))
			GUI.addTut();
	}

	@Override
	public String getStatsArgs()
	{
		return "username="+General.getTRiBotUsername()+","
				+ "tableName=fc_questing,"
				+ "runtime="+(getRunningTime()/1000)+","
				+ "quests_completed="+questsCompleted;
	}

}
