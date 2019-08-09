package root.bitport;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Request implements Serializable
{
	private static final long serialVersionUID = -7014343323445149760L;
	/**
	 * request packet 
	 */
	public Patent by;
	public RequestType type;
	public InetAddress address;
	public Request(Patent by,RequestType type) throws UnknownHostException 
	{
		this.by=by;
		this.type=type;
		address=InetAddress.getLocalHost();
	}
	public String toString()
	{
		return by.toString()+"  RequestType type<"+type+">  address<"+address+">";
	}
	public static void main(String ...a) throws UnknownHostException
	{
		/**
		 * module testing 
		 */
		System.out.println(new Request(BackEndRootx.HEARTBEAT.by,RequestType.RELATION_PACKET_REQUEST));
	}
}

enum RequestType
{
	RELATION_PACKET_REQUEST
}