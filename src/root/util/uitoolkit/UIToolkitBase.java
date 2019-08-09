package root.util.uitoolkit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Do not create instance of this UIToolkit, intended to be used as static
 * utilities only
 *
 */
public class UIToolkitBase extends Application {

	private static class DisposeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	private static volatile boolean isInitalized = false;
	private static final CountDownLatch initLatch = new CountDownLatch(1);
	private static final BlockingQueue<Runnable> uiElements = new LinkedTransferQueue<>();

	/**
	 * Initialize this UIToolkit, internally it Launch a standalone JavaFX
	 * application
	 * 
	 * @throws InterruptedException
	 */
	private static void initUIToolkit() throws InterruptedException {
		Thread launcherThread = new Thread(() ->
		{
			// Must be invoked from non daemon thread
			Application.launch(UIToolkitBase.class);
		});
		launcherThread.setName("UIToolkit.initUIToolkit.launcherThread");
		launcherThread.start();
		initLatch.await();
		isInitalized = true;
	}

	/**
	 * Shutdown this UIToolkit, terminates internal JavaFX application
	 */
	public static void dispose() {
		UIToolkitBase.submit(() ->
		{
			throw new DisposeException();
		});
	}

	/**
	 * Submit a UI render task in queue.
	 * 
	 * @param renderTask UIRender Task e.g. Display some UI component like alert,
	 *                   window.
	 */
	public static void submit(Runnable renderTask) {
		try
		{
			if (!isInitalized)
				initUIToolkit();
			uiElements.put(renderTask);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * For internal use only, not to be called by API user
	 */
	@Override
	public void init() {
		initLatch.countDown();
	}

	/**
	 * For internal[Called by javaFX launcher thread] use only, not to be called by
	 * API user
	 */
	@Override
	public void start(Stage stage) throws Exception {
		try
		{
			while (true)
				uiElements.take().run();
		} catch (DisposeException e)
		{
			// do nothing, we throw DisposeException when we intend to shutdown UIToolkit
			// i.e. JavaFX application thread
		}
	}
}
