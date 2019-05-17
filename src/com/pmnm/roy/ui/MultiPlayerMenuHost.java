package com.pmnm.roy.ui;

import java.awt.image.BufferedImage;
import java.io.File;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.main.Main;

public class MultiPlayerMenuHost extends DoaPanel {

	private static final long serialVersionUID = -5259890717501362272L;

	TextImageButton playButton = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.662f), UIInit.BUTTON_SIZE.x,
			UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE),
			"PLAY", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.752f), UIInit.BUTTON_SIZE.x,
			UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE),
			"BACK", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);

	DoaImageButton prevMapButton = DoaHandler.instantiate(DoaImageButton.class, Main.WINDOW_WIDTH * 0.731f,
			Main.WINDOW_HEIGHT * 0.27f, 38, 38, DoaSprites.get("ArrowLeftIdle"), DoaSprites.get("ArrowLeftClick"));
	DoaImageButton nextMapButton = DoaHandler.instantiate(DoaImageButton.class, Main.WINDOW_WIDTH * 0.887f,
			Main.WINDOW_HEIGHT * 0.27f, 38, 38, DoaSprites.get("ArrowRightIdle"), DoaSprites.get("ArrowRightClick"));

	RandomPlacementButton randomPlacementButton = DoaHandler.instantiate(RandomPlacementButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.315f,
			Main.WINDOW_HEIGHT * 0.635f), 22, 22, DoaSprites.get("ReadyCircle"), DoaSprites.get("Ready"), "RANDOM_PLACEMENT");

	DoaVectorF textRect = new DoaVectorF(Main.WINDOW_WIDTH * 0.092f, Main.WINDOW_HEIGHT * 0.040f);

	TypeComboButton[] tbca = new TypeComboButton[Globals.MAX_NUM_PLAYERS];
	ColorComboButton[] ccba = new ColorComboButton[Globals.MAX_NUM_PLAYERS];
	DifficultyComboButton[] dcba = new DifficultyComboButton[Globals.MAX_NUM_PLAYERS];

	private int mapNumber = 0;
	private String s;

	int numberOfPlayers = 2;

	public MultiPlayerMenuHost(MainMenu mm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
	}

	@Override
	public void render(DoaGraphicsContext g) {
		// TODO Auto-generated method stub
		
	}
}