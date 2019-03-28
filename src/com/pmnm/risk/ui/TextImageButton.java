package com.pmnm.risk.ui;

import java.awt.Color;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.maths.DoaVectorF;
import com.doa.ui.action.DoaUIAction;
import com.doa.ui.button.DoaImageButton;

public class TextImageButton extends DoaImageButton {

	private static final long serialVersionUID = -3498878656892515070L;

	private String text;
	private Color textColor;
	private Color hoverTextColor;

	public TextImageButton(DoaVectorF position, Integer width, Integer height, DoaUIAction action, DoaSprite idleImage, DoaSprite hoverImage, String text,
	        Color textColor, Color hoverTextColor) {
		super(position, width, height, action, idleImage, hoverImage);
		this.text = text;
		this.textColor = textColor;
		this.hoverTextColor = hoverTextColor;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
		g.setFont(UIInit.UI_FONT);
		g.setColor(textColor);
		if (hover) {
			g.setColor(hoverTextColor);
		}
		g.drawString(text, position.x + 20, position.y + height - 17);
	}
}