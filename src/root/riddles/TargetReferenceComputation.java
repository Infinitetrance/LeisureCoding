package root.riddles;
/**
 * Demonstrates target reference computation for data members and methods during
 * polymorphism
 *
 */
public class TargetReferenceComputation {
	public static void main(String[] args) {
		X orv = new Z();

		/**
		 * only the type of the "orv" (Primary expression), not the class of the actual
		 * object referred to at run time, is used in determining which field/data
		 * member to use. And type of "orv" here is class "X",
		 */
		System.out.println("orv.value: " + orv.value); // output orv.value: 10

		/**
		 * the instance method to be invoked is chosen according to the run-time class
		 * of the object referred to by "orv" which is class "Z" here
		 */
		System.out.println("orv.fun(): " + orv.fun()); // output orv.fun(): Z.fun
	}
}

class X {
	int value = 10;

	String fun() {
		return "X.fun";
	}
}

class Y extends X {
	int value = 20;

	String fun() {
		return "Y.fun";
	}
}

class Z extends Y {
	int value = 30;

	String fun() {
		return "Z.fun";
	}
}