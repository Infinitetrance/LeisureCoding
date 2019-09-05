
package lc.kra.system.mouse.event;

import java.util.EventListener;

public interface GlobalMouseListener extends EventListener {
    /**
     * Invoked when a mouse button has been pressed.
     */
	public void mousePressed(GlobalMouseEvent event);
    /**
     * Invoked when a mouse button has been released.
     */
	public void mouseReleased(GlobalMouseEvent event);
	
	/**
     * Invoked when the mouse was moved.
     */
	public void mouseMoved(GlobalMouseEvent event);
	
	/**
     * Invoked when a mouse wheel was scrolled.
     */
	public void mouseWheel(GlobalMouseEvent event);
}