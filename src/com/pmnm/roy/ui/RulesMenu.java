package com.pmnm.roy.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;

public class RulesMenu extends DoaPanel {

	private static final long serialVersionUID = -4228991106639373659L;
	private boolean one, two, three, four, five = false;
	
	List<BufferedImage> rulesList = new ArrayList<>();
	
	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.016f, Main.WINDOW_HEIGHT * 0.902f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	public RulesMenu(MainMenu mm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		rulesList.add(DoaSprites.get("pt0"));
		rulesList.add(DoaSprites.get("pt1"));
		rulesList.add(DoaSprites.get("pt2"));
		rulesList.add(DoaSprites.get("pt3"));
		rulesList.add(DoaSprites.get("pt4"));
		rulesList.add(DoaSprites.get("pt5"));
		backButton.addAction(() -> {
			hide();
			one = false;
			two = false;
			three = false;
			four = false;
			five = false;
			mm.show();
		});
		add(backButton);
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(rulesList.get(0), 0, 0);
		if(DoaKeyboard.ONE) {
			one = true;
			two = false;
			three = false;
			four = false;
			five = false;
		}
		if(DoaKeyboard.TWO) {
			one = false;
			two = true;
			three = false;
			four = false;
			five = false;
		}
		if(DoaKeyboard.THREE) {
			one = false;
			two = false;
			three = true;
			four = false;
			five = false;
		}
		if(DoaKeyboard.FOUR) {
			one = false;
			two = false;
			three = false;
			four = true;
			five = false;
		}
		if(DoaKeyboard.FIVE) {
			one = false;
			two = false;
			three = false;
			four = false;
			five = true;
		}
		
		if(one) {
			g.drawImage(rulesList.get(1), 0, 0);
		}
		if(two) {
			g.drawImage(rulesList.get(2), 0, 0);
		}
		if(three) {
			g.drawImage(rulesList.get(3), 0, 0);
		}
		if(four) {
			g.drawImage(rulesList.get(4), 0, 0);
		}
		if(five) {
			g.drawImage(rulesList.get(5), 0, 0);
		}
	}
}