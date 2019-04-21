package com.pmnm.risk.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Composite;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.pmnm.risk.main.Main;

public class SeasonEffect extends DoaObject {

	private static final long serialVersionUID = -784946746221681591L;

	public SeasonEffect() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		Composite oldComposite = g.getComposite();
		g.setColor(Season.getCurrentSeason().getSeasonColor());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		g.setComposite(oldComposite);
	}
}