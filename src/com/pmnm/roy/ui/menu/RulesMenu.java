package com.pmnm.roy.ui.menu;

import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.ZOrders;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaKeyboard;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;

@SuppressWarnings("serial")
public class RulesMenu extends RoyMenu {
	
	private int index = 0;
	private transient BufferedImage[] pages;
	private RoyButton backButton; 
	
	public RulesMenu() {
		pages = UIConstants.getRulesImages();
		
		backButton = RoyButton.builder()
			.textKey("BACK")
			.action(source -> {
				index = 0;
				setVisible(false);
				UIConstants.getBackground().setVisible(true);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(new DoaVector(30, 975));
		addElement(backButton);

		setzOrder(ZOrders.RULES_Z);
		addComponent(new Script());
		addComponent(new Renderer());
	}

	public class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible()) { return; }

			if (DoaKeyboard.ONE || DoaKeyboard.NUM_1) {
				index = 0;
			}
			if (DoaKeyboard.TWO || DoaKeyboard.NUM_2) {
				index = 1;
			}
			if (DoaKeyboard.THREE || DoaKeyboard.NUM_3) {
				index = 2;
			}
			if (DoaKeyboard.FOUR || DoaKeyboard.NUM_4) {
				index = 3;
			}
			if (DoaKeyboard.FIVE || DoaKeyboard.NUM_5) {
				index = 4;
			}
			if (DoaKeyboard.SIX || DoaKeyboard.NUM_6) {
				index = 5;
			}
		}
	}

	public class Renderer extends DoaRenderer {
		
		@Override
		public void render() {
			if (!isVisible()) { return; }
			
			DoaGraphicsFunctions.drawImage(pages[index], 0, 0, 1920, 1080);
		}
	}

}