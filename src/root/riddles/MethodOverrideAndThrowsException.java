package root.riddles;
import java.io.IOException;

public class MethodOverrideAndThrowsException {

	public static void main(String[] args) {
		A ob = new B();
		try
		{
			// checked exception thrown here
			ob.fun1();
			// checked exception thrown here
			ob.fun2();
		} catch (IOException e)
		{
		}

		B obb = new B();
		try
		{
			// checked exception thrown here
			obb.fun1();
		} catch (IOException e)
		{
		}
		// no checked exception thrown here
		obb.fun2();
	}

}

class A {
	void fun1() throws IOException {

	}

	void fun2() throws IOException {

	}

	void fun3() {

	}

	void fun4() {

	}

}

class B extends A {
	/**
	 * Keeping throws exception as it is from parent class without any change is
	 * obviously valid.
	 */
	@Override
	void fun1() throws IOException {

	}

	/**
	 * Removing throws exception put by parent class is valid too.
	 */
	@Override
	void fun2() {

	}

	/**
	 * But adding an exception at throws section of overridden method is not valid,
	 * why ?</br>
	 * Because the purpose of checked exception is to enforce exception handling at
	 * compile time and if we add checked exception to throws of an overridden
	 * method than in case of polymorphism e.g. {@code Parent p = new Child(); 
	 * p.name();} Now if Child.name have added throws IOException, than through
	 * invocation {@code p.name ();} compiler could not enforce exception handling
	 * at compile time because method Parent.name do not throw exception and JVM
	 * would not know that p hold actual object of Child (that throws a checked
	 * exception) until runtime.
	 */
	@Override
	void fun3() throws IOException {

	}

	/**
	 * But no issues with keeping/adding/removing of unchecked exception at throws
	 * of overridden method
	 */
	@Override
	void fun4() throws RuntimeException {

	}

}
