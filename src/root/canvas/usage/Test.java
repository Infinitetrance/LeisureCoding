package root.canvas.usage;

import java.util.ArrayList;
import java.util.List;

import root.canvas.CMDRendererHalfPixel;
import root.canvas.Canvas;
import root.canvas.Circle;
import root.canvas.Figure;
import root.canvas.Line;
import root.canvas.Rectangle;
import root.canvas.Renderer;

public class Test {

	public static void main(String[] args) {
		Renderer rend = new CMDRendererHalfPixel();
		Canvas canvas = new Canvas(150, 40);
		List<Figure> l = new ArrayList<>();

		l.add(new Circle(10, 10, 5));

		l.add(new Line(18, 10, 26, 10));
		l.add(new Line(22, 2, 22, 19));

		l.add(new Rectangle(28, 2, 10, 10));

		l.forEach(canvas::add);
		rend.render(canvas);

	}

}
