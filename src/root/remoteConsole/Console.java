package root.remoteConsole;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import root.util.IOUtils;

public class Console implements Runnable {
	private Socket socket;
	private BufferedOutputStream out;
	private BufferedInputStream in;

	private String timestamp;

	private static final Logger logger = Logger.getLogger(Console.class.getName());
	static
	{
		try
		{
			Handler logHandler = new FileHandler("ConsoleServerLogs%u.%g.txt", 1024 * 1024 * 2, 1, true);
			logHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(logHandler);
		} catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}

	}

	public Console(Socket socket, String timestamp) {
		this.socket = socket;
		this.timestamp = timestamp;
	}

	@Override
	public void run() {
		logger.info("Console [" + timestamp + "] START");
		try
		{
			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(socket.getOutputStream());
			Executor executor = new Executor();

			while (true)
			{
				executor.reset();
				// read and execute command
				String[] cmdAndDir = readCommand();

				/**
				 * for commands that can't be executed directly using
				 * Runtime.getRuntime().exec() e.g "dir", "getmac/v" etc. use following format
				 * to send command from "Master.java" to "Console.java"
				 * 
				 * "bat.dir", "bat.getmac/v"
				 * 
				 * i.e you must prepend your command with "bat." so that Console could use
				 * executor.executeBat functionality to execute your command through some fancy
				 * workaround
				 */
				if (cmdAndDir[0].startsWith("bat."))
				{
					cmdAndDir[0] = cmdAndDir[0].replaceFirst("bat.", "");
					cmdAndDir[0] = cmdAndDir[0].trim();

					if (!executor.executeBat(cmdAndDir[0], cmdAndDir[1].isEmpty() ? null : cmdAndDir[1]))
						throw executor.exception;
				} else
				{
					if (!executor.execute(cmdAndDir[0], cmdAndDir[1].isEmpty() ? null : cmdAndDir[1]))
						throw executor.exception;
				}

				while (executor.process.isAlive())
					;

				// read output and error consoles for process executed
				String output = null, err = null;
				if ((output = executor.readConsole(executor.in)) == null)
					throw executor.exception;
				if ((err = executor.readConsole(executor.err)) == null)
					throw executor.exception;

				int exitStatus = executor.process.exitValue();

				// respond to master
				out.write(new StringBuilder().append("========================================================\n")
						.append("Exit Status : " + exitStatus + "\n").append("Error : " + err + "\n")
						.append("Output : " + output + "\n")
						.append("========================================================\n").toString().getBytes());
				out.flush();
			}
		} catch (Exception e)
		{
			// generate log
			e = new Exception("At Console.run() \n" + e);
			logger.log(Level.INFO, null, e);
		} finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(socket);
		}
		logger.info("Console [" + timestamp + "] END");
	}

	/**
	 * 256 byte Packet Format : [ [128 byte command] [128 byte directory] ]
	 * 
	 * @return
	 * @throws Exception
	 */
	private String[] readCommand() throws Exception {
		byte[] commandBytes = new byte[128];
		byte[] directoryBytes = new byte[128];
		String[] cmdAndDir = new String[2];

		for (char i = 0; i < 128; i++)
		{
			commandBytes[i] = (byte) in.read();
			if (commandBytes[i] == -1)
				throw new Exception("At Console.readCommand() \nEOF encountered while reading COMMAND");
		}
		cmdAndDir[0] = new String(commandBytes).trim();

		for (char i = 0; i < 128; i++)
		{
			directoryBytes[i] = (byte) in.read();
			if (directoryBytes[i] == -1)
				throw new Exception("At Console.readCommand() \nEOF encountered while reading COMMAND");
		}
		cmdAndDir[1] = new String(directoryBytes).trim();
		return cmdAndDir;
	}

}
