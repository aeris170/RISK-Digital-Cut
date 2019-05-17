package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

public class SinglePlayerMenu extends DoaPanel {

	private static final long serialVersionUID = -7552086909580890620L;

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

	public SinglePlayerMenu(PlayOfflineMenu pom) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		playButton.addAction(() -> {
			hide();
			// TODO find a better way
			List<Integer> playerTypes = new ArrayList<>();
			List<String> playerNames = new ArrayList<>();
			List<Color> playerColors = new ArrayList<>();
			List<String> aiNames = new ArrayList<>();
			List<Color> aiColors = new ArrayList<>();
			List<Integer> difficulties = new ArrayList<>();
			for (int i = 0; i < Globals.MAX_NUM_PLAYERS; i++) {
				if (tbca[i].index == 1) {
					playerTypes.add(1);
					playerNames.add("Player" + i);
					playerColors.add(ccba[i].getColor());
				} else if (tbca[i].index == 2) {
					playerTypes.add(2);
					aiNames.add("AI" + i);
					aiColors.add(ccba[i].getColor());
					difficulties.add(dcba[i].index);
				}
			}
			RiskGameScreenUI.initUI(s.replaceAll(" ", "/"), playerTypes, playerNames, playerColors, aiNames, aiColors,
					difficulties, randomPlacementButton.getClick());
		});
		backButton.addAction(() -> {
			hide();
			DoaHandler.remove(this);
			TypeComboButton.COMBO_BUTTONS.forEach(b -> DoaHandler.remove(b));
			TypeComboButton.COMBO_BUTTONS.clear();
			ColorComboButton.COMBO_BUTTONS.forEach(b -> DoaHandler.remove(b));
			ColorComboButton.COMBO_BUTTONS.clear();
			DifficultyComboButton.DIFFICULTY_COMBO_BUTTONS.forEach(b -> DoaHandler.remove(b));
			DifficultyComboButton.DIFFICULTY_COMBO_BUTTONS.clear();
			DoaHandler.remove(playButton);
			DoaHandler.remove(backButton);
			DoaHandler.remove(prevMapButton);
			DoaHandler.remove(nextMapButton);
			pom.show();
		});
		randomPlacementButton.addAction(() -> {

		});
		prevMapButton.addAction(() -> {
			if (mapNumber <= 0) {
				mapNumber = 1;
			} else {
				mapNumber--;
			}
		});
		nextMapButton.addAction(() -> {
			if (mapNumber >= 1) {
				mapNumber = 0;
			} else {
				mapNumber++;
			}
		});
		add(playButton);
		add(backButton);
		add(randomPlacementButton);
		add(prevMapButton);
		add(nextMapButton);
		for (int i = Globals.MAX_NUM_PLAYERS - 1; i >= 0; i--) {
			TypeComboButton tbc = DoaHandler.instantiate(TypeComboButton.class, new DoaVectorF(
					Main.WINDOW_WIDTH * 0.182f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)));
			add(tbc);
			tbca[i] = tbc;

			DifficultyComboButton dcb = DoaHandler.instantiate(DifficultyComboButton.class, new DoaVectorF(
					Main.WINDOW_WIDTH * 0.289f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)));
			add(dcb);
			dcba[i] = dcb;

			ColorComboButton ccb = DoaHandler.instantiate(ColorComboButton.class, new DoaVectorF(
					Main.WINDOW_WIDTH * 0.347f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)));
			add(ccb);
			ccba[i] = ccb;

		}
		tbca[0].index = 1;
		tbca[1].index = 2;
		show();
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
		s = Globals.MAP_NAMES[mapNumber];
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("BG2"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH,
					UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d,
					UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT,
					UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0,
				Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		g.drawImage(DoaSprites.get("MainScroll"), Main.WINDOW_WIDTH * 0.0125f, Main.WINDOW_HEIGHT * 0.163f);
		g.drawImage(DoaSprites.get("MapChooserBackground"), Main.WINDOW_WIDTH * 0.71f, Main.WINDOW_HEIGHT * 0.24f);

		DoaVectorF bounds = new DoaVectorF(nextMapButton.getPosition().x - prevMapButton.getPosition().x
				+ prevMapButton.getWidth() - prevMapButton.getWidth() * 2, prevMapButton.getHeight());
		g.setFont(UIInit.UI_FONT
				.deriveFont(Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT.deriveFont(1), bounds, s)));
		g.setColor(UIInit.FONT_COLOR);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, prevMapButton.getPosition().x + prevMapButton.getWidth() + (bounds.x - fm.stringWidth(s)) / 2,
				prevMapButton.getPosition().y + bounds.y * 3 / 4);

		BufferedImage mapBorder = DoaSprites.get("MapBorder");

		g.drawImage(DoaSprites.get("MAP#" + mapNumber), Main.WINDOW_WIDTH * 0.734f, Main.WINDOW_HEIGHT * 0.332f,
				mapBorder.getWidth() - Main.WINDOW_WIDTH * 0.003f, mapBorder.getHeight() - Main.WINDOW_HEIGHT * 0.003f);
		g.drawImage(mapBorder, Main.WINDOW_WIDTH * 0.732f, Main.WINDOW_HEIGHT * 0.33f);
	}
}