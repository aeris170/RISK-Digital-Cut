package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
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
	protected String text;

	@Getter
	@Setter
	@NonNull
	private Color textColor = UIConstants.getTextColor();

	@Getter
	@Setter
	@NonNull
	private Color hoverTextColor = UIConstants.getHoverTextColor();

	@Getter
	@Setter
	protected DoaVector textMargin = new DoaVector(0.60f, 0.60f);

	protected transient BufferedImage currentImage;

	private int width = 0;
	private int height = 0;
	
	@Setter
	private transient Shape interactionArea;

	private Renderer renderer;

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
		
		renderer = new Renderer();
		addComponent(new Script());
		addComponent(renderer);

		renderer.enableDebugRender = true;
	}

	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	public void setText(@NonNull String text) {
		this.text = text;
		renderer.font = null;
	}

	public void setScale(float f) {
		width = (int) (currentImage.getWidth() * f);
		height = (int) (currentImage.getHeight() * f);
	}

	@Override
	public Shape getContentArea() {
		return new Rectangle(
			(int) transform.position.x,
			(int) transform.position.y,
			width,
			height
		);
	}
	
	@Override
	public Shape getInteractionArea() {
		if (interactionArea == null) { return getContentArea(); }
		return interactionArea;
	}

	public final class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible) { return; }
			
			if (!RoyImageButton.this.isEnabled()) {
				currentImage = disabledImage;
				return;
			}

			Shape area = getInteractionArea();
			int mouseX = DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			if (area.contains(new Point(mouseX, mouseY))) {
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
				int[] size = DoaGraphicsFunctions.warp(width * textMargin.x, height * textMargin.y);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				font = UIUtils.adjustFontToFitInArea(text, contentSize);
				textWidth = UIUtils.textWidth(font, text);
				textHeight = UIUtils.textHeight(font);
			}

			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(textColor);
			if (currentImage == hoverImage || currentImage == pressImage) {
				DoaGraphicsFunctions.setColor(hoverTextColor);
			}

			DoaGraphicsFunctions.drawString(
				text,
				(width - textWidth) / 2f,
				height / 2f + textHeight / 4f
			);
		}

		@Override
		public void debugRender() {
			if (!isVisible) { return; }
			DoaGraphicsFunctions.pushTransform();
			DoaGraphicsFunctions.resetTransform();
			DoaGraphicsFunctions.setColor(Color.YELLOW);
			DoaGraphicsFunctions.draw(getContentArea());
			DoaGraphicsFunctions.setColor(Color.RED);
			DoaGraphicsFunctions.draw(getInteractionArea());
			DoaGraphicsFunctions.popTransform();
		}
	}
}
