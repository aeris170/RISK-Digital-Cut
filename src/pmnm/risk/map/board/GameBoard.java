package pmnm.risk.map.board;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaSpriteRenderer;
import lombok.NonNull;
import pmnm.risk.map.MapData;

public final class GameBoard extends DoaObject {

	private static final long serialVersionUID = -781062122233404639L;

	public GameBoard(@NonNull MapData data) {
		DoaSpriteRenderer r = new DoaSpriteRenderer();
		r.setSprite(data.getBackgroundImage(), false);
		r.setOffset(new DoaVector(0, 0));
		r.setDimensions(new DoaVector(1920, 1080));
		addComponent(r);
	}
}
