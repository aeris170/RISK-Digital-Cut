package com.pmnm.risk.map.board;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.pmnm.risk.main.Main;

public final class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	private static GameBoard _this = null;

	private GameBoard() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, -1);
	}

	@Override
	public void tick() {
		if (DoaKeyboard.ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("WorldMap"), position.x, position.y, width, height, null);
	}

	public static GameBoard getInstance() {
		return _this == null ? _this = DoaHandler.instantiate(GameBoard.class) : _this;
	}
}