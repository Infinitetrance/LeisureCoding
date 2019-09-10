package root.asciiBanner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ASCIIBannerTest {

	public static void main(String[] args) throws Exception {
		ASCIIBanner.print(" Java ");
		ASCIIBanner.print('.', ' ', " Java ");
		ASCIIBanner.print('X', ' ', " Java ");
		ASCIIBanner.print(' ', '█', " Java ");
		ASCIIBanner.print(' ', '▄', " Java ");
		ASCIIBanner.print(1, 4, "JAVA", " ", " JAVA ");

		Path binaryImage = Paths.get(ASCIIBannerTest.class.getResource("bio79x39").toURI());
		ASCIIBanner.printBinaryImage(new String(Files.readAllBytes(binaryImage)), 'x', ' ');
	}

}
