package com.pmnm.risk.map.board;

import com.pmnm.risk.main.Main;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaSpriteRenderer;
import lombok.NonNull;
import pmnm.risk.game.databasedimpl.MapData;

public final class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public GameBoard(@NonNull MapData data) {
		DoaSpriteRenderer r = new DoaSpriteRenderer();
		r.setSprite(data.getBackgroundImage(), false);
		r.setOffset(new DoaVector(0, 0));
		r.setDimensions(new DoaVector(Main.WINDOW_WIDTH - 1f, Main.WINDOW_HEIGHT - 1f));
		addComponent(r);
	}
}