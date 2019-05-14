package com.pmnm.roy.ui.gameui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.DoaUIContainer;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.card.Card;

public class CardPanel extends DoaUIContainer {

	//serial version UID needed;


	public CardPanel() {
		super(1404f, 258f, 488, 558);
		super.show();
	}

	@Override
	public void tick() {
		super.tick();
		
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("scroll"), 1404f, 258f);
		
	}
}