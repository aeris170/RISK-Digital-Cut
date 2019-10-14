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

	private boolean fontNeedsRecalculation;
	private Font textFont;
	private String displayedText;
	private int stringWidth;
	private int stringHeight;

	public TextImageButton(DoaVectorF position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor, Color hoverTextColor) {
		this(position, width, height, idleImage, hoverImage, text, textColor, hoverTextColor, false);
	}

	public TextImageButton(DoaVectorF position, int width, int height, BufferedImage idleImage, BufferedImage hoverImage, String text, Color textColor, Color hoverTextColor,
	        boolean isCentered) {
		super(position, width, height, idleImage, hoverImage);
		this.text = text;
		displayedText = Translator.getInstance().getTranslatedString(text).toUpperCase();
		this.textColor = textColor;
		this.hoverTextColor = hoverTextColor;
		textRect = new DoaVectorF(width - 20f, height - 20f);
		this.isCentered = isCentered;
	}

	public void setText(String s) {
		text = s;
		displayedText = Translator.getInstance().getTranslatedString(text).toUpperCase();
	}

	@Override
	public void recalibrateBounds() {
		super.recalibrateBounds();
		fontNeedsRecalculation = true;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
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
			g.drawString(displayedText, position.x + (width - stringWidth) / 2d, height + position.y - stringHeight / 2d);
		} else {
			g.drawString(displayedText, position.x + 20, position.y + height - 17);
		}
	}
}