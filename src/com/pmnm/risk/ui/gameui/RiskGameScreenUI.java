package com.pmnm.risk.ui.gameui;

import java.io.File;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.map.MapLoader;
import com.pmnm.risk.map.board.GameBoard;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;

	private RiskGameScreenUI() {}

	public static void initUI() {
		MapLoader.readMapData(new File(""));
		DicePanel = DoaHandler.instantiate(DicePanel.class);
		DoaHandler.instantiate(TopPanel.class);
		DoaHandler.instantiate(BottomPanel.class);
		DoaHandler.instantiate(GameManager.class);
		DoaHandler.instantiate(GameBoard.class);
		GameManager.dicePanel = DicePanel;
	}
}