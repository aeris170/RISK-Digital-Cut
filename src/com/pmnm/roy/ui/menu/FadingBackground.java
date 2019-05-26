package com.pmnm.roy.ui.menu;

import java.awt.AlphaComposite;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.task.DoaTaskGuard;
import com.doa.engine.task.DoaTasker;
import com.doa.maths.DoaMath;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.UIInit;

public class FadingBackground extends DoaObject {

	private static final long serialVersionUID = -873986354085676812L;

	private static final int BACKGROUND_COUNT = 6;
	private static final int TIME_BETWEEN_FADES = 10000;

	private float alpha = 1f;
	private float alphaDelta = 0.01f;

	private DoaTaskGuard fadeGuard = new DoaTaskGuard();
	private DoaTaskGuard alphaGuard = new DoaTaskGuard();

	private int index = 0;

	private boolean isVisible = true;

	public FadingBackground() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, -1);
		DoaTasker.guard(fadeGuard, TIME_BETWEEN_FADES);
	}

	public void show() {
		isVisible = true;
	}

	public void hide() {
		isVisible = false;
	}

	@Override
	public void tick() {
		if (isVisible) {
			DoaTasker.guardExecution(() -> {
				while (alpha >= 0) {
					DoaTasker.guardExecution(() -> alpha -= alphaDelta, alphaGuard, 10);
				}
				alpha = 1;
				index++;
			}, fadeGuard, TIME_BETWEEN_FADES);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isVisible) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(alpha, 0, 1)));
			g.drawImage(DoaSprites.get("BG" + index % BACKGROUND_COUNT), position.x, position.y, width, height);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DoaMath.clamp(1 - alpha, 0, 1)));
			g.drawImage(DoaSprites.get("BG" + (index + 1) % BACKGROUND_COUNT), position.x, position.y, width, height);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
				g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
				g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
				g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
				g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			}
			g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
			g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		}
	}
}