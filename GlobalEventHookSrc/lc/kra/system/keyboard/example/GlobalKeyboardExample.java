package lc.kra.system.keyboard.example;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class GlobalKeyboardExample {
	private static boolean run = true;

	public static void main(String[] args) {
		// might throw a UnsatisfiedLinkError if the native library fails to
		// load or a RuntimeException if hooking fails
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown.");
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				System.out.println(event);
				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE)
					run = false;
			}

			@Override
			public void keyReleased(GlobalKeyEvent event) {
				System.out.println(event.getVirtualKeyCode() + "," + event.getKeyChar());
			}
		});

		try
		{
			while (run)
				Thread.sleep(128);
		} catch (InterruptedException e)
		{
			/* nothing to do here */ } finally
		{
			keyboardHook.shutdownHook();
		}
	}
}