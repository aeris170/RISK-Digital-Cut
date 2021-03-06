package com.pmnm.roy.ui.gameui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.DoaUIContainer;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.Builders;
import com.pmnm.roy.ui.gameui.actions.BlitzButtonAction;
import com.pmnm.roy.ui.gameui.actions.DiceButtonAction;

public class DicePanel extends DoaUIContainer {

	private static final long serialVersionUID = 8009744806803376915L;

	public static DicePanel INSTANCE;

	private static final float ACCELERATION = 0.064f;

	private static final DoaVectorF MIN = new DoaVectorF(-160f, 258f);
	private static final DoaVectorF MAX = new DoaVectorF(0f, 823f);

	private DoaImageButton one = Builders.DIBB.args(-111f, 367f, 54, 60, DoaSprites.get("dice1Idle"), DoaSprites.get("dice1Hover")).instantiate();
	private DoaImageButton two = Builders.DIBB.args(-138f, 441f, 109, 62, DoaSprites.get("dice2Idle"), DoaSprites.get("dice2Hover")).instantiate();
	private DoaImageButton three = Builders.DIBB.args(-139f, 524f, 109, 86, DoaSprites.get("dice3Idle"), DoaSprites.get("dice3Hover")).instantiate();
	private DoaImageButton blitz;

	private boolean moving = false;

	public DicePanel() {
		super(MIN.clone(), (int) (MAX.x - MIN.x), (int) (MAX.y - MIN.y));
		one.addAction(new DiceButtonAction(1));
		two.addAction(new DiceButtonAction(2));
		three.addAction(new DiceButtonAction(3));
		add(one);
		add(two);
		add(three);
		blitz = Builders.BBB.args(-130f, 643f, 85, 60, DoaSprites.get("blitzIdle"), DoaSprites.get("blitzHover")).instantiate();
		blitz.addAction(new BlitzButtonAction());
		add(blitz);
		super.show();
		INSTANCE = this;
	}

	@Override
	public void tick() {
		if (moving) {
			if (velocity.x > 0) {
				velocity.x += ACCELERATION;
			} else if (velocity.x < 0) {
				velocity.x -= ACCELERATION;
			}
			if (position.x > MAX.x) {
				position.x = MAX.x;
				velocity.x = 0;
				moving = false;
			}
			if (position.x < MIN.x) {
				position.x = MIN.x;
				velocity.x = 0;
				moving = false;
			}
			position.add(velocity);
			one.getPosition().add(velocity);
			two.getPosition().add(velocity);
			three.getPosition().add(velocity);
			blitz.getPosition().add(velocity);
		}
	}

	@Override
	public void show() {
		if (position.x != MAX.x) {
			moving = true;
			velocity.x = 1;
		}
	}

	@Override
	public void hide() {
		if (position.x != MIN.x) {
			moving = true;
			velocity.x = -1;
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("diceScroll"), position.x, position.y);
	}
}