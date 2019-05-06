package com.pmnm.risk.ui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.ui.button.DoaButton;
import com.doa.ui.button.DoaImageButton;
import com.doa.ui.panel.DoaPanel;

public class PlayerTypeComboBox extends DoaPanel {

	private static final String[] OPTIONS = new String[] { "CLOSED", "HUMAN", "COMPUTER" };

	private DoaImageButton comboButton;
	private DoaButton closedButton;
	private DoaButton humanButton;
	private DoaButton computerButton;

	public PlayerTypeComboBox(){
		super(0f, 0f, 0, 0);
	}

	@Override
	public void render(DoaGraphicsContext g) {

	}

}
