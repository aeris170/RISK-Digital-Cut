package com.pmnm.risk.ui;

import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;

public class ComboButton extends DoaImageButton {

	private static final long serialVersionUID = -3852059675270216284L;

	private DoaSprite idleImage;
	private DoaSprite clickImage;

	private boolean isClicked = false;

	public ComboButton(DoaVectorF position, Integer width, Integer height, DoaSprite idleImage, DoaSprite clickImage) {
		super(position, width, height, idleImage, idleImage, clickImage);
		this.idleImage = idleImage;
		this.clickImage = clickImage;
	}

	@Override
	public void tick() {
		super.tick();
		if (isEnabled) {
			if (getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
				if (DoaMouse.MB1) {
					click = true;
				}
				if (DoaMouse.MB1_RELEASE) {
					DoaMouse.MB1 = false;
					DoaMouse.MB1_HOLD = false;
					DoaMouse.MB1_RELEASE = false;
				}
			} else if (DoaMouse.MB1) {
				click = false;
			}
		}
	}

	public boolean isClicked() {
		return click;
	}
}