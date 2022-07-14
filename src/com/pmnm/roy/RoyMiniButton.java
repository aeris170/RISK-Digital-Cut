package com.pmnm.roy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Locale;

import com.google.errorprone.annotations.ForOverride;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaComponent;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import doa.engine.utils.DoaUtils;
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
	
	private DoaVector contentSize;
	private DoaVector textPosition;

	@Builder
	RoyMiniButton(@NonNull String text, @NonNull IRoyAction action) {
		this.text = Translator.getInstance().getTranslatedString(text).toUpperCase(Locale.getDefault());
		this.action = action;
		this.image = UIConstants.getMiniButtonIdleSprite();
		this.hoverImage = UIConstants.getMiniButtonHoverSprite();
		this.pressImage = UIConstants.getMiniButtonPressedSprite();

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
				contentSize = new DoaVector(image.getWidth() * 0.70f, image.getHeight() * 0.70f);
				font = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), contentSize, text), 0)[0]
				);
							
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
	}
}
