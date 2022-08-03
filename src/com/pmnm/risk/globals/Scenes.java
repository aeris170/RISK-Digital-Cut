package com.pmnm.risk.globals;

import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Scenes {

	@Getter private static final DoaScene loadingScene = DoaSceneHandler.createScene("loading");
	@Getter private static final DoaScene menuScene = DoaSceneHandler.createScene("menu");
	@Getter private static final DoaScene gameScene = DoaSceneHandler.createScene("game");
	
	public static void switchToLoadingScreen() { DoaSceneHandler.loadScene(loadingScene); }
	public static void loadMenuScene() { DoaSceneHandler.loadScene(menuScene); }
	public static void loadGameScene() { DoaSceneHandler.loadScene(gameScene); }
}
