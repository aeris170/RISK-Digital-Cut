package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.io.File;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.map.MapLoader;
import com.pmnm.risk.map.board.GameBoard;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;
	public static CardPanel CardPanel;

	private RiskGameScreenUI() {}

	public static void initUI(String mapName, List<String> playerNames, List<Color> playerColors, List<String> aiNames, List<Color> aiColors) {
		// TODO make mapName a parameter and pass from UI
		MapLoader.readMapData(new File(mapName));
		DicePanel = DoaHandler.instantiate(DicePanel.class);
		CardPanel = DoaHandler.instantiate(CardPanel.class);
		DoaHandler.instantiate(TopPanel.class);
		DoaHandler.instantiate(BottomPanel.class);
		DoaHandler.instantiate(GameManager.class, mapName, playerNames, playerColors, aiNames, aiColors);
		DoaHandler.instantiate(GameBoard.class);
	}
}
