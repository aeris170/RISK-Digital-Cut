package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIConstants;

public class SpinnerCenterPiece extends DoaImageButton {

	private static final long serialVersionUID = -832996023052328680L;

	public static SpinnerCenterPiece INSTANCE;

	protected String text;
	protected Color textColor;
	protected Color hoverTextColor;

	public SpinnerCenterPiece(DoaVectorF position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor, Color hoverTextColor) {
		super(position, width, height, idleImage, hoverImage);
		if (INSTANCE != null) {
			Scenes.GAME_SCENE.remove(INSTANCE);
		}
		this.text = text;
		this.textColor = textColor;
		this.hoverTextColor = hoverTextColor;
		INSTANCE = this;
	}

	public void setText(String s) {
		text = s;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
		g.setFont(UIConstants.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIConstants.UI_FONT, new DoaVectorF(width * 0.6f, height * 0.6f), text)));
		g.setColor(textColor);
		if (hover) {
			g.setColor(hoverTextColor);
		}
		g.drawString(text, position.x + (width - g.getFontMetrics().stringWidth(text)) / 2f,
		        position.y + (height - g.getFontMetrics().getHeight()) / 2f + g.getFontMetrics().getAscent());
	}
}
