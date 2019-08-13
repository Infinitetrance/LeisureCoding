package root.canvas;
import java.util.List;
import static java.lang.Math.*;

/**
 * contains static functions(algorithms) which can manipulate given collection
 * of coordinates(footprint) as per specified 2D Transformation
 *
 */
public class Transformations
{

	/**
	 * Creates a two-dimensional Rotate transform with pivot.
	 * 
	 * @param xCoordinates
	 * @param yCoordinates
	 *            (xCoordinates,yCoordinates) collection of coordinates which we
	 *            want to transform
	 * @param pivotX
	 *            the X coordinate of the rotation pivot point
	 * @param pivotY
	 *            the Y coordinate of the rotation pivot point
	 * @param degree
	 *            the angle of rotation measured in degrees
	 */
	public static void rotate(List<Integer> xCoordinates, List<Integer> yCoordinates, int pivotX, int pivotY,
			double degree)
	{
		if (xCoordinates.size() != yCoordinates.size())
			throw new IllegalArgumentException("Invalid Coordinates, xCoordinates.size() != yCoordinates.size()");

		int x, y, x_, y_;
		degree = Math.toRadians(degree);
		for (int i = 0; i < xCoordinates.size(); i++)
		{
			x = xCoordinates.get(i);
			y = yCoordinates.get(i);

			x_ = (int) Math
					.ceil((x * cos(degree) + y * (-sin(degree)) + (pivotX * (1 - cos(degree)) + pivotY * sin(degree))));

			y_ = (int) Math
					.ceil((x * sin(degree) + y * cos(degree) + (pivotY * (1 - cos(degree)) - pivotX * sin(degree))));

			xCoordinates.set(i, x_);
			yCoordinates.set(i, y_);
		}
	}

	/**
	 * Creates a two-dimensional Translate transform with pivot.
	 * 
	 * @param xCoordinates
	 * @param yCoordinates
	 *            (xCoordinates,yCoordinates) collection of coordinates which we
	 *            want to transform
	 * @param translateX
	 *            Defines the x coordinate of the translation that is added to
	 *            xCoordinates.
	 * @param translateY
	 *            Defines the y coordinate of the translation that is added to
	 *            yCoordinates
	 */
	public static void translate(List<Integer> xCoordinates, List<Integer> yCoordinates, int translateX, int translateY)
	{
		if (xCoordinates.size() != yCoordinates.size())
			throw new IllegalArgumentException("Invalid Coordinates, xCoordinates.size() != yCoordinates.size()");

		for (int i = 0; i < xCoordinates.size(); i++)
		{
			xCoordinates.set(i, xCoordinates.get(i) + translateX);
			yCoordinates.set(i, yCoordinates.get(i) + translateY);
		}
	}

	/**
	 * Creates a two-dimensional Scale transform with pivot/ reference point .
	 * 
	 * @param xCoordinates
	 * @param yCoordinates
	 *            (xCoordinates,yCoordinates) collection of coordinates which we
	 *            want to transform
	 * @param scaleX
	 * @param scaleY
	 *            (scaleX,scaleY) Defines the factor by which coordinates are
	 *            scaled about the (refrenceX,refrenceY) point of the object
	 *            along the X,Y axis of this Object
	 * @param refrenceX
	 * @param refrenceY
	 *            (refrenceX,refrenceY) point with respect to which scaling will
	 *            be done
	 */
	public static void scale(List<Integer> xCoordinates, List<Integer> yCoordinates, double scaleX, double scaleY,
			int referenceX, int referenceY)
	{
		if (xCoordinates.size() != yCoordinates.size())
			throw new IllegalArgumentException("Invalid Coordinates, xCoordinates.size() != yCoordinates.size()");

		int x_, y_;
		for (int i = 0; i < xCoordinates.size(); i++)
		{
			x_ = (int) (xCoordinates.get(i) * scaleX + referenceX * (1.0 - scaleX));
			y_ = (int) (yCoordinates.get(i) * scaleY + referenceY * (1.0 - scaleY));

			xCoordinates.set(i, x_);
			yCoordinates.set(i, y_);
		}
	}
}
