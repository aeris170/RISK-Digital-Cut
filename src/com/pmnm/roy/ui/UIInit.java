package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.main.GameInstance;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.actions.ExitButtonAction;
import com.pmnm.roy.ui.actions.LoadButtonAction;
import com.pmnm.roy.ui.actions.PlayOfflineButtonAction;
import com.pmnm.roy.ui.actions.PlayOnlineButtonAction;
import com.pmnm.roy.ui.actions.RulesButtonAction;
import com.pmnm.roy.ui.actions.SettingsButtonAction;

public final class UIInit {

	public static final String FLEUR_DE_LIS = "FleurDeLis";

	public static final int FLEUR_WIDTH = DoaSprites.get(FLEUR_DE_LIS).getWidth() / 6;
	public static final int FLEUR_HEIGHT = DoaSprites.get(FLEUR_DE_LIS).getHeight() / 6;

	public static final String BUTTON_IDLE_SPRITE = "ButtonIdle";
	public static final String BUTTON_HOVER_SPRITE = "ButtonHover";

	public static final DoaVectorI BUTTON_SIZE = new DoaVectorI(387, 60);

	private static final DoaVectorF MM_PLAY_OFFLINE_LOCATION = new DoaVectorF(1377f, 511f);
	private static final DoaVectorF MM_PLAY_ONLINE_LOCATION = new DoaVectorF(1377f, 584f);
	private static final DoaVectorF MM_SETTING_LOCATION = new DoaVectorF(1377f, 657f);
	private static final DoaVectorF MM_RULES_LOCATION = new DoaVectorF(1377f, 730f);
	private static final DoaVectorF MM_EXIT_LOCATION = new DoaVectorF(1377f, 803f);

	public static final Font UI_FONT = new Font("Book Antiqua", Font.PLAIN, 1);
	public static final Color FONT_COLOR = new Color(189, 164, 79);
	public static final Color HOVER_FONT_COLOR = new Color(94, 82, 42);
	
	private static MainMenu mm;
	private static SettingsMenu sm;
	private static RulesMenu rm;
	private static LoadMenu lm;
	private static ExitPopup ep;

	private UIInit() {}

	public static void initUI() {
		mm = DoaHandler.instantiate(MainMenu.class);
		sm = DoaHandler.instantiate(SettingsMenu.class, mm);
		rm = DoaHandler.instantiate(RulesMenu.class, mm);
		lm = DoaHandler.instantiate(LoadMenu.class, mm);
		ep = DoaHandler.instantiate(ExitPopup.class);
		PlayOfflineMenu pom = DoaHandler.instantiate(PlayOfflineMenu.class, mm, lm, ep);
		PlayOnlineMenu ponm = DoaHandler.instantiate(PlayOnlineMenu.class, mm);
		
		TextImageButton playOfflineButton = DoaHandler.instantiate(TextImageButton.class, MM_PLAY_OFFLINE_LOCATION, BUTTON_SIZE.x, BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "PLAY_OFFLINE", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton playOnlineButton = DoaHandler.instantiate(TextImageButton.class, MM_PLAY_ONLINE_LOCATION, BUTTON_SIZE.x, BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "PLAY_ONLINE", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton settingsButton = DoaHandler.instantiate(TextImageButton.class, MM_SETTING_LOCATION, BUTTON_SIZE.x, BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "SETTINGS", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton rulesButton = DoaHandler.instantiate(TextImageButton.class, MM_RULES_LOCATION, BUTTON_SIZE.x, BUTTON_SIZE.y, DoaSprites.get(BUTTON_IDLE_SPRITE),
		        DoaSprites.get(BUTTON_HOVER_SPRITE), "RULES", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton exitButton = DoaHandler.instantiate(TextImageButton.class, MM_EXIT_LOCATION, BUTTON_SIZE.x, BUTTON_SIZE.y, DoaSprites.get(BUTTON_IDLE_SPRITE),
		        DoaSprites.get(BUTTON_HOVER_SPRITE), "EXIT", FONT_COLOR, HOVER_FONT_COLOR);
		playOfflineButton.addAction(new PlayOfflineButtonAction(mm, pom, ep, lm));
		playOnlineButton.addAction(new PlayOnlineButtonAction(mm, ponm, ep));
		settingsButton.addAction(new SettingsButtonAction(mm, sm, ep));
		rulesButton.addAction(new RulesButtonAction(mm, rm, ep));
		exitButton.addAction(new ExitButtonAction(ep));
		mm.add(playOfflineButton);
		mm.add(playOnlineButton);
		mm.add(settingsButton);
		mm.add(rulesButton);
		mm.add(exitButton);
		mm.show();
	}
	public static MainMenu getMM() {
		return mm;
	}
	
	public static SettingsMenu getSM() {
		return sm;
	}
	
	public static RulesMenu getRM() {
		return rm;
	}
	
	public static LoadMenu getLM() {
		return lm;
	}
	
	public static ExitPopup getEP() {
		return ep;
	}
}
