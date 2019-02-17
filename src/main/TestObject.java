package main;

import java.awt.Color;
import java.awt.Shape;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;

public class TestObject extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public TestObject(Float x, Float y) {
		super(x, y);
	}

	@Override
	public synchronized void tick() {
		if (DoaKeyboard.ESCAPE) {
			System.exit(0);
		}
		if (DoaKeyboard.A) {
			position.x -= 6f;
		}
		if (DoaKeyboard.D) {
			position.x += 6f;
		}
		if (DoaKeyboard.W) {
			position.y -= 6f;
		}
		if (DoaKeyboard.S) {
			position.y += 6f;
		}
	}

	@Override
	public synchronized void render(DoaGraphicsContext g) {
		g.setColor(Color.RED);
		g.fillRect(position.x, position.y, 200, 200);
	}

	@Override
	public Shape getBounds() {
		return null;
	}

}
