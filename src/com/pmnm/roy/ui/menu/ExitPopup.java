package com.pmnm.roy.ui.menu;

import java.awt.Font;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;

public final class ExitPopup extends DoaPanel {

	private static final long serialVersionUID = 3563748749529849739L;

	TextImageButton yesButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.347f, Main.WINDOW_HEIGHT * 0.519f),
	        (int) (Main.WINDOW_WIDTH * 0.096f), (int) (Main.WINDOW_HEIGHT * 0.055f), DoaSprites.get("MiniButtonIdle"), DoaSprites.get("MiniButtonHover"), "YES",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton noButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.555f, Main.WINDOW_HEIGHT * 0.519f),
	        (int) (Main.WINDOW_WIDTH * 0.096f), (int) (Main.WINDOW_HEIGHT * 0.055f), DoaSprites.get("MiniButtonIdle"), DoaSprites.get("MiniButtonHover"), "NO",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);

	private DoaVectorF bounds = new DoaVectorF(Main.WINDOW_WIDTH * 0.300f, Main.WINDOW_HEIGHT * 0.036f);

	private String s;

	public ExitPopup() {
		super(Main.WINDOW_WIDTH * 0.314f, Main.WINDOW_HEIGHT * 0.388f, (int) (Main.WINDOW_WIDTH * 0.371f), (int) (Main.WINDOW_HEIGHT * 0.222f));
		yesButton.addAction(() -> UIInit.ef.beginFade());
		noButton.addAction(() -> hide());
		setzOrder(1000);
		add(yesButton);
		add(noButton);
		hide();
	}

	@Override
	public void show() {
		super.show();
		s = Translator.getInstance().getTranslatedString("ARE_YOU_SURE_WANT_TO_EXIT").toUpperCase();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("ExitPopupBackground"), position.x, position.y, width, height);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.BOLD, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT.deriveFont(Font.BOLD, 1), bounds, s)));
		g.setColor(UIInit.FONT_COLOR);
		g.drawString(s, Main.WINDOW_WIDTH * 0.350f + (bounds.x - g.getFontMetrics().stringWidth(s)) / 2d, Main.WINDOW_WIDTH * 0.262f);
	}
}