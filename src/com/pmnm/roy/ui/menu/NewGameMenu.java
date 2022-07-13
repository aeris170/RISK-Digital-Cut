package com.pmnm.roy.ui.menu;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.ColorComboButton;
import com.pmnm.roy.ui.DifficultyComboButton;
import com.pmnm.roy.ui.RandomPlacementButton;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.TypeComboButton;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.ui.button.DoaUIImageButton;
import doa.engine.ui.panel.DoaUIPanel;

public class NewGameMenu extends DoaUIPanel {

	private static final long serialVersionUID = -7552086909580890620L;

	private static final TextImageButton playButton = Builders.TIBB.args(new DoaVector(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.662f), UIInit.UIConstants.x,
	        UIInit.UIConstants.y, DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "PLAY", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR)
	        .instantiate();
	private static final TextImageButton backButton = Builders.TIBB.args(new DoaVector(Main.WINDOW_WIDTH * 0.716f, Main.WINDOW_HEIGHT * 0.752f), UIInit.UIConstants.x,
	        UIInit.UIConstants.y, DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "BACK", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR)
	        .instantiate();

	private static final DoaUIImageButton prevMapButton = Builders.DIBB
	        .args(Main.WINDOW_WIDTH * 0.731f, Main.WINDOW_HEIGHT * 0.27f, 38, 38, DoaSprites.get("ArrowLeftIdle"), DoaSprites.get("ArrowLeftClick")).instantiate();
	private static final DoaUIImageButton nextMapButton = Builders.DIBB
	        .args(Main.WINDOW_WIDTH * 0.887f, Main.WINDOW_HEIGHT * 0.27f, 38, 38, DoaSprites.get("ArrowRightIdle"), DoaSprites.get("ArrowRightClick")).instantiate();

	private static final RandomPlacementButton randomPlacementButton = Builders.RPBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.318f, Main.WINDOW_HEIGHT * 0.636f), 22, 22, DoaSprites.get("ReadyCircle"), DoaSprites.get("Ready"), "RANDOM_PLACEMENT")
	        .instantiate();

	private static final DoaVector textRect = new DoaVector(Main.WINDOW_WIDTH * 0.092f, Main.WINDOW_HEIGHT * 0.040f);

	private static final TypeComboButton[] tbca = new TypeComboButton[Globals.MAX_NUM_PLAYERS];
	private static final ColorComboButton[] ccba = new ColorComboButton[Globals.MAX_NUM_PLAYERS];
	private static final DifficultyComboButton[] dcba = new DifficultyComboButton[Globals.MAX_NUM_PLAYERS];

	private int mapNumber = 0;
	private String s;

	int numberOfPlayers = 2;

	public NewGameMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		playButton.addAction(() -> {
			hide();
			UIConstants.fb.hide();
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
			RiskGameScreenUI.initUI(s.replaceAll(" ", "_"), playerTypes, playerNames, playerColors, aiNames, aiColors, difficulties, randomPlacementButton.getClick());
		});
		backButton.addAction(() -> {
			hide();
			UIConstants.pofm.show();
		});
		prevMapButton.addAction(() -> {
			mapNumber--;
			mapNumber += Globals.MAP_NAMES.length;
			mapNumber %= Globals.MAP_NAMES.length;
		});
		nextMapButton.addAction(() -> {
			mapNumber++;
			mapNumber += Globals.MAP_NAMES.length;
			mapNumber %= Globals.MAP_NAMES.length;
		});
		add(playButton);
		add(backButton);
		add(randomPlacementButton);
		add(prevMapButton);
		add(nextMapButton);
		for (int i = 0; i < Globals.MAX_NUM_PLAYERS; i++) {
			System.out.println(i + ": " + (getzOrder() + Globals.MAX_NUM_PLAYERS - i + 1));
			TypeComboButton tbc = Builders.TCBB.args(new DoaVector(Main.WINDOW_WIDTH * 0.182f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)), true)
			        .instantiate();
			tbc.setzOrder(getzOrder() + Globals.MAX_NUM_PLAYERS - i + 1);
			add(tbc);
			tbca[i] = tbc;

			DifficultyComboButton dcb = Builders.DCBB.args(new DoaVector(Main.WINDOW_WIDTH * 0.289f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i)))
			        .instantiate();
			dcb.setzOrder(getzOrder() + Globals.MAX_NUM_PLAYERS - i + 1);
			add(dcb);
			dcba[i] = dcb;

			ColorComboButton ccb = Builders.CCBB.args(new DoaVector(Main.WINDOW_WIDTH * 0.347f, Main.WINDOW_HEIGHT * 0.275f + (Main.WINDOW_HEIGHT * 0.048f * i))).instantiate();
			ccb.setzOrder(getzOrder() + Globals.MAX_NUM_PLAYERS - i + 1);
			add(ccb);
			ccba[i] = ccb;
		}
		tbca[0].index = 1;
		tbca[1].index = 2;
		s = Globals.MAP_NAMES[mapNumber];
		hide();
	}

	@Override
	public void tick() {
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
		g.drawImage(DoaSprites.get("MainScroll"), Main.WINDOW_WIDTH * 0.0125f, Main.WINDOW_HEIGHT * 0.163f);
		g.drawImage(DoaSprites.get("MapChooserBackground"), Main.WINDOW_WIDTH * 0.71f, Main.WINDOW_HEIGHT * 0.24f);

		DoaVector bounds = new DoaVector(nextMapButton.getPosition().x - prevMapButton.getPosition().x + prevMapButton.getWidth() - prevMapButton.getWidth() * 2,
		        prevMapButton.getHeight());
		g.setFont(UIConstants.UI_FONT.deriveFont(Utils.findMaxFontSizeToFitInArea(g, UIConstants.UI_FONT.deriveFont(1), bounds, s)));
		g.setColor(UIConstants.FONT_COLOR);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, prevMapButton.getPosition().x + prevMapButton.getWidth() + (bounds.x - fm.stringWidth(s)) / 2, prevMapButton.getPosition().y + bounds.y * 3 / 4);

		BufferedImage mapBorder = DoaSprites.get("MapBorder");

		g.drawImage(DoaSprites.get("MAP#" + mapNumber), Main.WINDOW_WIDTH * 0.734f, Main.WINDOW_HEIGHT * 0.332f, mapBorder.getWidth() - Main.WINDOW_WIDTH * 0.003f,
		        mapBorder.getHeight() - Main.WINDOW_HEIGHT * 0.003f);
		g.drawImage(mapBorder, Main.WINDOW_WIDTH * 0.732f, Main.WINDOW_HEIGHT * 0.33f);
	}
}