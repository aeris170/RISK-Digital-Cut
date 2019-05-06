package com.pmnm.risk.ui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.risk.ui.gameui.RiskGameScreenUI;

public class SinglePlayerMenu extends DoaPanel {

	private static final long serialVersionUID = -7552086909580890620L;

	TextImageButton playButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.662f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);
	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.752f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	DoaVectorF textRect = new DoaVectorF(Main.WINDOW_WIDTH * 0.092f, Main.WINDOW_HEIGHT * 0.040f);

	ComboBoxText t;
	ComboBoxImage f;

	public SinglePlayerMenu(MainMenu mm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		playButton.addAction(() -> {
			hide();
			// TODO find a better way
			RiskGameScreenUI.initUI();
		});
		backButton.addAction(() -> {
			hide();
			mm.show();
		});
		add(playButton);
		add(backButton);
		DoaSprite[] colors = new DoaSprite[PlayerColorBank.size()];
		for (int i = 0; i < PlayerColorBank.size(); i++) {
			DoaSprite w = DoaSprites.deepCopyDoaSprite(DoaSprites.get("White"));
			Utils.paintImage(w, PlayerColorBank.get(i));
			colors[i] = w;
		}
		String[] options = new String[] { "OPEN", "HUMAN", "AIPASSIVE", "AIEASY", "AIMEDIUM", "AIHARD", "AIINSANE", "AICHEATER" };
		for (int i = 0; i < 1; i++) {
			t = DoaHandler.instantiate(ComboBoxText.class, new DoaVectorF(300f, 300f), (int) (Main.WINDOW_WIDTH * 0.019f), (int) (Main.WINDOW_HEIGHT * 0.035f),
			        DoaSprites.get("ArrowDownIdle"), DoaSprites.get("ArrowDownClick"), options);
			add(t);
			f = DoaHandler.instantiate(ComboBoxImage.class, new DoaVectorF(600f, 300f), (int) (Main.WINDOW_WIDTH * 0.019f), (int) (Main.WINDOW_HEIGHT * 0.035f),
			        DoaSprites.get("ArrowDownIdle"), DoaSprites.get("ArrowDownClick"), colors);
			add(f);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("GenericBackground"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		g.drawImage(DoaSprites.get("MainScroll"), Main.WINDOW_WIDTH * 0.0125f, Main.WINDOW_HEIGHT * 0.163f);
		g.setColor(UIInit.FONT_COLOR);
		for (int i = 0; i < Globals.MAX_NUM_PLAYERS; i++) {
			g.drawImage(DoaSprites.get("PlayerNumberBorder"), Main.WINDOW_WIDTH * 0.079f, Main.WINDOW_HEIGHT * 0.272f + (Main.WINDOW_HEIGHT * 0.048f * i));
			String s = (Translator.getInstance().getTranslatedString("PLAYER") + " " + (i + 1) + ":").toUpperCase();
			g.setFont(UIInit.UI_FONT.deriveFont(Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, textRect, s)));
			g.drawString(s, Main.WINDOW_WIDTH * 0.082f, Main.WINDOW_HEIGHT * 0.305f + (Main.WINDOW_HEIGHT * 0.048f * i));
			g.drawImage(DoaSprites.get("PlayerTypeBorder"), Main.WINDOW_WIDTH * 0.178f, Main.WINDOW_HEIGHT * 0.272f + (Main.WINDOW_HEIGHT * 0.048f * i));
			g.drawImage(DoaSprites.get("ColorBorder"), Main.WINDOW_WIDTH * 0.251f, Main.WINDOW_HEIGHT * 0.272f + (Main.WINDOW_HEIGHT * 0.048f * i));
		}
		t.setPosition(new DoaVectorF(440f, 296f));
		f.setPosition(new DoaVectorF(535f, 296f));
	}
}