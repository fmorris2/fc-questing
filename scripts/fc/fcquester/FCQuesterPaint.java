package scripts.fc.fcquester;

import java.awt.Color;

import javax.swing.JFrame;

import org.tribot.api.Timing;
import org.tribot.api2007.Skills.SKILLS;

import scripts.fc.framework.paint.FCDetailedPaint;
import scripts.fc.framework.paint.FCPaintable;

public class FCQuesterPaint extends FCDetailedPaint
{

	public FCQuesterPaint(FCPaintable paintable, JFrame gui, Color color, SKILLS[] skills)
	{
		super(paintable, gui, color, skills);
	}

	@Override
	public Color getRectColor()
	{
		return new Color(36, 114, 232, 60);
	}

	@Override
	public Color getHoverColor()
	{
		return new Color(6, 134, 232);
	}

	@Override
	public String getImageUrl()
	{
		return "http://i.imgur.com/Ka5VYn3.png";
	}

	@Override
	public void resetStatistics()
	{
		startTime = Timing.currentTimeMillis();
		
		FCQuester script = (FCQuester)paintable;
		script.currentQuestStart = Timing.currentTimeMillis();
	}

}
