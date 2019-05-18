package com.pmnm.roy.ui;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;

public class RandomPlacementButton extends DoaImageButton {

	private static final long serialVersionUID = 1106009971325314959L;

	BufferedImage idle;
	BufferedImage clicked;
	String s;

	public RandomPlacementButton(DoaVectorF position, Integer width, Integer height, BufferedImage idle,
			BufferedImage clicked, String s) {
		super(position, width, height, idle, idle, clicked);
		this.idle = idle;
		this.clicked = clicked;
		this.s = Translator.getInstance().getTranslatedString(s).toUpperCase();
	}

	@Override
	public void tick() {
		recalibrateBounds();
		if (isEnabled && DoaMouse.MB1 && getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
			click = !click;
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		BufferedImage randomPlacementBG = DoaSprites.get("RandomPlacementBorder");
		float textX = Main.WINDOW_WIDTH * 0.115f;
		float textY = Main.WINDOW_HEIGHT * 0.62f;
		g.drawImage(randomPlacementBG, Main.WINDOW_WIDTH * 0.115f, Main.WINDOW_HEIGHT * 0.62f);
		g.drawImage(idle, position.x, position.y);
		if (click) {
			g.drawImage(clicked, position.x + (idle.getWidth() - clicked.getWidth()) / 2,
					position.y + (idle.getHeight() - clicked.getHeight()) / 2);
		}
		DoaVectorF bounds = new DoaVectorF(randomPlacementBG.getWidth() * 0.9f, randomPlacementBG.getHeight());
		g.setFont(UIInit.UI_FONT
				.deriveFont(Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT.deriveFont(1), bounds, s)));
		g.setColor(UIInit.FONT_COLOR);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, textX + (randomPlacementBG.getWidth() * 0.9f - fm.stringWidth(s)) / 2, textY + fm.getHeight());
	}

	public boolean getClick() {
		return click;
	}
}
