package com.pmnm.risk.ui;

import java.awt.Color;
import java.awt.Font;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.ui.actions.ExitButtonAction;
import com.pmnm.risk.ui.actions.PlayOfflineButtonAction;
import com.pmnm.risk.ui.actions.PlayOnlineButtonAction;
import com.pmnm.risk.ui.actions.RulesButtonAction;
import com.pmnm.risk.ui.actions.SettingsButtonAction;

public final class UIInit {

	private static final String BUTTON_IDLE_SPRITE = "ButtonIdle";
	private static final String BUTTON_HOVER_SPRITE = "ButtonHover";

	private static final DoaVectorI MM_BUTTON_SIZE = new DoaVectorI(387, 60);

	private static final DoaVectorF MM_PLAY_OFFLINE_LOCATION = new DoaVectorF(1377f, 511f);
	private static final DoaVectorF MM_PLAY_ONLINE_LOCATION = new DoaVectorF(1377f, 584f);
	private static final DoaVectorF MM_SETTING_LOCATION = new DoaVectorF(1377f, 657f);
	private static final DoaVectorF MM_RULES_LOCATION = new DoaVectorF(1377f, 730f);
	private static final DoaVectorF MM_EXIT_LOCATION = new DoaVectorF(1377f, 803f);

	// public static final Font UI_FONT_ALT = new Font("Constantia", Font.PLAIN, 1);
	public static final Font UI_FONT = new Font("Book Antiqua", Font.PLAIN, 1);
	public static final Color FONT_COLOR = new Color(189, 164, 79);
	public static final Color HOVER_FONT_COLOR = new Color(94, 82, 42);

	private UIInit() {}

	public static void initUI() {
		MainMenu mm = DoaHandler.instantiateDoaObject(MainMenu.class);
		TextImageButton playOfflineButton = DoaHandler.instantiateDoaObject(TextImageButton.class, MM_PLAY_OFFLINE_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "PLAY OFFLINE", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton playOnlineButton = DoaHandler.instantiateDoaObject(TextImageButton.class, MM_PLAY_ONLINE_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "PLAY ONLINE", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton settingsButton = DoaHandler.instantiateDoaObject(TextImageButton.class, MM_SETTING_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "SETTINGS", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton rulesButton = DoaHandler.instantiateDoaObject(TextImageButton.class, MM_RULES_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "RULES", FONT_COLOR, HOVER_FONT_COLOR);
		TextImageButton exitButton = DoaHandler.instantiateDoaObject(TextImageButton.class, MM_EXIT_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y,
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE), "EXIT", FONT_COLOR, HOVER_FONT_COLOR);
		playOfflineButton.addAction(new PlayOfflineButtonAction(mm));
		playOnlineButton.addAction(new PlayOnlineButtonAction());
		settingsButton.addAction(new SettingsButtonAction());
		rulesButton.addAction(new RulesButtonAction());
		exitButton.addAction(new ExitButtonAction());
		mm.add(playOfflineButton);
		mm.add(playOnlineButton);
		mm.add(settingsButton);
		mm.add(rulesButton);
		mm.add(exitButton);
		mm.show();
	}
}
