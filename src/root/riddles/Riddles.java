package root.riddles;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Set of interesting java questions
 *
 */
public class Riddles {
	/**
	 * what should be return value of function {@code CatchFinallyReturn} and order
	 * of execution of sysout statements
	 */
	public static int CatchFinallyReturn() {
		try
		{
			System.out.println("tryStart");
			throw new Exception();
		} catch (Exception e)
		{
			System.out.println("catch");
			return -1;
		} finally
		{
			System.out.println("finally");
			return 0;
		}
	}

	/**
	 * CR - carriage return, valued 0x0D (13 decimal). LF - Line feed, valued 0x0A
	 * (10 decimal). CR and LF are control characters used to mark a line break in a
	 * text file.
	 * 
	 */
	public static void commentExecutes() {
		// \u000d System.out.println("This Comment Executed!");
		// \u000a System.out.println("This Comment Executed!");
	}

	/**
	 * Cannot switch on a value of type double. Only convertible int values, strings
	 * or enum variables are permitted
	 */
	public static void switchParam() {
		// switch (10.0)
		{
			// case 1.0:
			// break;
		}
	}

	/**
	 * U+202e is a unicode control character(also called RTL character ) that
	 * changes all subsequent text to right-to-left Annâ€®txt.exe Symbolâ€®txt.jar
	 * txt.exe
	 */
	public static void RTLCharTrick() {
		System.out.println("â€®ABC");
	}

	public static void shorHandOperatorTrick() {
		double d = 10.0;
		int i = 10;

		// it needs explicit type casting here or else will show compile time
		// error
		i = (int) (i + d);

		// but after using short hand operator no explicit type casting is
		// needed
		i += d;
	}

	/**
	 * not a riddle but just a beautiful approach which is usually written as
	 * if(file.exists())file.delete;
	 * 
	 * similarly {@code
	 * 
	 * instead of this
	 * 
	 * if(object != null && !object.equals("")){ }
	 * 
	 * you can write
	 * 
	 * if(!"".equals(object)) { } }
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		final File file = new File(fileName);
		return !file.exists() || file.delete();
		// return file.exists() && file.delete(); this statement is also an
		// alternate of above statement
	}

	/**
	 * crazy return types
	 * 
	 * @return
	 */
	int[][] a() {
		return new int[0][];
	}

	int[] b()[] {
		return new int[0][];
	}

	int c()[][] {
		return new int[0][];
	}

	/**
	 * When an operator applies binary numeric promotion to a pair of operands, each
	 * of which must denote a value that is convertible to a numeric type, the
	 * following rules apply, in order, using widening conversion to convert
	 * operands as necessary:<br/>
	 * 
	 * If any of the operands is of a reference type, unboxing conversion is
	 * performed. Then:<br/>
	 * If either operand is of type double, the other is converted to double.<br/>
	 * Otherwise, if either operand is of type float, the other is converted to
	 * float.<br/>
	 * Otherwise, if either operand is of type long, the other is converted to
	 * long.<br/>
	 * Otherwise, both operands are converted to type int.<br/>
	 */
	static void typePromo() {
		short a, b, c;
		a = 2;
		b = 1;
		// c = a+b; // compile time error
	}

	/**
	 * {@link http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.4}
	 * because A LocalVariableDeclarationStatement is not a statement and syntax for
	 * if statement is<br/>
	 * if ( Expression ) Statement
	 */
	static void variableDeclaration() {
		// if(true) int a = 0; // compile time error
		if (true)
		{
			int a = 0;
		}
	}

	/**
	 * When we directly define strings like {@code String str = "some string";}
	 * 
	 * the string str's value gets stored in an intern pool and any string variable
	 * assigned the same value will reference the same memory location as str. This
	 * is possible because strings are immutable in Java.
	 * 
	 * Defining strings like {@code String str_object = new String("some string");}
	 * 
	 * will generate a different string referencing another memory location.
	 */
	static void stringIntern() {
		String str1 = "some string";
		String str2 = "some string";
		String str3 = "some string";
		String str4 = new String("some string");

		System.out.println(str1 == str2 && str1 == str3); // true
		System.out.println(str1 == str4); // false
	}

	/**
	 * every class file in java starts with hex code CAFEBABE as class file
	 * validator
	 * 
	 * @throws IOException
	 */
	static void classFileValidator() throws IOException {
		byte[] bt = Files.readAllBytes(Paths.get("bin/Entropy.class"));
		for (int i = 0; i < 10; i++)
			System.out.print(String.format("%02X", bt[i])); // output is
															// CAFEBABE+some
															// thing
	}

	/**
	 * Why does first sysout prints 10 Number of operators involved in statement "x
	 * = x++;" are "=" and "post increment ++" Now precedence of ++ is higher than =
	 * so ++ should happen before = but at same time associativity of = is right to
	 * left and ++ has no associativity So it happens like this take value of x in
	 * temporary memory location which is 10 than happen post increment and now x
	 * has become 11 than from temporary memory value 10 is assigned to x
	 */
	static void precedenceAndAssociativityTest() {
		int x = 10;
		x = x++;
		System.out.println("x : " + x);

		x = ++x;
		System.out.println("x :: " + x);
	}

	/**
	 * What is type of b and d b is int array but d is int
	 */
	static void arrayType() {
		int[] a, b;
		int c[], d;
		/*
		 * a = new int[1]; b = new int[1];
		 * 
		 * 
		 * c= new int[1]; d= 1;
		 */
	}

	/**
	 * Use BigDecimal to avoid precision loss
	 */
	static void precisionLossInPrimitives() {
		System.out.println((2.0 - 1.1) == 0.9); // outputs false
		System.out.println(2.0 - 1.1);
	}

	/**
	 * Problem Statement write a piece of code inside function callMe such that
	 * function remembers its invocation sequence. e.g. If function is called first
	 * time it prints 1 when call second time it prints 2 and so on. Constraint is
	 * that you are not allowed write any code outside of function callMe except
	 * invoking function itself.
	 * 
	 * @throws Exception
	 */
	static void callMe() throws Exception {
		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);

		Field OTHER_NUMBER = Character.class.getDeclaredField("OTHER_NUMBER");
		OTHER_NUMBER.setAccessible(true);
		modifiers.setInt(OTHER_NUMBER, OTHER_NUMBER.getModifiers() & ~Modifier.FINAL);

		byte callCnt = (byte) OTHER_NUMBER.get(null);
		callCnt++;
		OTHER_NUMBER.setByte(null, (byte) callCnt);
		System.out.println("callMe() Invocation Sequence : " + (callCnt - 11));
	}

	/**
	 * Yes! function httpLabel does perfectly compiles. wonder how ? because http is
	 * a label[named http just to confuse you to look like URL] that labels code
	 * snippet following it and google.com is comment so it doesn't make any sense
	 * to compiler.
	 */
	static void httpLabel() {
		http: // google.com
		System.out.println();
	}

	/**
	 * Try predicting output of function sabotagingIntegers :)
	 * 
	 */
	static void sabotagingIntegers() throws Exception {
		\u0065\u006e\u0074\u0072\u006f\u0070\u0079\u0047\u0065\u006e\u0065\u0072\u0061\u0074\u006f\u0072\u0028\u0029\u003b

		System.out.println("sabotagingIntegers() ");

		Integer one = 1;
		Integer ten = 10;
		Integer hundred = 100;
		System.out.println("one is " + one + ", ten is " + ten + " hundred is " + hundred);
	}

	static void entropyGenerator() throws Exception {
		// Extract the IntegerCache through reflection
		Class<?> clazz = Class.forName("java.lang.Integer$IntegerCache");
		Field field = clazz.getDeclaredField("cache");
		field.setAccessible(true);
		Integer[] cache = (Integer[]) field.get(clazz);

		// inspect cache
		// System.out.println("cache.length : " + cache.length);
		// for (int i = 0; i < cache.length; i++)
		// System.out.println("cache[" + i + "] = " + cache[i]);

		// Rewrite the Integer cache
		for (int i = 0; i < cache.length; i++)
			cache[i] = new Integer(new Random().nextInt(cache.length));
	}

	/**
	 * Guess why no NullPointerException is thrown.
	 * 
	 * When a target reference is computed then it will be discarded if the
	 * invocation mode is static, the reference will be not examined to see whether
	 * it is null.
	 */
	static void staticInvocationTargetReferenceInference() {
		Arrays arrays = null;
		System.out.println(
				arrays.asList("staticInvocationTargetReferenceInference() ", "Why ?", "No", "NullPointerException"));
	}

	static void ternaryOpTypePromo() {
		// output is 1.0 because The conditional operator will implement numeric
		// type promotion, if â€œneededâ€�,
		System.out.println(true ? 1L : 2.5D);

		// same here
		System.out.println(true ? new Long(2) : new Double(2.5));

		Long lv = null;
		// NullPointerException here , because numeric promotion may implicitly
		// perform unboxing and type promotion over "lv"
		System.out.println(true ? lv : new Double(2.5));
	}

	/**
	 * Guess output of function sabotagingString :)
	 */
	static void sabotagingString() {
		\u0070\u006f\u006c\u006c\u0075\u0074\u0065\u0049\u006e\u0074\u0065\u0072\u006e\u0050\u006f\u006f\u006c\u0028
				\u0022\u004a\u0061\u0076\u0061\u0022\u002c \u0022\u0043\u002b\u002b\u0022\u0029\u003b

		new String("Java");

		System.out.println("sabotagingString() ");
		System.out.println("Java");
	}

	static void polluteInternPool(String original, String replacement) {
		try
		{
			Field stringValue = String.class.getDeclaredField("value");
			stringValue.setAccessible(true);
			stringValue.set(original, replacement.toCharArray());
		} catch (Exception ex)
		{
			// Ignore exceptions
		}
	}

	static void wrapperOperandAndPrimitiveOperand() {
		Long nullLong = null, tenInWrapper = 10L;

		// If both operand in expression are wrapper type than outcome of expression
		// will be wrapper type, so it prints straight away null here
		System.out.println(true ? nullLong : tenInWrapper);

		int tenInPrimitive = 10;
		// But if one of the operand in expression is primitive type than other operand
		// has to be converted to primitive type too. Now because tenInPrimitive is
		// primitive, so nullLong has to be converted to primitive too(which means
		// nullLong needs to be unboxed and this causes NullPointerException)
		System.out.println(true ? nullLong : tenInPrimitive);
	}

	static void invokefunctionTwins() {
		// resolving ambiguous invocation for null values
		functionTwins((String) null);
		functionTwins((Integer) null);
	}

	static void functionTwins(Integer a) {
		System.out.println("functionTwins(Integer a) {");
	}

	static void functionTwins(String a) {
		System.out.println("functionTwins(String a) {");
	}

	// Return is always from outermost finally
	static int fun() {
		try
		{
			System.out.println("START try outer");
			try
			{
				System.out.println("START try inner");
			} finally
			{
				System.out.println("finally inner");
				return 1;
			}
		} finally
		{
			System.out.println("finally outer");
			return 2;
		}
	}

	static void spaceDoNotMatter() {
		// zero space between array operator and identifier is valid java syntax
		int[] a;

		// this too valid
		int[] b;

		// this too valid
		int c[];

		// valid
		Consumer<String[]> con = (String... x) ->
		{
		};
		// this too valid
		Consumer<String[]> con1 = (String... x) ->
		{
		};

	}

	// Getting name of enclosing class in static scope without hard coding class
	// name like EnclosingClassName.class.getname()
	static String enclosingClassName = MethodHandles.lookup().lookupClass().getName();

	/**
	 * does java have pointers ?<br/>
	 * - if no than why it throws NullPointerException<br/>
	 * - should't it be like NullReferenceException
	 */

	/**
	 * It is a compile time error to import a type from the unnamed(default)
	 * package.
	 */

	public static void main(String... a) throws Exception {
		System.out.println(CatchFinallyReturn());

		commentExecutes();

		RTLCharTrick();

		classFileValidator();

		precedenceAndAssociativityTest();

		precisionLossInPrimitives();

		for (int i = 0; i < 4; i++)
			callMe();

		sabotagingIntegers();

		staticInvocationTargetReferenceInference();

		ternaryOpTypePromo();

		sabotagingString();
	}
}
