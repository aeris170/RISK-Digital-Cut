package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.ZOrders;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaAnimation;
import doa.engine.graphics.DoaAnimations;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaMath;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;

@SuppressWarnings("serial")
public class SeasonEffect extends DoaObject {

	public static SeasonEffect INSTANCE;

	private float godrayAlpha = 0.7f;
	private float godrayAlphaDelta = 0.001f;
	private double godrayAngle = 0;

	public SeasonEffect() {
		setzOrder(ZOrders.SEASON_EFFECT_Z);
		INSTANCE = this;
		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {

		@Override
		public void tick() {
			godrayAngle += 0.05f;
			godrayAlpha += godrayAlphaDelta;
			if (godrayAlpha >= 1f || godrayAlpha <= 0.5f) {
				godrayAlphaDelta *= -1;
				godrayAlpha = DoaMath.clamp(godrayAlpha, 0.5f, 1f);
			}
		}
		
	}
	
	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			Composite oldComposite = DoaGraphicsFunctions.getComposite();
			
			//DoaGraphicsFunctions.setColor(Season.getCurrentSeason().getSeasonColor());
			//DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			//DoaGraphicsFunctions.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
			
			DoaGraphicsFunctions.setComposite(oldComposite);
			AffineTransform oldTransform = DoaGraphicsFunctions.getTransform();
			switch (Season.getCurrentSeason()) {
				case WINTER:
					DoaAnimation snowfall = DoaAnimations.getAnimation("Snowfall");
					DoaGraphicsFunctions.translate(Main.WINDOW_WIDTH / 2f, 0);
					// g.setClip(new Ellipse2D.Float(-200, -200, 400, 400)); same as below, made
					// resolution friendly
					DoaGraphicsFunctions.setClip(new Ellipse2D.Float(-Main.WINDOW_WIDTH * 0.104f, -Main.WINDOW_HEIGHT * 0.185f, Main.WINDOW_WIDTH * 0.208f, Main.WINDOW_HEIGHT * 0.370f));
					DoaGraphicsFunctions.scale(1.5f, 1.5f);
					DoaGraphicsFunctions.drawAnimation(snowfall, -snowfall.getFrames().get(0).getWidth() / 2f, -snowfall.getFrames().get(0).getHeight() / 2f);
					break;
				case SPRING:
					DoaAnimation petals = DoaAnimations.getAnimation("CherryPetals");
					DoaGraphicsFunctions.translate(Main.WINDOW_WIDTH / 2f, Main.WINDOW_HEIGHT * 0.05f);
					DoaGraphicsFunctions.setClip(new Ellipse2D.Float(-Main.WINDOW_WIDTH * 0.104f, -Main.WINDOW_HEIGHT * 0.185f, Main.WINDOW_WIDTH * 0.208f, Main.WINDOW_HEIGHT * 0.370f));
					DoaGraphicsFunctions.scale(1.5f, 1.5f);
					DoaGraphicsFunctions.rotate(45f);
					DoaGraphicsFunctions.drawAnimation(petals, -petals.getFrames().get(0).getWidth() / 2f, -petals.getFrames().get(0).getHeight() / 2f);
					break;
				case SUMMER:
					BufferedImage godray = DoaSprites.getSprite("godray");
					DoaGraphicsFunctions.translate(Main.WINDOW_WIDTH / 2f, 0);
					DoaGraphicsFunctions.rotate((float) Math.toRadians(godrayAngle));
					DoaGraphicsFunctions.scale(1.2f, 1.2f);
					DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, godrayAlpha));
					DoaGraphicsFunctions.drawImage(godray, -godray.getWidth() / 2f, -godray.getHeight() / 2f);
					break;
				case FALL:
					DoaAnimation rain = DoaAnimations.getAnimation("FloatingLeaves");
					DoaGraphicsFunctions.translate(Main.WINDOW_WIDTH / 2f, 0);
					DoaGraphicsFunctions.setClip(new Ellipse2D.Float(-Main.WINDOW_WIDTH * 0.104f, -Main.WINDOW_HEIGHT * 0.185f, Main.WINDOW_WIDTH * 0.208f, Main.WINDOW_HEIGHT * 0.370f));
					DoaGraphicsFunctions.scale(1.8f, 1.8f);
					DoaGraphicsFunctions.rotate(22.5f);
					DoaGraphicsFunctions.drawAnimation(rain, -rain.getFrames().get(0).getWidth() / 2f, -rain.getFrames().get(0).getHeight() / 2f);
					break;
			}
			DoaGraphicsFunctions.setComposite(oldComposite);
			DoaGraphicsFunctions.setTransform(oldTransform);
			DoaGraphicsFunctions.setClip(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		}
		
	}
}