package scripts.fc.fcquester.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import scripts.fc.fcquester.FCQuester;
import scripts.fc.framework.mission.Mission;
import scripts.fc.framework.quest.QuestMission;
import scripts.fc.missions.fc_druidic_ritual.FCDruidicRitual;
import scripts.fc.missions.fc_ernest_the_chicken.FCErnestTheChicken;
import scripts.fc.missions.fc_knights_sword.FCKnightsSword;
import scripts.fc.missions.fc_prince_ali_rescue.FCPrinceAliRescue;
import scripts.fc.missions.fc_restless_ghost.FCRestlessGhost;
import scripts.fc.missions.fc_witchs_potion.FCWitchsPotion;
import scripts.fc.missions.fccooksassistant.FCCooksAssistant;
import scripts.fc.missions.fcdoricsquest.FCDoricsQuest;
import scripts.fc.missions.fcgoblindiplomacy.FCGoblinDiplomacy;
import scripts.fc.missions.fcimpcatcher.FCImpCatcher;
import scripts.fc.missions.fcromeoandjuliet.FCRomeoAndJuliet;
import scripts.fc.missions.fcrunemysteries.FCRuneMysteries;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fctutorial.FCTutorial;


public class QuestLoader
{
	public static Map<String, Supplier<Mission>> MISSION_MAP = new HashMap<>();
	
	private List<Supplier<Mission>> questSuppliers;
	private Mission[] quests;
	
	public QuestLoader(FCQuester script)
	{
		MISSION_MAP.clear();
		
		questSuppliers = Arrays.asList(
				() -> new FCRestlessGhost(script),
				() -> new FCErnestTheChicken(script),
				() -> new FCKnightsSword(script),
				() -> new FCPrinceAliRescue(script),
				() -> new FCWitchsPotion(script),
				() -> new FCDruidicRitual(script),
				() -> new FCSheepShearer(script), 
				() -> new FCRuneMysteries(script),
				() -> new FCRomeoAndJuliet(script),
				() -> new FCCooksAssistant(script),
				() -> new FCDoricsQuest(script),
				() -> new FCGoblinDiplomacy(script),
				() -> new FCImpCatcher(script),
				() -> new FCTutorial(script));
		
		quests = questSuppliers.stream()
					.map(s -> s.get())
					.toArray(Mission[]::new);
		
		for(int i = 0; i < quests.length; i++) {
			MISSION_MAP.put(quests[i].toString(), questSuppliers.get(i));
		}
	}
	
	public Mission[] getQuests()
	{
		return Arrays.stream(quests).filter(m -> {
			if(m instanceof QuestMission && !((QuestMission)m).canStart())
				return false;
			
			return !m.hasReachedEndingCondition();
		}).toArray(Mission[]::new);
	}
}
