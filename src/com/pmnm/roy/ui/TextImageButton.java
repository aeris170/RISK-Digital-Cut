package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.maths.DoaVector;
import doa.engine.ui.button.DoaUIImageButton;

public class TextImageButton extends DoaUIImageButton {

	private static final long serialVersionUID = -3498878656892515070L;

	protected String text;
	protected Color textColor;
	protected Color hoverTextColor;
	private DoaVector textRect;
	private boolean isCentered;

	private boolean fontNeedsRecalculation;
	private Font textFont;
	private String displayedText;
	private int stringWidth;
	private int stringHeight;

	public TextImageButton(DoaVector position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor, Color hoverTextColor) {
		this(position, width, height, idleImage, hoverImage, text, textColor, hoverTextColor, false);
	}

	public TextImageButton(DoaVector position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor, Color hoverTextColor,
	        boolean isCentered) {
		super(position, width, height, idleImage, hoverImage);
		this.text = text;
		displayedText = Translator.getInstance().getTranslatedString(text).toUpperCase();
		this.textColor = textColor;
		this.hoverTextColor = hoverTextColor;
		textRect = new DoaVector(width - 20f, height - 20f);
		this.isCentered = isCentered;
	}

	@Override
	public void recalibrateBounds() {
		super.recalibrateBounds();
		fontNeedsRecalculation = true;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
		displayedText = Translator.getInstance().getTranslatedString(text).toUpperCase();
		if (fontNeedsRecalculation) {
			textFont = UIInit.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, textRect, displayedText));
			g.setFont(textFont);
			FontMetrics fm = g.getFontMetrics();
			stringWidth = fm.stringWidth(displayedText);
			stringHeight = fm.getHeight();
			fontNeedsRecalculation = false;
		}
		g.setFont(textFont);
		g.setColor(textColor);
		if (hover) {
			g.setColor(hoverTextColor);
		}
		if (isCentered) {
			g.drawString(displayedText, position.x + (dimensions.x - stringWidth) / 2f, dimensions.y + position.y - stringHeight / 2f);
		} else {
			g.drawString(displayedText, position.x + 20, position.y + dimensions.y - 17);
		}
	}
}