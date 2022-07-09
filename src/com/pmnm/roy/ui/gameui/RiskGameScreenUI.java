package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.io.File;
import java.util.List;

import com.doa.engine.scene.DoaScene;
import com.doa.engine.scene.DoaSceneHandler;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.roy.ui.UIInit;

import pmnm.risk.map.MapLoader;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;
	public static GameScreenExitPopup ExitPopup;
	public static GameScreenExitFadeToBlack ExitFadeToBlack;

	private RiskGameScreenUI() {}

	public static void initUI(String mapName, List<Integer> playerTypes, List<String> playerNames, List<Color> playerColors, List<String> aiNames, List<Color> aiColors,
	        List<Integer> difficulties, boolean randomPlacement)
	{
		DoaScene gameScene = Scenes.GAME_SCENE;
		MapLoader.readMapData(new File(mapName));
		DicePanel = Builders.DPB.scene(gameScene).instantiate();
		ExitFadeToBlack = Builders.GSEFTBB.scene(gameScene).instantiate();
		ExitPopup = Builders.GSEPB.scene(gameScene).instantiate();
		Builders.TPB.scene(gameScene).instantiate();
		Builders.BPB.scene(gameScene).instantiate();
		Builders.GMB.args(mapName, playerTypes, playerNames, playerColors, aiNames, aiColors, difficulties, randomPlacement).scene(gameScene).instantiate();
		Builders.GBB.scene(gameScene).instantiate();
		UIInit.pm = Builders.PMB.scene(gameScene).instantiate();
		DoaSceneHandler.loadScene(gameScene);
	}
}