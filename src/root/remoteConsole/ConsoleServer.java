package root.remoteConsole;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import root.util.IOUtils;

public class ConsoleServer {

	public static final Logger logger = Logger.getLogger(ConsoleServer.class.getName());

	public static void main(String[] args) {

		ServerSocket server = null;
		ExecutorService pool = null;

		// local machine
		String IPAddress = null;
		final String THING_NAME = "Q3" + "VMJ" + "TMJ" + "3M99" + "RF9" + "CVP" + "J3Q" + "7VF3";

		SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
		try
		{
			Handler logHandler = new FileHandler("ConsoleServerLogs%u.%g.txt", 1024 * 1024 * 2, 1, true);
			logHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(logHandler);

			{
				// reporting master for IP address and service UP time
				IPAddress = InetAddress.getLocalHost().toString();
				String serviceUPTimestamp = dateFormater.format(new Date()).replace(" ", "-");
				String url = "http://dweet.io/dweet/for/" + THING_NAME + "?REPORT=[[serviceUPTimestamp:"
						+ serviceUPTimestamp + "][IPAddress:" + IPAddress + "]]";
				// to test if it can hit Internet
				// url="https://www.google.co.in/";
				int responseCode = ((HttpURLConnection) new URL(url).openConnection()).getResponseCode();
				if (responseCode != 200)
					throw new RuntimeException("Problem Connecting Report Center ErrorCode[" + responseCode + "]");
			}

			server = new ServerSocket(8051, 10);
			pool = Executors.newFixedThreadPool(10);

			while (true)
			{
				pool.execute(new Console(server.accept(), dateFormater.format(new Date())));
			}
		} catch (Exception e)
		{
			logger.log(Level.INFO, "At ConsoleServer", e);
		} finally
		{
			IOUtils.closeQuietly(server);
			if (pool != null)
				pool.shutdown();
		}
	}

}
