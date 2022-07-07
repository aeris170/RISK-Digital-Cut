package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.ui.button.DoaUIImageButton;

public class DifficultyComboButton extends DoaUIImageButton {
	private static final long serialVersionUID = -6269009134113146366L;

	public static final String[] DIFFICULTIES = new String[] { "PASSIVE", "EASY", "MEDIUM", "HARD", "INSANE", "CHEATER" };

	public static final List<DifficultyComboButton> DIFFICULTY_COMBO_BUTTONS = new ArrayList<>();

	public int index = 0;

	public boolean hidden = false;

	public DifficultyComboButton(DoaVectorF position) {
		super(position, (int) (Main.WINDOW_WIDTH * 0.019f), (int) (Main.WINDOW_HEIGHT * 0.035f), DoaSprites.get("ArrowDownIdle"), DoaSprites.get("ArrowDownIdle"),
		        DoaSprites.get("ArrowDownClick"));
		DIFFICULTY_COMBO_BUTTONS.add(this);
	}

	@Override
	public void tick() {
		recalibrateBounds();
		if (isEnabled && DoaMouse.MB1) {
			if (click) {
				if (passiveHitBox().contains(DoaMouse.X, DoaMouse.Y)) {
					index = 0;
				} else if (easyHitBox().contains(DoaMouse.X, DoaMouse.Y)) {
					index = 1;
				} else if (mediumHitBox().contains(DoaMouse.X, DoaMouse.Y)) {
					index = 2;
				} else if (hardHitBox().contains(DoaMouse.X, DoaMouse.Y)) {
					index = 3;
				} else if (insaneHitBox().contains(DoaMouse.X, DoaMouse.Y)) {
					index = 4;
				} else if (cheaterHitBox().contains(DoaMouse.X, DoaMouse.Y)) {
					index = 5;
				}
				click = false;
			} else if (getBounds().contains(DoaMouse.X, DoaMouse.Y) && DIFFICULTY_COMBO_BUTTONS.stream().allMatch(cb -> (!cb.click || (cb.click && cb.noneHit())))) {
				click = !click;
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (!hidden) {
			g.setColor(UIInit.FONT_COLOR);
			g.drawImage(DoaSprites.get("DifficultyBorder"), position.x - Main.WINDOW_WIDTH * 0.077f, position.y - Main.WINDOW_HEIGHT * 0.003f);
			String s = Translator.getInstance().getTranslatedString(DIFFICULTIES[index]);
			g.drawString(s.substring(0, 1).toUpperCase() + s.substring(1), position.x - Main.WINDOW_WIDTH * 0.070f, position.y + Main.WINDOW_HEIGHT * 0.029f);
			super.render(g);
			if (click) {
				BufferedImage dropDown = DoaSprites.get("DropDown");
				int width = dropDown.getWidth();
				int height = dropDown.getHeight();
				g.drawImage(dropDown, position.x - Main.WINDOW_WIDTH * 0.077f, position.y + Main.WINDOW_HEIGHT * 0.040f, width, height);
				g.pushComposite();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
				g.drawImage(DoaSprites.get("DropDownTypeTex"), position.x - Main.WINDOW_WIDTH * 0.077f, position.y + Main.WINDOW_HEIGHT * 0.040f, width, height);
				g.popComposite();
				for (int i = 0; i < DIFFICULTIES.length; i++) {
					s = Translator.getInstance().getTranslatedString(DIFFICULTIES[i]);
					if (i < 3) {
						g.drawString(s.substring(0, 1).toUpperCase() + s.substring(1), position.x - Main.WINDOW_WIDTH * 0.070f,
						        position.y + Main.WINDOW_HEIGHT * 0.070f + (Main.WINDOW_HEIGHT * 0.028f * i));
					} else {
						g.drawString(s.substring(0, 1).toUpperCase() + s.substring(1) + " (WIP)", position.x - Main.WINDOW_WIDTH * 0.070f,
						        position.y + Main.WINDOW_HEIGHT * 0.070f + (Main.WINDOW_HEIGHT * 0.028f * i));
					}
				}
			}
		}
	}

	@Override
	public void hide() {
		super.hide();
		hidden = true;
	}

	@Override
	public void show() {
		super.show();
		hidden = false;
	}

	private Rectangle2D passiveHitBox() {
		return new Rectangle2D.Float(position.x - Main.WINDOW_WIDTH * 0.098f, position.y + Main.WINDOW_HEIGHT * 0.046f, Main.WINDOW_WIDTH * 0.115f, height * 0.8f);
	}

	private Rectangle2D easyHitBox() {
		return new Rectangle2D.Float(position.x - Main.WINDOW_WIDTH * 0.098f, position.y + Main.WINDOW_HEIGHT * 0.074f, Main.WINDOW_WIDTH * 0.115f, height * 0.8f);
	}

	private Rectangle2D mediumHitBox() {
		return new Rectangle2D.Float(position.x - Main.WINDOW_WIDTH * 0.098f, position.y + Main.WINDOW_HEIGHT * 0.102f, Main.WINDOW_WIDTH * 0.115f, height * 0.8f);
	}

	private Rectangle2D hardHitBox() {
		return new Rectangle2D.Float(position.x - Main.WINDOW_WIDTH * 0.098f, position.y + Main.WINDOW_HEIGHT * 0.130f, Main.WINDOW_WIDTH * 0.115f, height * 0.8f);
	}

	private Rectangle2D insaneHitBox() {
		return new Rectangle2D.Float(position.x - Main.WINDOW_WIDTH * 0.098f, position.y + Main.WINDOW_HEIGHT * 0.158f, Main.WINDOW_WIDTH * 0.115f, height * 0.8f);
	}

	private Rectangle2D cheaterHitBox() {
		return new Rectangle2D.Float(position.x - Main.WINDOW_WIDTH * 0.098f, position.y + Main.WINDOW_HEIGHT * 0.186f, Main.WINDOW_WIDTH * 0.115f, height * 0.8f);
	}

	private boolean noneHit() {
		return !passiveHitBox().contains(DoaMouse.X, DoaMouse.Y) && !easyHitBox().contains(DoaMouse.X, DoaMouse.Y) && !mediumHitBox().contains(DoaMouse.X, DoaMouse.Y)
		        && !hardHitBox().contains(DoaMouse.X, DoaMouse.Y) && !insaneHitBox().contains(DoaMouse.X, DoaMouse.Y)
		        && !cheaterHitBox().contains(DoaMouse.X, DoaMouse.Y);
	}

}
