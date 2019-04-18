package com.pmnm.risk.ui.gameui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.ui.button.DoaImageButton;

public class BlitzButton extends DoaImageButton {

	private static final long serialVersionUID = -4633067220666124307L;

	private DoaSprite idleImage;
	private DoaSprite hoverImage;
	private DoaSprite clickImage;

	public BlitzButton(Float x, Float y, Integer width, Integer height, DoaSprite idleImage, DoaSprite hoverImage) {
		super(x, y, width, height, idleImage, hoverImage);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		this.clickImage = hoverImage;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (click) {
			g.drawImage(clickImage, position.x, position.y - 84d, width, height + 99d);
		} else if (hover) {
			g.drawImage(hoverImage, position.x, position.y - 84d, width, height + 99d);
		} else {
			g.drawImage(idleImage, position.x, position.y - 84d, width, height + 99d);
		}
	}
}