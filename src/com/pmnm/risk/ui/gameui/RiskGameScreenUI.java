package com.pmnm.risk.ui.gameui;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.main.GameManager;

public final class RiskGameScreenUI {

	public static DicePanel DicePanel;

	private RiskGameScreenUI() {}

	public static void initUI() {
		DicePanel = DoaHandler.instantiateDoaObject(DicePanel.class);
		// DoaHandler.instantiateDoaObject(MiddleSectionScroll.class);
		DoaHandler.instantiateDoaObject(TopPanel.class);
		// DoaHandler.instantiateDoaObject(PieceGauge.class);
		DoaHandler.instantiateDoaObject(BottomPanel.class);
		DoaHandler.instantiateDoaObject(GameManager.class);
		GameManager.dicePanel = DicePanel;
	}
}
