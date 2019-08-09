package root.failSafe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

import root.failSafe.FailSafe.FailSafeTask;
import root.failSafe.FailSafe.NetworkService;
import root.util.IOUtils;

/**
 * Unit Tests for FailSafe
 */
public class FailSafeTest {

	public static void main(String[] args) throws Exception {
		// testKeepHosted();
		testExecute(args);
	}

	/**
	 * Unit test for FailSafe.keepHosted
	 * 
	 * @throws IOException
	 */
	static void testKeepHosted() throws IOException {
		FailSafe.configLogger("localhost", 9191);
		FailSafe.logger.setLevel(Level.FINER);

		NetworkService echoService = (hostIP, hostPort) ->
		{
			FailSafe.logger.log(Level.FINER, "START echoService");
			Socket socket = null;
			Scanner in = null;
			PrintWriter out = null;
			try (ServerSocket server = new ServerSocket(hostPort, 3, InetAddress.getByName(hostIP)))
			{
				socket = server.accept();

				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);
				while (in.hasNextLine())
				{
					String msg = in.nextLine();
					FailSafe.logger.info("echoService msg: " + msg);
					out.println(msg);
				}

			} catch (IOException e)
			{
				boolean isSocketException = e instanceof SocketException;
				FailSafe.logger.log(Level.SEVERE, "echoService isSocketException: " + isSocketException, e);
				if (isSocketException)
					throw new SocketException(e.toString());
			} finally
			{
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(socket);
				FailSafe.logger.log(Level.FINER, "END echoService");
			}
		};

		FailSafe.keepHosted("slc12eof.us.ora.com", 6600, 5051, "echoService", 8051, echoService);
	}

	/**
	 * Unit test for FailSafe.execute
	 * 
	 * @param args
	 * @throws Exception
	 */
	static void testExecute(String[] args) throws Exception {
		OutputStream err = new FileOutputStream("system.err.txt");
		PrintStream printErr = new PrintStream(err, true);
		// To stop flooding err stream because we are checking logs at remote socket
		System.setErr(printErr);

		FailSafe.configLogger("localhost", 9191);
		FailSafe.logger.setLevel(Level.FINER);

		FailSafeTask timeCast = (enclosingProcessName, cmdLineArgs) ->
		{
			FailSafe.logger.log(Level.FINER, "ProcessName: " + enclosingProcessName + ", START timeCast");
			FailSafe.logger.log(Level.INFO,
					"ProcessName: " + enclosingProcessName + ",timeCast cmdLineArgs: " + Arrays.toString(cmdLineArgs));
			try
			{
				String txIP;
				int txPort;
				if (cmdLineArgs.length == 0)
				{
					txIP = "localhost";
					txPort = 8051;
				} else
				{
					txIP = cmdLineArgs[0];
					txPort = Integer.valueOf(cmdLineArgs[1]);
				}

				Consumer<String> out = IOUtils.getUDPWriter(txIP, txPort);
				while (true)
				{
					out.accept("ProcessName: " + enclosingProcessName + ", Time.now: " + ZonedDateTime.now().toString()
							+ "\n");
					Thread.sleep(5000);
				}
			} catch (Exception e)
			{
				FailSafe.logger.log(Level.SEVERE, "timeCast", e);
			}
			FailSafe.logger.log(Level.FINER, "ProcessName: " + enclosingProcessName + ", END timeCast");
		};

		FailSafeTask hostAddressCast = (enclosingProcessName, cmdLineArgs) ->
		{
			FailSafe.logger.log(Level.FINER, "ProcessName: " + enclosingProcessName + ", START hostAddressCast");
			FailSafe.logger.log(Level.INFO, "ProcessName: " + enclosingProcessName + ",hostAddressCast cmdLineArgs: "
					+ Arrays.toString(cmdLineArgs));
			try
			{
				Consumer<String> out = IOUtils.getUDPWriter("localhost", 6001);
				String hostAddress = InetAddress.getLocalHost().toString();
				while (true)
				{
					out.accept(hostAddress + "\n");
					Thread.sleep(5000);
				}
			} catch (Exception e)
			{
				FailSafe.logger.log(Level.SEVERE, "hostAddressCast", e);
			}
			FailSafe.logger.log(Level.FINER, "ProcessName: " + enclosingProcessName + ", END hostAddressCast");
		};

		FailSafeTask greetingsCast = (enclosingProcessName, cmdLineArgs) ->
		{
			FailSafe.logger.log(Level.FINER, "ProcessName: " + enclosingProcessName + ", START greetingsCast");
			FailSafe.logger.log(Level.INFO, "ProcessName: " + enclosingProcessName + ",greetingsCast cmdLineArgs: "
					+ Arrays.toString(cmdLineArgs));
			try
			{
				Consumer<String> out = IOUtils.getUDPWriter("localhost", 6003);
				String greetings = "Hello, I am " + System.getProperty("user.name");
				while (true)
				{
					out.accept(greetings + "\n");
					Thread.sleep(5000);
				}
			} catch (Exception e)
			{
				FailSafe.logger.log(Level.SEVERE, "greetingsCast", e);
			}
			FailSafe.logger.log(Level.FINER, "ProcessName: " + enclosingProcessName + ", END greetingsCast");
		};

		Map<Character, FailSafeTask> failSafeTaskGenerationSpecificMap = new HashMap<>();
		failSafeTaskGenerationSpecificMap.put('A', hostAddressCast);
		failSafeTaskGenerationSpecificMap.put('C', greetingsCast);

		FailSafe.execute(args, MethodHandles.lookup().lookupClass(), timeCast, failSafeTaskGenerationSpecificMap);
	}

}
