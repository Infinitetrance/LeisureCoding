package root.canvas;
import java.util.Arrays;
import java.util.List;

public class Circle extends Figure
{
	private int radius;
	private int centerX, centerY;

	public Circle(int centerX, int centerY, int radius)
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;

		Shapes.circle_MidPointAlgorithm((x, y) ->
		{
			Circle.this.footprintX.add(x);
			Circle.this.footprintY.add(y);
		}, centerX, centerY, radius);
	}

	public int getCenterX()
	{
		return centerX;
	}

	public int getCenterY()
	{
		return centerY;
	}

	public int getRadius()
	{
		return radius;
	}

	@Override
	public Figure translate(int translateX, int translateY)
	{
		// erasing Figure at current position first before drawing at next
		// transformed position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// updating layout
		this.centerX += translateX;
		this.centerY += translateY;

		// manipulating footprint of figure as per transformation operation
		Transformations.translate(footprintX, footprintY, translateX, translateY);

		// drawing the figure at new location on parent as computed by applying
		// transformation operation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);

		return this;
	}

	public Figure rotate(int degree)
	{
		return rotate(centerX, centerY, degree);
	}

	@Override
	public Figure rotate(int pivotX, int pivotY, int degree)
	{
		// erasing Figure at current position first before drawing at next
		// transformed position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// computing new center point as per transformation
		List<Integer> cx, cy;
		Transformations.rotate(cx = Arrays.asList(centerX), cy = Arrays.asList(centerY), pivotX, pivotY, degree);

		// updating layout
		this.centerX = cx.get(0);
		this.centerY = cy.get(0);

		// removing old footprint
		footprintX.clear();
		footprintY.clear();

		// recomputing the footprint of figure as per updated layout
		Shapes.circle_MidPointAlgorithm((a, b) ->
		{
			footprintX.add(a);
			footprintY.add(b);
		}, cx.get(0), cy.get(0), radius);

		// drawing the figure at new location on parent as computed by applying
		// transformation operation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);
		return this;
	}

	public Figure scale(double scaleX, double scaleY)
	{
		return scale(scaleX, scaleY, centerX, centerY);
	}

	@Override
	public Figure scale(double scaleX, double scaleY, int referenceX, int referenceY)
	{
		if (scaleX != scaleY)
			throw new IllegalArgumentException("scaleX and scaleY must be same for Figure type Circle");

		// erasing Figure at current position first before drawing at next
		// transformed position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// updating layout
		radius *= scaleX;

		// removing old footprint
		footprintX.clear();
		footprintY.clear();

		// recomputing the footprint of figure as per updated layout
		Shapes.circle_MidPointAlgorithm((a, b) ->
		{
			footprintX.add(a);
			footprintY.add(b);
		}, centerX, centerY, radius);

		// drawing the figure at new location on parent as computed by applying
		// transformation operation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);
		return this;
	}

	@Override
	public String toString()
	{
		return "Circle [radius=" + radius + ", centerX=" + centerX + ", centerY=" + centerY + "]";
	}

}
