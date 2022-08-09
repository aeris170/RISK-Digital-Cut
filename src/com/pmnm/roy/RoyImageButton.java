package com.pmnm.roy;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@SuppressWarnings("serial")
public class RoyImageButton extends DoaObject implements IRoyInteractableElement, IRoyScaleableElement {
	
	@Getter
	@Setter
	private boolean isVisible;

	@Getter
	@Setter
	private boolean isEnabled = true;
	
	@NonNull
	protected transient BufferedImage image;

	@NonNull
	protected transient BufferedImage hoverImage;
	
	@NonNull
	protected transient BufferedImage pressImage;
	
	protected transient BufferedImage disabledImage = null;
	
	@Setter
	protected transient IRoyAction action = null;
	
	@Getter
	@Setter
	protected String text;
	
	@Getter
	@Setter
	protected DoaVector textMargin = new DoaVector(0.60f, 0.60f);
	
	protected transient BufferedImage currentImage;
	
	private int width = 0;
	private int height = 0;
	
	@Builder
	protected RoyImageButton(@NonNull BufferedImage image, @NonNull BufferedImage hoverImage, @NonNull BufferedImage pressImage, BufferedImage disabledImage, @NonNull IRoyAction action) {
		this.image = image;
		this.hoverImage = hoverImage;
		this.pressImage = pressImage;
		this.disabledImage = disabledImage;
		if (this.disabledImage == null) {
			this.disabledImage = image;
		}
		this.action = action;
		
		currentImage = image;
		
		width = image.getWidth();
		height = image.getHeight();
		
		addComponent(new Script());
		addComponent(new Renderer());
	}

	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	public void setScale(float f) {
		width = (int) (currentImage.getWidth() * f);
		height = (int) (currentImage.getHeight() * f);
	}

	@Override
	public Rectangle getContentArea() {
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(width, height);
		return new Rectangle(
			pos[0],
			pos[1],
			size[0],
			size[1]
		);
	}
	
	private final class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible) { return; }
			
			if (!RoyImageButton.this.isEnabled()) {
				currentImage = disabledImage;
				return;
			}
			
			Rectangle area = getContentArea();
			if (area.contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
				if(currentImage == pressImage && DoaMouse.MB1_RELEASE) {
					action.execute(RoyImageButton.this);
				} else if(DoaMouse.MB1 || DoaMouse.MB1_HOLD) {
					currentImage = pressImage;
				} else {
					currentImage = hoverImage;
				}
			} else {
				currentImage = image;
			}

			if (!RoyImageButton.this.isEnabled()) { currentImage = disabledImage; }
		}
	}
	
	private final class Renderer extends DoaRenderer {

		private Font font;
		private int textWidth;
		private int textHeight;
		
		@Override
		public void render() {
			if (!isVisible) return;
			
			DoaGraphicsFunctions.drawImage(currentImage, 0, 0, width, height);
			
			if (text == null || text.isEmpty()) { return; }
			if (font == null) {
				int[] size = DoaGraphicsFunctions.warp(image.getWidth() * textMargin.x, image.getHeight() * textMargin.y);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				font = UIUtils.adjustFontToFitInArea(text, contentSize);
				textWidth = UIUtils.textWidth(font, text);
				textHeight = UIUtils.textHeight(font);
			}
			
			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			if (currentImage == hoverImage) {
				DoaGraphicsFunctions.setColor(UIConstants.getHoverTextColor());
			}
			
			DoaGraphicsFunctions.drawString(
				text,
				image.getWidth() / 2 - textWidth / 2f,
				image.getHeight() / 2f + textHeight / 4f
			);
			
		}
	}
}
