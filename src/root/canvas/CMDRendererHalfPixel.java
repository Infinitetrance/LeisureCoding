package root.canvas;

import java.io.IOException;

public class CMDRendererHalfPixel implements Renderer {
	public static final char FORGROUND1 = '▀';
	public static final char FORGROUND2 = '▄';

	public static final char FORGROUND3 = '█';
	public static final char BACKGROUND = ' ';

	/**
	 * alt+220 ▄█ alt+254 ■█ alt+223 ▀█ alt+236 ∞ alt+219 █ .Windows command prompt
	 * by default support 80 characters per line, but on printing 80th character
	 * command prompt automatically inserts new line so we will use only 79
	 * characters per line and than print \n(as 80th character) to go to next line.
	 */
	@Override
	public void render(Canvas canvas) {
		try
		{
			int height = canvas.getHeight();
			if (height % 2 != 0)
				height -= 1;

			clrscr();
			for (int y = 0; y < height; y += 2)
			{
				for (int x = 0; x < canvas.getWidth(); x++)
				{
					if (canvas.getPixel(x, y) && canvas.getPixel(x, y + 1))
						System.out.print(FORGROUND3);

					else if (canvas.getPixel(x, y) && !canvas.getPixel(x, y + 1))
						System.out.print(FORGROUND1);

					else if (!canvas.getPixel(x, y) && canvas.getPixel(x, y + 1))
						System.out.print(FORGROUND2);

					else
						System.out.print(BACKGROUND);
				}
				System.out.println();
			}
			if (height != canvas.getHeight())
			{
				for (int x = 0; x < canvas.getWidth(); x++)
				{
					if (canvas.getPixel(x, height))
						System.out.print(FORGROUND1);
					else
						System.out.print(BACKGROUND);
				}
				System.out.println();
			}
		} catch (InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Carries out the "CLS" command in new terminal with IO pipeline connected to
	 * parent process(terminal) and then terminates newly instantiated terminal
	 * while keeping parent process terminal alive.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void clrscr() throws InterruptedException, IOException {
		new ProcessBuilder("CMD", "/C", "CLS").inheritIO().start().waitFor();
	}

}
