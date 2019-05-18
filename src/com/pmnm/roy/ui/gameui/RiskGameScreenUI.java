package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.io.File;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.map.MapLoader;
import com.pmnm.risk.map.board.GameBoard;
import com.pmnm.roy.ui.EscPopup;
import com.pmnm.roy.ui.UIInit;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;
	public static CardPanel CardPanel;
	public static EscPopup esc;

	private RiskGameScreenUI() {
	}

	public static void initUI(String mapName, List<Integer> playerTypes, List<String> playerNames,
			List<Color> playerColors, List<String> aiNames, List<Color> aiColors, List<Integer> difficulties, boolean randomPlacement) {
		MapLoader.readMapData(new File(mapName));
		DicePanel = DoaHandler.instantiate(DicePanel.class);
		CardPanel = DoaHandler.instantiate(CardPanel.class);
		DoaHandler.instantiate(TopPanel.class);
		DoaHandler.instantiate(BottomPanel.class);
		DoaHandler.instantiate(GameManager.class, mapName, playerTypes, playerNames, playerColors, aiNames, aiColors, difficulties, randomPlacement);
		DoaHandler.instantiate(GameBoard.class);
		esc = DoaHandler.instantiate(EscPopup.class, UIInit.getMM(), UIInit.getSM(), UIInit.getRM(), UIInit.getLM(), UIInit.getEP(), UIInit.getPOM());
	}
}
