package com.pmnm.roy.ui.menu;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;
import com.pmnm.roy.ui.ZOrders;

public class RulesMenu extends DoaPanel {

	private static final long serialVersionUID = 7874225793360662873L;

	private Map<BufferedImage, Boolean> pages = new HashMap<>();

	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.016f, Main.WINDOW_HEIGHT * 0.902f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	public RulesMenu() {
		super(0f, (float) -Main.WINDOW_HEIGHT, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		pages.put(DoaSprites.get("pt0"), true);
		pages.put(DoaSprites.get("pt1"), false);
		pages.put(DoaSprites.get("pt2"), false);
		pages.put(DoaSprites.get("pt3"), false);
		pages.put(DoaSprites.get("pt4"), false);
		pages.put(DoaSprites.get("pt5"), false);
		backButton.addAction(() -> {
			hide();
			pages.replaceAll((k, v) -> false);
		});
		add(backButton);
		setzOrder(ZOrders.RULES_Z);
		hide();
	}

	@Override
	public void tick() {
		if (DoaKeyboard.ONE || DoaKeyboard.NUM_1) {
			pages.replaceAll((k, v) -> false);
			pages.replace(DoaSprites.get("pt0"), true);
		}
		if (DoaKeyboard.TWO || DoaKeyboard.NUM_2) {
			pages.replaceAll((k, v) -> false);
			pages.replace(DoaSprites.get("pt1"), true);
		}
		if (DoaKeyboard.THREE || DoaKeyboard.NUM_3) {
			pages.replaceAll((k, v) -> false);
			pages.replace(DoaSprites.get("pt2"), true);
		}
		if (DoaKeyboard.FOUR || DoaKeyboard.NUM_4) {
			pages.replaceAll((k, v) -> false);
			pages.replace(DoaSprites.get("pt3"), true);
		}
		if (DoaKeyboard.FIVE || DoaKeyboard.NUM_5) {
			pages.replaceAll((k, v) -> false);
			pages.replace(DoaSprites.get("pt4"), true);
		}
		if (DoaKeyboard.SIX || DoaKeyboard.NUM_6) {
			pages.replaceAll((k, v) -> false);
			pages.replace(DoaSprites.get("pt5"), true);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(pages.entrySet().stream().filter(entry -> entry.getValue()).map(Map.Entry::getKey).findFirst().orElse(DoaSprites.get("pt0")), 0f, 0f,
		        Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
	}
}