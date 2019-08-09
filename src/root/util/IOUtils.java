package root.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class IOUtils {

	public static void main(String[] mainClassArgs) throws Exception {
	}

	/**
	 * To be used in replacement of {@code new Scanner(System.in).hasNextLine()}, so
	 * as to be able to unblock hasNextLine call on thread interruption.
	 * 
	 * @return true if and only if System.in has another line of input
	 * @throws IOException
	 */
	public static boolean hasNextLine() throws IOException {
		while (System.in.available() == 0)
		{
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				return false;
			}

		}
		return true;
	}

	/**
	 * Receives single payload through UDP channel. For performance reasons,
	 * numerous invocations use {@link IOUtils.getUDPListener}
	 * 
	 * @return payload received
	 */
	public static byte[] receiveAsUDP(int rxPort) throws IOException {
		try (DatagramSocket socket = new DatagramSocket(rxPort))
		{
			DatagramPacket inbox = new DatagramPacket(new byte[512], 512);
			socket.receive(inbox);
			return inbox.getData();
		}
	}

	public static final byte ZERO_BYTE = 0;

	public static Supplier<String> getUDPReader(int rxPort) throws SocketException {
		DatagramSocket socket = new DatagramSocket(rxPort);
		byte[] inboxBuffer = new byte[512];
		DatagramPacket inbox = new DatagramPacket(inboxBuffer, inboxBuffer.length);
		return () ->
		{
			try
			{
				// So as to avoid type casting Arrays.fill(inboxBuffer, (byte)0);
				Arrays.fill(inboxBuffer, ZERO_BYTE);
				socket.receive(inbox);
				return new String(inboxBuffer);
			} catch (Exception e)
			{
				closeQuietly(socket);
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Sends payload through UDP channel. For performance reasons, numerous
	 * invocations use {@link IOUtils.getUDPWriter}
	 * 
	 * @return Resolved transmission IP address
	 */
	public static String sendAsUDP(byte[] payload, String txIP, int txPort) throws IOException {
		InetAddress txIPAddress = InetAddress.getByName(txIP);
		DatagramPacket payloadDP = new DatagramPacket(payload, payload.length, txIPAddress, txPort);

		try (DatagramSocket socket = new DatagramSocket())
		{
			socket.send(payloadDP);
		}
		return txIPAddress.toString();
	}

	public static Consumer<String> getUDPWriter(String txIP, int txPort) throws SocketException, UnknownHostException {
		InetAddress txIPAddress = InetAddress.getByName(txIP);
		DatagramPacket payloadDP = new DatagramPacket(new byte[0], 0, txIPAddress, txPort);
		DatagramSocket socket = new DatagramSocket();
		return (payload) ->
		{
			try
			{
				payloadDP.setData(payload.getBytes(), 0, payload.getBytes().length);
				socket.send(payloadDP);
			} catch (Exception e)
			{
				closeQuietly(socket);
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Listen for messages sent on multicast group.
	 * 
	 * A multicast group is specified by a class D IP address and by a standard UDP
	 * port number. Class D IP addresses are in the range 224.0.0.0 to
	 * 239.255.255.255, inclusive. The address 224.0.0.0 is reserved and should not
	 * be used.
	 */
	public static Thread listenMulticast(int broadcastMessageLength, String broadcastIP, int broadcastPort)
			throws IOException {
		MulticastSocket ms = new MulticastSocket(broadcastPort);
		ms.joinGroup(InetAddress.getByName(broadcastIP));
		DatagramPacket inbox = new DatagramPacket(new byte[broadcastMessageLength], broadcastMessageLength);

		Thread listenerThread = new Thread(() ->
		{
			try
			{
				while (true)
				{
					ms.receive(inbox);
					System.out.println("<< " + new String(inbox.getData()));
					Arrays.fill(inbox.getData(), ZERO_BYTE);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				ms.close();
			}
		});

		listenerThread.setDaemon(true);
		return listenerThread;
	}

	/**
	 * Repetitively send same message(through UDP channel) for purpose of
	 * broadcasting to multicast group
	 */
	public static Thread cast(String message, String broadcastIP, int broadcastPort) throws IOException {
		SocketAddress broadcastAddress = new InetSocketAddress(InetAddress.getByName(broadcastIP), broadcastPort);

		DatagramChannel dcTx = DatagramChannel.open();
		dcTx.connect(broadcastAddress);

		ByteBuffer messageByteBuffer = ByteBuffer.wrap(message.getBytes());

		Thread broadcastThread = new Thread(() ->
		{
			try
			{
				while (true)
				{
					dcTx.write(messageByteBuffer);
					messageByteBuffer.rewind();
					Thread.sleep(5000);
				}
			} catch (IOException | InterruptedException e)
			{
				e.printStackTrace();
			} finally
			{
				closeQuietly(dcTx);
			}
		});
		broadcastThread.setDaemon(true);
		return broadcastThread;
	}

	public static void closeQuietly(Closeable resource) {
		try
		{
			if (resource != null)
				resource.close();
		} catch (Exception e)
		{
			System.err.println(String.format("At closeQuietly(%s), failed to close resource.\nException: %s",
					resource.getClass().getName(), e.getMessage()));
		}
	}

	public static class OneField {
		public volatile double value = 0;
	}

	public static class UDPHandler extends Handler {

		private Consumer<String> out;

		public UDPHandler(String host, int port) throws IOException {
			out = getUDPWriter(host, port);
		}

		@Override
		public void close() throws SecurityException {
		}

		@Override
		public void flush() {
		}

		@Override
		public void publish(LogRecord logRecord) {
			StringBuilder log = new StringBuilder();
			log.append("logger: ");
			log.append(logRecord.getLoggerName());
			log.append(", level: ");
			log.append(logRecord.getLevel());
			log.append(", message: ");
			log.append(logRecord.getMessage());

			Throwable thrown = logRecord.getThrown();
			if (thrown != null)
			{
				log.append(", thrown: ");
				log.append(thrown);
			}
			log.append("\n");

			out.accept(log.toString());
		}

	}

}
