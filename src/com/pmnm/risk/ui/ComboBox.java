package com.pmnm.risk.ui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;

public class ComboBox extends DoaPanel {

	private static final long serialVersionUID = -7497744962127178890L;

	ComboButton comboButton = DoaHandler.instantiate(ComboButton.class, new DoaVectorF(300f, 300f), 500, 500, DoaSprites.get("ArrowDownIdle"),
	        DoaSprites.get("ArrowDownClick"));

	public ComboBox(DoaVectorF position, Integer width, Integer height) {
		super(position, width, height);
		add(comboButton);
	}

	@Override
	public void render(DoaGraphicsContext g) {}

}