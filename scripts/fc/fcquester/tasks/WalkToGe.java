package scripts.fc.fcquester.tasks;

import scripts.fc.framework.task.Task;
import scripts.fc.framework.grand_exchange.GEOrder;

public class WalkToGe extends Task
{
	private static final long serialVersionUID = -8416868839874093269L;

	@Override
	public boolean execute()
	{
		return GEOrder.travelToGe();
	}

	@Override
	public boolean shouldExecute()
	{
		return !GEOrder.isInGe();
	}

	@Override
	public String getStatus()
	{
		return "Walk to GE";
	}

}
