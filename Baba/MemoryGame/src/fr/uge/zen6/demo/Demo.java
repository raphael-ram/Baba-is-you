package src.fr.uge.zen6.demo;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;


class Area {
	private Ellipse2D.Float ellipse = new Ellipse2D.Float(0, 0, 0, 0);

	void draw(ApplicationContext context, float x, float y) {
		context.renderFrame(graphics -> {
			// hide the previous rectangle
			graphics.setColor(Color.ORANGE);
			graphics.fill(ellipse);

			// show a new ellipse at the position of the pointer
			graphics.setColor(Color.MAGENTA);
			ellipse = new Ellipse2D.Float(x - 20, y - 20, 40, 40);
			graphics.fill(ellipse);
		});
	}
}

public class Demo {

	private static void checkRange(double min, double value, double max) {
		if (value < min || value > max) {
			throw new IllegalArgumentException("Invalid coordinate: " + value);
		}
	}

	public static void main(String[] args) {
		Application.run(Color.ORANGE, context -> {
			var counter = 0;
			var screenInfo = context.getScreenInfo();
			var width = screenInfo.width();
			var height = screenInfo.height();
			System.out.println("size of the screen (" + width + " x " + height + ")");

			context.renderFrame(graphics -> {
				graphics.setColor(Color.ORANGE);
				graphics.fill(new Rectangle2D.Float(0, 0, width, height));
			});

			var area = new Area();
			while (true) {
				var event = context.pollOrWaitEvent(10);
				if (event == null) {
					continue;
				}
				switch (event) {
					case KeyboardEvent keyboardEvent -> {
						var action = keyboardEvent.action();
						switch (action) {
							case KEY_PRESSED -> {
								System.out.println(keyboardEvent.key());
							}
							case KEY_RELEASED -> {
								counter++;
								if (counter == 10) {
									context.dispose();
									return;
								}
							}
						}
					}
					case PointerEvent pointerEvent -> {
						var location = pointerEvent.location();
						checkRange(0, location.x(), width);
						checkRange(0, location.y(), height);
						area.draw(context, location.x(), location.y());
					}
				}

			}
		});
	}

}
