package com.pmnm.risk.ui.gameui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;

public class MiddleSectionScroll extends DoaPanel {

	private static final long serialVersionUID = -3397313460439190782L;

	private static final float ACCELERATION = 0.064f;

	private static final DoaVectorF MIN = new DoaVectorF(616f, 489f);
	private static final DoaVectorF MAX = new DoaVectorF(616f, 929f);

	private static final DoaSprite TEXTURE = DoaSprites.get("scroll");

	boolean moving = false;

	public MiddleSectionScroll() {
		super(MAX.clone(), TEXTURE.getWidth(), TEXTURE.getHeight());
		show();
	}

	public void move() {
		moving = true;
		if (position.y == MAX.y) {
			velocity.y = -1;
		}
		if (position.y == MIN.y) {
			velocity.y = 1;
		}
	}

	@Override
	public void tick() {
		if (moving) {
			if (velocity.y > 0) {
				velocity.y += ACCELERATION;
			} else if (velocity.y < 0) {
				velocity.y -= ACCELERATION;
			}
			position.add(velocity);
			if (position.y > MAX.y) {
				position.y = MAX.y;
				velocity.y = 0;
				moving = false;
			}
			if (position.y < MIN.y) {
				position.y = MIN.y;
				velocity.y = 0;
				moving = false;
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(TEXTURE, position.x, position.y);
	}
}