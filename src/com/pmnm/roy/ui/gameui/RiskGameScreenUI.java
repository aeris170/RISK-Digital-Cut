package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.util.List;

import doa.engine.scene.DoaScene;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import pmnm.risk.game.IRiskGameContext;

@UtilityClass
public final class RiskGameScreenUI {

	@Getter
	private boolean isInitialized;
	@Getter
	private IRiskGameContext context;
	
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
	
	public static void initUIFor(final IRiskGameContext context, final DoaScene gameScene) {
		if (isInitialized()) { throw new IllegalStateException("Please call destroyUI() first"); }
		
		RiskGameScreenUI.context = context;
		isInitialized = true;
	}
	
	public static void destroyUI() {
		context = null;
		isInitialized = false;
	}
}