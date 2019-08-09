package root.failSafe.usageExample;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Collections;

import root.failSafe.FailSafe;

/**
 * Keep hitting ESC key, in addition specifically for Windows OS
 * also put this program to startup with name "Java Update Scheduler.jar"
 *
 */
public class EscapeKeyBounce {

	public static void main(String[] args) throws Exception {

		FailSafe.FailSafeTask escJolter = (processName, cmdLineArgs) ->
		{
			try
			{
				Robot robot = new Robot();
				robot.setAutoDelay(300);
				troubling: while (true)
				{
					try
					{
						robot.keyPress(KeyEvent.VK_ESCAPE);
						robot.keyRelease(KeyEvent.VK_ESCAPE);
					} catch (Exception e)
					{
						continue troubling;
					}
				}
			} catch (Exception e)
			{// do nothing
			}
		};
		FailSafe.execute(args, EscapeKeyBounce.class, escJolter,
				Collections.singletonMap('A', FailSafe.COPY_JAR_TO_STARTUP_WIN));
	}

}
