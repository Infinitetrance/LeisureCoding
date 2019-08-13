package root.canvas;
import java.util.ArrayList;
import java.util.List;

public abstract class Figure
{
	/**
	 * footprint is collection of coordinates(pixel positions) which represent
	 * this figure
	 */
	public final List<Integer> footprintX = new ArrayList<Integer>(), footprintY = new ArrayList<Integer>();
	/**
	 * the parent over which footprint of this Figure is rendered or drawn
	 */
	protected Canvas parent;

	/**
	 * 
	 * @return the parent over which footprint of this Figure is rendered or
	 *         drawn
	 */
	public Canvas getParent()
	{
		return parent;
	}

	public void setParent(Canvas parent)
	{
		this.parent = parent;
	}

	/**
	 * Creates a two-dimensional Translate transform with pivot.
	 * 
	 * @param translateX
	 * @param translateY
	 * @return
	 */
	public abstract Figure translate(int translateX, int translateY);

	/**
	 * Creates a two-dimensional Rotate transform with center of Figure as pivot
	 * point.
	 * 
	 * @param degree
	 * @return
	 */
	public abstract Figure rotate(int degree);

	/**
	 * Creates a two-dimensional Rotate transform over given pivot point.
	 * 
	 * @param pivotX
	 * @param pivotY
	 * @param degree
	 * @return
	 */
	public abstract Figure rotate(int pivotX, int pivotY, int degree);

	/**
	 * Creates a two-dimensional Scale transform with center of Figure as pivot/
	 * reference point.
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @return
	 */
	public abstract Figure scale(double scaleX, double scaleY);

	/**
	 * Creates a two-dimensional Scale transform with pivot/ reference point.
	 * Consider that scaleX and scaleY are(must be) same for Figure type Circle
	 * and Line. Also Consider that referenceX and referenceY are unused for
	 * scale transformation on Figure type Circle.
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param referenceX
	 * @param referenceY
	 * @return
	 */
	public abstract Figure scale(double scaleX, double scaleY, int referenceX, int referenceY);
}
