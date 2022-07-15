package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Locale;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

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
public final class RoyButton extends DoaObject implements IRoyInteractableElement, Observer {

	@Getter
	@Setter
	private boolean isVisible;
	
	private String textKey;
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
	
	private DoaVector contentSize;

	@Builder
	RoyButton(@NonNull String textKey, @NonNull IRoyAction action) {
		this.textKey = textKey;
		this.text = Translator.getInstance().getTranslatedString(textKey);
		this.action = action;
		this.image = UIConstants.getButtonIdleSprite();
		this.hoverImage = UIConstants.getButtonHoverSprite();
		this.pressImage = UIConstants.getButtonPressedSprite();
		
		this.textColor = UIConstants.getTextColor();
		this.hoverTextColor = UIConstants.getHoverTextColor();
		
		currentImage = image;

		addComponent(new Script());
		addComponent(new Renderer());
		
		Translator.getInstance().registerObserver(this);
	}

	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	@Override
	public Rectangle getContentArea() {
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(image.getWidth(), image.getHeight());
		return new Rectangle(
			pos[0],
			pos[1],
			size[0],
			size[1]
		);
	}
	
	@Override
	public void onNotify(Observable b) {
		this.text = Translator.getInstance().getTranslatedString(textKey);
		font = null; /* make font null so on next frame it will be calculated again with appropriate size */
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
			if (font == null) {
				int[] size = DoaGraphicsFunctions.warp(image.getWidth()* .70f, image.getHeight() * .70f);
				contentSize = new DoaVector(size[0], size[1]);
				font = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), contentSize, text)
				);
			}
			
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
