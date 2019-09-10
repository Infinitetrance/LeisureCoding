package root.riddles;

public class Riddles1 {

	public static void main(String[] args) {
		Base ob = new Child();
		ob.print(10); // Base.print(int number: 10)
		/*
		 * because type of ORV ob is Base and print method is not overridden in Child
		 * class, so no polymorphism[method resolution is done at runtime on basis of
		 * actual object instead of type of ORV] is involved here and thus method
		 * resolution has to be done at compile time. Now compiler does method
		 * resolution on basis of type ORV so Base.print(int number: 10) is invoked here
		 */

		((Child) ob).print(100); // Base.print(int number: 100)

		/*
		 * due to explicit type cast type of ORV has become Child instead of Base and
		 * thus during method resolution compiler is aware of both[print(int number) and
		 * print(Integer number)] methods on object ob, now argument passed to print
		 * method is integer literal instead of Integer object and as per this most
		 * suitable method resolves to print method with formal parameter int instead of
		 * Integer
		 */
	}

}

class Base {
	public void print(int number) {
		System.out.println("Base.print(int number: " + number + ")");
	}
}

class Child extends Base {
	public void print(Integer number) {
		System.out.println("Child.print(Integer number: " + number + ")");
	}
}
