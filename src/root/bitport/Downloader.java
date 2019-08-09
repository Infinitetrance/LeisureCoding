package root.bitport;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import static java.lang.System.*;
public class Downloader extends HBox
{
	private static ScheduledExecutorService executor;
	//to open and close side content pane acting as downloading panel
	public static TranslateTransition openDownloadingPanel,closeDownloadingPanel;
	//a reference to side content pane of download page  
	private static ContentPane downloadingPanel;
	//VBox on scrollpane in side content pane of download page  to which Downloader's GUI node are to added   
	private static VBox container;
	//Initial translateX of side content pane of download page
	private static	double closedTransaltX;
	
	/**
	 * currently on going downloads so as to paint downloading panel
	 */
	private static List<Downloader> downloading;
	//denotes whether side content pane is opened by downloader or not 
	public static boolean isOpened;
	
	private Label name;
	private Label status;
	private ProgressIndicator pi;
	private Button cancel;
	
	private File locator;
	//time consumed to download
	private long time;
	private Task<Float> downloader;
	
	private SocketChannel channel;
	//user ip from where to download
	private InetAddress src;
	static{
		isOpened=false;
		executor=Executors.newScheduledThreadPool(5);
		downloading=new ArrayList<Downloader>(20);
	}
	public Downloader(String name,File locator,InetAddress src)
	{
		try
		{
			this.channel=SocketChannel.open();
			/**
			 * no need to bind now initially done for testing on same system
			 */
			//this.channel.bind(new InetSocketAddress(BackEndRootx.myip,0));////////////////////////////////////////////////////////////////////////////////////////////////
		}
		catch(IOException e)
		{
			err.println("SocketChannel creation malfunction at downloader");
			e.printStackTrace();
		}
		
		this.src=src;
		
		this.name=new Label(name);
		this.name.setFont(new Font(15));
		this.name.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		
		this.status=new Label();
		this.status.setFont(new Font(15));
		this.status.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		
		Circle cancelIcon=new Circle(8);
		cancelIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0,Color.ORANGE),new Stop(0.3,Color.CHOCOLATE)}));
		this.cancel=new Button("",cancelIcon);
		this.cancel.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		this.cancel.setOnAction(cancelAction->{
			downloader.cancel();
		//	System.out.println("Downloader cancelAction name::"+this.name.getText());
		});
		
		this.pi=new ProgressIndicator();
		this.locator=locator;
		
		this.downloader=this.getDownloader();
		this.downloader.valueProperty().addListener(i->{
			pi.setProgress(this.downloader.getValue());
		});
		this.downloader.messageProperty().addListener(i->{
			this.status.setText(this.downloader.getMessage());
		});
		
		HBox pi_cancelBox=new HBox(status,pi,cancel);
		pi_cancelBox.setSpacing(20);
		HBox.setMargin(this.cancel,new Insets(0,0,20,5));
		pi_cancelBox.setAlignment(Pos.CENTER_RIGHT);
		pi_cancelBox.setStyle("-fx-background-color:white");
		
		this.setAlignment(Pos.CENTER_LEFT);
		this.setStyle("-fx-background-color:white");
		this.setPadding(new Insets(10,5,10,5));
		HBox.setHgrow(pi_cancelBox, Priority.ALWAYS);
		this.getChildren().addAll(this.name,pi_cancelBox);
		
		executor.submit(this.downloader);
	}
	private Task<Float> getDownloader()
	{
		Task<Float> downloader=new Task<Float>()
		{
			@SuppressWarnings("incomplete-switch")
			protected Float call() throws IOException, ClassNotFoundException
			{
				//err.println("[downloader in]");

				channel.connect(new InetSocketAddress(src,BackEndRootx.TCPRx_PORT));
			//	err.println("[downloader connected to uploader]");
			
				try(ObjectOutputStream writer=new ObjectOutputStream(channel.socket().getOutputStream());
					ObjectInputStream reader=new ObjectInputStream(channel.socket().getInputStream());	)
				{					
			//		err.println("[Writer created]");
			//		err.println("[reader created]");
				
					//send service request
					writer.writeObject(new ServiceRequest(ServiceConstants.DOWNLOAD,locator));
			//		err.println("[service req sent]");
					//get service response*/
					ServiceResponse respo=(ServiceResponse)reader.readObject();
					switch(respo.type)
					{
						case DOWNLOAD:{
			//				err.println("<ready to download>");
							
							//downloaded files will be stored at current directory
							final Path TARGET_DIR=Paths.get(".").toRealPath(); 
							final Path FILE=locator.toPath();
							final long SIZE=(Integer)respo.data0;
							final int BUFFER_SIZE=(Integer)respo.data1;
							long downloaded=0;
							ByteBuffer buf=ByteBuffer.allocate((int) (BUFFER_SIZE));
							try(SeekableByteChannel sbc=Files.newByteChannel(TARGET_DIR.resolve(FILE.getFileName()),StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE)									)
							{
								sbc.position(0);
								time=System.currentTimeMillis();
								while(downloaded!=SIZE)
								{
									buf.clear();
									downloaded+=channel.read(buf);
									this.updateValue((float)downloaded/SIZE);
									buf.flip();
									sbc.write(buf);										
								}
								time=System.currentTimeMillis()-time;
					//			System.out.println(("Downloaded in "+(System.currentTimeMillis()-startTime)/1000.0)+" seconds");
					//			System.out.println("<file unloaded>");
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
						}
						break;
						case CANCELED:{
							this.updateMessage("Cancelled");
							this.cancel(true);
						}
						break;
						case UNAVILABLE:{
							//err.println("UNAVILABLE");
							this.updateMessage("UNAVILABLE");
							this.cancel(true);
						}							
					}
			//		err.println("[downloader out]");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					channel.close();
				}
				return 0f;
			}
			@Override
			protected void cancelled()
			{
				this.updateValue(0.0f);
			}
			@Override
			protected void failed()
			{
				this.updateMessage("Failed");
			}
			@Override
			protected void succeeded()
			{
				this.updateValue(1f);
				this.updateMessage("Done["+(time/1000.0)+" sec]");
			}
			@Override
			protected void running()
			{
				this.updateMessage("Downloading");
			}
		};
		return downloader;
	}	
	public static ScheduledExecutorService getExecutor()
	{
		return Downloader.executor;
	}
	public static List<Downloader> getDownloading() {
		return downloading;
	}
	public static void setDownloadingPanel(ContentPane downloadingPanel) {
		Downloader.downloadingPanel = downloadingPanel;
		Downloader.closedTransaltX=downloadingPanel.getTranslateX();
		
		openDownloadingPanel=new TranslateTransition();
		openDownloadingPanel.setToX(100);
		openDownloadingPanel.setDuration(new Duration(500));
		openDownloadingPanel.setNode(Downloader.downloadingPanel);
		
		closeDownloadingPanel=new TranslateTransition();
		closeDownloadingPanel.setToX(Downloader.closedTransaltX);
		closeDownloadingPanel.setDuration(new Duration(500));
		closeDownloadingPanel.setNode(Downloader.downloadingPanel);
		
	}
	public static VBox getContainer() {
		return container;
	}
	public static void setContainer(VBox container) {
		Downloader.container = container;
	}
}
