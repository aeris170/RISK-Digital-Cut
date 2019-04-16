package com.pmnm.risk.ui.gameui;

import com.doa.maths.DoaVectorF;
import com.doa.ui.DoaUIContainer;

public class DicePanel extends DoaUIContainer {

	private static final long serialVersionUID = 8009744806803376915L;

	public DicePanel(DoaVectorF position, Integer width, Integer height) {
		super(position, width, height);
	}

	public DicePanel(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
	}
}