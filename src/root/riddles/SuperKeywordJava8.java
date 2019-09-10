package root.riddles;

public class SuperKeywordJava8 {

	public static void main(String[] args) {

	}

}

interface Generation0 {
	default String gen0default() {
		return "Generation0.gen0default()";
	}
}

interface Generation1 extends Generation0 {
	/* (it is) public static final (by default, so no need to explicitly mention) */String GEN1 = "Generation1.GEN1";

	/* public abstract */String gen1();

	/* public */ default String gen1default() {
		return "Generation1.gen1default()";
	}
}

interface Generation1A extends Generation0 {
	/* public */ default String gen1default() {
		return "Generation1A.gen1default()";
	}
}

abstract class AbstractGeneration0 {
	public String gen() {
		return "AbstractGeneration0.gen()";
	}
}

abstract class AbstractGeneration1 extends AbstractGeneration0 {
	public static final String AGEN1_ST = "AbstractGeneration1.AGEN1_ST";
	public final String AGEN1_NS = "AbstractGeneration1.AGEN1_NS";

	public AbstractGeneration1() {
		System.out.println("AbstractGeneration1()");
	}

	public String gen() {
		return "AbstractGeneration1.gen()";
	}
}

class Generation1Impl extends AbstractGeneration1 implements Generation1, Generation1A {

	public Generation1Impl() {
		// invoking parent class constructor with super keyword
		super();
		System.out.println("Generation1Impl()");
	}

	public String gen() {
		return "Generation1Impl.gen()";
	}

	@Override
	public String gen1() {
		// super keyword usage for interface data members(allowed only for class data
		// members) is not allowed
		// System.out.println(super.GEN1); // CTE(Compile Time Error)

		System.out.println(super.AGEN1_ST);
		System.out.println(super.AGEN1_NS);

		// super keyword usage for abstract interface method not allowed
		// System.out.println(super.gen1()); // CTE(Compile Time Error)

		// super keyword usage for default interface method is allowed but need to
		// prefix interface name
		System.out.println(Generation1.super.gen1default());

		// super keyword usage for default interface method is allowed(but only for
		// direct parent interfaces) but need to prefix interface name
		// System.out.println(Generation0.super.gen0default());// CTE(Compile Time
		// Error)

		System.out.println(gen()); // invokes this.gen()
		// invokes parent class's gen(), but accessing grandparent's method is not
		// possible using super because it breaks encapsulation(If the parent class
		// wants to allow you to call the grandparent method directly, it can expose
		// that via a separate method but that's up to the parent class.)
		System.out.println(super.gen());

		return null;
	}

	/*
	 * when we have duplicate default methods inherited from multiple interfaces
	 * than we have to override method to resolve ambiguity
	 */
	@Override
	public String gen1default() {
		return Generation1A.super.gen1default();
	}

}
