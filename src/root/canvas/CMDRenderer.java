package root.canvas;
import java.io.IOException;

public class CMDRenderer implements Renderer
{
	/**
	 * alt+220 ▄█ alt+254 ■█ alt+223 ▀█ alt+236 ∞ alt+219 █ .Windows command
	 * prompt by default support 80 characters per line, but on printing 80th
	 * character command prompt automatically inserts new line so we will use
	 * only 79 characters per line and than print \n(as 80th character) to go to
	 * next line.
	 */
	public static final char FORGROUND = '█';
	public static final char BACKGROUND = ' ';

	@Override
	public void render(Canvas canvas)
	{
		try
		{
			clrscr();
			for (int y = 0; y < canvas.getHeight(); y++)
			{
				for (int x = 0; x < canvas.getWidth(); x++)
				{
					if (canvas.getPixel(x, y))
						System.out.print(FORGROUND);
					else
						System.out.print(BACKGROUND);
				}
				System.out.println();
			}
		}
		catch (InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Carries out the "CLS" command in new terminal with IO pipeline connected
	 * to parent process(terminal) and then terminates newly instantiated
	 * terminal while keeping parent process terminal alive.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void clrscr() throws InterruptedException, IOException
	{
		new ProcessBuilder("CMD", "/C", "CLS").inheritIO().start().waitFor();
	}

}
