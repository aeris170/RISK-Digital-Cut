package main;

import java.awt.Color;
import java.awt.Shape;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;

public class TestObject extends DoaObject {

	public TestObject(Float x, Float y) {
		super(x, y);
	}

	@Override
	public void tick() {
		position.x += 0.1f;
		if (DoaKeyboard.ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.setColor(Color.RED);
		g.fillRect(position.x, position.y, 20, 20);
	}

	@Override
	public Shape getBounds() {
		return null;
	}

}
