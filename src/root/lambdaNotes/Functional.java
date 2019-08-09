package root.lambdaNotes;

/**
 * although having multiple abstract methods but still its a functional
 * interface because methods overriding public methods of “java.lang.Object”
 * does not count toward the interface's abstract method count.
 * 
 * @author kuldeepsin
 *
 */
@FunctionalInterface
public interface Functional {
	abstract void someMethod();

	abstract String toString();

	abstract boolean equals(Object other);

}
