package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;

public class LanguageButton extends DoaImageButton {

	private static final long serialVersionUID = -3852059675270216284L;

	private BufferedImage idleImage;

	private boolean selected = false;

	public LanguageButton(DoaVectorF position, Integer width, Integer height, BufferedImage idleImage) {
		super(position, width, height, idleImage);
		this.idleImage = idleImage;
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isEnabled) {
			g.drawImage(idleImage, position.x, position.y, width, height);
			g.pushComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g.drawImage(DoaSprites.get("Lens"), position.x, position.y, width, height);
			if (selected) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				g.drawImage(DoaSprites.get("LensSelect"), position.x, position.y, width, height);
			} else if (hover) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				g.drawImage(DoaSprites.get("LensHover"), position.x, position.y, width, height);
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