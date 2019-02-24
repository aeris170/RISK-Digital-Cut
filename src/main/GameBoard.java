package main;

import java.awt.Shape;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;

public class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public GameBoard(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height, DoaObject.BACK);
	}

	@Override
	public void tick() {
		// TICKING BOARD MAKES NO SENSE! REMOVE THIS LINE BEFORE RELEASE!
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("DummyMap"), position.x, position.y, width, height, null);
	}

	@Override
	public Shape getBounds() {
		return null;
	}

}
