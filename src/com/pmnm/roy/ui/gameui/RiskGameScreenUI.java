package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.util.List;

import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.SystemSpecs;
import com.pmnm.roy.ui.menu.FadingBackground;
import com.pmnm.roy.ui.menu.LoadGameMenu;
import com.pmnm.roy.ui.menu.SaveGameMenu;

import doa.engine.scene.DoaScene;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import pmnm.risk.game.IRiskGameContext.GameType;
import pmnm.risk.game.databasedimpl.RiskGameContext;

@UtilityClass
public final class RiskGameScreenUI {

	@Getter
	private boolean isInitialized;
	@Getter
	private RiskGameContext context;

	private FadingBackground background;
	private SaveGameMenu saveGameMenu;
	private LoadGameMenu loadGameMenu;

	public static DicePanel DicePanel;
	public static GameScreenExitPopup ExitPopup;
	public static GameScreenExitFadeToBlack ExitFadeToBlack;

	public static void initUI(String mapName, List<Integer> playerTypes, List<String> playerNames, List<Color> playerColors, List<String> aiNames, List<Color> aiColors,
	        List<Integer> difficulties, boolean randomPlacement) {
		/*
		DicePanel = Builders.DPB.scene(gameScene).instantiate();
		ExitFadeToBlack = Builders.GSEFTBB.scene(gameScene).instantiate();
		ExitPopup = Builders.GSEPB.scene(gameScene).instantiate();
		Builders.TPB.scene(gameScene).instantiate();
		Builders.BPB.scene(gameScene).instantiate();
		Builders.GMB.args(mapName, playerTypes, playerNames, playerColors, aiNames, aiColors, difficulties, randomPlacement).scene(gameScene).instantiate();
		Builders.GBB.scene(gameScene).instantiate();
		*/
	}

	public static void initUIFor(final RiskGameContext context, final DoaScene gameScene, final GameType type) {
		if (isInitialized()) { throw new IllegalStateException("Please call destroyUI() first"); }
		if (!context.isInitialized()) { throw new IllegalStateException("Please initialize context first"); }

		gameScene.add(new SystemSpecs());
		gameScene.add(new PauseMenu(context, type));

		Water water  = new Water(context);
		gameScene.add(water);

		TopPanel topPanel = new TopPanel(context);
		topPanel.setVisible(true);
		gameScene.add(topPanel);

		BottomPanel bottomPanel = new BottomPanel(context);
		bottomPanel.setVisible(true);
		gameScene.add(bottomPanel);

		SeasonEffect seasonEffect = new SeasonEffect();
		gameScene.add(seasonEffect);

		DicePanel dicePanel = new DicePanel(context);
		dicePanel.setVisible(true);
		gameScene.add(dicePanel);

		DoaScene menus = Scenes.getGameMenuScene();

		background = new FadingBackground();
		menus.add(background);

		saveGameMenu = new SaveGameMenu(context);
		menus.add(saveGameMenu);

		loadGameMenu = new LoadGameMenu();
		menus.add(loadGameMenu);

		RiskGameScreenUI.context = context;
		isInitialized = true;
	}

	public static void destroyUI(final DoaScene gameScene) {
		context = null;
		isInitialized = false;

		gameScene.clear();
	}

	public static void setSaveMenuVisibility(boolean value) {
		if (value) {
			Scenes.loadGameMenuScene();
			saveGameMenu.setVisible(true);
		} else {
			Scenes.loadGameScene();
			saveGameMenu.setVisible(false);
		}
	}

	public static void setLoadMenuVisibility(boolean value) {
		if (value) {
			Scenes.loadGameMenuScene();
			loadGameMenu.setVisible(true);
		} else {
			Scenes.loadGameScene();
			loadGameMenu.setVisible(false);
		}
	}
}
