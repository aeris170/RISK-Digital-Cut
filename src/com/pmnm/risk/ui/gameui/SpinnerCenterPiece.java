package com.pmnm.risk.ui.gameui;

import java.awt.Color;
import java.awt.Font;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.risk.ui.UIInit;

public class SpinnerCenterPiece extends DoaImageButton {

	private static final long serialVersionUID = -832996023052328680L;

	protected String text;
	protected Color textColor;
	protected Color hoverTextColor;

	public SpinnerCenterPiece(DoaVectorF position, int width, int height, DoaSprite idleImage, DoaSprite hoverImage, String text, Color textColor, Color hoverTextColor) {
		super(position, width, height, idleImage, hoverImage);
		this.text = text;
		this.textColor = textColor;
		this.hoverTextColor = hoverTextColor;
	}

	public void setText(String s) {
		text = s;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, new DoaVectorF(width * 0.6f, height * 0.6f), text)));
		g.setColor(textColor);
		if (hover) {
			g.setColor(hoverTextColor);
		}
		g.drawString(text, position.x + (width - g.getFontMetrics().stringWidth(text)) / 2f,
		        position.y + (height - g.getFontMetrics().getHeight()) / 2f + g.getFontMetrics().getAscent());
	}
}
