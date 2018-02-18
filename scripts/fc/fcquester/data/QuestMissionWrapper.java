package scripts.fc.fcquester.data;

import java.io.Serializable;

import scripts.fc.framework.mission.Mission;

public class QuestMissionWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String MISSION_IDENTIFIER;
	private transient Mission mission;
	
	public QuestMissionWrapper(String identifier) {
		MISSION_IDENTIFIER = identifier;
	}
	
	public Mission getMission() {
		return mission == null ? QuestLoader.MISSION_MAP.get(MISSION_IDENTIFIER).get() : mission;
	}
	
	public void resetMission() {
		mission = null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestMissionWrapper other = (QuestMissionWrapper) obj;
		if (getMission() == null) {
			if (other.getMission() != null)
				return false;
		} else if (!getMission().toString().equals(other.getMission().toString()))
			return false;
		return true;
	}
	
	public String toString() {
		return getMission().toString();
	}
}
