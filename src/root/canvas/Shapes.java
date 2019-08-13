package root.canvas;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

/**
 * contains static functions(algorithms) which can produce the
 * footprint(collection of pixel coordinates representing that shape) of
 * specified shape
 * 
 */
public class Shapes
{

	/**
	 * Generalized Bresenham's Line Drawing Algorithm
	 * 
	 * @param x1
	 * @param y1
	 *            (x1,y1) start point of line
	 * @param x2
	 * @param y2
	 *            (x2,y2) end point of line
	 */
	public static void line_BresenhamAlgorithm(Pen pen, int x1, int y1, int x2, int y2)
	{
		int dx, dy, x, y, d, s1, s2, temp;
		boolean swap = false;
		dx = abs(x2 - x1);
		dy = abs(y2 - y1);

		// both point of line are same
		if (dx == 0 && dy == 0)
		{
			pen.plot(x1, y1);
			return;
		}

		s1 = (int) signum(x2 - x1);
		s2 = (int) signum(y2 - y1);

		/* Check if dx or dy has a greater range */
		/* if dy has a greater range than dx swap dx and dy */
		if (dy > dx)
		{
			temp = dx;
			dx = dy;
			dy = temp;
			swap = true;
		}

		/* Set the initial decision parameter and the initial point */
		d = 2 * dy - dx;
		x = x1;
		y = y1;

		int i;
		for (i = 0; i <= dx; i++)
		{
			pen.plot(x, y);

			while (d >= 0)
			{
				if (swap)
					x = x + s1;
				else
					y = y + s2;
				d = d - 2 * dx;

			}
			if (swap)
				y = y + s2;
			else
				x = x + s1;
			d = d + 2 * dy;
		}
	}

	/**
	 * digital differential analyzer line drawing algorithm uses interpolation
	 * of variables over an interval between start and end point.
	 * 
	 * Bresenham algorithm is less expensive than DDA algorithm as it uses only
	 * addition and subtraction besides that Bresenham algorithm does not round
	 * off but takes the incremental value in its operation where as DDA
	 * algorithm round off the coordinates to integer that is nearest to the
	 * line hence compromising accuracy, however DDA approach is easier to
	 * implement as it involves interpolation technique.
	 * 
	 * @param x1
	 * @param y1
	 *            (x1,y1) start point of line
	 * @param x2S
	 * @param y2
	 *            (x2,y2) end point of line
	 */
	public static void line_DDA(Pen pen, int x1, int y1, int x2, int y2)
	{
		double x = x1, y = y1;

		int step;
		int dx = x2 - x1;
		int dy = y2 - y1;

		if (Math.abs(dx) > Math.abs(dy))
			step = Math.abs(dx);
		else
			step = Math.abs(dy);

		double xnxt = (double) dx / step;
		double ynxt = (double) dy / step;

		pen.plot((int) x, (int) y);
		for (int i = 0; i < step; i++)
		{
			x += xnxt;
			y += ynxt;
			pen.plot((int) x, (int) y);
		}
	}

	public static void rectangle(Pen pen, int x, int y, int width, int height)
	{
		width -= 1;
		height -= 1;
		line_BresenhamAlgorithm(pen, x, y, x + width, y);
		line_BresenhamAlgorithm(pen, x, y, x, y + height);
		line_BresenhamAlgorithm(pen, x, y + height, x + width, y + height);
		line_BresenhamAlgorithm(pen, x + width, y, x + width, y + height);
	}

	/**
	 * [(x1,y1)(TOP-LEFT corner)] [(x2,y2)(TOP-RIGHT corner)]
	 * [(x3,y3)(BOTTOM-LEFT corner)] [(x4,y4)(BOTTOM-RIGHT corner)]
	 * 
	 * @param pen
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	public static void rectangle(Pen pen, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
	{
		line_BresenhamAlgorithm(pen, x1, y1, x2, y2);
		line_BresenhamAlgorithm(pen, x3, y3, x4, y4);
		line_BresenhamAlgorithm(pen, x1, y1, x3, y3);
		line_BresenhamAlgorithm(pen, x2, y2, x4, y4);
	}

	/**
	 * uses polar coordinate system inorder to find a path through the pixel
	 * grid using pixels which are as close as possible to solutions of x^2 +
	 * y^2 = r^2, however mid point algorithm is far efficient than this
	 * approach.
	 * 
	 * @param centerX
	 * @param centerY
	 *            (centerX, centerY) center point of circle
	 * @param radius
	 */
	public static void circle_PolarCoordinates(Pen pen, int centerX, int centerY, int radius)
	{
		int x, y;
		for (double degree = 0; degree <= 360; degree++)
		{
			x = (int) (centerX + radius * Math.cos(degree));
			y = (int) (centerY + radius * Math.sin(degree));
			pen.plot(x, y);
		}
	}

	/**
	 * mid point algorithm draws all eight octants simultaneously, starting from
	 * each cardinal direction (0°, 90°, 180°, 270°) and extends both ways to
	 * reach the nearest multiple of 45° (45°, 135°, 225°, 315°), inorder to
	 * find a path through the pixel grid using pixels which are as close as
	 * possible to solutions of x^2 + y^2 = r^2
	 * 
	 * @param centerX
	 * @param centerY
	 *            (centerX, centerY) center point of circle
	 * @param radius
	 */
	public static void circle_MidPointAlgorithm(Pen pen, int centerX, int centerY, int radius)
	{
		int x, y, p;
		p = 1 - radius;
		x = 0;
		y = radius;
		{
			pen.plot(centerX + x, centerY + y);
			pen.plot(centerX - x, centerY + y);
			pen.plot(centerX + x, centerY - y);
			pen.plot(centerX - x, centerY - y);

			pen.plot(centerX + y, centerY + x);
			pen.plot(centerX - y, centerY + x);
			pen.plot(centerX + y, centerY - x);
			pen.plot(centerX - y, centerY - x);
		}
		while (x < y)
		{
			if (p < 0)
				x++;
			else
			{
				x++;
				y--;
			}
			if (p < 0)
				p = p + (2 * x) + 1;
			else
				p = p + 2 * (x - y) + 1;

			{
				pen.plot(centerX + x, centerY + y);
				pen.plot(centerX - x, centerY + y);
				pen.plot(centerX + x, centerY - y);
				pen.plot(centerX - x, centerY - y);

				pen.plot(centerX + y, centerY + x);
				pen.plot(centerX - y, centerY + x);
				pen.plot(centerX + y, centerY - x);
				pen.plot(centerX - y, centerY - x);
			}
		}
	}

	/**
	 * Bresenham's Ellipse Drawing Algorithm
	 * 
	 * @param centerX
	 * @param centerY
	 *            (centerX, centerY) center point of ellipse
	 * @param radiusX
	 *            radius in x/horizontal direction
	 * @param radiusY
	 *            radius in y/vertical direction
	 */
	public static void ellipse_BresenhamAlgorithm(Pen pen, int centerX, int centerY, int radiusX, int radiusY)
	{
		final int rxsq = radiusX * radiusX;
		final int rysq = radiusY * radiusY;
		final int drxsq = 2 * rxsq;
		final int drysq = 2 * rysq;
		int p;
		int x = 0, y = radiusY;
		int px = 0, py = drxsq * y;
		{
			pen.plot(centerX + x, centerY + y);
			pen.plot(centerX - x, centerY + y);
			pen.plot(centerX + x, centerY - y);
			pen.plot(centerX - x, centerY - y);
		}
		p = (int) (rysq - (rxsq * radiusY) + (0.25 * rxsq));
		while (px < py)
		{
			x++;
			px += drysq;
			if (p < 0)
				p += rysq + px;
			else
			{
				y--;
				py -= drxsq;
				p += rysq + px - py;
			}
			{
				pen.plot(centerX + x, centerY + y);
				pen.plot(centerX - x, centerY + y);
				pen.plot(centerX + x, centerY - y);
				pen.plot(centerX - x, centerY - y);
			}
		}
		p = (int) ((rysq * (x + 0.5) * (x + 0.5)) + (rxsq * (y - 1) * (y - 1)) - (rxsq * rysq));
		while (y > 0)
		{
			y--;
			py -= drxsq;
			if (p > 0)
				p += rxsq - py;
			else
			{
				x++;
				px += drysq;
				p += rxsq - py + px;
			}
			{
				pen.plot(centerX + x, centerY + y);
				pen.plot(centerX - x, centerY + y);
				pen.plot(centerX + x, centerY - y);
				pen.plot(centerX - x, centerY - y);
			}
		}
	}

}
