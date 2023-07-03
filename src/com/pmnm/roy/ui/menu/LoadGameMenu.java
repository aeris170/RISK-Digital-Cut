package com.pmnm.roy.ui.menu;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.javatuples.Quintet;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.RoyMiniButton;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.utils.DoaUtils;
import pmnm.risk.game.databasedimpl.GameInstance;
import pmnm.risk.game.databasedimpl.GameInstance.Metadata;

@SuppressWarnings("serial")
public class LoadGameMenu extends RoyMenu implements Observer {
	
	private static final DoaVector[] LOAD_BUTTON_LOCATIONS = {
		new DoaVector(147f, 413f),
		new DoaVector(787f, 413f),
		new DoaVector(1427f, 413f),
		new DoaVector(147f, 805f),
		new DoaVector(787f, 805f)
	};
	private static final DoaVector[] SAVE_SCROLL_LOCATIONS = {
		new DoaVector(15f, 148f),
		new DoaVector(655f, 148f),
		new DoaVector(1295f, 148f),
		new DoaVector(15f, 540f),
		new DoaVector(655f, 540f)
	};
	private static final DoaVector[] SAVE_MAP_CONTAINER_LOCATIONS = {
		new DoaVector(127f, 242f),
		new DoaVector(767f, 242f),
		new DoaVector(1407f, 242f),
		new DoaVector(127f, 634f),
		new DoaVector(767f, 634f)
	};
	private static final DoaVector BACK_LOCATION = new DoaVector(1377f, 803f);

	private Quintet<Metadata, Metadata, Metadata, Metadata, Metadata> metas = Quintet.with(null, null, null, null, null);
	private List<RoyMiniButton> loadButtons = new ArrayList<>();

	public LoadGameMenu() {
		RoyButton backButton = RoyButton
			.builder()
			.textKey("BACK")
			.action(source -> {
				DoaScene scene = DoaSceneHandler.getLoadedScene();
				if (scene == Scenes.getMenuScene()) {
					UIConstants.getLoadGameMenu().setVisible(false);
					UIConstants.getPlayOfflineMenu().setVisible(true);
				} else if (scene == Scenes.getGameScene()) {
					RiskGameScreenUI.setLoadMenuVisibility(false);
				}
			})
			.build();
		backButton.setPosition(BACK_LOCATION);
		addElement(backButton);
		
		for(int i = 0; i < metas.getSize(); i++) {
			final int order = i;
			RoyMiniButton loadButton  = RoyMiniButton.builder()
				.textKey("LOAD")
				.action(source -> {
					/* 
					 * this.setVisible(false) will null
					 * metas, therefore we need to set the
					 * config of loading screen before that.
					 */
					Metadata meta = (Metadata) metas.getValue(order);
					UIConstants.getLoadingScreen().setGameConfig(meta.getConfig());
					
					DoaScene scene = DoaSceneHandler.getLoadedScene();
					if (scene == Scenes.getMenuScene()) {
						UIConstants.getLoadGameMenu().setVisible(false);
					} else if (scene == Scenes.getGameScene()) {
						RiskGameScreenUI.setLoadMenuVisibility(false);
					}

					UIConstants.getEmbroidments().setVisible(false);
					UIConstants.getLoadingScreen().setVisible(true);
					new Thread(()-> {
						UIConstants.getLoadingScreen().setLoadingText("Loading Game Context...");
						DoaUtils.sleepFor(2500L);
						UIConstants.getLoadingScreen().setLoadingBarProgress(0.40f);
						GameInstance instance = GameInstance.loadGame(order);
						UIConstants.getLoadingScreen().setLoadingBarProgress(0.60f);
						
						UIConstants.getLoadingScreen().setLoadingText("Initializing UI...");
						DoaUtils.sleepFor(2500L);
						UIConstants.getLoadingScreen().setLoadingBarProgress(0.70f);
						DoaScene gameScene = Scenes.getGameScene();
						UIConstants.getLoadingScreen().setLoadingBarProgress(0.85f);
						gameScene.clear();
						DoaUtils.sleepFor(2500L);
						UIConstants.getLoadingScreen().setLoadingBarProgress(0.95f);
						GameInstance.instantiateGameWithUI(instance);
						UIConstants.getLoadingScreen().setLoadingBarProgress(1.0f);
						UIConstants.getLoadingScreen().setLoadingText("Get Ready!!");
						DoaUtils.sleepFor(2000L);
						Scenes.loadGameScene();
						UIConstants.getLoadingScreen().setVisible(false);
						UIConstants.getEmbroidments().setVisible(true);
					}).start();
				}).build();
			loadButton.setPosition(LOAD_BUTTON_LOCATIONS[i]);
			loadButton.setVisible(false);
			loadButtons.add(loadButton);
			
			addElement(loadButton);
		}
		
		Translator.getInstance().registerObserver(this);
		
		addComponent(new Renderer());
	}

	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);

		if (isVisible) {
			metas = GameInstance.readMetadataFromDisk();
		} else {
			metas = Quintet.with(null, null, null, null, null);
		}
		for(int i = 0; i < metas.getSize(); i++) {
			Metadata m = (Metadata) metas.getValue(i);
			loadButtons.get(i).setVisible(m != null);
		}
	}
	
	private final class Renderer extends DoaRenderer {
		
		private transient BufferedImage saveScroll;	
		private transient BufferedImage saveMapContainer;
		
		private String emptyString = "???";
		private Font emptyStringFont;
		private DoaVector emptyStringPos;

		private DoaVector mapNameOffset = new DoaVector(350, 90);
		private DoaVector mapNameSize = new DoaVector(165, 50);
		
		private Font dateStringFont;
		private DoaVector dateOffset = new DoaVector(350, 150);
		private DoaVector dateSize = new DoaVector(165, 30);
		
		private Font timeStringFont;
		private DoaVector timeOffset = new DoaVector(350, 190);
		private DoaVector timeSize = new DoaVector(165, 30);
		
		private Font versionStringFont;
		private DoaVector versionOffset = new DoaVector(350, 230);
		private DoaVector versionSize = new DoaVector(165, 40);
		
		private String dateFormat = "dd MMM yyyy";
		private String timeFormat = "HH:mm:ss";
		private SimpleDateFormat dateFormatter;
		private SimpleDateFormat timeFormatter;
		
		private Renderer() {
			saveScroll = UIConstants.getSaveScrollSprite();
			saveMapContainer = UIConstants.getSaveMapContainerSprite();
			refreshLocale(Translator.getInstance().getCurrentLanguage().getLocale());
		}
		
		public void refreshLocale(Locale l) {
			dateFormatter = new SimpleDateFormat(dateFormat, l);
			timeFormatter = new SimpleDateFormat(timeFormat, l);
		}
		
		@Override
		public void render() {
			if (!isVisible()) { return; }
			if (emptyStringFont == null) {
				int[] size = DoaGraphicsFunctions.warp(saveMapContainer.getWidth() * 0.70f, saveMapContainer.getHeight() * 0.70f);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				emptyStringFont = UIUtils.adjustFontToFitInArea(emptyString, contentSize);
				DoaVector area = UIUtils.textArea(emptyStringFont, emptyString);
				emptyStringPos = new DoaVector(
					saveMapContainer.getWidth() / 2f - area.x / 2f,
					saveMapContainer.getHeight() / 2f + area.y / 4f
				);
			}
			if (dateStringFont == null) {
				int[] size = DoaGraphicsFunctions.warp(dateSize.x, dateSize.y);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				dateStringFont = UIUtils.adjustFontToFitInArea(dateFormat, contentSize);
			}
			if (timeStringFont == null) {
				int[] size = DoaGraphicsFunctions.warp(timeSize.x, timeSize.y);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				timeStringFont = UIUtils.adjustFontToFitInArea(timeFormat, contentSize);
			}
			if (versionStringFont == null) {
				int[] size = DoaGraphicsFunctions.warp(versionSize.x, versionSize.y);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				versionStringFont = UIUtils.adjustFontToFitInArea(Globals.GAME_VERSION, contentSize);
			}
			for(int i = 0; i < SAVE_SCROLL_LOCATIONS.length; i++) {
				DoaVector pos = SAVE_SCROLL_LOCATIONS[i];
				DoaGraphicsFunctions.drawImage(saveScroll, pos.x, pos.y, saveScroll.getWidth(), saveScroll.getHeight());
			}
			
			for(int i = 0; i < SAVE_MAP_CONTAINER_LOCATIONS.length; i++) {
				DoaVector pos = SAVE_MAP_CONTAINER_LOCATIONS[i];
				DoaGraphicsFunctions.drawImage(saveMapContainer, pos.x, pos.y, saveMapContainer.getWidth(), saveMapContainer.getHeight());
			}
			
			for (int i = 0; i < metas.getSize(); i++) {
				Metadata m = (Metadata) metas.getValue(i);

				DoaVector scrollPos = SAVE_SCROLL_LOCATIONS[i];
				DoaVector imgPos = SAVE_MAP_CONTAINER_LOCATIONS[i];
				if (m == null) { 
					DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
					DoaGraphicsFunctions.setFont(emptyStringFont);
					DoaGraphicsFunctions.drawString(
						emptyString,
						imgPos.x + emptyStringPos.x,
						imgPos.y + emptyStringPos.y
					);
					continue;
				}
				
				{ // snapshot
					DoaGraphicsFunctions.drawImage(
						m.getSnapshotImage(),
						imgPos.x + 2, 
						imgPos.y - 2, 
						saveMapContainer.getWidth() - 4,
						saveMapContainer.getHeight() - 1
					);
				}
				
				{ // map name
					DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
					String mapName = m.getMapName().toUpperCase(Translator.getInstance().getCurrentLanguage().getLocale());
					int[] size = DoaGraphicsFunctions.warp(mapNameSize.x, mapNameSize.y);
					Font font = UIUtils.adjustFontToFitInArea(mapName, new DoaVector(size[0], size[1]));
					DoaGraphicsFunctions.setFont(font);
					DoaVector area = UIUtils.textArea(font, mapName);
					DoaGraphicsFunctions.drawString(
						mapName,
						scrollPos.x + mapNameOffset.x + mapNameSize.x / 2f - area.x / 2f,
						scrollPos.y + mapNameOffset.y + mapNameSize.y / 2f + area.y / 4f
					);
				}
				
				
				{ // date
					DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
					DoaGraphicsFunctions.setFont(dateStringFont);
					String date = dateFormatter.format(m.getTimestamp());
					DoaVector area = UIUtils.textArea(dateStringFont, date);
					DoaGraphicsFunctions.drawString(
						date,
						scrollPos.x + dateOffset.x + dateSize.x / 2f - area.x / 2f,
						scrollPos.y + dateOffset.y + dateSize.y / 2f + area.y / 4f
					);
				}
				
				{ // time
					DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
					DoaGraphicsFunctions.setFont(timeStringFont);
					String time = timeFormatter.format(m.getTimestamp());
					DoaVector area = UIUtils.textArea(dateStringFont, time);
					DoaGraphicsFunctions.drawString(
						time,
						scrollPos.x + timeOffset.x + timeSize.x / 2f - area.x / 2f, 
						scrollPos.y + timeOffset.y + timeSize.y / 2f + area.y / 4f 
					);
				}
				
				{ // version
					DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
					DoaGraphicsFunctions.setFont(versionStringFont);
					String version = m.getVersion();
					DoaVector area = UIUtils.textArea(versionStringFont, version);
					DoaGraphicsFunctions.drawString(
						version,
						scrollPos.x + versionOffset.x + versionSize.x / 2f - area.x / 2f, 
						scrollPos.y + versionOffset.y + versionSize.y / 2f + area.y / 4f 
					);
				}
			}
		}
		
		@Override
		public void debugRender() {
			for (int i = 0; i < metas.getSize(); i++) {
				DoaVector scrollPos = SAVE_SCROLL_LOCATIONS[i];
				DoaGraphicsFunctions.setColor(java.awt.Color.RED);
				DoaGraphicsFunctions.drawRect(scrollPos.x + mapNameOffset.x, scrollPos.y + mapNameOffset.y, mapNameSize.x, mapNameSize.y);
				DoaGraphicsFunctions.drawRect(scrollPos.x + dateOffset.x, scrollPos.y +dateOffset.y, dateSize.x, dateSize.y);
				DoaGraphicsFunctions.drawRect(scrollPos.x + timeOffset.x, scrollPos.y + timeOffset.y, timeSize.x, timeSize.y);
				DoaGraphicsFunctions.drawRect(scrollPos.x + versionOffset.x, scrollPos.y +versionOffset.y, versionSize.x, versionSize.y);
			}
		}
	}
	
	@Override
	public void onNotify(Observable b) {
		getComponentByType(Renderer.class).ifPresent((r) -> r.refreshLocale(Translator.getInstance().getCurrentLanguage().getLocale()));
	}
}