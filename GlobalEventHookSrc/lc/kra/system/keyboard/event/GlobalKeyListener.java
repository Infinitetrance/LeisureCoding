
package lc.kra.system.keyboard.event;

import java.util.EventListener;
import java.util.function.Consumer;

public interface GlobalKeyListener extends EventListener {
	/**
	 * Invoked when a key has been pressed.
	 */
	void keyPressed(GlobalKeyEvent event);

	/**
	 * Invoked when a key has been released.
	 */
	void keyReleased(GlobalKeyEvent event);

	static GlobalKeyListener createKeyListener(Consumer<GlobalKeyEvent> press, Consumer<GlobalKeyEvent> release) {

		return new GlobalKeyListener() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				press.accept(event);
			}

			@Override
			public void keyReleased(GlobalKeyEvent event) {
				release.accept(event);
			}
		};
	}
}