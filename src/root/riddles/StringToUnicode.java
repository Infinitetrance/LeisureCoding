package root.riddles;
/**
 * Utility for String to Unicode conversion
 *
 */
public class StringToUnicode {
	public static void main(String[] args) throws Exception {
		// A fun use could be to convert java source code text to unicode text and than
		// compile and run it.

		Runnable r = () ->
		{
			System.out.println("Hello World!");
		};
		// e.g. following call
		r.run();

		// could also be written in unicode like below.
		\u0072\u002e\u0072\u0075\u006e\u0028\u0029\u003b

		String text = "r.run();";

		String encoded = encode(text);
		System.out.println("encoded : " + encoded);

		String decoded = decode(encoded);
		System.out.println("decoded : " + decoded);

	}

	public static String unicodeEscaped(char ch) {
		if (ch < 0x10)
		{
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 0x100)
		{
			return "\\u00" + Integer.toHexString(ch);
		} else if (ch < 0x1000)
		{
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}

	public static String encode(String text) {
		if (text == null)
			return null;

		char[] characters = text.toCharArray();
		StringBuilder builder = new StringBuilder();

		for (char ch : characters)
			builder.append(unicodeEscaped(ch));

		return builder.toString();
	}

	public static String decode(String text) throws Exception {
		if (text == null)
			return null;

		String[] unicodes = text.split("\\\\u");
		if (unicodes == null)
			return null;

		byte[] ascii = new byte[unicodes.length];

		for (int i = 0; i < unicodes.length; i++)
			if (!unicodes[i].isEmpty())
				ascii[i] = (byte) Integer.parseInt(unicodes[i], 16);

		return new String(ascii);
	}

}
