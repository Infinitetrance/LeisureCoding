package root.bitport;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

class Heartbeat implements Serializable {
	private static final long serialVersionUID = 8692040182595779727L;

	public Patent by;
	public InetAddress address;
	private boolean isAlive;

	Heartbeat(String spice, String designer) throws UnknownHostException {
		by = new Patent(spice, designer);
		address = InetAddress.getLocalHost();
		isAlive = true;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public void setIsAlive(boolean value) {
		this.isAlive = value;
	}

	@Override
	public boolean equals(Object arg0) {
		// System.out.println("Heartbeat equals()");

		if (arg0 instanceof Heartbeat)
		{
			Heartbeat other = (Heartbeat) arg0;
			return other.by.equals(this.by) && other.address.equals(this.address);
		} else
			return false;
	}

	@Override
	public String toString() {
		return by.toString() + "IP<" + address + ">  isAlive<" + isAlive + ">";
	}

	public static void main(String... a) throws UnknownHostException {
		/**
		 * for module testing
		 */
		Heartbeat h1 = new Heartbeat("BitPort", "Infinity Labs∞ Kuldeep Singh");
		Heartbeat h2 = new Heartbeat("BitPort", "Infinity Labs∞ Infitron");
		Heartbeat h3 = new Heartbeat("BitPort v6", "Infinity Labs∞ Infi");

		System.out.println(h1.equals(h1));
		System.out.println(h1.equals(h2));
		System.out.println(h3.equals(h2));
		System.out.println(h3.equals(h3) + "\n");

		List<Heartbeat> beats = new ArrayList<Heartbeat>();
		beats.add(h1);
		beats.add(h2);

		System.out.println(beats.contains(h1));
		System.out.println(beats.contains(h2));
		System.out.println(beats.contains(h3) + "\n");

		System.out.println(beats.indexOf(h1));
		System.out.println(beats.indexOf(h2));
		System.out.println(beats.indexOf(h3) + "\n");

		List<Heartbeat> beats2 = new ArrayList<Heartbeat>();
		beats2.add(h1);
		beats2.add(h2);

		System.out.println(beats.equals(beats2) + "\n");

		// Concatenation of two lists
		beats2.addAll(beats);
		for (Heartbeat h : beats2)
			System.out.println(h);
		/*
		 * new Thread() { { this.setName("t1"); } public void run() {
		 * System.out.println("t1 in"); h1.setIsAlive(true);
		 * System.out.println("t1 out"); } }.start();
		 * 
		 * new Thread() { { this.setName("t2"); } public void run() {
		 * System.out.println("t2 in"); System.out.println(h1.isAlive());
		 * System.out.println("t2 out"); } }.start();
		 */
	}
}

@SuppressWarnings("serial")
class Patent implements Serializable {
	public String spice;
	public String designer;

	Patent(String spice, String designer) {
		this.spice = spice;
		this.designer = designer;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Patent)
		{
			Patent other = (Patent) arg0;
			return this.spice.equalsIgnoreCase(other.spice) && this.designer.equalsIgnoreCase(other.designer);
		} else
			return false;
	}

	@Override
	public String toString() {
		return "spice<" + spice + ">  designer<" + designer + ">";
	}
}