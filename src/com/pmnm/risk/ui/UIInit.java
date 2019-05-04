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

	private UIInit() {}

	public static void initUI() {
		MainMenu mm = DoaHandler.instantiate(MainMenu.class);
		SinglePlayerMenu spm = DoaHandler.instantiate(SinglePlayerMenu.class);
		MultiPlayerMenu mpm = DoaHandler.instantiate(MultiPlayerMenu.class);
		SettingsMenu sm = DoaHandler.instantiate(SettingsMenu.class, mm);
		RulesMenu rm = DoaHandler.instantiate(RulesMenu.class);
		ExitPopup ep = DoaHandler.instantiate(ExitPopup.class);
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
		playOfflineButton.addAction(new PlayOfflineButtonAction(mm, spm, ep));
		playOnlineButton.addAction(new PlayOnlineButtonAction(mm, mpm, ep));
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
}
