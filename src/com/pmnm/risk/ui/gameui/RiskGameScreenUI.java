package com.pmnm.risk.ui.gameui;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.main.GameManager;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;

	private RiskGameScreenUI() {}

	public static void initUI() {
		DicePanel = DoaHandler.instantiate(DicePanel.class);
		DoaHandler.instantiate(TopPanel.class);
		DoaHandler.instantiate(BottomPanel.class);
		DoaHandler.instantiate(GameManager.class);
		GameManager.dicePanel = DicePanel;
	}
}