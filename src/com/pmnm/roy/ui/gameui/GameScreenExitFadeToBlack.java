package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Color;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaMath;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.ZOrders;
import com.pmnm.risk.main.Main;

public class GameScreenExitFadeToBlack extends DoaPanel {

	private static final long serialVersionUID = -4303158801177879099L;

	private float exitingFadeAlpha = 0;
	private float exitingFadeAlphaDelta = 0.0015f;
	private float exitingFadeAlphaDeltaJerk = 0.00005f;

	public GameScreenExitFadeToBlack() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		setzOrder(ZOrders.EXIT_FADE_TO_BLACK_Z);
		hide();
	}

	@Override
	public void tick() {
		exitingFadeAlpha += exitingFadeAlphaDelta;
		exitingFadeAlphaDelta += exitingFadeAlphaDeltaJerk;
		if (exitingFadeAlpha > 1) {
			System.exit(0);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.pushComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(exitingFadeAlpha, 0f, 1f)));
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		g.popComposite();
	}
}