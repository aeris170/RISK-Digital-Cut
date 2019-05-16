package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;


import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;


public final class EscPopup extends DoaPanel {

	// needs Serial Version UID

	private DoaVectorF bounds = new DoaVectorF(Main.WINDOW_WIDTH * 0.300f, Main.WINDOW_HEIGHT * 0.036f);

	public EscPopup() {
		super(Main.WINDOW_WIDTH * 0.376f, Main.WINDOW_HEIGHT * 0.291f, (int) (Main.WINDOW_WIDTH * 0.240f),
				(int) (Main.WINDOW_HEIGHT * 0.419f));
		//super.show();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		Composite oldComposite = g.getComposite();
		g.setColor(Color.darkGray);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		g.setComposite(oldComposite);

		g.drawImage(DoaSprites.get("escapeMenu"), position.x, position.y, width, height);
		
	}
}