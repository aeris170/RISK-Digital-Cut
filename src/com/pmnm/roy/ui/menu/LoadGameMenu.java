package com.pmnm.roy.ui.menu;

import java.io.File;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;

public class LoadGameMenu extends DoaPanel {

	private static final long serialVersionUID = -7370360337478315048L;

	private File[] f;

	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 803f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);

	public LoadGameMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		f = new File(System.getProperty("user.home") + "\\Documents\\My Games\\RiskDigitalCut\\Saves\\").listFiles();
		backButton.addAction(() -> {
			hide();
			UIInit.pofm.show();
		});
		add(backButton);
		hide();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {}
}