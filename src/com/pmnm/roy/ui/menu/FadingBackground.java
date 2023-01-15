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
import lombok.NonNull;
import lombok.Setter;

public class FadingBackground extends DoaObject {

	private static final long serialVersionUID = -873986354085676812L;

	private static final int TIME_BETWEEN_FADES = 10000; // 10 seconds
	
	@Setter private boolean isVisible = true;

	private float alpha = 1f;
	private float alphaDelta = 0.01f;

	private DoaTaskGuard fadeGuard = new DoaTaskGuard();
	private DoaTaskGuard alphaGuard = new DoaTaskGuard();

	private int index = 0;
	
	private transient BufferedImage fleurDeLis;
	private transient BufferedImage topRing;
	private transient BufferedImage bottomRing;
	private transient BufferedImage[] backgrounds;

	public FadingBackground() {
		this.fleurDeLis = UIConstants.getFleurDeLis();
		this.topRing = UIConstants.getTopRing();
		this.bottomRing = UIConstants.getBottomRing();
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
			
			int width = Main.WINDOW_WIDTH;
			int height = Main.WINDOW_HEIGHT;
				
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(alpha, 0, 1)));
			DoaGraphicsFunctions.drawImage(get(index), 0, 0, width, height);
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(1 - alpha, 0, 1)));
			DoaGraphicsFunctions.drawImage(get(index + 1), 0, 0, width, height);
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			
			for (int i = 0; i < Main.WINDOW_WIDTH; i += UIConstants.FLEUR_WIDTH - 1) { // on big displays, fleur has gaps between. -1 fixes that
				DoaGraphicsFunctions.drawImage(fleurDeLis, i, 0,												UIConstants.FLEUR_WIDTH, UIConstants.FLEUR_HEIGHT);
				DoaGraphicsFunctions.drawImage(fleurDeLis, i, UIConstants.FLEUR_HEIGHT, 								UIConstants.FLEUR_WIDTH, UIConstants.FLEUR_HEIGHT);
				DoaGraphicsFunctions.drawImage(fleurDeLis, i, Main.WINDOW_HEIGHT - UIConstants.FLEUR_HEIGHT * 2f,	UIConstants.FLEUR_WIDTH, UIConstants.FLEUR_HEIGHT);
				DoaGraphicsFunctions.drawImage(fleurDeLis, i, Main.WINDOW_HEIGHT - UIConstants.FLEUR_HEIGHT, 		UIConstants.FLEUR_WIDTH, UIConstants.FLEUR_HEIGHT);
			}
			
			DoaGraphicsFunctions.drawImage(topRing, 	0, UIConstants.FLEUR_HEIGHT * 1.5f, 960, bottomRing.getHeight());
			DoaGraphicsFunctions.drawImage(topRing, 	0, UIConstants.FLEUR_HEIGHT * 1.5f, 1920, bottomRing.getHeight());
			DoaGraphicsFunctions.drawImage(bottomRing,	0, Main.WINDOW_HEIGHT - UIConstants.FLEUR_HEIGHT * 1.5f - bottomRing.getHeight(), 1920, bottomRing.getHeight());
		}
		
		private BufferedImage get(int index) {
			return backgrounds[index % backgrounds.length];
		}
	}

}