package root.canvas;
import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Figure
{
	private int x, y;
	private int width;
	private int height;

	/**
	 * values in cornersX and cornersY are added in following sequence
	 * (x1,y1)(TOP-LEFT corner)] [(x2,y2)(TOP-RIGHT corner)]
	 * [(x3,y3)(BOTTOM-LEFT corner)] [(x4,y4)(BOTTOM-RIGHT corner)]
	 */
	private final List<Integer> cornersX = new ArrayList<Integer>();
	private final List<Integer> cornersY = new ArrayList<Integer>();

	public Rectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		Shapes.rectangle((xCoordinate, yCoordinate) ->
		{
			Rectangle.this.footprintX.add(xCoordinate);
			Rectangle.this.footprintY.add(yCoordinate);
		}, x, y, width, height);

		cornersX.add(x);
		cornersY.add(y);
		cornersX.add(x + width - 1);
		cornersY.add(y);
		cornersX.add(x);
		cornersY.add(y + height - 1);
		cornersX.add(x + width - 1);
		cornersY.add(y + height - 1);
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public List<Integer> getCornersX()
	{
		return cornersX;
	}

	public List<Integer> getCornersY()
	{
		return cornersY;
	}

	public Rectangle translate(int translateX, int translateY)
	{
		// erasing rectangle at current position first before drawing at next
		// translated position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// updating layout of object
		this.x += translateX;
		this.y += translateY;

		// manipulating current footprint of figure as per translation
		Transformations.translate(footprintX, footprintY, translateX, translateY);

		// drawing the figure at new location on canvas as per translation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);

		return this;
	}

	public Rectangle rotate(int degree)
	{
		// pivot point is center of figure
		rotate(x + (width / 2) - 1, y + (height / 2) - 1, degree);
		return this;
	}

	public Rectangle rotate(int pivotX, int pivotY, int degree)
	{
		// erasing rectangle at current position first before drawing at next
		// rotated position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// manipulating current corner points of rectangle as per rotation
		// because on manipulation of complete footprint as per rotation
		// transformation footprint is manipulated in such a way that shape of
		// rectangle is deformed due to floating point errors, so will rotate
		// corner points only and on basis of new corner points will compute new
		// footprint of rectangle.
		Transformations.rotate(cornersX, cornersY, pivotX, pivotY, degree);

		// removing old footprint
		footprintX.clear();
		footprintY.clear();

		// computing new footprint on basis of transformed corner points
		Shapes.rectangle((xCoordinate, yCoordinate) ->
		{
			Rectangle.this.footprintX.add(xCoordinate);
			Rectangle.this.footprintY.add(yCoordinate);
		}, cornersX.get(0), cornersY.get(0), cornersX.get(1), cornersY.get(1), cornersX.get(2), cornersY.get(2),
				cornersX.get(3), cornersY.get(3));

		// drawing the figure at new location on canvas as per rotation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);
		return this;
	}

	public Rectangle scale(double scaleX, double scaleY)
	{
		// reference point is center of figure
		scale(scaleX, scaleY, x + (width / 2) - 1, y + (height / 2) - 1);
		return this;
	}

	public Rectangle scale(double scaleX, double scaleY, int referenceX, int referenceY)
	{
		// erasing rectangle at current position first before drawing at next
		// scaled position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// updating dimensions of rectangle
		this.width *= scaleX;
		this.height *= scaleY;

		// manipulating current corner points of rectangle as per scaling
		Transformations.scale(cornersX, cornersY, scaleX, scaleY, referenceX, referenceY);

		// removing old footprint
		footprintX.clear();
		footprintY.clear();

		// computing new footprint on basis of transformed corner points
		Shapes.rectangle((xCoordinate, yCoordinate) ->
		{
			Rectangle.this.footprintX.add(xCoordinate);
			Rectangle.this.footprintY.add(yCoordinate);
		}, cornersX.get(0), cornersY.get(0), cornersX.get(1), cornersY.get(1), cornersX.get(2), cornersY.get(2),
				cornersX.get(3), cornersY.get(3));

		// drawing the figure at new location on canvas as per translation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);

		return this;
	}

	@Override
	public String toString()
	{
		return "Rectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ",\ncornersX=" + cornersX
				+ ",\ncornersY=" + cornersY + "]";
	}

}
