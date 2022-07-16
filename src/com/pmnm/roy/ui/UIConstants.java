package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.globals.localization.Translator.Language;
import com.pmnm.risk.main.SystemSpecs;
import com.pmnm.roy.ui.gameui.PauseMenu;
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
	public static final String EXIT_POPUP_BG = "ExitPopupBackground";
	public static final String BUTTON_IDLE = "ButtonIdle";
	public static final String BUTTON_HOVER = "ButtonHover";
	public static final String BUTTON_PRESSED = "ButtonPressed";
	public static final String MINI_BUTTON_IDLE = "MiniButtonIdle";
	public static final String MINI_BUTTON_HOVER = "MiniButtonHover";
	public static final String MINI_BUTTON_PRESSED = "MiniButtonPressed";

	public static final String RULES_IMAGES = "pt";
	public static final int RULES_IMAGES_COUNT = 6;
	
	public static final String LENS = "Lens";
	public static final String LENS_HOVER = "LensHover";
	public static final String LENS_SELECTED = "LensSelect";
	public static final String EN = "English";
	public static final String DE = "Deutch";
	public static final String ES = "Espanol";
	public static final String FR = "France";
	public static final String IT = "Italian";
	public static final String RU = "Russian";
	public static final String TR = "Turkish";

	public static final int FLEUR_WIDTH = DoaSprites.getSprite(FLEUR_DE_LIS).getWidth() / 6;
	public static final int FLEUR_HEIGHT = DoaSprites.getSprite(FLEUR_DE_LIS).getHeight() / 6;

	@Getter
	@NonNull
	private static FadingBackground background;
	@Getter
	@NonNull
	private static MainMenu mainMenu;
	public static PlayOfflineMenu pofm;
	public static PlayOnlineMenu ponm;
	@Getter
	@NonNull
	private static SettingsMenu settingsMenu;
	@Getter
	@NonNull
	private static RulesMenu rulesMenu;

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
	private static BufferedImage exitPopupBackground;
	
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
	private static BufferedImage[] rulesImages;
	
	@Getter
	@NonNull
	private static BufferedImage lensImage;
	
	@Getter
	@NonNull
	private static BufferedImage lensHoverImage;
	
	@Getter
	@NonNull
	private static BufferedImage lensSelectedImage;
	
	@Getter
	@NonNull
	private static EnumMap<Language, BufferedImage> languageImages = new EnumMap<>(Language.class);
	
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
		exitPopupBackground = DoaSprites.getSprite(EXIT_POPUP_BG);
		
		buttonIdleSprite = DoaSprites.getSprite(BUTTON_IDLE);
		buttonHoverSprite = DoaSprites.getSprite(BUTTON_HOVER);
		buttonPressedSprite = DoaSprites.getSprite(BUTTON_PRESSED);
		miniButtonIdleSprite = DoaSprites.getSprite(MINI_BUTTON_IDLE);
		miniButtonHoverSprite = DoaSprites.getSprite(MINI_BUTTON_HOVER);
		miniButtonPressedSprite = DoaSprites.getSprite(MINI_BUTTON_PRESSED);
		
		rulesImages = new BufferedImage[RULES_IMAGES_COUNT];
		for (int i = 0; i < RULES_IMAGES_COUNT; i++) {
			rulesImages[i] = DoaSprites.getSprite(RULES_IMAGES + i);
		}
		
		lensImage = DoaSprites.getSprite(LENS);
		lensHoverImage = DoaSprites.getSprite(LENS_HOVER);
		lensSelectedImage = DoaSprites.getSprite(LENS_SELECTED);
		
		languageImages.put(Language.EN, DoaSprites.getSprite(EN));
		languageImages.put(Language.DE, DoaSprites.getSprite(DE));
		languageImages.put(Language.ES, DoaSprites.getSprite(ES));
		languageImages.put(Language.FR, DoaSprites.getSprite(FR));
		languageImages.put(Language.IT, DoaSprites.getSprite(IT));
		languageImages.put(Language.RU, DoaSprites.getSprite(RU));
		languageImages.put(Language.TR, DoaSprites.getSprite(TR));
		
		font = DoaFonts.getFont("BookAntiqua");
		
		DoaScene menuScene = Scenes.MENU_SCENE;
		background = new FadingBackground(fleurDeLis, topRing, bottomRing, backgrounds);
		mainMenu = new MainMenu();
		mainMenu.setVisible(true);
		//fb = Builders.FBB.scene(menuScene).instantiate();
		//mm = Builders.MMB.scene(menuScene).instantiate();
		//pofm = Builders.POFMB.scene(menuScene).instantiate();
		//ponm = Builders.PONMB.scene(menuScene).instantiate();
		settingsMenu = new SettingsMenu();
		rulesMenu = new RulesMenu();
		//ngm = Builders.NGMB.scene(menuScene).instantiate();
		//lgm = Builders.LGMB.scene(menuScene).instantiate();
		//hgm = Builders.HGMB.scene(menuScene).instantiate();
		//jgm = Builders.JGMB.scene(menuScene).instantiate();
		//ep = Builders.EPB.scene(menuScene).instantiate();
		//ef = Builders.EFTBB.scene(menuScene).instantiate();
		
		menuScene.add(background);
		menuScene.add(mainMenu);
		menuScene.add(settingsMenu);
		menuScene.add(rulesMenu);
		menuScene.add(new SystemSpecs());
		DoaSceneHandler.loadScene(Scenes.MENU_SCENE);
	}
}