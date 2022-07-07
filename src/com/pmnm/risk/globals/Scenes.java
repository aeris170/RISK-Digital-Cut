package com.pmnm.risk.globals;

import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;

public final class Scenes {

	public static final DoaScene MENU_SCENE = DoaSceneHandler.createScene("menu");
	public static final DoaScene GAME_SCENE = DoaSceneHandler.createScene("game");

	private Scenes() {}
}
