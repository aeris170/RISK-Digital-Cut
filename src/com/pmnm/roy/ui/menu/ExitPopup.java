package com.pmnm.roy.ui.menu;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.IRoyContainer;
import com.pmnm.roy.IRoyElement;
import com.pmnm.roy.RoyMiniButton;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.ZOrders;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("serial")
public final class ExitPopup extends DoaObject implements IRoyContainer {

	@Getter
	private boolean isVisible;

	private ExitFadeToBlack ef;
	private List<IRoyElement> elements;
	
	private String areYouSureText;
	private DoaVector textDimensions = new DoaVector(576, 40);

	public ExitPopup() {
		transform.position = new DoaVector(600, 420);
		
		elements = new ArrayList<>();
		
		RoyMiniButton yesButton = RoyMiniButton.builder()
			.text("YES")
			.action(() -> {
				setVisible(false);
				ef.setVisible(true);
			})
			.build();
		yesButton.setPosition(new DoaVector(666, 560));
		addElement(yesButton);
		
		RoyMiniButton noButton = RoyMiniButton.builder()
			.text("NO")
			.action(() -> setVisible(false))
			.build();
		noButton.setPosition(new DoaVector(1066, 560));
		addElement(noButton);
		
		ef = new ExitFadeToBlack();
		addElement(ef);
		
		setzOrder(ZOrders.EXIT_MENU_Z);

		addComponent(new Renderer());
		setVisible(false);
	}
	
	public class Renderer extends DoaRenderer {
		
		@Override
		public void render() {
			if (!isVisible) { return; }
			
			DoaGraphicsFunctions.drawImage(UIConstants.getExitPopupBackground(), 0, 0, 712, 240);
			
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.BOLD, Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), textDimensions, areYouSureText)));
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			
			DoaGraphicsFunctions.drawString(
				areYouSureText, 
				72 + (textDimensions.x - DoaGraphicsFunctions.getFontMetrics().stringWidth(areYouSureText)) / 2f,
				100
			);
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
		areYouSureText = Translator.getInstance().getTranslatedString("ARE_YOU_SURE_WANT_TO_EXIT").toUpperCase();
		isVisible = value;
		for (IRoyElement e : elements) {
			e.setVisible(value);
		}
		ef.setVisible(false); /* ef should never be affected by this call, otherwise the game will close on its own */
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
}