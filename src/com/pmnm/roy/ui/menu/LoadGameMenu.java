package com.pmnm.roy.ui.menu;

import java.awt.image.BufferedImage;
import java.io.File;

import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;

@SuppressWarnings("serial")
public class LoadGameMenu extends RoyMenu {
	
	private static final DoaVector BACK_LOCATION = new DoaVector(1377f, 803f);

	private File[] f;

	public LoadGameMenu() {
		f = new File(System.getProperty("user.home") + "\\Documents\\My Games\\RiskDigitalCut\\Saves\\").listFiles();
		
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
			
			DoaGraphicsFunctions.drawImage(saveScroll, 15, 148);
			DoaGraphicsFunctions.drawImage(saveMapContainer, 127, 242);
	
			DoaGraphicsFunctions.drawImage(saveScroll, 655, 148);
			DoaGraphicsFunctions.drawImage(saveMapContainer, 767, 242);
	
			DoaGraphicsFunctions.drawImage(saveScroll, 1295, 148);
			DoaGraphicsFunctions.drawImage(saveMapContainer, 1407, 242);
	
			DoaGraphicsFunctions.drawImage(saveScroll, 15, 540);
			DoaGraphicsFunctions.drawImage(saveMapContainer, 127, 634);
	
			DoaGraphicsFunctions.drawImage(saveScroll, 655, 540);
			DoaGraphicsFunctions.drawImage(saveMapContainer, 767, 634);
		}
	}
}