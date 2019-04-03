package com.pmnm.risk.ui.gameui;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;

public class PieceGauge extends DoaObject {

	private static final long serialVersionUID = -59674351675589726L;

	private static final float ACCELERATION = 0.032f;

	private static final DoaVectorF MIN = new DoaVectorF(1591f, 732f);
	private static final DoaVectorF MAX = new DoaVectorF(1801f, 732f);

	private static final DoaSprite TEXTURE = DoaSprites.get("pieceGauge");
	private static final DoaSprite NEEDLE = DoaSprites.get("gaugeNeedle");

	private float angle = 0;
	private boolean movementLock = false;

	public PieceGauge() {
		super(MAX.clone(), TEXTURE.getWidth() - 100, TEXTURE.getHeight() - 100, DoaObject.STATIC_FRONT);
	}

	@Override
	public void tick() {
		if (getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
			if (DoaMouse.MB1) {
				movementLock = !movementLock;
			}
			if (position.x > MIN.x && !movementLock) {
				velocity.x -= ACCELERATION;

			} else {
				angle -= 0.0005f;
			}
		} else {
			if (position.x < MAX.x && !movementLock) {
				velocity.x += ACCELERATION;
			}
		}
		position.x += velocity.x;
		if (position.x < MIN.x) {
			position.x = MIN.x;
			velocity.x = 0;
		}
		if (position.x > MAX.x) {
			position.x = MAX.x;
			velocity.x = 0;
		}

	}

	@Override
	public void render(DoaGraphicsContext g) {
		AffineTransform oldTransform = g.getTransform();
		g.drawImage(TEXTURE, position.x, position.y, width, height);
		g.rotate(angle, position.x + width / 2d, position.y + height / 2d);
		g.drawImage(NEEDLE, position.x + 266, position.y + 80, NEEDLE.getWidth() - 10d, NEEDLE.getHeight() - 40d);
		g.setTransform(oldTransform);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) position.x, (int) position.y, width, height);
	}
}
