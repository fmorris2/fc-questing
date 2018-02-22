package scripts.fc.fcquester.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Queue;

import org.tribot.util.Util;

import scripts.fc.api.utils.Utils;
import scripts.fc.framework.data.Vars;


public class FCQuestingProfile implements Serializable {
	
	public static final String PROFILE_PATH = Utils.getTribotDir() + "/FC_Scripts/FC_Questing/profiles/";
	
	private static final long serialVersionUID = 1L;
	
	public final Queue<QuestMissionWrapper> QUEST_QUEUE;
	public final boolean ABC2_ENABLED;
	
	public FCQuestingProfile(Queue<QuestMissionWrapper> mq, boolean abc2) {
		QUEST_QUEUE = mq;
		ABC2_ENABLED = abc2;
	}
	
	public static FCQuestingProfile get(String profileName) {
		try(FileInputStream fIn = new FileInputStream(PROFILE_PATH + profileName);
				ObjectInputStream oIn = new ObjectInputStream(fIn);) {
				FCQuestingProfile profile = (FCQuestingProfile)oIn.readObject();
				Vars.get().add("abc2Enabled", profile.ABC2_ENABLED);
				return profile;
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		return null;
	}
	
}
