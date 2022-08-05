package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.util.List;

import com.pmnm.risk.main.SystemSpecs;

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
		gameScene.clear();
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
		
		RiskGameScreenUI.context = context;
		isInitialized = true;
	}
	
	public static void destroyUI() {
		context = null;
		isInitialized = false;
	}
}