package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@SuppressWarnings("serial")
public final class RoyMiniButton extends DoaObject implements IRoyInteractableElement  {

	@Getter
	@Setter
	private boolean isVisible = true;
	
	@NonNull
	private String text = "";
	
	@NonNull
	private transient BufferedImage image;

	@NonNull
	private transient BufferedImage hoverImage;
	
	@NonNull
	private transient BufferedImage pressImage;

	private transient Font font = null;
	
	@NonNull
	private Color textColor = Color.BLACK;
	
	@NonNull
	private Color hoverTextColor = Color.BLACK;

	@Setter
	private transient IRoyAction action = null;
	
	private transient BufferedImage currentImage;

	@Builder
	RoyMiniButton(boolean isVisible, @NonNull String text, @NonNull IRoyAction action) {
		this.isVisible = isVisible;
		this.text = Translator.getInstance().getTranslatedString(text);
		this.action = action;
		this.image = UIConstants.getMiniButtonIdleSprite();
		this.hoverImage = UIConstants.getMiniButtonHoverSprite();
		this.pressImage = UIConstants.getMiniButtonPressedSprite();
		this.font = UIConstants.getFont().deriveFont(
			Font.PLAIN,
			Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), new DoaVector(image.getWidth(), image.getHeight()), this.text)
		);
		this.textColor = UIConstants.getTextColor();
		this.hoverTextColor = UIConstants.getHoverTextColor();
		
		currentImage = image;

		addComponent(new Script());
		addComponent(new Renderer());
	}

	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	@Override
	public Rectangle getContentArea() {
		return new Rectangle(
			(int) transform.position.x,
			(int) transform.position.y,
			image.getWidth(),
			image.getHeight()
		);
	}
	
	private final class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible) return;
			
			Rectangle area = getContentArea();
			if (area.contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
				if(currentImage == pressImage && DoaMouse.MB1_RELEASE) {
					action.execute();
				} else if(DoaMouse.MB1 || DoaMouse.MB1_HOLD) {
					currentImage = pressImage;
				} else {
					currentImage = hoverImage;
				}
			} else {
				currentImage = image;
			}
		}
	}
	
	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible) return;
			DoaGraphicsFunctions.pushAll();
			
			DoaGraphicsFunctions.drawImage(currentImage, 0, 0, image.getWidth(), image.getHeight());
			
			DoaGraphicsFunctions.setColor(textColor);
			if (currentImage == hoverImage) {
				DoaGraphicsFunctions.setColor(hoverTextColor);
			}
			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawString(text, 20, image.getHeight() - 17);
			
			DoaGraphicsFunctions.popAll();
		}		
	}
}
