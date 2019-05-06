package com.pmnm.risk.ui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;

public class ComboBoxText extends DoaImageButton {

	private static final long serialVersionUID = -3852059675270216284L;

	private DoaSprite idleImage;
	private DoaSprite clickImage;
	private String[] comboValues;
	private int index = 0;

	public ComboBoxText(DoaVectorF position, Integer width, Integer height, DoaSprite idleImage, DoaSprite clickImage, String[] comboValues) {
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
		String s = Translator.getInstance().getTranslatedString(comboValues[index]).toUpperCase();
		g.setFont(UIInit.UI_FONT.deriveFont(Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, new DoaVectorF(90f, height), s)));
		g.setColor(UIInit.FONT_COLOR);
		g.drawString(s, position.x * 0.800f, position.y * 1.11f);
		if (click) {
			index++;
			click = false;
		}
	}
}