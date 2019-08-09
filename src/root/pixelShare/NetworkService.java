package root.pixelShare;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicBoolean;

public interface NetworkService {

	AtomicBoolean DEBUG_ENABLED = new AtomicBoolean(false), INFO_ENABLED = new AtomicBoolean(false);

	Dimension SCREEN_DIMENSION_D = /* new Dimension(2000,1500); */ Toolkit.getDefaultToolkit().getScreenSize();

	// Making such contraption because for some weird reason screen size returned
	// by toolkit makes screen capture around ~40% smaller, so full screen is not
	// captured, NEED TO DEBUG THIS ISSUE FURTHER.
	Rectangle SCREEN_DIMENSION = /* new Rectangle(SCREEN_DIMENSION_D); */ new Rectangle(
			SCREEN_DIMENSION_D.width + (int) (SCREEN_DIMENSION_D.width * 0.7),
			SCREEN_DIMENSION_D.height + (int) (SCREEN_DIMENSION_D.height * 0.7));

	void initService(String ip, int port) throws Exception;

	void startService(String ip, int port) throws Exception;

	void stopService() throws Exception;

	boolean isAlive();

	static void debugLog(Object debugLog) {
		if (DEBUG_ENABLED.get())
			System.out.println(debugLog);
	}

	static void logInfo(Object info) {
		if (INFO_ENABLED.get())
		{
			System.out.println(info);
		}
	}

}
