package com.pmnm.roy;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.prefs.Preferences;

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
			if (!isVisible) return;
			
			if (group.getSelected() == RoyLanguageButton.this) {
				currentLensImage = lensSelected;
				currentComposite = compositeSelected;
			} else if (getContentArea().contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
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
			if (!isVisible) return;
			
			DoaGraphicsFunctions.drawImage(flagImage, 0, 0, 140, 140);
			DoaGraphicsFunctions.pushComposite();
			DoaGraphicsFunctions.setComposite(currentComposite);
			DoaGraphicsFunctions.drawImage(currentLensImage, 0, 0, 140, 140);
			DoaGraphicsFunctions.popComposite();
		}
	}
		
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	@Override
	public Rectangle getContentArea() {
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(flagImage.getWidth(), flagImage.getHeight());
		return new Rectangle(
			pos[0],
			pos[1],
			size[0],
			size[1]
		);
	}
}
