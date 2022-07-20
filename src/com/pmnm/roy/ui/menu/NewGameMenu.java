package com.pmnm.roy.ui.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyComboBox;
import com.pmnm.roy.RoyImageButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.game.databasedimpl.RiskGameContext;
import pmnm.risk.map.MapConfig;
import pmnm.risk.map.MapData;
import pmnm.risk.map.MapLoader;

@SuppressWarnings("serial")
public class NewGameMenu extends RoyMenu implements Observer {

	private static final DoaVector PLAY_POSITION = new DoaVector(1377f, 715f);
	private static final DoaVector BACK_POSITION = new DoaVector(1377f, 803f);
	
	private static final DoaVector PREV_MAP_POSITION = new DoaVector(1400, 290);
	private static final DoaVector NEXT_MAP_POSITION = new DoaVector(1700, 290);

	private final RoyImageButton prevMapButton; 
	private final RoyImageButton nextMapButton;

	private static final DoaVector COMBO_BOX_POSITION = new DoaVector(150, 290);
	private static final DoaVector COLOR_COMBO_BOX_POSITION = new DoaVector(400, 290);

	/*
	private static final RandomPlacementButton randomPlacementButton = Builders.RPBB
	        .args(new DoaVector(610, 687), DoaSprites.get("ReadyCircle"), DoaSprites.get("Ready"), "RANDOM_PLACEMENT")
	        .instantiate();
	*/

	private final Slot[] slots = new Slot[Globals.MAX_NUM_PLAYERS];

	private BufferedImage selectedMapPreview;
	private String selectedMapName;
	private int selectedMapIndex;

	private List<Integer> selectedColorIndices = new ArrayList<>();
	private List<RoyComboBox> colorComboBoxes = new ArrayList<>();
	
	public NewGameMenu() {
		selectedMapIndex = 0;
		setSelectedMap(MapConfig.getConfigs().get(selectedMapIndex));

		for (int i = 0; i < slots.length; i++) {
			slots[i] = new Slot(i);
		}

		// COMBOBOXES
		String[] names = new String[]{"Simge","Doa <3 ŞÜMOŞ","SMG"};
		Color[] colors = PlayerColorBank.COLORS;
		//Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE};
		for(int i = 0; i < 3; i++) {
			RoyComboBox comboBox = new RoyComboBox(names);
			comboBox.setPosition(new DoaVector(COMBO_BOX_POSITION.x, COMBO_BOX_POSITION.y + (i * 55)));
			addElement(comboBox);
			
			RoyComboBox comboBox2 = new RoyComboBox(colors);
			comboBox2.setPosition(new DoaVector(COLOR_COMBO_BOX_POSITION.x, COLOR_COMBO_BOX_POSITION.y + (i * 55)));
			comboBox2.setSelectedIndex(i);
			selectedColorIndices.add(i);
			colorComboBoxes.add(comboBox2);
			comboBox2.registerObserver(this);
			addElement(comboBox2);
		}
		
		for(RoyComboBox b : colorComboBoxes) {
			b.setLockedIndices(selectedColorIndices);
		}
		
		// COMBOBOXES END
		
		RoyButton playButton = RoyButton.builder()
			.textKey("PLAY")
			.action(this::startGame)
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
		selectedMapName = config.getName().replace("_", " ").toUpperCase(Locale.ENGLISH); /* map names have _ instead of spaces */
		selectedMapPreview = config.getBackgroundImagePreview();
		getComponentByType(Renderer.class).ifPresent(renderer -> renderer.font = null);
	}
	
	private void startGame() {
		List<@NonNull MapConfig> configs = MapConfig.getConfigs();
		MapConfig selectedConfig = configs.get(selectedMapIndex);
		MapData data = MapLoader.loadMap(selectedConfig);
		IRiskGameContext context = RiskGameContext.of(data);
	}
	
	private final class Renderer extends DoaRenderer {
		
		private transient BufferedImage mainScroll;
		private transient BufferedImage mapChooserBg;
		private transient BufferedImage mapBorder;
		
		private Font font;
		private int stringWidth;
		private DoaVector textDimensions;
		private DoaVector textPosition;
		
		private Renderer() {
			mainScroll = DoaSprites.getSprite("MainScroll");
			mapChooserBg = DoaSprites.getSprite("MapChooserBackground");
			mapBorder = DoaSprites.getSprite("MapBorder");
		}

		@Override
		public void render() {
			if (!isVisible()) { return; }
			if (font == null) {
				Rectangle nextMapButton = NewGameMenu.this.nextMapButton.getContentArea();
				Rectangle prevMapButton = NewGameMenu.this.prevMapButton.getContentArea();
				textDimensions = new DoaVector(
					nextMapButton.x - prevMapButton.x - prevMapButton.width,
					prevMapButton.height
				);
				textDimensions.x *= 0.9;
				
				font = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), textDimensions, selectedMapName), 0)[0]
				);
				
				FontMetrics fm = DoaGraphicsFunctions.getFontMetrics(font);
				stringWidth = fm.stringWidth(selectedMapName);
				int[] strSize = DoaGraphicsFunctions.unwarp(stringWidth, 0);
				stringWidth = strSize[0];

				textPosition = new DoaVector(
					prevMapButton.x + prevMapButton.width + (nextMapButton.x - prevMapButton.x - prevMapButton.width - textDimensions.x) / 2,
					prevMapButton.y + prevMapButton.height / 2f + fm.getHeight() / 3f // why divide by 3??!?!?!?!?!?
				);
			}

			DoaGraphicsFunctions.drawImage(mainScroll, 24, 176, mainScroll.getWidth(), mainScroll.getHeight());
			DoaGraphicsFunctions.drawImage(mapChooserBg, 1363, 259, mapChooserBg.getWidth(), mapChooserBg.getHeight());
	
			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.drawString(
				selectedMapName,
				textPosition.x + (textDimensions.x - stringWidth) / 2f,
				textPosition.y
			);
		
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
	@ToString(includeFieldNames = true)
	@EqualsAndHashCode(callSuper = true)
	private static final class Slot extends DoaObject {
		
		private Status status;
		private String playerName;
		private int playerColor;
		private final int playerPawn;
		
		private Slot(int index) {
			status = Status.OPEN;
			playerName = null;
			playerColor = index;
			playerPawn = index;
		}
		
		private enum Status {
			OPEN, CLOSED, HUMAN;
		}
	}

	@Override
	public void onNotify(Observable b) {
		selectedColorIndices.clear();
		colorComboBoxes.forEach(c -> selectedColorIndices.add(c.getSelectedIndex()));
		
		for(RoyComboBox rcb : colorComboBoxes) {
			rcb.setLockedIndices(selectedColorIndices);
		}
		
	}
	
}