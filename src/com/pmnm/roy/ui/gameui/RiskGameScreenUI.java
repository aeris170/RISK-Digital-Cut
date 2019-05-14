package com.pmnm.roy.ui.gameui;

import java.io.File;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.map.MapLoader;
import com.pmnm.risk.map.board.GameBoard;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;

	private RiskGameScreenUI() {}

	public static void initUI() {
		// TODO make mapName a parameter and pass from UI
		String mapName = "classic";
		MapLoader.readMapData(new File(mapName));
		DicePanel = DoaHandler.instantiate(DicePanel.class);
		DoaHandler.instantiate(TopPanel.class);
		DoaHandler.instantiate(BottomPanel.class);
		DoaHandler.instantiate(GameManager.class, mapName);
		DoaHandler.instantiate(GameBoard.class);
		GameManager.INSTANCE.dicePanel = DicePanel;
	}
}