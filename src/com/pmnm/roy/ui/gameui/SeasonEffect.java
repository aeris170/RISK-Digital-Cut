package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaAnimation;
import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaMath;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.UIInit;

public class SeasonEffect extends DoaObject {

	private static final long serialVersionUID = -784946746221681591L;

	private float godrayAlpha = 0.7f;
	private float godrayAlphaDelta = 0.001f;
	private double godrayAngle = 0;

	public SeasonEffect() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, 100);
		DoaHandler.instantiate(Water.class);
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
				DoaAnimation snowfall = DoaAnimations.get("Snowfall");
				g.translate(Main.WINDOW_WIDTH / 2f, 0);
				// g.setClip(new Ellipse2D.Float(-200, -200, 400, 400)); same as below, made
				// resolution friendly
				g.setClip(new Ellipse2D.Float(-Main.WINDOW_WIDTH * 0.104f, -Main.WINDOW_HEIGHT * 0.185f, Main.WINDOW_WIDTH * 0.208f, Main.WINDOW_HEIGHT * 0.370f));
				g.scale(1.5f, 1.5f);
				g.drawAnimation(snowfall, -snowfall.getFrames().get(0).getWidth() / 2f, -snowfall.getFrames().get(0).getHeight() / 2f);
				break;
			case SPRING:
				DoaAnimation petals = DoaAnimations.get("CherryPetals");
				g.translate(Main.WINDOW_WIDTH / 2f, Main.WINDOW_HEIGHT * 0.05f);
				g.setClip(new Ellipse2D.Float(-Main.WINDOW_WIDTH * 0.104f, -Main.WINDOW_HEIGHT * 0.185f, Main.WINDOW_WIDTH * 0.208f, Main.WINDOW_HEIGHT * 0.370f));
				g.scale(1.5f, 1.5f);
				g.rotate(45f);
				g.drawAnimation(petals, -petals.getFrames().get(0).getWidth() / 2f, -petals.getFrames().get(0).getHeight() / 2f);
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
				DoaAnimation rain = DoaAnimations.get("Rain");
				g.translate(Main.WINDOW_WIDTH / 2f, 0);
				g.setClip(new Ellipse2D.Float(-Main.WINDOW_WIDTH * 0.104f, -Main.WINDOW_HEIGHT * 0.185f, Main.WINDOW_WIDTH * 0.208f, Main.WINDOW_HEIGHT * 0.370f));
				g.scale(1.5f, 1.5f);
				g.rotate(22.5f);
				g.drawAnimation(rain, -rain.getFrames().get(0).getWidth() / 2f, -rain.getFrames().get(0).getHeight() / 2f);
				break;
		}
		g.setComposite(oldComposite);
		g.setTransform(oldTransform);
		g.setClip(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
	}
}