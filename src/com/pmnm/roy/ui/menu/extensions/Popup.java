package com.pmnm.roy.ui.menu.extensions;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.IRoyElement;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@SuppressWarnings("serial")
public final class Popup extends DoaObject implements IRoyElement, Observer {
	
	@Getter
	@Setter
	private boolean isVisible;
	
	@Getter
	@NonNull
	private String textKey;
	
	private transient BufferedImage backgroundImage;
	
	public Popup(@NonNull String textKey) {
		this.textKey = textKey;
		backgroundImage = UIConstants.getPopupBackground();

		transform.position = new DoaVector(960 - backgroundImage.getWidth() / 2f, 540 - backgroundImage.getHeight() / 2f);
		
		addComponent(new Renderer());
	}
	
	public class Renderer extends DoaRenderer {
		
		private String text;
		private Font font;
		private DoaVector textPos;
		
		@Override
		public void render() {
			if (!isVisible) { return; }
			if (font == null) {
				text = Translator.getInstance().getTranslatedString(textKey) + "...";
				DoaVector textSize = new DoaVector(backgroundImage.getWidth() * 0.70f, backgroundImage.getHeight() * 0.70f);
				font = UIUtils.adjustFontToFitInArea(text, textSize);
				
				DoaVector textOffset = new DoaVector(textSize.x / 4f, textSize.y / 4f);
				DoaVector textArea = UIUtils.textArea(font, text);
				textPos = new DoaVector(
					textOffset.x + textSize.x / 2f - textArea.x / 2f,
					textOffset.y + textSize.y / 2f + textArea.y / 4f
				);
			}
			
			DoaGraphicsFunctions.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.drawString(
				text, 
				textPos.x,
				textPos.y
			);
		}
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	@Override
	public Rectangle getContentArea() { return null; }


	@Override
	public void onNotify(Observable b) {
		getComponentByType(Renderer.class).ifPresent(r -> r.font = null);
	}
}
