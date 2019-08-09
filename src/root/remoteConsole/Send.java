package root.remoteConsole;

import java.net.InetAddress;
import java.net.Socket;

import root.util.IOUtils;

/**
 * Utility to send a file
 */
public class Send {

	public static void main(String[] args) {
		if (args.length == 0 || args.length > 4)
		{
			System.out.println("Usage: java Send [-options] HostName PortNumber FilePath");
			System.out.println("where options include:");
			System.out.println("   -showProgress   print data transfer progress");
			return;
		}

		boolean showProgress;
		String hostName;
		int portNumber;
		String filePath;

		Socket socket = null;

		try
		{
			if (args[0].equalsIgnoreCase("-showProgress"))
			{
				showProgress = true;
				hostName = args[1];
				portNumber = Integer.parseInt(args[2]);
				filePath = args[3];

			} else
			{
				showProgress = false;
				hostName = args[0];
				portNumber = Integer.parseInt(args[1]);
				filePath = args[2];

			}

			socket = new Socket(InetAddress.getByName(hostName), portNumber);
			Result exitStatus = FTP.send(socket, filePath, showProgress);
			System.out.println(exitStatus);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(socket);
		}

	}

}
