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

	private UIInit() {}

	public static void initUI() {
		MainMenu mm = DoaHandler.instantiate(MainMenu.class);
		SettingsMenu sm = DoaHandler.instantiate(SettingsMenu.class, mm);
		RulesMenu rm = DoaHandler.instantiate(RulesMenu.class, mm);
		LoadMenu lm = DoaHandler.instantiate(LoadMenu.class, mm);
		ExitPopup ep = DoaHandler.instantiate(ExitPopup.class);
		EscPopup esc = DoaHandler.instantiate(EscPopup.class);
		SinglePlayerMenu spm = DoaHandler.instantiate(SinglePlayerMenu.class, mm);
		MultiPlayerMenuHost mpmh = DoaHandler.instantiate(MultiPlayerMenuHost.class, mm);
		PlayOfflineMenu pom = DoaHandler.instantiate(PlayOfflineMenu.class, mm, spm, lm, ep);
		PlayOnlineMenu ponm = DoaHandler.instantiate(PlayOnlineMenu.class, mm, mpmh);
		
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
		
		
		// pos width height
		TextImageButton saveButton = DoaHandler.instantiate(TextImageButton.class,
				new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.342f), (int) (Main.WINDOW_WIDTH * 0.202),
				(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "SAVE",
				UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
		saveButton.setzOrder(10000);
		TextImageButton loadButtonPop = DoaHandler.instantiate(TextImageButton.class,
				new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.407f), (int) (Main.WINDOW_WIDTH * 0.202),
				(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "LOAD",
				UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
		TextImageButton rulesButtonPop = DoaHandler.instantiate(TextImageButton.class,
				new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.472f), (int) (Main.WINDOW_WIDTH * 0.202),
				(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "RULES",
				UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
		TextImageButton settingsButtonPop = DoaHandler.instantiate(TextImageButton.class,
				new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.537f), (int) (Main.WINDOW_WIDTH * 0.202),
				(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "SETTINGS",
				UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
		TextImageButton exitButtonPop = DoaHandler.instantiate(TextImageButton.class,
				new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.602f), (int) (Main.WINDOW_WIDTH * 0.202),
				(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "QUIT",
				UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
		exitButtonPop.addAction(new ExitButtonAction(ep));
		settingsButtonPop.addAction(new SettingsButtonAction(mm, sm, ep));
		rulesButtonPop.addAction(new RulesButtonAction(mm, rm, ep));
		loadButtonPop.addAction(new LoadButtonAction(mm, lm, ep, pom));
		saveButton.addAction(() -> {
			try {
				GameInstance.saveGame();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//I do not know what to catch here tbh
				e.printStackTrace();
			}
		});
		esc.add(exitButtonPop);
		esc.add(settingsButtonPop);
		esc.add(rulesButtonPop);
		esc.add(loadButtonPop);
		esc.add(saveButton);
		esc.hide();
	}
}
