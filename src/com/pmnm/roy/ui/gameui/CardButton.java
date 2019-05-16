package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.card.Card;
import com.pmnm.risk.card.CardType;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIInit;

public class CardButton extends DoaImageButton {

	private static final long serialVersionUID = -3852059675270216284L;

	private BufferedImage idleImage;
	private BufferedImage cardTex;

	private CardType cardType;
	private String cardName;

	private boolean selected = false;

	public CardButton(DoaVectorF position, Integer width, Integer height, BufferedImage idleImage) {
		super(position, width, height, idleImage);
		this.idleImage = idleImage;
		setzOrder(1000);
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isEnabled) {
			g.drawImage(idleImage, position.x, position.y, width, height);

			BufferedImage cardTypeSprite = DoaSprites.get(cardType.toString());
			g.drawImage(cardTypeSprite, position.x + (idleImage.getWidth() - cardTypeSprite.getWidth()) / 2,
			        position.y + idleImage.getHeight() * 0.9f - cardTypeSprite.getHeight(), cardTypeSprite.getWidth(), cardTypeSprite.getHeight());

			BufferedImage nameHolder = DoaSprites.get("provinceNameHolder");
			g.drawImage(nameHolder, position.x + (idleImage.getWidth() - nameHolder.getWidth() / 2) / 2,
			        position.y + (idleImage.getHeight() - nameHolder.getHeight()) / 2, nameHolder.getWidth() / 2, nameHolder.getHeight());
			g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT,
			        new DoaVectorF(nameHolder.getWidth() / 2 * 0.95f, nameHolder.getHeight()), cardName.toUpperCase())));
			FontMetrics fm = g.getFontMetrics();
			g.setColor(UIInit.FONT_COLOR);
			fm = g.getFontMetrics();
			g.drawString(cardName.toUpperCase(),
			        position.x + ((idleImage.getWidth() - nameHolder.getWidth()) / 2 + (nameHolder.getWidth() - fm.stringWidth(cardName)) / 2f) * 0.5f,
			        position.y + (idleImage.getHeight() / 2) * 1.03f);

			g.drawImage(cardTex, position.x + (idleImage.getWidth() - cardTex.getWidth()) / 2d, position.y * 1.06f);

			g.pushComposite();
			if (selected) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				g.drawImage(DoaSprites.get("cardSelected"), position.x, position.y, width, height);
			} else if (hover) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				g.drawImage(DoaSprites.get("cardHover"), position.x, position.y, width, height);
			}
			g.popComposite();
		}
	}

	public void select() {
		selected = true;
	}

	public void deselect() {
		selected = false;
	}

	public void configure(Card card) {
		cardType = card.getType();
		cardName = card.getProvince().getName();
		cardTex = card.getTex();
	}
}