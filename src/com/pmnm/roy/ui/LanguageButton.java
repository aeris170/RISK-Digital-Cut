package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.ui.button.DoaUIImageButton;

public class LanguageButton extends DoaUIImageButton {

	private static final long serialVersionUID = -3852059675270216284L;

	private BufferedImage idleImage;

	public LanguageButton(DoaVector position, Integer width, Integer height, BufferedImage idleImage) {
		super(position, width, height, idleImage);
		this.idleImage = idleImage;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isEnabled) {
			g.drawImage(idleImage, position.x, position.y, dimensions.x, dimensions.y);
			g.pushComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g.drawImage(DoaSprites.get("Lens"), position.x, position.y, dimensions.x, dimensions.y);
			if (selected) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				g.drawImage(DoaSprites.get("LensSelect"), position.x, position.y, dimensions.x, dimensions.y);
			} else if (hover) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				g.drawImage(DoaSprites.get("LensHover"), position.x, position.y, dimensions.x, dimensions.y);
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
}