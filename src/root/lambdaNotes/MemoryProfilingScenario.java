package root.lambdaNotes;

public class MemoryProfilingScenario {
	public static void main(String... a) {
		System.out.println(MemoryProfilingScenario.class);

		fun();
//		 fun1();
//		fun2();
	}

	// no memory usage issues as call site returns same instance of Runnable on
	// each call to nonCapturingLambdaFactory
	static void fun() {
		SomeClass object = new SomeClass();
		while (true)
			object.nonCapturingLambdaFactory().run();
	}

	// high memory usage issues because call site generates new instance of
	// Runnable on each call to capturingLambdaFactory so as to hold latest copy
	// captured ORV heavyStateObject
	static void fun1() {
		SomeClass object = new SomeClass();
		while (true)
			object.capturingLambdaFactory().run();
	}

	// solution to high memory usage issue in case of capturing lambdas with
	// help of hoisted instance
	static void fun2() {
		SomeClass object = new SomeClass();
		Runnable lambda = object.capturingLambdaFactory();
		while (true)
			lambda.run();
	}
}

class SomeClass {
	private Object[] heavyStateObject = new Object[100_000_000];

	public Runnable capturingLambdaFactory() {
		return () ->
		{
			System.out.println("SomeClass.capturingLambdaFactory" + this);
		};
	}

	public Runnable nonCapturingLambdaFactory() {
		return () ->
		{
			System.out.println("SomeClass.nonCapturingLambdaFactory");
		};
	}
}
