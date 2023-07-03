package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;
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
public final class RoyMiniButton extends DoaObject implements IRoyInteractableElement, Observer {

	@Getter
	@Setter
	private boolean isVisible = true;

	@Getter
	@Setter
	private boolean isEnabled = true;
	
	private String textKey;
	@NonNull
	private String text = "";
	
	@NonNull
	private transient BufferedImage image;

	@NonNull
	private transient BufferedImage hoverImage;
	
	@NonNull
	private transient BufferedImage pressImage;
	
	@NonNull
	private transient BufferedImage disabledImage;

	private transient Font font = null;

	@Getter
	@Setter
	@NonNull
	private Color textColor = UIConstants.getTextColor();

	@Getter
	@Setter
	@NonNull
	private Color hoverTextColor = UIConstants.getHoverTextColor();

	@Setter
	private transient IRoyAction action = null;
	
	private transient BufferedImage currentImage;

	private int width = 0;
	private int height = 0;
	
	private DoaVector contentSize;
	private DoaVector textPosition;

	@Builder
	RoyMiniButton(@NonNull String textKey, @NonNull IRoyAction action) {
		this.textKey = textKey;
		this.text = Translator.getInstance().getTranslatedString(textKey);
		this.action = action;
		this.image = UIConstants.getMiniButtonIdleSprite();
		this.hoverImage = UIConstants.getMiniButtonHoverSprite();
		this.pressImage = UIConstants.getMiniButtonPressedSprite();
		this.disabledImage = image;
		
		currentImage = image;

		width = image.getWidth();
		height = image.getHeight();

		Renderer r = new Renderer();
		addComponent(new Script());
		addComponent(r);
		
		Translator.getInstance().registerObserver(this);
		
		r.enableDebugRender = true;
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
	
	@Override
	public void onNotify(Observable b) {
		this.text = Translator.getInstance().getTranslatedString(textKey);
		font = null; /* make font null so on next frame it will be calculated again with appropriate size */
	}
	
	private final class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible()) { return; }
			
			if (!RoyMiniButton.this.isEnabled()) {
				currentImage = disabledImage;
				return;
			}
			Rectangle area = getContentArea();
			int mouseX = DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			if (area.contains(new Point(mouseX, mouseY))) {
				if(currentImage == pressImage && DoaMouse.MB1_RELEASE) {
					action.execute(RoyMiniButton.this);
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
				contentSize = new DoaVector(image.getWidth() * 0.70f, image.getHeight() * 0.70f);
				font = UIUtils.adjustFontToFitInArea(text, contentSize);
							
				FontMetrics fm = DoaGraphicsFunctions.getFontMetrics(font);
				int stringWidth = fm.stringWidth(text);
				int stringHeight = fm.getHeight();
				int[] strSize = DoaGraphicsFunctions.unwarp(stringWidth, stringHeight);
				stringWidth = strSize[0];
				stringHeight = strSize[1];
				textPosition = new DoaVector(image.getWidth() / 2f - stringWidth / 2f, image.getHeight() / 2f + stringHeight / 4f);
			}
			
			DoaGraphicsFunctions.pushAll();
			
			DoaGraphicsFunctions.drawImage(currentImage, 0, 0, image.getWidth(), image.getHeight());
			
			DoaGraphicsFunctions.setColor(textColor);
			if (currentImage == hoverImage) {
				DoaGraphicsFunctions.setColor(hoverTextColor);
			}
			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawString(
				text,
				textPosition.x,
				textPosition.y
			);
			
			DoaGraphicsFunctions.popAll();
		}

		@Override
		public void debugRender() {
			if (!isVisible) { return; }
			DoaGraphicsFunctions.setColor(Color.RED);
			DoaGraphicsFunctions.drawRect(0, 0, width, height);
		}
	}
}
