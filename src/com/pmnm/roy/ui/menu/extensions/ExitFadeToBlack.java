package com.pmnm.roy.ui.menu.extensions;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Rectangle;

import com.pmnm.risk.globals.ZOrders;
import com.pmnm.roy.IRoyElement;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaMath;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class ExitFadeToBlack extends DoaObject implements IRoyElement {

	private static final long serialVersionUID = -4303158801177879099L;

	@Getter
	@Setter
	private boolean isVisible = false;

	private float exitingFadeAlpha = 0;
	private float exitingFadeAlphaDelta = 0.0015f;
	private float exitingFadeAlphaDeltaJerk = 0.00005f;

	public ExitFadeToBlack() {
		setzOrder(ZOrders.EXIT_FADE_TO_BLACK_Z);

		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible) { return; }
			exitingFadeAlpha += exitingFadeAlphaDelta;
			exitingFadeAlphaDelta += exitingFadeAlphaDeltaJerk;
			if (exitingFadeAlpha > 1) {
				System.exit(0);
			}
		}
	}

	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible) { return; }
			DoaGraphicsFunctions.pushComposite();
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(exitingFadeAlpha, 0f, 1f)));
			DoaGraphicsFunctions.setColor(Color.BLACK);
			DoaGraphicsFunctions.fill(getContentArea());
			DoaGraphicsFunctions.popComposite();
		}
	}

	@Override
	public void setPosition(DoaVector position) { /* no-op*/ }

	@Override
	public Rectangle getContentArea() { return new Rectangle(0, 0, 1920, 1080); }
}
