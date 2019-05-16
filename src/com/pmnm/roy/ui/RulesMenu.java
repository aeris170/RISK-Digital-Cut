package com.pmnm.roy.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;

public class RulesMenu extends DoaPanel {

	private static final long serialVersionUID = -4228991106639373659L;
	
	List<BufferedImage> rulesList = new ArrayList<>();

	public RulesMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		
		//rulesList.add();
	}

	@Override
	public void render(DoaGraphicsContext g) {

	}
}