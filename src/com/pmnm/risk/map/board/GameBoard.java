package com.pmnm.risk.map.board;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;
import com.pmnm.risk.exceptions.RiskSingletonInstantiationException;

public class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public static GameBoard INSTANCE;

	public GameBoard(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height, DoaObject.BACK);
		if (INSTANCE != null) {
			DoaHandler.remove(this);
			throw new RiskSingletonInstantiationException(getClass());
		}
		INSTANCE = this;
	}

	@Override
	public void tick() {
		if (DoaKeyboard.ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		// g.drawImage(DoaSprites.get("WorldMap"), position.x, position.y, width,
		// height, null);
	}
}