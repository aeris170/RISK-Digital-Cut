package com.pmnm.risk.map.board;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.scene.DoaObject;
import com.pmnm.risk.main.Main;

public final class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	private GameBoard() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, -1);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MapBackground"), position.x, position.y, width, height, null);
	}
}