package com.pmnm.roy;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
public final class RoyImageButton extends DoaObject implements IRoyInteractableElement {
	
	@Getter
	@Setter
	private boolean isVisible;
	
	@NonNull
	private transient BufferedImage image;

	@NonNull
	private transient BufferedImage hoverImage;
	
	@NonNull
	private transient BufferedImage pressImage;
	
	@Setter
	private transient IRoyAction action = null;
	
	private transient BufferedImage currentImage;
	
	@Builder
	RoyImageButton(@NonNull BufferedImage image, @NonNull BufferedImage hoverImage, @NonNull BufferedImage pressImage, @NonNull IRoyAction action) {
		this.image = image;
		this.hoverImage = hoverImage;
		this.pressImage = pressImage;
		this.action = action;
		
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
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(image.getWidth(), image.getHeight());
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
			if (!isVisible) return;
			
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
		}
	}
	
	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible) return;
			DoaGraphicsFunctions.drawImage(currentImage, 0, 0, image.getWidth(), image.getHeight());
		}
	}
}
