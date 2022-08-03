package com.pmnm.roy.ui.menu.extensions;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.ZOrders;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.IRoyAction;
import com.pmnm.roy.IRoyContainer;
import com.pmnm.roy.IRoyElement;
import com.pmnm.roy.IRoyInteractableElement;
import com.pmnm.roy.RoyMiniButton;
import com.pmnm.roy.RoyTextField;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@SuppressWarnings("serial")
public final class InputPopup extends DoaObject implements IRoyContainer, IRoyInteractableElement, Observer {

	@Getter
	private boolean isVisible;
	
	@Setter
	private transient IRoyAction action = null;

	private List<IRoyElement> elements;
	
	private String hostIDText = "Host ID:";
	private String nameText = "Name:";

	RoyTextField nameTextField = new RoyTextField(450, 50);
	RoyMiniButton yesButton;

	public InputPopup() {
		transform.position = new DoaVector(600, 420);
		
		elements = new ArrayList<>();
		
		nameTextField.setPosition(new DoaVector(800, 470));
		nameTextField.setPlaceholder("max. 10 characters");
		nameTextField.setMaxCharacters(10);
		nameTextField.registerObserver(this);
		addElement(nameTextField);
		
		RoyMiniButton noButton = RoyMiniButton.builder()
			.textKey("BACK")
			.action((source) -> {
				setVisible(false);
			})
			.build();
		noButton.setPosition(new DoaVector(650, 550));
		addElement(noButton);
		
		yesButton = RoyMiniButton.builder()
			.textKey("YES")
			.action((source) -> {
				setVisible(false);
				if (action != null) {
					action.execute(this);
				}
			})
			.build();
		yesButton.setPosition(new DoaVector(1070, 550));
		yesButton.setVisible(false);
		addElement(yesButton);
		
		setzOrder(ZOrders.POPUP_Z);
		addComponent(new Renderer());
	}
	
	public class Renderer extends DoaRenderer {
		
		private Font font;
		private int stringWidth;
		private DoaVector textDimensions = new DoaVector(576, 40);
		
		@Override
		public void render() {
			if (!isVisible) { return; }
			if (font == null) {
				font = UIConstants.getFont().deriveFont(
					Font.BOLD,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), textDimensions, hostIDText), 0)[0]
				);
				
				FontMetrics fm = DoaGraphicsFunctions.getFontMetrics(font);
				stringWidth = fm.stringWidth(hostIDText);
				int[] strSize = DoaGraphicsFunctions.unwarp(stringWidth, 0);
				stringWidth = strSize[0];
			}
			
			DoaGraphicsFunctions.drawImage(UIConstants.getExitPopupBackground(), 0, 0, 712, 240);
			
			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			
			DoaGraphicsFunctions.drawString(nameText, 50, 85);
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
	public void setVisible(boolean value) {
		isVisible = value;
		for (IRoyElement e : elements) {
			e.setVisible(value);
		}
		yesButton.setVisible(false);
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	@Override
	public Rectangle getContentArea() { return null; }

	@Override
	public Iterable<IRoyElement> getElements() { return elements; }

	@Override
	public void addElement(@NonNull IRoyElement element) { elements.add(element); }

	@Override
	public boolean removeElement(@NonNull IRoyElement element) { return elements.remove(element); }
	
	@Override
	public void onNotify(Observable b) {
		if (b instanceof RoyTextField) {
			int textLength = ((RoyTextField) b).getText().length();
			yesButton.setVisible(textLength != 0);
		}
	}
}