package root.canvas;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * Represents a logical canvas(a parent container) having pixel level read write
 * capability. However this logical canvas will reflect its actual rendering/
 * output through the provided Renderer only, it can not by its own(without
 * Renderer) reflect or render itself over something tangible such frame/window
 * or a file. Although default Renderer is provided which can reflect/ draw/
 * paint current state of canvas over systems's "Command Prompt/ Terminal".
 * 
 */
public class Canvas
{
	/**
	 * each element of "pixels array" represents collection of pixels in a row
	 */
	public BitSet[] pixels;

	private int width, height;
	private final List<Figure> children;;

	public static final int DEFAULT_WIDTH = 79;
	public static final int DEFAULT_HEIGHT = 30;

	public Canvas()
	{
		this(Canvas.DEFAULT_WIDTH, Canvas.DEFAULT_HEIGHT);
	}

	public Canvas(int width, int height)
	{
		this.width = width;
		this.height = height;

		pixels = new BitSet[height];
		for (int y = 0; y < height; y++)
			pixels[y] = new BitSet(width);

		this.children = new ArrayList<Figure>();
	}

	public void setPixel(int x, int y)
	{
		setPixel(x, y, true);
	}

	public void clearPixel(int x, int y)
	{
		setPixel(x, y, false);
	}

	/**
	 * sets pixel at given coordinates in this canvas with given value, however
	 * changes of this call will not reflect over actual target to be rendered
	 * unless {@link #render()} method is called.
	 * 
	 * @param x
	 * @param y
	 * @param value
	 */
	public void setPixel(int x, int y, boolean value)
	{
		if ((y > -1 && y < height) && (x > -1 && x < width))
			this.pixels[y].set(x, value);
		else
			throw new IllegalArgumentException(
					"Pixel out of bounds. x: " + x + ", y: " + y + ". width: " + width + ", height: " + height);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return pixel value at given coordinates in this logical canvas
	 */
	public boolean getPixel(int x, int y)
	{
		if ((y > -1 && y < height) && (x > -1 && x < width))
			return this.pixels[y].get(x);
		else
			throw new IllegalArgumentException(
					"Pixel out of bounds. x: " + x + ", y: " + y + ". width: " + width + ", height: " + height);
	}

	/**
	 * sets all the pixels in this logical canvas to false/clear.
	 */
	public void clear()
	{
		for (int y = 0; y < height; y++)
			pixels[y].clear();
	}

	/**
	 * 
	 * @return width of this logical canvas.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * 
	 * @return the height of this logical canvas.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Add or render the figure at this canvas(parent)
	 * 
	 * @param figure
	 */
	public void add(Figure figure)
	{
		if (figure.footprintX.size() == figure.footprintY.size() && Collections.max(figure.footprintX) < width
				&& Collections.max(figure.footprintY) < height)
		{
			children.add(figure);
			figure.setParent(this);
			for (int i = 0; i < figure.footprintX.size(); i++)
				setPixel(figure.footprintX.get(i), figure.footprintY.get(i), true);
		} else
			throw new IllegalArgumentException("Figure is too big to be contained by Canvas");
	}
}
