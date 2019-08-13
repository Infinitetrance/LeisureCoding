package root.canvas;
import java.util.Arrays;
import java.util.List;

public class Line extends Figure
{
	private int[] endCoordinates;

	public Line(int startX, int startY, int endX, int endY)
	{
		endCoordinates = new int[]
		{ startX, startY, endX, endY };

		Shapes.line_BresenhamAlgorithm((a, b) ->
		{
			footprintX.add(a);
			footprintY.add(b);
		}, startX, startY, endX, endY);
	}

	/**
	 * 
	 * @return end points in array sequenced as startX, int startY, int endX,
	 *         int endY
	 */
	public int[] getEndPoints()
	{
		return endCoordinates;
	}

	@Override
	public Figure translate(int translateX, int translateY)
	{
		// erasing Figure at current position first before drawing at next
		// transformed position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// manipulating footprint of figure as per transformation operation
		Transformations.translate(footprintX, footprintY, translateX, translateY);

		// updating layout
		endCoordinates[0] += translateX;
		endCoordinates[1] += translateY;
		endCoordinates[2] += translateX;
		endCoordinates[3] += translateY;

		// drawing the figure at new location on parent as computed by applying
		// transformation operation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);
		return this;
	}

	@Override
	public Figure rotate(int degree)
	{
		rotate((endCoordinates[0] + endCoordinates[2]) / 2, (endCoordinates[1] + endCoordinates[3]) / 2, degree);
		return this;
	}

	@Override
	public Figure rotate(int pivotX, int pivotY, int degree)
	{
		// erasing Figure at current position first before drawing at next
		// transformed position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// manipulating layout of figure as per transformation operation
		List<Integer> start_endX = Arrays.asList(endCoordinates[0], endCoordinates[2]),
				start_endY = Arrays.asList(endCoordinates[1], endCoordinates[3]);
		Transformations.rotate(start_endX, start_endY, pivotX, pivotY, degree);

		// updating layout
		endCoordinates[0] = start_endX.get(0);
		endCoordinates[1] = start_endY.get(0);
		endCoordinates[2] = start_endX.get(1);
		endCoordinates[3] = start_endY.get(1);

		// removing old footprint
		footprintX.clear();
		footprintY.clear();

		// recomputing the footprint of figure as per updated layout
		Shapes.line_BresenhamAlgorithm((a, b) ->
		{
			footprintX.add(a);
			footprintY.add(b);
		}, endCoordinates[0], endCoordinates[1], endCoordinates[2], endCoordinates[3]);

		// drawing the figure at new location on parent as computed by applying
		// transformation operation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);
		return this;
	}

	@Override
	public Figure scale(double scaleX, double scaleY)
	{
		scale(scaleX, scaleY, (endCoordinates[0] + endCoordinates[2]) / 2, (endCoordinates[1] + endCoordinates[3]) / 2);
		return this;
	}

	@Override
	public Figure scale(double scaleX, double scaleY, int referenceX, int referenceY)
	{
		if (scaleX != scaleY)
			throw new IllegalArgumentException("scaleX and scaleY must be same for Figure type Line");

		// erasing Figure at current position first before drawing at next
		// transformed position
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), false);

		// manipulating layout of figure as per transformation operation
		List<Integer> start_endX = Arrays.asList(endCoordinates[0], endCoordinates[2]),
				start_endY = Arrays.asList(endCoordinates[1], endCoordinates[3]);
		Transformations.scale(start_endX, start_endY, scaleX, scaleY, referenceX, referenceY);

		// updating layout
		endCoordinates[0] = start_endX.get(0);
		endCoordinates[1] = start_endY.get(0);
		endCoordinates[2] = start_endX.get(1);
		endCoordinates[3] = start_endY.get(1);

		// removing old footprint
		footprintX.clear();
		footprintY.clear();

		// recomputing the footprint of figure as per updated layout
		Shapes.line_BresenhamAlgorithm((a, b) ->
		{
			footprintX.add(a);
			footprintY.add(b);
		}, endCoordinates[0], endCoordinates[1], endCoordinates[2], endCoordinates[3]);

		// drawing the figure at new location on parent as computed by applying
		// transformation operation
		for (int i = 0; i < this.footprintX.size(); i++)
			parent.setPixel(this.footprintX.get(i), this.footprintY.get(i), true);
		return this;
	}

	@Override
	public String toString()
	{
		return "x1: " + endCoordinates[0] + ", y1: " + endCoordinates[1] + "\nx2: " + endCoordinates[2] + ", y1: "
				+ endCoordinates[3];
	}
}
