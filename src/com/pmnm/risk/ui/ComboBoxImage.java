package com.pmnm.risk.ui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;

public class ComboBoxImage extends DoaImageButton {

	private static final long serialVersionUID = -3852059675270216284L;

	private DoaSprite idleImage;
	private DoaSprite clickImage;
	private DoaSprite[] comboValues;
	private int index = 0;

	public ComboBoxImage(DoaVectorF position, Integer width, Integer height, DoaSprite idleImage, DoaSprite clickImage, DoaSprite[] comboValues) {
		super(position, width, height, idleImage, idleImage, clickImage);
		this.idleImage = idleImage;
		this.clickImage = clickImage;
		this.comboValues = comboValues;
	}

	@Override
	public void tick() {
		recalibrateBounds();
		if (isEnabled) {
			if (getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
				if (DoaMouse.MB1) {
					click = !click;
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

	@Override
	public void render(DoaGraphicsContext g) {
		super.render(g);
		g.drawImage(comboValues[index], position.x * 0.905f, position.y);
		if (click) {
			for (DoaSprite s : comboValues) {

			}
		}
	}
}