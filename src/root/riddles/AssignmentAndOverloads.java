package root.riddles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AssignmentAndOverloads {

	public static void main(String[] args) {

		/**
		 * Integer literal is implicitly associated to "int" data type
		 * 
		 * Decimal literal is implicitly associated to "double" data type
		 */

		/**
		 * if the expression is a constant expression (§15.28) of type byte, short,
		 * char, or int:
		 * 
		 * A narrowing primitive conversion may be used if the variable is of type byte,
		 * short, or char, and the value of the constant expression is representable in
		 * the type of the variable.
		 */
		byte a_byte = 100; // implicit conversion from int to byte

		/**
		 * because right side of assignment statement is all integer literal, so
		 * computation can be done at compile time and result of computation int is
		 * implicitly casted to byte as it is in range of byte else CTE would have been
		 * thrown
		 */
		byte b_byte = 128 - 1;
		// possible loss of precision during implicit conversion from int to byte so CTE
		// (compile time error)
//		byte c_byte = 128;

		// CTE, because arithmetic computation on literals can be done at compile time
		// to check storage limit of data type,
//		short short_a = 32767 + 1;

		// same as above, range of char is 0 to 65535
		char a_char = 65536 - 1;

		int a_int = 2147483647;

		/**
		 * but why computation among integer literals is not done here so as to decide
		 * if range of value assigned is in range of int data type [as done above with
		 * short, byte, char]</br>
		 * because 2147483648 is integer literal which are by default associated to int
		 * and 2147483648 is out of bounds for int
		 */
//		int b_int = 2147483648 - 1;

		/**
		 * so to do so we could use literal specifier 'L' to represent 2147483648L as
		 * long literal
		 */

		int c_int = (int) (2147483648L - 1);

		/**
		 * CTE (compile time error) because Decimal literal is implicitly associated to
		 * "double" data type in Java. So to make it float use literal specifier for
		 * float e.g 0.0F
		 */
//		float s_float = 0.0;
		float sf_float = 0.0F;

		/**
		 * if we can assign 0 here
		 */
		byte byte_a = 0;

		/**
		 * why can not we pass 0 argument here, why does it show CTE because in
		 * assignment context narrowing conversion is allowed whereas in invocation
		 * context narrowing conversion is not allowed
		 */
//		setByte(0);

		/**
		 * when choosing method definition to invoke among overloads Java chooses "most
		 * specific" method definition
		 */

		/**
		 * why method with int formal param is invoked ? because 0 is integer literal
		 * and integer literal is associated to int data type and int data type is
		 * available in formal params of one of method
		 * 
		 * why not byte ? because conversion from int to byte have to be done and more
		 * specific method is available
		 * 
		 * if primitiveOverload(int ) was not available than primitiveOverload(long )
		 * would be called, why ? because type promotion of int to long leads to most
		 * specific method invocation
		 */
		primitiveOverload(0); // int
		primitiveOverload(0.0); // double

		primitiveOverload((Integer) 0); // Object

		/**
		 * when method overload is such that, there is parent child relationship between
		 * formal parameter of different methods than on passing null, method with child
		 * most formal param will be invoked because it would have access to class
		 * definition of all parent formal params
		 */
		parentChildOverload(null); // ArrayList

		// same as above
		varArgsOverload1(); // String...

		// CTE, because there is no parent child rel between formal param types String
		// and Integer, so leads to invocation ambiguity
//		twins(null);

		// same as above
//		varArgsOverload2();

		// resolving ambiguous invocation for null values
		twins((String) null);
		twins((Integer) null);

		// choose method with smallest formal param data type, because it would be most
		// specific to
		// store zero parameters[minimal memory consumption]
		varArgsOverload(); // byte ...

		// CTE
		// varArgsOverload3();

		// CTE, because boolean and int are unrelated like String... and Integer..., so
		// both method are equally valid and so causing ambiguity
		// varArgsOverload4();

		// one method could take only single argument and another could take single as well
		// as multiple argument, so one method is more specific
		// as compared to another and thus ambiguity could be resolved
		varArgsOverload5(0); // int a

		// one method could take only single argument and another could take single as well
		// as multiple argument, so one method is more specific
		// as compared to another and thus ambiguity could be resolved
		varArgsOverload6(0); // int a

		// CTE both method could take single argument as well as multiple arguments so
		// ambiguity could not be resolved for single argument
		// varArgsOverload7(0);
	}

	static void setByte(byte a) {
		System.out.println("setByte");
	}

	static void primitiveOverload(byte a) {
		System.out.println("primitiveOverload(byte a)");
	}

	static void primitiveOverload(short a) {
		System.out.println("primitiveOverload(short a)");
	}

	static void primitiveOverload(char a) {
		System.out.println("primitiveOverload(char a)");
	}

	static void primitiveOverload(int a) {
		System.out.println("primitiveOverload(int a)");
	}

	static void primitiveOverload(long a) {
		System.out.println("primitiveOverload(long a)");
	}

	static void primitiveOverload(float a) {
		System.out.println("primitiveOverload(float a)");
	}

	static void primitiveOverload(double a) {
		System.out.println("primitiveOverload(double a)");
	}

	static void primitiveOverload(Object a) {
		System.out.println("primitiveOverload(Object a)");
	}

	static void parentChildOverload(Collection c) {
		System.out.println("parentChildOverload(Collection c)");
	}

	static void parentChildOverload(List l) {
		System.out.println("parentChildOverload(List l)");
	}

	static void parentChildOverload(ArrayList al) {
		System.out.println("parentChildOverload(ArrayList al)");
	}

	static void twins(Integer a) {
		System.out.println("twins(Integer a) {");
	}

	static void twins(String a) {
		System.out.println("twins(String a) {");
	}

	static void varArgsOverload(byte... a) {
		System.out.println("varArgsOverload(byte ...a)");
	}

	static void varArgsOverload(short... a) {
		System.out.println("varArgsOverload(short ...a)");
	}

	static void varArgsOverload(int... a) {
		System.out.println("varArgsOverload(int ...a)");
	}

	static void varArgsOverload(long... a) {
		System.out.println("varArgsOverload(long ...a)");
	}

	static void varArgsOverload(float... a) {
		System.out.println("varArgsOverload(float ...a)");
	}

	static void varArgsOverload(double... a) {
		System.out.println("varArgsOverload(double ...a)");
	}

	static void varArgsOverload1(String... a) {
		System.out.println("varArgsOverload1(String ...a)");
	}

	static void varArgsOverload1(Object... a) {
		System.out.println("varArgsOverload1(Object ...a)");
	}

	static void varArgsOverload2(String... a) {
		System.out.println("varArgsOverload2(String ...a)");
	}

	static void varArgsOverload2(Integer... a) {
		System.out.println("varArgsOverload2(Integer ...a)");
	}

	static void varArgsOverload3(char... a) {
		System.out.println("varArgsOverload3(char ...a)");
	}

	static void varArgsOverload3(short... a) {
		System.out.println("varArgsOverload3(int ...a)");
	}

	static void varArgsOverload4(boolean... a) {
		System.out.println("varArgsOverload4(boolean ...a)");
	}

	static void varArgsOverload4(int... a) {
		System.out.println("varArgsOverload4(int ...a)");
	}

	static void varArgsOverload5(int... a) {
		System.out.println("varArgsOverload5(int... a)");
	}

	static void varArgsOverload5(int a) {
		System.out.println("varArgsOverload5(int a)");
	}

	static void varArgsOverload6(int a) {
		System.out.println("varArgsOverload6(int a)");
	}

	static void varArgsOverload6(int a, int... b) {
		System.out.println("varArgsOverload6(int a, int... b)");
	}

	static void varArgsOverload7(int... a) {
		System.out.println("varArgsOverload7(int... a)");
	}

	static void varArgsOverload7(int a, int... b) {
		System.out.println("varArgsOverload7(int a, int... b)");
	}
}
