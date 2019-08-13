package root.canvas;
@FunctionalInterface
public interface Pen
{
	/**
	 * Defines the action to be performed over given coordinate points(which
	 * collectively represents footprint of some Figure or Shape). Actions to be
	 * performed may be adding them to some collection, painting them over some
	 * canvas/ frame, printing raw values to console etc.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public void plot(int x, int y);
}
