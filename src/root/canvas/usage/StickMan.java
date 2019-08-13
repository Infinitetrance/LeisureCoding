package root.canvas.usage;
import java.util.ArrayList;
import java.util.List;

import root.canvas.CMDRendererHalfPixel;
import root.canvas.Canvas;
import root.canvas.Circle;
import root.canvas.Figure;
import root.canvas.Line;
import root.canvas.Renderer;

/**
 * A moving stickman
 */
public class StickMan {

	public static void main(String[] args) throws InterruptedException {
		Renderer rend = new CMDRendererHalfPixel();
		Canvas canvas = new Canvas(150, 40);
		List<Figure> l = new ArrayList<>();

		// Head
		l.add(new Circle(10, 10, 5));
		// Left Eye
		l.add(new Circle(7, 8, 1));
		// Right Eye
		l.add(new Circle(13, 8, 1));
		
		// Mouth 
		l.add(new Line(8, 12, 12, 12));
		// Torso
		l.add(new Line(10, 15, 10, 30));
		// Hands
		l.add(new Line(2, 23, 18, 23));
		// Legs
		l.add(new Line(10, 30, 2, 37));
		l.add(new Line(10, 30, 18, 37));
		
		// Add StickMan to Canvas
		l.forEach(canvas::add);
		rend.render(canvas);

		// Move/walk StickMan
		for (int i = 0; i < 20; i++) {
			l.forEach(e -> e.translate(3, 0));
			rend.render(canvas);
			Thread.sleep(500);
		}

	}

}
