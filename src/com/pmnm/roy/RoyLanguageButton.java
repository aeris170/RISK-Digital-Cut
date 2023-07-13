package com.pmnm.roy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.globals.localization.Translator.Language;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class RoyLanguageButton extends DoaObject implements IRoyElement {

	@Getter
	@Setter
	private boolean isVisible;

	private transient BufferedImage lens;
	private transient BufferedImage lensHover;
	private transient BufferedImage lensSelected;
	private transient BufferedImage currentLensImage;

	private transient BufferedImage flagImage;

	private transient Composite composite;
	private transient Composite compositeHover;
	private transient Composite compositeSelected;
	private transient Composite currentComposite;

	private RoyLanguageButtonGroup group;
	private Language language;

	private int width = 140;
	private int height = 140;

	RoyLanguageButton(RoyLanguageButtonGroup group, Language language) {
		this.group = group;
		this.language = language;
		lens = UIConstants.getLensImage();
		lensHover = UIConstants.getLensHoverImage();
		lensSelected = UIConstants.getLensSelectedImage();
		currentLensImage = lens;

		flagImage = UIConstants.getLanguageImages().get(language);

		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
		compositeHover = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
		compositeSelected = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
		currentComposite = composite;

		addComponent(new Script());
		addComponent(new Renderer());
	}

	private class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible) { return; }

			int mouseX = (int) DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = (int) DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			if (group.getSelected() == RoyLanguageButton.this) {
				currentLensImage = lensSelected;
				currentComposite = compositeSelected;
			} else if (getContentArea().contains(new Point(mouseX, mouseY))) {
				currentLensImage = lensHover;
				currentComposite = compositeHover;
				if (DoaMouse.MB1_RELEASE) {
					group.setSelected(RoyLanguageButton.this);
					Translator.getInstance().setCurrentLanguage(language);
				}
			} else {
				currentLensImage = lens;
				currentComposite = composite;
			}
		}
	}

	private class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible) { return; }

			DoaGraphicsFunctions.drawImage(flagImage, 0, 0, width, height);
			DoaGraphicsFunctions.pushComposite();
			DoaGraphicsFunctions.setComposite(currentComposite);
			DoaGraphicsFunctions.drawImage(currentLensImage, 0, 0, width, height);
			DoaGraphicsFunctions.popComposite();
		}

		@Override
		public void debugRender() {
			if (!isVisible) { return; }

			switch (language) {
				case EN:
				case FR:
				case RU:
					DoaGraphicsFunctions.setColor(Color.BLUE); break;
				case DE:
				case TR:
					DoaGraphicsFunctions.setColor(Color.RED); break;
				case ES:
					DoaGraphicsFunctions.setColor(Color.ORANGE); break;
				case IT:
					DoaGraphicsFunctions.setColor(Color.GREEN); break;
				default:
					DoaGraphicsFunctions.setColor(Color.BLACK); break;
			}
			DoaGraphicsFunctions.drawRect(0, 0, width, height);
		}
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
			width,
			height
		);
	}
}
