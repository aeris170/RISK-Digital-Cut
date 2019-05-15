package com.pmnm.roy.ui.gameui;

import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.doa.ui.DoaUIContainer;
import com.pmnm.risk.card.Card;
import com.pmnm.risk.main.GameManager;
import com.pmnm.roy.ui.CardButton;

public class CardPanel extends DoaUIContainer {

	private static final DoaVectorI CARD_BUTTON_SIZE = new DoaVectorI(126, 244);
	// serial version UID needed;

	List<CardButton> buttonList = new ArrayList<>();

	public CardPanel() {
		super(881f, 258f, 1011, 558);
		super.show();
		for (int i = 0; i < 6; i++) {
			buttonList
			        .add(DoaHandler.instantiate(CardButton.class, new DoaVectorF(1012f + i * 138, 417f), CARD_BUTTON_SIZE.x, CARD_BUTTON_SIZE.y, DoaSprites.get("card")));
		}
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("scroll"), 881f, 258f);
		// button x934, y507, wh 62
		// text x937, y450, w63, h61
		// leftmost card x1012, y417, w126, h244
		// coordinate difference btw two cards' origins x138

	}

	public void updateCards() {
		buttonList.forEach(b -> b.hide());
		List<Card> cardList = GameManager.INSTANCE.currentPlayer.getCards();
		for (int i = 0; i < cardList.size(); i++) {
			CardButton curButton = buttonList.get(i);
			curButton.configure(cardList.get(i));
			curButton.show();
		}

	}
}