package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;

public class TextImageButton extends DoaImageButton {

	private static final long serialVersionUID = -3498878656892515070L;

	protected String text;
	protected Color textColor;
	protected Color hoverTextColor;
	private DoaVectorF textRect;
	private boolean isCentered;

	public TextImageButton(DoaVectorF position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor,
	        Color hoverTextColor) {
		this(position, width, height, idleImage, hoverImage, text, textColor, hoverTextColor, false);
	}

	public TextImageButton(DoaVectorF position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor,
	        Color hoverTextColor, boolean isCentered) {
		super(position, width, height, idleImage, hoverImage);
		this.text = text;
		this.textColor = textColor;
		this.hoverTextColor = hoverTextColor;
		textRect = new DoaVectorF(width - 20f, height - 20f);
		this.isCentered = isCentered;
	}

	public void setText(String s) {
		text = s;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
		String s = Translator.getInstance().getTranslatedString(text).toUpperCase();
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, textRect, s)));
		FontMetrics fm = g.getFontMetrics();
		g.setColor(textColor);
		if (hover) {
			g.setColor(hoverTextColor);
		}
		if (isCentered) {
			g.drawString(s, position.x + (width - fm.stringWidth(s)) / 2d, height + position.y - fm.getHeight() / 2d);
		} else {
			g.drawString(s, position.x + 20, position.y + height - 17);
		}
	}
}