package scripts.fc.fcquester.data;

import scripts.fc.fcquester.FCQuester;
import scripts.fc.framework.mission.Mission;
import scripts.fc.missions.fccooksassistant.FCCooksAssistant;
import scripts.fc.missions.fcdoricsquest.FCDoricsQuest;
import scripts.fc.missions.fcromeoandjuliet.FCRomeoAndJuliet;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fctutorial.FCTutorial;

public class QuestLoader
{
	private Mission[] quests;
	
	public QuestLoader(FCQuester script)
	{
		quests = new Mission[]{
				new FCSheepShearer(script), 
				new FCRomeoAndJuliet(script),
				new FCCooksAssistant(script),
				new FCDoricsQuest(script),
				new FCTutorial(script)
		};
	}
	
	public Mission[] getQuests()
	{
		return quests;
	}
}
