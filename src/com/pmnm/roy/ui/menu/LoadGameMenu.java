package com.pmnm.roy.ui.menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.stream.StreamSupport;

import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.RoyMiniButton;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;

@SuppressWarnings("serial")
public class LoadGameMenu extends RoyMenu {
	
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

	private File[] f;

	public LoadGameMenu() {
		f = new File(System.getProperty("user.home") + "\\Documents\\My Games\\RiskDigitalCut\\Saves\\").listFiles();
		if (f == null) f = new File[5];
		
		RoyButton backButton = RoyButton
			.builder()
			.textKey("BACK")
			.action(() -> {
				setVisible(false);
				UIConstants.getPlayOfflineMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(BACK_LOCATION);
		addElement(backButton);
		
		for(int i = 0; i < f.length; i++) {
			final int index = i;
			RoyMiniButton loadButton  = RoyMiniButton.builder()
				.textKey("LOAD")
				.action(() -> {
					setVisible(false);
					// load game
					File loadThisGame = f[index];
				}).build();
			loadButton.setPosition(LOAD_BUTTON_LOCATIONS[i]);
			loadButton.setVisible(false);
			addElement(loadButton);
		}
		
		addComponent(new Renderer());
	}

	
	private final class Renderer extends DoaRenderer {
		
		private transient BufferedImage saveScroll;	
		private transient BufferedImage saveMapContainer;
		
		private Renderer() {
			saveScroll = UIConstants.getSaveScrollSprite();
			saveMapContainer = UIConstants.getSaveMapContainerSprite();
		}
		
		@Override
		public void render() {
			if(!isVisible()) { return; }
			
			for(int i = 0; i < SAVE_SCROLL_LOCATIONS.length; i++) {
				DoaVector pos = SAVE_SCROLL_LOCATIONS[i];
				DoaGraphicsFunctions.drawImage(saveScroll, pos.x, pos.y, saveScroll.getWidth(), saveScroll.getHeight());
			}
			
			for(int i = 0; i < SAVE_MAP_CONTAINER_LOCATIONS.length; i++) {
				DoaVector pos = SAVE_MAP_CONTAINER_LOCATIONS[i];
				DoaGraphicsFunctions.drawImage(saveMapContainer, pos.x, pos.y, saveMapContainer.getWidth(), saveMapContainer.getHeight());
			}
		}
	}
}