package com.pmnm.roy.ui.menu;

import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaMath;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import doa.engine.task.DoaTaskGuard;
import doa.engine.task.DoaTasker;
import lombok.Setter;

public class FadingBackground extends DoaObject {

	private static final long serialVersionUID = -873986354085676812L;

	private static final int TIME_BETWEEN_FADES = 10_000; // 10 seconds

	@Setter private boolean isVisible = true;

	private float alpha = 1.0f;
	private float alphaDelta = 0.01f;

	private DoaTaskGuard fadeGuard = new DoaTaskGuard();
	private DoaTaskGuard alphaGuard = new DoaTaskGuard();

	private int index = 0;

	private transient BufferedImage[] backgrounds;

	public FadingBackground() {
		this.backgrounds = UIConstants.getBackgrounds();
		
		addComponent(new FadeScript());
		addComponent(new Renderer());
		
		DoaTasker.guard(fadeGuard, TIME_BETWEEN_FADES);
	}

	private final class FadeScript extends DoaScript {

		private static final long serialVersionUID = 6250939523998330886L;

		@Override
		public void tick() {
			if (!isVisible) { return; }

			DoaTasker.guardExecution(() -> {
				while (alpha >= 0) {
					DoaTasker.guardExecution(() -> alpha -= alphaDelta, alphaGuard, 10);
				}
				alpha = 1;
				index++;
			}, fadeGuard, TIME_BETWEEN_FADES);
		}
	}

	private final class Renderer extends DoaRenderer {

		private static final long serialVersionUID = 3054835329533888073L;

		@Override
		public void render() {
			if (!isVisible) { return; }

			DoaGraphicsFunctions.pushComposite();
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(alpha, 0, 1)));
			DoaGraphicsFunctions.drawImage(get(index), 0, 0, 1920, 1080);
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(1 - alpha, 0, 1)));
			DoaGraphicsFunctions.drawImage(get(index + 1), 0, 0, 1920, 1080);
			DoaGraphicsFunctions.popComposite();
		}

		private BufferedImage get(int index) {
			return backgrounds[index % backgrounds.length];
		}
	}
}