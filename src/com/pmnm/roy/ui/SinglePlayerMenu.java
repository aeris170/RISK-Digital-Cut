package com.pmnm.roy.ui;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

public class SinglePlayerMenu extends DoaPanel {

	private static final long serialVersionUID = -7552086909580890620L;

	TextImageButton playButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.662f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);
	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.752f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	TextImageButton nextMapButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.662f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	DoaVectorF textRect = new DoaVectorF(Main.WINDOW_WIDTH * 0.092f, Main.WINDOW_HEIGHT * 0.040f);

	TypeComboButton[] tbca = new TypeComboButton[Globals.MAX_NUM_PLAYERS];
	ColorComboButton[] ccba = new ColorComboButton[Globals.MAX_NUM_PLAYERS];
	DifficultyComboButton[] dcba = new DifficultyComboButton[Globals.MAX_NUM_PLAYERS];

	File folder = new File("res/maps/");
	String s = folder.listFiles()[0].getName();

	int numberOfPlayers = 2;

	public SinglePlayerMenu(MainMenu mm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		playButton.addAction(() -> {
			hide();
			// TODO find a better way
			List<String> playerNames = new ArrayList<>();
			List<Color> playerColors = new ArrayList<>();
			List<String> aiNames = new ArrayList<>();
			List<Color> aiColors = new ArrayList<>();
			for (int i = 0; i < Globals.MAX_NUM_PLAYERS; i++) {
				if (tbca[i].index == 1) {
					playerNames.add("Player" + i);
					playerColors.add(ccba[i].getColor());
				} else if (tbca[i].index == 2) {
					aiNames.add("AI" + i);
					aiColors.add(ccba[i].getColor());
				}
			}
			RiskGameScreenUI.initUI(s, playerNames, playerColors, aiNames, aiColors);
		});
		backButton.addAction(() -> {
			hide();
			mm.show();
		});
		add(playButton);
		add(backButton);
		for (int i = Globals.MAX_NUM_PLAYERS - 1; i >= 0; i--) {
			TypeComboButton tbc = DoaHandler.instantiate(TypeComboButton.class,
			        new DoaVectorF(Main.WINDOW_WIDTH * 0.182f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)));
			add(tbc);
			tbca[i] = tbc;

			DifficultyComboButton dcb = DoaHandler.instantiate(DifficultyComboButton.class,
			        new DoaVectorF(Main.WINDOW_WIDTH * 0.312f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)));
			add(dcb);
			dcba[i] = dcb;

			ColorComboButton ccb = DoaHandler.instantiate(ColorComboButton.class,
			        new DoaVectorF(Main.WINDOW_WIDTH * 0.342f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)));
			add(ccb);
			ccba[i] = ccb;

		}
		tbca[0].index = 1;
		tbca[1].index = 2;
	}

	@Override
	public void tick() {
		super.tick();
		for (int i = 0; i < Globals.MAX_NUM_PLAYERS; i++) {
			if (tbca[i].index == 0) {
				dcba[i].hide();
				ccba[i].hide();
			} else if (tbca[i].index == 1) {
				dcba[i].hide();
				ccba[i].show();
			} else {
				dcba[i].show();
				ccba[i].show();
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("GenericBackground"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		g.drawImage(DoaSprites.get("MainScroll"), Main.WINDOW_WIDTH * 0.0125f, Main.WINDOW_HEIGHT * 0.163f);
		g.drawImage(DoaSprites.get("MapChooserBackground"), Main.WINDOW_WIDTH * 0.71f, Main.WINDOW_HEIGHT * 0.24f);
		g.drawImage(DoaSprites.get("ArrowLeftIdle"), Main.WINDOW_WIDTH * 0.74f, Main.WINDOW_HEIGHT * 0.27f);

		g.drawString(s, Main.WINDOW_WIDTH * 0.8f, Main.WINDOW_HEIGHT * 0.3f);

		g.drawImage(DoaSprites.get("ArrowRightIdle"), Main.WINDOW_WIDTH * 0.88f, Main.WINDOW_HEIGHT * 0.27f);
		g.drawImage(DoaSprites.get("MapBorder"), Main.WINDOW_WIDTH * 0.732f, Main.WINDOW_HEIGHT * 0.33f);

		/* File sourceimage = new File(path + "classic/map.png"); try { BufferedImage
		 * image = ImageIO.read(sourceimage); } catch (IOException e) {
		 * e.printStackTrace(); } */

	}
}