package com.pmnm.risk.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.geom.AffineTransform;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaMath;
import com.pmnm.risk.main.Main;

public class SeasonEffect extends DoaObject {

	private static final long serialVersionUID = -784946746221681591L;

	private float godrayAlpha = 0.7f;
	private float godrayAlphaDelta = 0.001f;
	private double godrayAngle = 0;

	public SeasonEffect() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, DoaObject.BACK);
	}

	@Override
	public void tick() {
		godrayAngle += 0.05f;
		godrayAlpha += godrayAlphaDelta;
		if (godrayAlpha >= 1f || godrayAlpha <= 0.5f) {
			godrayAlphaDelta *= -1;
			godrayAlpha = DoaMath.clamp(godrayAlpha, 0.5f, 1f);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		Composite oldComposite = g.getComposite();
		g.setColor(Season.getCurrentSeason().getSeasonColor());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		g.setComposite(oldComposite);
		AffineTransform oldTransform = g.getTransform();
		switch (Season.getCurrentSeason()) {
			case WINTER:
				break;
			case SPRING:
				break;
			case SUMMER:
				DoaSprite godray = DoaSprites.get("godray");
				g.translate(Main.WINDOW_WIDTH / 2f, 0);
				g.rotate(Math.toRadians(godrayAngle));
				g.scale(1.2f, 1.2f);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, godrayAlpha));
				g.drawImage(godray, -godray.getWidth() / 2f, -godray.getHeight() / 2f);
				break;
			case FALL:
			default:
				break;
		}
		g.setComposite(oldComposite);
		g.setTransform(oldTransform);
	}
}