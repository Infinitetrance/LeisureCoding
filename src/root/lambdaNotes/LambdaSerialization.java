package root.lambdaNotes;

import java.io.Serializable;

/**
 * just in case you’d like to send it(lambda expression) over wire and execute
 * it somewhere else, as we do in RMI
 * 
 */
public class LambdaSerialization {

	public static void main(String[] args) {
		// Lambda is Runnable but not Serializable
		execution(() -> System.out.println("Hey, I am Lambda from Vanaheimr"));

		// Lambda is Serializable now but not Runnable
		// execution((Serializable) (() -> System.out.println("Hey, I am Lambda from
		// Vanaheimr")));

		// #Approach 1
		execution((Runnable & Serializable) (() -> System.out.println("Hey, I am Lambda from Vanaheimr")));

		// #Approach 2
		remoteExecution(() -> System.out.println("Hey, I am Lambda from Asgard"));
	}

	public static void execution(Runnable t) {
		// send t over wire
		// lets check before sending whether its Serializable or not
		if (t instanceof Serializable)
		{
			// send(t,IP address)
			System.out.println("Lambda sent to Niflheim");
		} else
		{
			System.err.println("Lambda is not Serializable");
			return;
		}

		// execute t over there
		// lets check before executing whether its Runnable or not
		if (t instanceof Runnable)
		{
			t.run();
		} else
		{
			System.err.println("Lambda is not Runnable");
			return;
		}

	}

	public static <T extends Runnable & Serializable> void remoteExecution(T t) {
		// send t over wire
		// lets check before sending whether its Serializable or not
		if (t instanceof Serializable)
		{
			// send(t,IP address)
			System.out.println("Lambda sent to Midgard");
		} else
		{
			System.err.println("Lambda is not Serializable");
			return;
		}

		// execute t over there
		// lets check before executing whether its Runnable or not
		if (t instanceof Runnable)
		{
			t.run();
		} else
		{
			System.err.println("Lambda is not Runnable");
			return;
		}

	}

}
