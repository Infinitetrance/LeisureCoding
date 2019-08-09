package root.asciiBanner;

import static root.asciiBanner.BitMap.asc2_1608;

import java.util.Scanner;

import root.util.IOUtils;

/**
 * Utility to print fancy ASCII banner text, as seen on terminals
 */
public class ASCIIBanner {

	public static void print(char forgroundPixel, char backgroundPixel, String text) {
		print(1, 1, forgroundPixel, backgroundPixel, text);
	}

	public static void print(int fontHeight, int fontWidth, char forgroundPixel, char backgroundPixel, String text) {
		char rowBits;
		for (byte row = 0; row < 16; row++)
		{
			for (byte fh = 0; fh < fontHeight; fh++)
			{
				for (char character : text.toCharArray())
				{
					// ASCII code mapping, characters between 0 to 32 in ASCII
					// are not available in asc2_1608[]
					character -= 32;
					rowBits = asc2_1608[character * 16 + row];
					for (byte col = 0; col < 8; col++)
						for (byte fw = 0; fw < fontWidth; fw++)
							if (((rowBits >> col) & 0x0001) == 0x0001)
								System.out.print(forgroundPixel);
							else
								System.out.print(backgroundPixel);

				}
				System.out.print("\n");
			}
		}
	}

	/**
	 * Prints {@code @param text} such that each character of text would be having
	 * itself as its constituent pixel.
	 * 
	 * @param text
	 */
	public static void print(String text) {
		char rowBits;
		for (byte row = 0; row < 16; row++)
		{
			for (byte fh = 0; fh < 1; fh++)
			{
				for (char character : text.toCharArray())
				{
					char charAsciiCode = character;
					character -= 32;
					rowBits = asc2_1608[character * 16 + row];
					for (byte col = 0; col < 8; col++)
						for (byte fw = 0; fw < 1; fw++)
							if (((rowBits >> col) & 0x0001) == 0x0001)
								System.out.print(charAsciiCode);
							else
								System.out.print(' ');

				}
				System.out.print("\n");
			}
		}
	}

	public static void print(int fontHeight, int fontWidth, String forgroundPixel, String backgroundPixel,
			String text) {
		char rowBits;
		int pixelIndex = 0;

		for (byte row = 0; row < 16; row++)
		{
			for (byte fh = 0; fh < fontHeight; fh++)
			{
				for (char character : text.toCharArray())
				{
					character -= 32;
					rowBits = asc2_1608[character * 16 + row];
					for (byte col = 0; col < 8; col++)
						for (byte fw = 0; fw < fontWidth; fw++)
						{
							if (((rowBits >> col) & 0x0001) == 0x0001)
							{
								if (pixelIndex >= forgroundPixel.length())
									pixelIndex = 0;
								System.out.print(forgroundPixel.charAt(pixelIndex++));
							} else
								System.out.print(backgroundPixel);
						}
				}
				pixelIndex = 0;
				System.out.print("\n");
			}
		}
	}

	/**
	 * Print binary image as text
	 * 
	 * binaryImage must contain either 0 or 1 in text format, each line can have any
	 * number of characters( where characters allowed are [0,1]) but limit number of
	 * characters to 79 for CommandPormpt usage, file can have any number of lines
	 * but limit number of lines so that image can be rendered in single screen
	 */
	public static void printBinaryImage(String binaryImage, char forgroundPixel, char backgroundPixel) {
		Scanner in = null;
		try
		{
			in = new Scanner(binaryImage);
			while (in.hasNextLine())
			{
				String row = in.nextLine();
				for (short bitNo = 0; bitNo < row.length(); bitNo++)
				{
					switch (row.charAt(bitNo))
					{
					case '0':
						System.out.print(backgroundPixel);
						break;
					case '1':
						System.out.print(forgroundPixel);
						break;
					default:
						throw new RuntimeException(
								"BinaryImage contains non binary data ['" + row.charAt(bitNo) + "']");
					}
				}
				System.out.println();
			}
		} finally
		{
			IOUtils.closeQuietly(in);
		}
	}

}
