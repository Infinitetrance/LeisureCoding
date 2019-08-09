package root.pixelShare;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.application.Application;
import root.util.IOUtils;

/**
 * PixelShare is screen share utility.
 *
 */
public class PixelShare implements NetworkService {

	private ServerSocketChannel serverListenChannel;
	private Socket servingSocket;

	private ObjectOutputStream servingSocketOutputStream;

	private Thread screenShareTask;

	public PixelShare() {
	}

	public void initService(int serverListenerPort) throws IOException {
		initService(InetAddress.getLocalHost().getHostAddress(), serverListenerPort);
	}

	@Override
	public void initService(String serverIP, int serverListenerPort) throws IOException {
		serverListenChannel = ServerSocketChannel.open();
		InetAddress serverIPAddress = InetAddress.getByName(serverIP);
		serverListenChannel.bind(new InetSocketAddress(serverIPAddress, serverListenerPort));

		screenShareTask = new Thread(createScreenShareRunnable());
		screenShareTask.setName("screenShareTask");
		NetworkService.logInfo(String.format("INFO: server initialized at serverIP: %s, serverListenerPort: %d",
				serverIPAddress, serverListenerPort));
	}

	@Override
	public void startService(String serverIP, int serverListenerPort) throws IOException {
		if (serverListenChannel == null || screenShareTask == null)
		{
			System.err.println("Unable to start server, server not initialized");
			return;
		}
		NetworkService.logInfo("INFO: server started, awating client to connect");

		servingSocket = serverListenChannel.accept().socket();
		servingSocketOutputStream = new ObjectOutputStream(servingSocket.getOutputStream());
		screenShareTask.start();
		NetworkService.logInfo(
				String.format("INFO: server-client connection establised.\nClient IP: %s, Client Host Name: %s\n",
						servingSocket.getInetAddress().getHostAddress(), servingSocket.getInetAddress().getHostName()));
	}

	@Override
	public void stopService() throws InterruptedException {
		if (screenShareTask == null)
		{
			System.err.println("Unable to stop server, server not initialized/ started");
			return;
		}
		screenShareTask.interrupt();
		NetworkService.logInfo("INFO: server stop requested");
		Thread.sleep(200);
	}

	@Override
	public boolean isAlive() {
		return screenShareTask == null ? false : screenShareTask.isAlive();
	}

	private void disposeServer() {
		IOUtils.closeQuietly(servingSocketOutputStream);
		IOUtils.closeQuietly(servingSocket);
		IOUtils.closeQuietly(serverListenChannel);
		NetworkService.logInfo("INFO: server disposed");
	}

	private Runnable createScreenShareRunnable() {
		return () ->
		{
			try
			{
				final short FPS = 5;
				final short REFRESH_RATE = 1000 / FPS;

				final Robot ROBOT = new Robot();

				BufferedImage capturedScreen;
				ByteArrayOutputStream capturedScreenBytes;

				// capturedScreen = ROBOT.createScreenCapture(SCREEN_DIMENSION); // DEBUG:
				// Performance [Time Consumed 30ms]
				// capturedScreenBytes = new ByteArrayOutputStream(); // DEBUG: Performance
				// ImageIO.write(capturedScreen, "png", capturedScreenBytes); // DEBUG:
				// Performance [Time Consumed 150ms]

				while (!Thread.interrupted())
				{
					capturedScreen = ROBOT.createScreenCapture(SCREEN_DIMENSION);

					capturedScreenBytes = new ByteArrayOutputStream();
					ImageIO.write(capturedScreen, "png", capturedScreenBytes);

					servingSocketOutputStream.writeObject(new Data(capturedScreenBytes.toByteArray()));

					Thread.sleep(REFRESH_RATE);
				} // End of while
			} // End of try
			catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				disposeServer();
			}
		};
	}

	public static void main(String[] args) throws Exception {
		NetworkService.DEBUG_ENABLED.set(false);
		NetworkService.INFO_ENABLED.set(true);

		String mode = null, ip = null;
		int port = 0;

		System.out.println("<<<<<<Infinity Labs ∞>>>>>>");
		if (args.length == 0)
		{
			System.out.println("Usage:                                       \n"
					+ "java PixelShare -CLIENT Server_IP Server_Port         \n"
					+ "(To see screen shared from server)\n\n"

					+ "java PixelShare -SERVER Server_IP(Your Machine's IP) Server_Port(Your Machine's port listening for client request)\n"
					+ "(To share your screen with remote client)\n\n"

					+ "Your Machine's IP: " + InetAddress.getLocalHost());
			System.exit(0);
		} else if (args.length == 3)
		{
			mode = args[0].toUpperCase();
			ip = args[1];
			port = Integer.parseInt(args[2]);
		} else
		{
			System.err.println("Invalid arguments! Check usage");
			System.exit(0);
		}

		NetworkService.debugLog("Detected screen size: " + SCREEN_DIMENSION);
		NetworkService networkService;

		switch (mode)
		{
		case "-CLIENT":
			Application.launch(PixelShareClient.class, args);
			NetworkService.debugLog("[DEBUG: client case: out]");
			break;

		case "-SERVER":
			networkService = new PixelShare();
			networkService.initService(ip, port);
			networkService.startService(ip, port);

			System.out.println("Input 11 to terminate server");
			try (Scanner in = new Scanner(System.in))
			{
				while (in.nextInt() != 11)
					;
				if (networkService.isAlive())
					networkService.stopService();
			}
			NetworkService.debugLog("[DEBUG: server case: out]");
			break;
		}

		NetworkService.debugLog("[DEBUG: END OF main]");
	}

}
