package com.pmnm.roy.ui.menu;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.main.Main;
import com.pmnm.roy.IRoyContainer;
import com.pmnm.roy.IRoyElement;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.ZOrders;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaKeyboard;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("serial")
public class RulesMenu extends DoaObject implements IRoyContainer {
	
	@Getter
	private boolean isVisible;
	private transient List<IRoyElement> elements = new ArrayList<>();

	private int index = 0;
	private transient BufferedImage[] pages;
	private RoyButton backButton; 
	
	public RulesMenu() {
		pages = UIConstants.getRulesImages();
		
		backButton = RoyButton.builder()
			.text("BACK")
			.action(() -> {
				index = 0;
				setVisible(false);
				UIConstants.getBackground().setVisible(true);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(new DoaVector(30, 975));
		elements.add(backButton);

		setzOrder(ZOrders.RULES_Z);
		addComponent(new Script());
		addComponent(new Renderer());
	}

	public class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible) { return; }
			
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
			if (!isVisible) { return; }
			
			DoaGraphicsFunctions.drawImage(pages[index], 0, 0, 1920, 1080);
		}
	}
	
	@Override
	public void onAddToScene(DoaScene scene) {
		super.onAddToScene(scene);
		for (IRoyElement e : elements) {
			if (e instanceof DoaObject) {
				scene.add((DoaObject) e);
			}
		}
	}
	
	@Override
	public void onRemoveFromScene(DoaScene scene) {
		super.onRemoveFromScene(scene);
		for (IRoyElement e : elements) {
			if (e instanceof DoaObject) {
				scene.remove((DoaObject) e);
			}
		}
	}
	
	@Override
	public void setzOrder(int zOrder) {
		super.setzOrder(zOrder);
		for (IRoyElement e : elements) {
			if(e instanceof DoaObject) {
				((DoaObject)e).setzOrder(zOrder + 1);
			}
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		elements.forEach(e -> e.setVisible(isVisible));
	}

	@Override
	public void setPosition(DoaVector position) { throw new UnsupportedOperationException("not implemented"); }

	@Override
	public Rectangle getContentArea() { return new Rectangle(0, 0, 1920, 1080); }

	@Override
	public Iterable<IRoyElement> getElements() { return elements; }

	@Override
	public void addElement(@NonNull IRoyElement element) { elements.add(element); }

	@Override
	public boolean removeElement(@NonNull IRoyElement element) { return elements.remove(element); }


}