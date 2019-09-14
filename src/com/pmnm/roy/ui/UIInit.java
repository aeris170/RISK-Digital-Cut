package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;

import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.scene.DoaScene;
import com.doa.engine.scene.DoaSceneHandler;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.roy.ui.gameui.PauseMenu;
import com.pmnm.roy.ui.menu.ExitFadeToBlack;
import com.pmnm.roy.ui.menu.ExitPopup;
import com.pmnm.roy.ui.menu.FadingBackground;
import com.pmnm.roy.ui.menu.HostGameMenu;
import com.pmnm.roy.ui.menu.JoinGameMenu;
import com.pmnm.roy.ui.menu.LoadGameMenu;
import com.pmnm.roy.ui.menu.MainMenu;
import com.pmnm.roy.ui.menu.NewGameMenu;
import com.pmnm.roy.ui.menu.PlayOfflineMenu;
import com.pmnm.roy.ui.menu.PlayOnlineMenu;
import com.pmnm.roy.ui.menu.RulesMenu;
import com.pmnm.roy.ui.menu.SettingsMenu;

public final class UIInit {

	public static final String FLEUR_DE_LIS = "FleurDeLis";

	public static final int FLEUR_WIDTH = DoaSprites.get(FLEUR_DE_LIS).getWidth() / 6;
	public static final int FLEUR_HEIGHT = DoaSprites.get(FLEUR_DE_LIS).getHeight() / 6;

	public static final String BUTTON_IDLE_SPRITE = "ButtonIdle";
	public static final String BUTTON_HOVER_SPRITE = "ButtonHover";

	public static final DoaVectorI BUTTON_SIZE = new DoaVectorI(387, 60);

	public static final Font UI_FONT = new Font("Book Antiqua", Font.PLAIN, 1);
	public static final Color FONT_COLOR = new Color(189, 164, 79);
	public static final Color HOVER_FONT_COLOR = new Color(94, 82, 42);

	public static FadingBackground fb;
	public static MainMenu mm;
	public static PlayOfflineMenu pofm;
	public static PlayOnlineMenu ponm;
	public static SettingsMenu sm;
	public static RulesMenu rm;

	public static NewGameMenu ngm;
	public static LoadGameMenu lgm;
	public static HostGameMenu hgm;
	public static JoinGameMenu jgm;

	public static ExitPopup ep;
	public static ExitFadeToBlack ef;

	public static PauseMenu pm;

	private UIInit() {}

	public static void initUI() {
		DoaScene menuScene = Scenes.MENU_SCENE;
		fb = Builders.FBB.scene(menuScene).instantiate();
		mm = Builders.MMB.scene(menuScene).instantiate();
		pofm = Builders.POFMB.scene(menuScene).instantiate();
		ponm = Builders.PONMB.scene(menuScene).instantiate();
		sm = Builders.SMB.scene(menuScene).instantiate();
		rm = Builders.RMB.scene(menuScene).instantiate();
		ngm = Builders.NGMB.scene(menuScene).instantiate();
		lgm = Builders.LGMB.scene(menuScene).instantiate();
		hgm = Builders.HGMB.scene(menuScene).instantiate();
		jgm = Builders.JGMB.scene(menuScene).instantiate();
		ep = Builders.EPB.scene(menuScene).instantiate();
		ef = Builders.EFTBB.scene(menuScene).instantiate();
		DoaSceneHandler.loadScene(Scenes.MENU_SCENE);
	}
}