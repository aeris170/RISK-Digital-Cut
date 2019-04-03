package com.pmnm.risk.ui.gameui;

import com.doa.engine.DoaHandler;

public final class RiskGameScreenUI {

	private RiskGameScreenUI() {}

	public static void initUI() {
		DoaHandler.instantiateDoaObject(GameScreenVisualElements.class);
		DoaHandler.instantiateDoaObject(InfoPanel.class);
		DoaHandler.instantiateDoaObject(PieceGauge.class);
		DoaHandler.instantiateDoaObject(MiddleSectionScroll.class);
	}
}
