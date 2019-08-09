package root.remoteConsole;
import java.nio.ByteBuffer;

public class ByteUtils
{
	public static byte[] longToBytes(long x)
	{
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(x);
		return buffer.array();
	}

	public static long BytesToLong(byte[] x)
	{
		return (x[0] << 56)
			 + ((x[1] & 0xFF) << 48)
			 + ((x[2] & 0xFF) << 40)
			 + ((x[3] & 0xFF) << 32)
			 + ((x[4] & 0xFF) << 24)
			 + ((x[5] & 0xFF) << 16)
			 + ((x[6] & 0xFF) << 8) 
			 + (x[7] & 0xFF);
	}
	
	public static byte[] stringTo56Bytes(String x)
	{
		ByteBuffer buffer = ByteBuffer.allocate(56);
		if (x.getBytes().length <= 56)
			buffer.put(x.getBytes());
		return buffer.array();
	}
}
