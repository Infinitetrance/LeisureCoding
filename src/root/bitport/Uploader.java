package root.bitport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

class Uploader extends HBox {
	private static ExecutorService executor;
	private static VBox container;

	private SocketChannel channel;
	private Task<Float> uploader;

	private Label fileName, userName, status;
	private ProgressIndicator pi;

	static
	{
		executor = Executors.newCachedThreadPool();
	}

	Uploader(SocketChannel sc) throws IOException {
		this.channel = sc;
		this.uploader = this.getUploader();

		this.fileName = new Label();
		this.fileName.setFont(new Font(15));
		this.fileName.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");

		this.userName = new Label(sc.getRemoteAddress().toString());
		this.userName.setFont(new Font(15));
		this.userName.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");

		this.status = new Label();
		this.status.setFont(new Font(15));
		this.status.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");

		this.pi = new ProgressIndicator();

		this.uploader.valueProperty().addListener(i ->
		{
			pi.setProgress(this.uploader.getValue());
		});
		this.uploader.messageProperty().addListener(i ->
		{
			this.status.setText(this.uploader.getMessage());
		});

		HBox pi_statusBox = new HBox(status, pi);
		pi_statusBox.setSpacing(20);
		pi_statusBox.setAlignment(Pos.CENTER_RIGHT);
		pi_statusBox.setStyle("-fx-background-color:white");

		VBox file_userBox = new VBox();
		file_userBox.setSpacing(10);
		file_userBox.getChildren().addAll(fileName, userName);
		file_userBox.setAlignment(Pos.CENTER_LEFT);

		this.setAlignment(Pos.CENTER_LEFT);
		this.setStyle("-fx-background-color:white");
		this.setPadding(new Insets(10, 5, 10, 5));
		HBox.setHgrow(pi_statusBox, Priority.ALWAYS);
		this.getChildren().addAll(file_userBox, pi_statusBox);
		// out.println("[new Uploader created]");

		executor.submit(this.uploader);
	}

	private Task<Float> getUploader() {
		Task<Float> uploader = new Task<Float>() {
			@SuppressWarnings("incomplete-switch")
			public Float call() throws ClassNotFoundException, IOException {
				try (ObjectInputStream reader = new ObjectInputStream(channel.socket().getInputStream());
						ObjectOutputStream writer = new ObjectOutputStream(channel.socket().getOutputStream());)
				{
					// err.println("uploader in");
					// err.println("[reader created]");
					// err.println("[writer created]");
					ServiceRequest req = (ServiceRequest) reader.readObject();
					// err.println("[service req received]");

					switch (req.type)
					{
					case DOWNLOAD:
					{
						// err.println("[donwload req received on uploader]");

						if (req.file.exists())
						{
							// err.println("[file exists]");
							{
								final Path FILE = req.file.toPath();

								/*********************** FRONT END ROOT INJECTION ***********************/
								Platform.runLater(() -> fileName.setText(FILE.toString()));

								final long SIZE = Files.size(FILE);
								long loaded = 0;
								final int BUFFER_SIZE;
								if (SIZE < 1024 * 1024 * 5)
									BUFFER_SIZE = (int) SIZE;
								else
									BUFFER_SIZE = 1024 * 1024 * 5;

								ByteBuffer buf = ByteBuffer.allocate((int) (BUFFER_SIZE));
								// tell downloader to be ready for download
								writer.writeObject(new ServiceResponse(ServiceConstants.DOWNLOAD,
										new Integer((int) SIZE), new Integer(BUFFER_SIZE)));
								//
								long startTime = System.currentTimeMillis();
								try (SeekableByteChannel sbc = Files.newByteChannel(FILE, StandardOpenOption.READ))
								{
									sbc.position(0);
									while (loaded != SIZE)
									{
										buf.clear();
										loaded += sbc.read(buf);
										this.updateValue((float) loaded / SIZE);
										buf.flip();
										channel.write(buf);
									}
									// System.err.println(("loaded in
									// "+(System.currentTimeMillis()-startTime)/1000.0)+" seconds");
									// System.err.println("[file loaded]");
								} catch (IOException e)
								{
									e.printStackTrace();
								}
							}

						} else
						{
							writer.writeObject(new ServiceResponse(ServiceConstants.UNAVILABLE, null, null));
							// err.println("[file do not exists and service Response sent]");
						}
					}
					}
					// err.println("[uploader out]");
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					channel.close();
				}
				return 0f;
			}

			@Override
			protected void cancelled() {
				this.updateValue(0.0f);
			}

			@Override
			protected void failed() {
				this.updateMessage("Failed");
			}

			@Override
			protected void succeeded() {
				this.updateValue(1f);
				this.updateMessage("Done");
			}

			@Override
			protected void running() {
				this.updateMessage("Uploading");
			}
		};
		return uploader;
	}

	public static ExecutorService getExecutor() {
		return Uploader.executor;
	}

	public static VBox getContainer() {
		return container;
	}

	public static void setContainer(VBox container) {
		Uploader.container = container;
	}
}