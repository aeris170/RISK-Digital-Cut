package main;

import java.awt.Color;
import java.awt.Shape;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;

public class TestObject extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public TestObject(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height, DoaObject.BACK);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("WorldMap"), position.x, position.y, width, height, null);
	}

	@Override
	public Shape getBounds() {
		return null;
	}

}
