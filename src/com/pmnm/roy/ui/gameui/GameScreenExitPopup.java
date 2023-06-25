package com.pmnm.roy.ui.gameui;

import java.awt.Font;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.ZOrders;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIConstants;

public final class GameScreenExitPopup extends DoaPanel {

	private static final long serialVersionUID = 3563748749529849739L;

	private DoaVectorF bounds = new DoaVectorF(Main.WINDOW_WIDTH * 0.300f, Main.WINDOW_HEIGHT * 0.036f);

	private String areYouSureText;

	public GameScreenExitPopup() {
		super(Main.WINDOW_WIDTH * 0.314f, Main.WINDOW_HEIGHT * 0.388f, (int) (Main.WINDOW_WIDTH * 0.371f), (int) (Main.WINDOW_HEIGHT * 0.222f));
		yesButton.addAction(RiskGameScreenUI.ExitFadeToBlack::show);
		noButton.addAction(this::hide);
		setzOrder(ZOrders.EXIT_MENU_Z);
		add(yesButton);
		add(noButton);
		hide();
	}

	@Override
	public void show() {
		super.show();
		areYouSureText = Translator.getInstance().getTranslatedString("ARE_YOU_SURE_WANT_TO_EXIT").toUpperCase();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("ExitPopupBackground"), position.x, position.y, width, height);
		g.setFont(UIConstants.UI_FONT.deriveFont(Font.BOLD, Utils.findMaxFontSizeToFitInArea(g, UIConstants.UI_FONT.deriveFont(Font.BOLD, 1), bounds, areYouSureText)));
		g.setColor(UIConstants.FONT_COLOR);
		g.drawString(areYouSureText, Main.WINDOW_WIDTH * 0.350f + (bounds.x - g.getFontMetrics().stringWidth(areYouSureText)) / 2d, Main.WINDOW_WIDTH * 0.262f);
	}

	private TextImageButton yesButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.347f, Main.WINDOW_HEIGHT * 0.519f), (int) (Main.WINDOW_WIDTH * 0.096f),
	        (int) (Main.WINDOW_HEIGHT * 0.055f), DoaSprites.get("MiniButtonIdle"), DoaSprites.get("MiniButtonHover"), "YES", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
	private TextImageButton noButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.555f, Main.WINDOW_HEIGHT * 0.519f), (int) (Main.WINDOW_WIDTH * 0.096f),
	        (int) (Main.WINDOW_HEIGHT * 0.055f), DoaSprites.get("MiniButtonIdle"), DoaSprites.get("MiniButtonHover"), "NO", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
}