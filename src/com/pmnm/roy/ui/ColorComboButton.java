package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;

public class ColorComboButton extends DoaImageButton {

	private static final long serialVersionUID = 2677754096284998205L;

	private static final BufferedImage[] OPTIONS = new BufferedImage[PlayerColorBank.size()];

	static {
		for (int i = 0; i < PlayerColorBank.size(); i++) {
			BufferedImage w = DoaSprites.deepCopyDoaSprite(DoaSprites.get("White"));
			Utils.paintImage(w, PlayerColorBank.get(i));
			OPTIONS[i] = w;
		}
	}

	private static final List<ColorComboButton> COMBO_BUTTONS = new ArrayList<>();

	int index = 0;

	public ColorComboButton(DoaVectorF position) {
		super(position, (int) (Main.WINDOW_WIDTH * 0.019f), (int) (Main.WINDOW_HEIGHT * 0.035f), DoaSprites.get("ArrowDownIdle"), DoaSprites.get("ArrowDownIdle"),
		        DoaSprites.get("ArrowDownClick"));
		index = Globals.MAX_NUM_PLAYERS - COMBO_BUTTONS.size() - 1;
		COMBO_BUTTONS.add(this);
	}

	@Override
	public void tick() {
		recalibrateBounds();
		if (isEnabled && DoaMouse.MB1) {
			if (click) {
				if (firstColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().filter(cb -> cb != this).allMatch(cb -> cb.index != 0)) {
					index = 0;
				} else if (secondColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().filter(cb -> cb != this).allMatch(cb -> cb.index != 1)) {
					index = 1;
				} else if (thirdColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().filter(cb -> cb != this).allMatch(cb -> cb.index != 2)) {
					index = 2;
				} else if (fourthColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().filter(cb -> cb != this).allMatch(cb -> cb.index != 3)) {
					index = 3;
				} else if (fifthColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().filter(cb -> cb != this).allMatch(cb -> cb.index != 4)) {
					index = 4;
				} else if (sixthColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().filter(cb -> cb != this).allMatch(cb -> cb.index != 5)) {
					index = 5;
				}
				click = false;
			} else if (getBounds().contains(DoaMouse.X, DoaMouse.Y) && COMBO_BUTTONS.stream().allMatch(cb -> (!cb.click || (cb.click && cb.noneHit())))) {
				click = !click;
			}
		}
	}

	// TODO IMPLEMENT
	@Override
	public void render(DoaGraphicsContext g) {
		g.setColor(UIInit.FONT_COLOR);
		g.drawImage(OPTIONS[index], position.x - Main.WINDOW_WIDTH * 0.027f, position.y);
		g.drawImage(DoaSprites.get("ColorBorder"), position.x - Main.WINDOW_WIDTH * 0.029f, position.y - Main.WINDOW_HEIGHT * 0.003f);
		super.render(g);
		if (click) {
			int height = DoaSprites.get("DropDownColor").getHeight();
			g.drawImage(DoaSprites.get("DropDownColor"), position.x - Main.WINDOW_WIDTH * 0.029f, position.y + Main.WINDOW_HEIGHT * 0.040f, Main.WINDOW_WIDTH * 0.050f,
			        height);
			g.pushComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g.drawImage(DoaSprites.get("DropDownColorTex"), position.x - Main.WINDOW_WIDTH * 0.029f, position.y + Main.WINDOW_HEIGHT * 0.040f, Main.WINDOW_WIDTH * 0.050f,
			        height);
			g.popComposite();
			for (int i = 0; i < OPTIONS.length; i++) {
				g.drawImage(OPTIONS[i], position.x - Main.WINDOW_WIDTH * 0.027f, position.y + Main.WINDOW_HEIGHT * 0.045f + (Main.WINDOW_HEIGHT * 0.040f * i),
				        OPTIONS[i].getWidth() + Main.WINDOW_WIDTH * 0.020f, OPTIONS[i].getHeight());
			}
		}
	}

	private Rectangle2D firstColorHitBox() {
		return new Rectangle2D.Float();
	}

	private Rectangle2D secondColorHitBox() {
		return new Rectangle2D.Float();
	}

	private Rectangle2D thirdColorHitBox() {
		return new Rectangle2D.Float();
	}

	private Rectangle2D fourthColorHitBox() {
		return new Rectangle2D.Float();
	}

	private Rectangle2D fifthColorHitBox() {
		return new Rectangle2D.Float();
	}

	private Rectangle2D sixthColorHitBox() {
		return new Rectangle2D.Float();
	}

	private boolean noneHit() {
		return !firstColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && !secondColorHitBox().contains(DoaMouse.X, DoaMouse.Y)
		        && !thirdColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && !fourthColorHitBox().contains(DoaMouse.X, DoaMouse.Y)
		        && !fifthColorHitBox().contains(DoaMouse.X, DoaMouse.Y) && !sixthColorHitBox().contains(DoaMouse.X, DoaMouse.Y);
	}
}
