package com.pmnm.roy.ui.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.globals.discordrichpresence.DiscordRichPresenceAdapter;
import com.pmnm.risk.globals.discordrichpresence.IDiscordActivityMutator;
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
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.utils.discordapi.DoaDiscordActivity;
import doa.engine.utils.discordapi.DoaDiscordService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import pmnm.risk.game.GameConfig;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.game.databasedimpl.RiskGameContext;
import pmnm.risk.map.MapConfig;
import pmnm.risk.map.MapData;
import pmnm.risk.map.MapLoader;

@SuppressWarnings("serial")
public class NewGameMenu extends RoyMenu implements Observer, IDiscordActivityMutator {
	
	public enum Type { SINGLE_PLAYER, MULTI_PLAYER }
	
	private static final String PLAY_KEY = "PLAY";
	private static final String BACK_KEY = "BACK";

	private static final DoaVector PLAY_POSITION = new DoaVector(1377f, 715f);
	private static final DoaVector BACK_POSITION = new DoaVector(1377f, 803f);
	
	private static final DoaVector PREV_MAP_POSITION = new DoaVector(1400, 290);
	private static final DoaVector NEXT_MAP_POSITION = new DoaVector(1700, 290);

	private final RoyImageButton prevMapButton; 
	private final RoyImageButton nextMapButton;

	private static final DoaVector COMBO_BOX_POSITION = new DoaVector(150, 290);
	private static final DoaVector COLOR_COMBO_BOX_POSITION = new DoaVector(400, 290);
	private static final DoaVector PAWN_COMBO_BOX_POSITION = new DoaVector(508, 290);

	private static final DoaVector COMBO_BOX_OFFSET = new DoaVector(0, 55);
	private static final DoaVector COLOR_COMBO_BOX_OFFSET = new DoaVector(0, 55);
	private static final DoaVector PAWN_COMBO_BOX_OFFSET = new DoaVector(0, 55);

	/*
	private static final RandomPlacementButton randomPlacementButton = Builders.RPBB
	        .args(new DoaVector(610, 687), DoaSprites.get("ReadyCircle"), DoaSprites.get("Ready"), "RANDOM_PLACEMENT")
	        .instantiate();
	*/

	private BufferedImage selectedMapPreview;
	private String selectedMapName;
	private int selectedMapIndex;
	
	private Type type;

	private Slot[] slots;
	private List<Integer> selectedColorIndices = new ArrayList<>();
	private List<Integer> selectedPawnIndices = new ArrayList<>();
	private List<RoyComboBox> playerComboBoxes = new ArrayList<>();
	private List<RoyComboBox> colorComboBoxes = new ArrayList<>();
	private List<RoyComboBox> pawnComboBoxes = new ArrayList<>();
	
	private RoyButton playButton;
	
	public NewGameMenu(Type t) {
		type = t;
		
		selectedMapIndex = 0;
		setSelectedMap(MapConfig.getConfigs().get(selectedMapIndex));

		// COMBOBOXES
		String[] names = new String[3];
		if (t == Type.SINGLE_PLAYER) {
			names[0] = "OPEN";
			names[1] = "LOCAL_PLAYER";
			names[2] = "AI";
				//"AI Passive",
				//"AI Easy",
				//"AI Medium",
				//"AI Hard",
				//"AI Insane",
				//"AI Impossible"
		} else if (t == Type.MULTI_PLAYER) {
			names[0] = "OPEN";
			names[1] = "CLOSED";
			names[2] = "AI";
		} else { throw new IllegalArgumentException("wtf"); }
		
		Color[] colors = PlayerColorBank.COLORS;
		BufferedImage[] pawns = UIConstants.getPlayerPawnSprites();
		slots = new Slot[Globals.MAX_NUM_PLAYERS];
		for(int i = 0; i < slots.length; i++) {
			slots[i] = new Slot(i);
			
			RoyComboBox playerBox = new RoyComboBox(names);
			playerBox.setPosition(new DoaVector(
				COMBO_BOX_POSITION.x + (i * COMBO_BOX_OFFSET.x),
				COMBO_BOX_POSITION.y + (i * COMBO_BOX_OFFSET.y)
			));
			playerBox.registerObserver(this);
			addElement(playerBox);
			playerComboBoxes.add(playerBox);
			
			RoyComboBox colorBox = new RoyComboBox(colors);
			colorBox.setPosition(new DoaVector(
				COLOR_COMBO_BOX_POSITION.x + (i * COLOR_COMBO_BOX_OFFSET.x),
				COLOR_COMBO_BOX_POSITION.y + (i * COLOR_COMBO_BOX_OFFSET.y)
			));
			colorBox.setSelectedIndex(i);
			colorBox.registerObserver(this);
			addElement(colorBox);
			colorComboBoxes.add(colorBox);
			
			RoyComboBox pawnBox = new RoyComboBox(pawns);
			colorBox.setPosition(new DoaVector(
				PAWN_COMBO_BOX_POSITION.x + (i * PAWN_COMBO_BOX_OFFSET.x),
				PAWN_COMBO_BOX_POSITION.y + (i * PAWN_COMBO_BOX_OFFSET.y)
			));
			pawnBox.setSelectedIndex(i);
			pawnBox.registerObserver(this);
			addElement(pawnBox);
			pawnComboBoxes.add(pawnBox);
			
			slots[i].playerBox = playerBox;
			slots[i].colorBox = colorBox;
			slots[i].pawnBox = pawnBox;
		}
		// COMBOBOXES END
		
		playButton = RoyButton.builder()
			.textKey(PLAY_KEY)
			.action(this::startGame)
			.build();
		playButton.setPosition(PLAY_POSITION);
		addElement(playButton);
		
		RoyButton backButton = RoyButton.builder()
			.textKey(BACK_KEY)
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

		addComponent(new Renderer());
	}
	
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
		List<Player.Data> playerDatas = new ArrayList<>(slots.length);
		for (Slot slot : slots) {
			if (slot.hasPlayer()) {
				Player.Data playerData = new Player.Data(slot.getPlayerName(), slot.getColor(), slot.getPawn(), slot.isLocalPlayer());
				playerDatas.add(playerData);
			}
		}
		GameConfig config = new GameConfig(playerDatas.toArray(Player.Data[]::new), false); // FIXME not always false, implement random selection
		context.initiliazeGame(config);
		// TODO add game stuff to game scene
		DoaSceneHandler.loadScene(Scenes.GAME_SCENE);
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
	private final class Slot extends DoaObject {
		
		private int index;
		private RoyComboBox playerBox;
		private RoyComboBox colorBox;
		private RoyComboBox pawnBox;
		// private RoyToggleButton readyButton;
		
		@Getter
		@Setter
		private String playerName;
		
		private Slot(int index) {
			this.index = index;
			playerName = "Player" + index;
		}

		private boolean hasPlayer() { 
			if (type == Type.SINGLE_PLAYER) {
				return playerBox.getSelectedIndex() != 0;	
			} else if (type == Type.MULTI_PLAYER) {
				return playerBox.getSelectedIndex() != 0 && playerBox.getSelectedIndex() != 1;	
			} else { throw new IllegalStateException("wtf?"); }
		}
		private Color getColor() { return PlayerColorBank.COLORS[colorBox.getSelectedIndex()]; }
		private String getPawn() { return DoaSprites.getSpriteName(UIConstants.getPlayerPawnSprites()[pawnBox.getSelectedIndex()]); }
		private boolean isLocalPlayer() { return type == Type.SINGLE_PLAYER; } /* TODO  */
	}
	
	public Slot findSlotOf(RoyComboBox box) {
		for (Slot slot : slots) {
			if (slot.playerBox == box
				|| slot.colorBox == box
				|| slot.pawnBox == box) {
				return slot;
			}
		}
		return null;
	}
	
	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
		playButton.setVisible(false);
		if (isVisible()) {
			for (Slot slot : slots) {
				slot.playerBox.setSelectedIndex(0);
				slot.colorBox.setSelectedIndex(0);
				slot.pawnBox.setSelectedIndex(0);
				slot.colorBox.setVisible(slot.hasPlayer());
				slot.pawnBox.setVisible(slot.hasPlayer());
			}

			applyCustomActivity();
		}
	}

	@Override
	public void onNotify(Observable b) {
		if (b instanceof RoyComboBox) {
			Slot changedSlot = findSlotOf((RoyComboBox)b);
			
			selectedColorIndices.clear();
			selectedPawnIndices.clear();
			for(int i = 0; i < slots.length; i++) {
				Slot slot = slots[i];
				if (slot != changedSlot && slot.hasPlayer()) {
					selectedColorIndices.add(slot.colorBox.getSelectedIndex());
					selectedPawnIndices.add(slot.pawnBox.getSelectedIndex());
				}
			}
			colorComboBoxes.forEach(c -> c.setLockedIndices(selectedColorIndices));
			pawnComboBoxes.forEach(c -> c.setLockedIndices(selectedPawnIndices));
			
			changedSlot.colorBox.setVisible(changedSlot.hasPlayer());
			changedSlot.pawnBox.setVisible(changedSlot.hasPlayer());
			
			selectedColorIndices.add(changedSlot.colorBox.getSelectedIndex());
			selectedPawnIndices.add(changedSlot.pawnBox.getSelectedIndex());
			
			colorComboBoxes.forEach(c -> c.setLockedIndices(selectedColorIndices));
			pawnComboBoxes.forEach(c -> c.setLockedIndices(selectedPawnIndices));

			int playerCount = (int)Stream.of(slots).filter(Slot::hasPlayer).count();
			if (playerCount > 0) {
				DoaDiscordActivity activity = DoaDiscordService.getCurrentActivity();
				activity.setPartySize(playerCount);
				activity.setPartyMaxSize(Globals.MAX_NUM_PLAYERS);
				DoaDiscordService.switchActivity(activity);
			} else {
				applyCustomActivity();
			}
			
			playButton.setVisible(playerCount >= 2);
		}		
	}
	
	@Override
	public void applyCustomActivity() {
		DoaDiscordActivity customActivity = DiscordRichPresenceAdapter.getDefaultActivity();
		customActivity.setDescription("Creating game");
		DoaDiscordService.switchActivity(customActivity);
	}
}