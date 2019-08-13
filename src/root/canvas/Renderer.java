package root.canvas;
@FunctionalInterface
public interface Renderer
{
	/**
	 * draws or paints this logical canvas over actual Frame, Console or file as
	 * defined in @Renderer.
	 */
	public void render(Canvas canvas);
}
