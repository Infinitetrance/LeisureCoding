
package lc.kra.system.mouse.event;

public class GlobalMouseAdapter implements GlobalMouseListener {
    /**
     * Invoked when a mouse button has been pressed.
     */
	@Override public void mousePressed(GlobalMouseEvent event) {}
	/**
     * Invoked when a mouse button was released.
     */
	@Override public void mouseReleased(GlobalMouseEvent event) {}
	
	/**
     * Invoked when the mouse was moved.
     */
	@Override public void mouseMoved(GlobalMouseEvent event) {}
	
	/**
     * Invoked when a mouse wheel was scrolled.
     */
	@Override public void mouseWheel(GlobalMouseEvent event) {}
}