package com.pmnm.risk.globals;

import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Scenes {

	@Getter private static final DoaScene menuScene = DoaSceneHandler.createScene("menu");
	@Getter private static final DoaScene gameScene = DoaSceneHandler.createScene("game");
	@Getter private static final DoaScene gameMenuScene = DoaSceneHandler.createScene("gameMenu");
	
	public static void loadMenuScene() { DoaSceneHandler.loadScene(menuScene); }
	public static void loadGameScene() { DoaSceneHandler.loadScene(gameScene); }
	public static void loadGameMenuScene() { DoaSceneHandler.loadScene(gameMenuScene); }
}
