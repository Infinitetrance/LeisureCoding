package root.bitport;

import static java.lang.System.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

class BackEndRootx {
	public static int MULTICAST_PORT, DCRX_PORT, HEARTBEAT_DEALY, HEART_MONITOR_DELAY, EXECUTOR_SIZE,
			HEARTBEAT_TUNNEL_SIZE, TCPRx_PORT;
	private static InetAddress MULTICAST_GROUP_IP, DCRX_IP;
	private static InetSocketAddress MULTICAST_SOCKET_ADDRESS;

	/**
	 * for module testing
	 */
	public static String myip = "localhost";

	MulticastSocket ms;
	DatagramChannel dcRx, dcTx;
	ServerSocketChannel TCPRx;

	Timeline heart, heartMonitor, heartAttack;

	static Heartbeat HEARTBEAT;
	static Relation RELATION_PACKET;

	/**
	 * the front end root beast
	 */
	Root FER;

	ArrayBlockingQueue<Heartbeat> heartbeatTunnel;

	ScheduledExecutorService executor;

	List<Relation> family;

	private Task<Void> dcRxTask, TCPRxTask;

	static
	{
		try
		{
			myip = "127.0.0.1";//InetAddress.getLocalHost().getHostAddress();
			MULTICAST_PORT = 16000;
			MULTICAST_GROUP_IP = InetAddress.getByName("224.0.0.9");
			MULTICAST_SOCKET_ADDRESS = new InetSocketAddress(MULTICAST_GROUP_IP, MULTICAST_PORT);

			DCRX_PORT = 16003;
			TCPRx_PORT = 16004;
			/**
			 * to test module on same system bind different instance to different IP
			 * value=ip1
			 */
			DCRX_IP = InetAddress.getByName(myip);
			System.out.println(DCRX_IP);

			HEARTBEAT_DEALY = 2000;
			HEART_MONITOR_DELAY = 5000;

			EXECUTOR_SIZE = 4;

			HEARTBEAT_TUNNEL_SIZE = 25;

			HEARTBEAT = new Heartbeat("BitPort", "Infinity Labs∞ Kuldeep Singh");
			/**
			 * for module testing changing IP of heartbeat
			 */
			HEARTBEAT.address = InetAddress.getByName(myip);
			// null should be replaced with list of shared files by this user
			RELATION_PACKET = new Relation(HEARTBEAT, new ArrayList<File>());
			/**
			 * setting realtion's source ip for module testing value =ip1
			 */
			RELATION_PACKET.address = InetAddress.getByName(myip);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	BackEndRootx(Root frontend) throws IOException {
		this.FER = frontend;

		ms = new MulticastSocket(MULTICAST_PORT);
		ms.joinGroup(MULTICAST_GROUP_IP);

		dcRx = DatagramChannel.open();
		dcRx.bind(new InetSocketAddress(DCRX_IP, DCRX_PORT));

		dcTx = DatagramChannel.open();
		/**
		 * to test module on same system bind different run to different IP
		 */
		dcTx.bind(new InetSocketAddress(InetAddress.getByName(myip), 10000));

		TCPRx = ServerSocketChannel.open();
		TCPRx.bind(new InetSocketAddress(myip, TCPRx_PORT));

		family = new ArrayList<Relation>();

		executor = Executors.newScheduledThreadPool(EXECUTOR_SIZE);

		heartbeatTunnel = new ArrayBlockingQueue<>(HEARTBEAT_TUNNEL_SIZE);

		heart = new Timeline();
		KeyFrame frame = new KeyFrame(new Duration(HEARTBEAT_DEALY), new EventHandler<ActionEvent>() {
			private ByteArrayOutputStream baos;
			{
				baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(HEARTBEAT);
			}

			@Override
			public void handle(ActionEvent arg0) {
				try
				{
					dcTx.send(ByteBuffer.wrap(baos.toByteArray()), MULTICAST_SOCKET_ADDRESS);
					out.println("<beat sent>");
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		heart.getKeyFrames().add(frame);
		heart.setCycleCount(Animation.INDEFINITE);

		heartMonitor = new Timeline();
		KeyFrame frame1 = new KeyFrame(new Duration(HEART_MONITOR_DELAY), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent action) {
				int size;
				{
					/**
					 * just for cross check remove this block at time of deployment
					 */
					size = family.size();
					err.println("[♥♥♥♥♥♥ MONITOR PHASE PRE ANYALYSIS REPORT]" + System.currentTimeMillis());
					for (int i = 0; i < size; i++)
					{
						err.println("[♥♥♥♥♥♥ found in family" + family.get(i).address + "]");
					}
					err.println("[♥♥♥♥♥♥ MONITOR PHASE PRE ANYALYSIS REPORT OVER]");
				}
				{////////////////////////////////////////////////////////////////////////
					size = family.size();
					for (int i = 0; i < size; i++)
					{
						Relation r = family.get(i);
						if (r.isAlive())
							r.setIsAlive(false);
						else if (!r.isAlive())
						{
							family.remove(i);
							/*****************************************
							 * FRONT END ROOT INJECTION
							 *********************************************/
							FER.downloadPanelMainRoot.getChildren().remove(i);

							i--;
							size--;
						}
					}
				} ////////////////////////////////////////////////////////////////////////
				{
					/**
					 * just for cross check remove this block at time of deployment
					 */
					size = family.size();
					err.println("[♥♥♥♥♥♥ MONITOR PHASE STARTED]");
					for (int i = 0; i < size; i++)
					{
						err.println("[♥♥♥♥♥♥ OK" + family.get(i).address + "]");
					}
					err.println("[♥♥♥♥♥♥ MONITOR PHASE OVER]" + System.currentTimeMillis());
				}
			}
		});
		heartMonitor.getKeyFrames().add(frame1);
		heartMonitor.setCycleCount(Animation.INDEFINITE);

		heartAttack = new Timeline();
		KeyFrame frame2 = new KeyFrame(new Duration(HEART_MONITOR_DELAY + 7000), action ->
		{
			err.println("♥♥♥♥♥♥ HEART ATTACK RECOVERED " + System.currentTimeMillis());
			heart.play();
		});
		heartAttack.getKeyFrames().add(frame2);
		heartAttack.setCycleCount(1);
	}

	public void play() {
		executor.submit(configHeartbeatAbsorber());
		executor.submit(TCPRxTask = configTCPRx());
		try
		{
			executor.submit(configHeartbeathandler());
		} catch (IOException e)
		{
			err.println("Heartbeathandler MALFUNCTION");
			e.printStackTrace();
		}
		try
		{
			executor.submit(this.dcRxTask = configDCRX());
		} catch (IOException e)
		{
			err.println("Datagram Channel RX /DCRX MALFUNCTION");
			e.printStackTrace();
		}
		heart.play();
		heartMonitor.play();
	}

	public void stop() {
		heart.stop();
		heartMonitor.stop();
		this.dcRxTask.cancel(true);
		this.TCPRxTask.cancel(true);
		this.executor.shutdownNow();
		this.ms.close();
		try
		{
			this.TCPRx.close();
		} catch (IOException e1)
		{
			err.println("UNABLE TO CLOSE TCPRx");
			e1.printStackTrace();
		}
		try
		{
			this.dcRx.close();
		} catch (IOException e)
		{
			err.println("UNABLE TO CLOSE DCRX CHANNEL");
			e.printStackTrace();
		}
		try
		{
			this.dcTx.close();
		} catch (IOException e)
		{
			err.println("UNABLE TO CLOSE DCTX CHANNEL");
			e.printStackTrace();
		}
	}

	public void setFER(Root frontend) {
		this.FER = frontend;
	}

	private Task<Heartbeat> configHeartbeatAbsorber() {
		Task<Heartbeat> HeartbeatAbsorber = new Task<Heartbeat>() {
			DatagramPacket inbox;
			byte inboxBase[];
			ObjectInputStream ois;
			{
				inboxBase = new byte[65000];
				inbox = new DatagramPacket(inboxBase, 65000);
			}

			private void flush() {
				for (int i = 0; i < inboxBase.length; i++)
					inboxBase[i] = 0;
			}

			public Heartbeat call() throws IOException, ClassNotFoundException {
				while (true)
				{
					ms.receive(inbox);

					ois = new ObjectInputStream(new ByteArrayInputStream(inbox.getData()));
					Heartbeat beat = (Heartbeat) ois.readObject();

					if (!beat.equals(HEARTBEAT))
					{
						// our own heartbeat do not need to be handled
						err.print("<absorbed a beat><heartbeatTunnel Size " + heartbeatTunnel.size() + ">");

						heartbeatTunnel.add(beat);

						err.println("<" + heartbeatTunnel.size() + ">" + System.currentTimeMillis());
					}

					flush();
				}
			}
		};
		return HeartbeatAbsorber;
	}

	private Task<Heartbeat> configHeartbeathandler() throws IOException {
		Task<Heartbeat> HeartbeatHandler = new Task<Heartbeat>() {
			private ObjectOutputStream oosh;
			private ByteArrayOutputStream baosh;
			{
				baosh = new ByteArrayOutputStream();
				oosh = new ObjectOutputStream(baosh);
			}

			private void flush() throws IOException {
				baosh.reset();
				oosh = new ObjectOutputStream(baosh);
			}

			public Heartbeat call() throws InterruptedException, IOException {
				while (true)
				{
					Heartbeat beat = heartbeatTunnel.take();

					out.println("<handling a beat><tunnel size " + heartbeatTunnel.size() + ">");
					/*
					 * because now we have stopped passing our own heartbeats to handler so as to
					 * reduce the load, bcz they don't need to be handled we flush them without
					 * adding to tunnel on heartbeat absorber site
					 * 
					 * if(beat.equals(HEARTBEAT)) { //out.println("<its my beat>"); continue; } else
					 */
					{
						if (family.contains(beat))
						{
							// beat found in list so set its isALive true
							Relation found = family.get(family.indexOf(beat));
							found.setIsAlive(true);
							out.println("<found in family list>" + beat);
						} else
						{
							// new beat so request for relation packet
							SocketAddress newBeatAddress = new InetSocketAddress(beat.address, DCRX_PORT);

							// out.println("<beat handler><new beat address>"+newBeatAddress);

							Request relationPacketRequest = new Request(HEARTBEAT.by,
									RequestType.RELATION_PACKET_REQUEST);
							/**
							 * changing address of request for module testing
							 */
							relationPacketRequest.address = InetAddress.getByName(myip);

							oosh.writeObject(relationPacketRequest);
							dcTx.send(ByteBuffer.wrap(baosh.toByteArray()), newBeatAddress);
							flush();
							out.println("<new beat" + beat);
							out.println("relation req sent to->" + newBeatAddress);
							out.println("relation req sent is->" + relationPacketRequest);
						}
					}
				}
			}
		};
		return HeartbeatHandler;
	}

	public Task<Void> configDCRX() throws IOException {
		/**
		 * receive requests and respond to them
		 */
		Task<Void> RequestHandler = new Task<Void>() {
			private ObjectInputStream ois;
			private byte inboxBase[];
			private ByteBuffer inbox;

			private ObjectOutputStream oos;
			private ByteArrayOutputStream baos;
			{
				inboxBase = new byte[65000];
				inbox = ByteBuffer.wrap(inboxBase);

				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
			}

			private void flushOut() throws IOException {
				baos.reset();
				oos = new ObjectOutputStream(baos);
			}

			private void flushIn() {
				for (int i = 0; i < inboxBase.length; i++)
					inboxBase[i] = 0;
				inbox.clear();
			}

			public Void call() {
				while (true)
				{
					try
					{
						dcRx.receive(inbox);

						// out.println("<dcrx received>");

						ois = new ObjectInputStream(new ByteArrayInputStream(inbox.array()));
						Object received = ois.readObject();

						if (received instanceof Request)
						{
							Request req = (Request) received;

							switch (req.type)
							{
							case RELATION_PACKET_REQUEST:
							{
								// send relation packet
								oos.writeObject(RELATION_PACKET);
								SocketAddress requester = new InetSocketAddress(req.address, DCRX_PORT);

								out.println("<dcrx RELATION_PACKET_REQUEST> from<" + req.address + ">");

								dcTx.send(ByteBuffer.wrap(baos.toByteArray()), requester);

								out.println("<dcrx RELATION_PACKET_REQUEST sent to<" + requester + ">>");

								flushOut();
							}
								break;
							}
						} else if (received instanceof Relation)
						{
							Relation rel = (Relation) received;
							family.add(rel);

							/*****************************************
							 * FRONT END ROOT INJECTION
							 *********************************************/
							UserNode node = new UserNode(rel.address);
							node.setsharedFileList(rel.sharedFiles);
							Platform.runLater(() -> FER.downloadPanelMainRoot.getChildren().add(node));

							out.println("<new relation in family<" + family.size() + ">>" + rel);
						}
						flushIn();
					} catch (IOException | ClassNotFoundException e)
					{
						if (this.isCancelled())
							return null;
						e.printStackTrace();
					}
				}
			}
		};
		return RequestHandler;
	}

	public Task<Void> configTCPRx() {
		Task<Void> TCPRx_task = new Task<Void>() {
			public Void call() {
				out.println("TCPRx live");
				while (true)
				{
					try
					{
						err.println("[TCPRx ready to accept conection]");
						SocketChannel sc = TCPRx.accept();
						err.println("[TCPRX new connection established]");
						Uploader up = new Uploader(sc);
						/*****************************************
						 * FRONT END ROOT INJECTION
						 *********************************************/
						Platform.runLater(() -> Uploader.getContainer().getChildren().addAll(up));
					} catch (IOException e)
					{
						if (this.isCancelled())
							return null;
						err.println("TCPRx MALFUNCTION");
						e.printStackTrace();
					}
				}
			}
		};
		return TCPRx_task;
	}
}

public class BackEndRoot extends Application {
	/**
	 * module testing
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		/*
		 * out.println("<BackEndRoot started "+BackEndRootx.myip+">");
		 * out.println("hb->"+BackEndRootx.HEARTBEAT);
		 * out.println("rp->"+BackEndRootx.RELATION_PACKET); new
		 * BackEndRootx(null).play();
		 */
		new Downloader("", new File("i:/newFile.txt"), InetAddress.getByName("127.0.0.2"));
	}

	public static void main(String... a) {
		launch(a);
	}
}