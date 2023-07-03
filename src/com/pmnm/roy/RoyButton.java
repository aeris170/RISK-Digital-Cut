
package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
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
public final class RoyButton extends DoaObject implements IRoyInteractableElement, Observer {

	@Getter
	@Setter
	private boolean isVisible;
	
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

	@Builder
	RoyButton(@NonNull String textKey, @NonNull IRoyAction action) {
		this.textKey = textKey;
		this.text = Translator.getInstance().getTranslatedString(textKey);
		this.action = action;
		this.image = UIConstants.getButtonIdleSprite();
		this.hoverImage = UIConstants.getButtonHoverSprite();
		this.pressImage = UIConstants.getButtonPressedSprite();
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

			if (!RoyButton.this.isEnabled()) {
				currentImage = disabledImage;
				return;
			}
			
			Rectangle area = getContentArea();
			int mouseX = DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			if (area.contains(new Point(mouseX, mouseY))) {
				if(currentImage == pressImage && DoaMouse.MB1_RELEASE) {
					action.execute(RoyButton.this);
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
		
		private int textHeight;
		
		@Override
		public void render() {
			if (!isVisible) return;
			if (font == null) {
				int[] size = DoaGraphicsFunctions.warp(image.getWidth()* .70f, image.getHeight() * .70f);
				contentSize = new DoaVector(size[0], size[1]);
				font = UIUtils.adjustFontToFitInArea(text, contentSize);
				textHeight = UIUtils.textHeight(font);
			}
			
			DoaGraphicsFunctions.pushAll();
			
			DoaGraphicsFunctions.drawImage(currentImage, 0, 0, image.getWidth(), image.getHeight());
			
			DoaGraphicsFunctions.setColor(textColor);
			if (currentImage == hoverImage) {
				DoaGraphicsFunctions.setColor(hoverTextColor);
			}
			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawString(text, image.getWidth() * 0.05f, image.getHeight() / 2f + textHeight / 4f);
			
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
