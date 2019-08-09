package root.pixelShare;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import root.util.IOUtils;

public class PixelShareClient extends Application implements NetworkService {

	private Scene scene;
	private ImageView imageView;

	private SocketChannel clientChannel;
	private Socket clientSocket;

	private ObjectInputStream clientSocketInputStream;

	private Thread remoteScreenRenderTask;

	@Override
	public void initService(String serverIP, int serverListenerPort) throws Exception {
		clientChannel = SocketChannel.open();
		remoteScreenRenderTask = new Thread(createRemoteScreenRenderRunnable());
		remoteScreenRenderTask.setName("remoteScreenRenderTask");
		remoteScreenRenderTask.setDaemon(true);

		NetworkService.logInfo("INFO: client initialized");
	}

	@Override
	public void startService(String serverIP, int serverListenerPort) throws Exception {
		if (clientChannel == null || remoteScreenRenderTask == null)
		{
			System.err.println("Unable to start client, client not initialized");
			return;
		}
		NetworkService.logInfo("INFO: awating client to connect server");

		clientChannel.connect(new InetSocketAddress(InetAddress.getByName(serverIP), serverListenerPort));
		clientSocket = clientChannel.socket();
		clientSocketInputStream = new ObjectInputStream(clientSocket.getInputStream());

		NetworkService.logInfo(String.format(
				"INFO: client-server connection establised.\n"
						+ "Server IP: %s, Server Name: %s, serverListenPort: %d\n"
						+ "Client IP: %s, Client Name: %s, Client Local Port: %d\n",
				clientSocket.getInetAddress().getHostAddress(), clientSocket.getInetAddress().getHostName(),
				clientSocket.getPort(), clientSocket.getLocalAddress().getHostAddress(),
				clientSocket.getLocalAddress().getHostName(), clientSocket.getLocalPort()));
	}

	private Runnable createRemoteScreenRenderRunnable() {
		return () ->
		{
			NetworkService.debugLog("[DEBUG: remoteScreenRenderTask RUNNING]");
			try
			{
				Data data = null;

				// long prev; // DEBUG: Performance

				while (!Thread.interrupted())
				{

					// prev = System.currentTimeMillis(); // DEBUG: Performance

					data = (Data) clientSocketInputStream.readObject();

					// System.out.println(
					// "Elapsed milliSecs since last networkRead is " + (System.currentTimeMillis()
					// - prev)); // DEBUG:
					// Performance

					BufferedImage remoteScreenImage = ImageIO.read(new ByteArrayInputStream(data.data));
					Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(remoteScreenImage, null)));

				} // END of while
			} // END of try
			catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				disposeClient();
			}
		};
	}

	private void disposeClient() {
		IOUtils.closeQuietly(clientSocketInputStream);
		IOUtils.closeQuietly(clientSocket);
		IOUtils.closeQuietly(clientChannel);
		NetworkService.logInfo("INFO: client disposed");
	}

	@Override
	public void stopService() throws InterruptedException {
		if (remoteScreenRenderTask == null)
		{
			System.err.println("Unable to stop client, client not initialized/ started");
			return;
		}
		remoteScreenRenderTask.interrupt();
		NetworkService.logInfo("INFO: client stop requested");
		Thread.sleep(200);
	}

	@Override
	public boolean isAlive() {
		return remoteScreenRenderTask == null ? false : remoteScreenRenderTask.isAlive();
	}

	@Override
	public void start(Stage stage) throws Exception {
		imageView = new ImageView();
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);

		Group root = new Group();
		root.setId("#PixelShareClientRoot");
		root.getChildren().add(imageView);

		scene = new Scene(root);
		scene.setOnKeyReleased((KeyEvent event) ->
		{
			if (event.getCode() == KeyCode.ENTER)
				stage.setFullScreen(!stage.isFullScreen());
		});

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.setTitle("PixelShare.CLIENT");

		List<String> cmdLineArgs = getParameters().getRaw();
		String serverIP = cmdLineArgs.get(1);
		int serverListenerPort = Integer.parseInt(cmdLineArgs.get(2));

		initService(serverIP, serverListenerPort);

		stage.show();
		NetworkService.debugLog("[DEBUG: PixelShareClient Application/Stage initialized/LIVE]");

		startService(serverIP, serverListenerPort);
		remoteScreenRenderTask.start();
	}

	@Override
	public void stop() throws InterruptedException {
		NetworkService.debugLog("[DEBUG: PixelShareClient.stop()]");

		if (isAlive())
			stopService();
	}
}
