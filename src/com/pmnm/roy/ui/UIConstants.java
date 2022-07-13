package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

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

import doa.engine.graphics.DoaFonts;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UIConstants {

	public static final String FLEUR_DE_LIS = "FleurDeLis";
	public static final String TOP_RING = "MainMenuTopRing";
	public static final String BOTTOM_RING = "MainMenuBottomRing";
	public static final String BG = "BG";
	public static final String BUTTON_IDLE = "ButtonIdle";
	public static final String BUTTON_HOVER = "ButtonHover";
	public static final String BUTTON_PRESSED = "ButtonPressed";
	public static final String MINI_BUTTON_IDLE = "MiniButtonIdle";
	public static final String MINI_BUTTON_HOVER = "MiniButtonHover";
	public static final String MINI_BUTTON_PRESSED = "MiniButtonPressed";

	public static final int FLEUR_WIDTH = DoaSprites.getSprite(FLEUR_DE_LIS).getWidth() / 6;
	public static final int FLEUR_HEIGHT = DoaSprites.getSprite(FLEUR_DE_LIS).getHeight() / 6;

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

	public static PauseMenu pm;
	
	@NonNull
	private static BufferedImage fleurDeLis;
	@NonNull
	private static BufferedImage topRing;
	@NonNull
	private static BufferedImage bottomRing;
	@NonNull
	private static BufferedImage[] backgrounds = new BufferedImage[6];
	
	@Getter
	@NonNull
	private static BufferedImage buttonIdleSprite;
	
	@Getter
	@NonNull
	private static BufferedImage buttonHoverSprite;
	
	@Getter
	@NonNull
	private static BufferedImage buttonPressedSprite;
	
	@Getter
	@NonNull
	private static BufferedImage miniButtonIdleSprite;
	
	@Getter
	@NonNull
	private static BufferedImage miniButtonHoverSprite;
	
	@Getter
	@NonNull
	private static BufferedImage miniButtonPressedSprite;
	
	@Getter
	@NonNull
	private static Font font;
	
	@Getter
	@NonNull
	private static final Color textColor = new Color(189, 164, 79);

	@Getter
	@NonNull
	private static final Color hoverTextColor = new Color(94, 82, 42);

	public static void initUI() {
		fleurDeLis = DoaSprites.getSprite(FLEUR_DE_LIS);
		topRing = DoaSprites.getSprite(TOP_RING);
		bottomRing = DoaSprites.getSprite(BOTTOM_RING);
		for (int i = 0; i < backgrounds.length; i++) {
			backgrounds[i] = DoaSprites.getSprite(BG.concat(Integer.toString(i)));
		}
		
		buttonIdleSprite = DoaSprites.getSprite(BUTTON_IDLE);
		buttonHoverSprite = DoaSprites.getSprite(BUTTON_HOVER);
		buttonPressedSprite = DoaSprites.getSprite(BUTTON_PRESSED);
		miniButtonIdleSprite = DoaSprites.getSprite(MINI_BUTTON_IDLE);
		miniButtonHoverSprite = DoaSprites.getSprite(MINI_BUTTON_HOVER);
		miniButtonPressedSprite = DoaSprites.getSprite(MINI_BUTTON_PRESSED);
		
		font = DoaFonts.getFont("BookAntiqua");
		
		DoaScene menuScene = Scenes.MENU_SCENE;
		FadingBackground fb = new FadingBackground(fleurDeLis, topRing, bottomRing, backgrounds);
		MainMenu mm = new MainMenu();
		//fb = Builders.FBB.scene(menuScene).instantiate();
		//mm = Builders.MMB.scene(menuScene).instantiate();
		//pofm = Builders.POFMB.scene(menuScene).instantiate();
		//ponm = Builders.PONMB.scene(menuScene).instantiate();
		//sm = Builders.SMB.scene(menuScene).instantiate();
		//rm = Builders.RMB.scene(menuScene).instantiate();
		//ngm = Builders.NGMB.scene(menuScene).instantiate();
		//lgm = Builders.LGMB.scene(menuScene).instantiate();
		//hgm = Builders.HGMB.scene(menuScene).instantiate();
		//jgm = Builders.JGMB.scene(menuScene).instantiate();
		//ep = Builders.EPB.scene(menuScene).instantiate();
		//ef = Builders.EFTBB.scene(menuScene).instantiate();
		
		menuScene.add(fb);
		menuScene.add(mm);
		DoaSceneHandler.loadScene(Scenes.MENU_SCENE);
	}
}