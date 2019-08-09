package root.util;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	public static void main(String[] args) {
	}

	public static String dumpAllThreads() {
		StringBuilder strBuilder = new StringBuilder();

		for (Thread thread : Thread.getAllStackTraces().keySet())
			strBuilder.append(String.format("Name: %50s, State: %25s, isAlive: %8s, isDaemon: %8s\n", thread.getName(),
					thread.getState(), thread.isAlive(), thread.isDaemon()));
		return strBuilder.toString();
	}

	static Map<Integer, Long> timeitCapturePoints = new HashMap<>();

	/**
	 * <pre>
	 * Convenient for computing time elapsed between two execution points.
	 * e.g. following code
	 * {@code 
	 *  long startTime = System.currentTimeMillis(); 
	 *  someCode(); 
	 *  long elapsedTime = System.currentTimeMillis() - startTime;
	 *  System.out.println("Time took to execute: " + elapsedTime = "ms");
	 *  }
	 *  
	 * could also be conveniently written as
	 * {@code 
	 * timeit(); 
	 * someCode(); 
	 * System.out.println("Time took to execute: " + timeit() = "ms");
	 * }
	 * </pre>
	 * 
	 * @return elapsed time in milliseconds since previous invocation of this
	 *         function
	 */
	public static long timeit() {
		return timeit(0);
	}

	/**
	 * <pre>
	 * Similar to {@code timeit();}, but support capturing multiple execution time simultaneously.
	 * e.g. {@code 
	 * timeit(0);
	 * timeit(1);
	 * initSomeTask();
	 * System.out.println("Time took to initSomeTask: " + timeit(1) = "ms");
	 * runSomeTask();
	 * System.out.println("Time took to initSomeTask and runSomeTask: " + timeit(0) = "ms");
	 * }
	 * </pre>
	 * 
	 * @param tag marker to differentiate different multiple execution time capture
	 * @return elapsed time in milliseconds since previous invocation of this
	 *         {@code timeit(tag)} function
	 */
	public static long timeit(int tag) {
		Long now = System.currentTimeMillis();
		Long prevTime = timeitCapturePoints.put(tag, now);
		if (prevTime != null)
		{
			System.out.println("timeit(tag-" + tag + "): " + (now - prevTime));
			return now - prevTime;
		}
		return 0;
	}

	public static String spaceSpreadArray(String[] args) {
		StringBuilder extract = new StringBuilder();
		for (String arg : args)
		{
			extract.append(arg);
			extract.append(" ");
		}
		return extract.toString().trim();
	}
}
