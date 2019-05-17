package com.pmnm.risk.map.board;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.gameui.BlitzButton;

public final class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public static GameBoard INSTANCE;
	
	private GameBoard() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, -1);
		if (INSTANCE != null) {
			DoaHandler.remove(INSTANCE);
		}
		INSTANCE = this;
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MapBackground"), position.x, position.y, width, height, null);
	}
}