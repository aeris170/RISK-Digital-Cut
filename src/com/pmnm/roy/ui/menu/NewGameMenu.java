package com.pmnm.roy.ui.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyImageButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.ColorComboButton;
import com.pmnm.roy.ui.DifficultyComboButton;
import com.pmnm.roy.ui.TypeComboButton;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.Data;
import lombok.NonNull;
import pmnm.risk.map.MapConfig;

@SuppressWarnings("serial")
public class NewGameMenu extends RoyMenu {

	private static final DoaVector PLAY_POSITION = new DoaVector(1377f, 715f);
	private static final DoaVector BACK_POSITION = new DoaVector(1377f, 803f);
	
	private static final DoaVector PREV_MAP_POSITION = new DoaVector(1400, 290);
	private static final DoaVector NEXT_MAP_POSITION = new DoaVector(1700, 290);

	private final RoyImageButton prevMapButton; 
	private final RoyImageButton nextMapButton;

	/*
	private static final RandomPlacementButton randomPlacementButton = Builders.RPBB
	        .args(new DoaVector(610, 687), DoaSprites.get("ReadyCircle"), DoaSprites.get("Ready"), "RANDOM_PLACEMENT")
	        .instantiate();
	*/
	
	private static final DoaVector mapNameArea = new DoaVector(175, 45);

	private static final TypeComboButton[] tbca = new TypeComboButton[Globals.MAX_NUM_PLAYERS];
	private static final ColorComboButton[] ccba = new ColorComboButton[Globals.MAX_NUM_PLAYERS];
	private static final DifficultyComboButton[] dcba = new DifficultyComboButton[Globals.MAX_NUM_PLAYERS];

	private final Slot[] slots = new Slot[Globals.MAX_NUM_PLAYERS];

	private BufferedImage selectedMapPreview;
	private String selectedMapName;
	private int selectedMapIndex;
	
	public NewGameMenu() {
		selectedMapIndex = 0;
		setSelectedMap(MapConfig.getConfigs().get(selectedMapIndex));
		
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new Slot(i);
		}
		
		RoyButton playButton = RoyButton.builder()
			.textKey("PLAY")
			.action(() -> {}) // TODO
			.build();
		playButton.setPosition(PLAY_POSITION);
		addElement(playButton);
		
		RoyButton backButton = RoyButton.builder()
			.textKey("BACK")
			.action(() -> {
				setVisible(false);
				UIConstants.getPlayOfflineMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(BACK_POSITION);
		addElement(backButton);
		
		prevMapButton = RoyImageButton.builder()
			.image(UIConstants.getArrowLeftIdleSprite())
			.hoverImage(UIConstants.getArrowLeftIdleSprite())
			.pressImage(UIConstants.getArrowLeftPressedSprite())
			.action(() -> {
				List<@NonNull MapConfig> configs = MapConfig.getConfigs();
				selectedMapIndex--;
				selectedMapIndex += configs.size();
				selectedMapIndex %= configs.size();

				setSelectedMap(configs.get(selectedMapIndex));
			})
			.build();
		prevMapButton.setPosition(PREV_MAP_POSITION);
		addElement(prevMapButton);
		
		nextMapButton = RoyImageButton.builder()
			.image(UIConstants.getArrowRightIdleSprite())
			.hoverImage(UIConstants.getArrowRightIdleSprite())
			.pressImage(UIConstants.getArrowRightPressedSprite())
			.action(() -> {
				List<@NonNull MapConfig> configs = MapConfig.getConfigs();
				selectedMapIndex++;
				selectedMapIndex %= MapConfig.getConfigs().size();

				setSelectedMap(configs.get(selectedMapIndex));
			})
			.build();
		nextMapButton.setPosition(NEXT_MAP_POSITION);
		addElement(nextMapButton);
		/*
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
		*/
		addComponent(new Renderer());
	}

	/*
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
	*/
	
	private void setSelectedMap(MapConfig config) {
		selectedMapName = config.getName().replace("_", " "); /* map names have _ instead of spaces */
		selectedMapPreview = config.getBackgroundImagePreview();
		getComponentByType(Renderer.class).ifPresent(renderer -> renderer.font = null);
	}
	
	private final class Renderer extends DoaRenderer {
		
		private transient BufferedImage mainScroll;
		private transient BufferedImage mapChooserBg;
		private transient BufferedImage mapBorder;
		
		private Font font;
		private int stringWidth;
		private DoaVector textDimensions;
		
		private Renderer() {
			mainScroll = DoaSprites.getSprite("MainScroll");
			mapChooserBg = DoaSprites.getSprite("MapChooserBackground");
			mapBorder = DoaSprites.getSprite("MapBorder");
		}

		@Override
		public void render() {
			if (!isVisible()) { return; }
			
			DoaGraphicsFunctions.drawImage(mainScroll, 24, 176, mainScroll.getWidth(), mainScroll.getHeight());
			DoaGraphicsFunctions.drawImage(mapChooserBg, 1363, 259, mapChooserBg.getWidth(), mapChooserBg.getHeight());
	
			if (font == null) {
				Rectangle nextMapButton = NewGameMenu.this.nextMapButton.getContentArea();
				Rectangle prevMapButton = NewGameMenu.this.prevMapButton.getContentArea();
				textDimensions = new DoaVector(
					nextMapButton.x - prevMapButton.x + prevMapButton.width - prevMapButton.width * 2,
					prevMapButton.height
				);
				
				font = UIConstants.getFont().deriveFont(
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), textDimensions, selectedMapName), 0)[0]
				);
				
				FontMetrics fm = DoaGraphicsFunctions.getFontMetrics(font);
				stringWidth = fm.stringWidth(selectedMapName);
				int[] strSize = DoaGraphicsFunctions.unwarp(stringWidth, 0);
				stringWidth = strSize[0];
			}
			
			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.drawString(
				selectedMapName, 
				mapNameArea.x + (textDimensions.x - stringWidth) / 2f,
				mapNameArea.y
			);
			//DoaGraphicsFunctions.drawString(s, prevMapButton.getPosition().x + prevMapButton.getWidth() + (bounds.x - fm.stringWidth(s)) / 2, prevMapButton.getPosition().y + bounds.y * 3 / 4);
		
			DoaGraphicsFunctions.drawImage(
				selectedMapPreview,
				1410,
				360,
				mapBorder.getWidth() - 5f,
				mapBorder.getHeight() - 3f);
			DoaGraphicsFunctions.drawImage(mapBorder, 1405, 357);
		}
	}
	
	@Data
	private static final class Slot {
		
		private Status status;
		private String playerName;
		private int playerColor;
		private int playerPawn;
		
		private Slot(int index) {
			status = Status.OPEN;
			playerName = null;
			playerColor = index;
			playerPawn = index;
		}
		
		private enum Status {
			OPEN, CLOSED, HUMAN,;
		}
	}
	
}