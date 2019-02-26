package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.input.DoaMouse;

public class DebugPanel extends DoaObject {

	private static final long serialVersionUID = 6850865807972514073L;

	private int fontSize = 20;
	private int textCount = 4;

	public DebugPanel() {
		super(0f, 0f, DoaObject.STATIC_FRONT);
	}

	@Override
	public void tick() {
		if (DoaKeyboard.ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		g.setColor(Color.WHITE);
		textCount = 4;
		drawString(g, "ZOOM: " + DoaMouse.WHEEL);
	}

	@Override
	public Shape getBounds() {
		return null;
	}

	private void drawString(DoaGraphicsContext g, String s) {
		g.setTransform(new AffineTransform());
		g.drawString(s, 0, fontSize * (textCount + 1d));
		textCount++;
	}
}
