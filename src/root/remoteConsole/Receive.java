package root.remoteConsole;

import java.net.ServerSocket;

import root.util.IOUtils;

/**
 * Utility to receive a file
 */
public class Receive {

	public static void main(String[] args) {
		if (args.length == 0 || args.length > 3)
		{
			System.out.println("Usage: java Receive [-options] PortNumber [DirectoryPath]");
			System.out.println("where options include:");
			System.out.println("   -showProgress   print data transfer progress");
			return;
		}

		int portNumber;
		String directoryPath;
		boolean showProgress;
		ServerSocket server = null;
		try
		{
			if (args[0].equalsIgnoreCase("-showProgress"))
			{
				showProgress = true;
				portNumber = Integer.parseInt(args[1]);
				if (args.length == 3)
					directoryPath = args[2];
				else
					directoryPath = System.getProperty("user.dir");
			} else
			{
				showProgress = false;
				portNumber = Integer.parseInt(args[0]);
				if (args.length == 2)
					directoryPath = args[1];
				else
					directoryPath = System.getProperty("user.dir");
			}

			server = new ServerSocket(portNumber);
			Result exitStatus = FTP.read(server.accept(), directoryPath, showProgress);
			System.out.println(exitStatus);

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(server);
		}

	}

}
