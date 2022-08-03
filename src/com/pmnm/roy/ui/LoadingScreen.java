package com.pmnm.roy.ui;

import java.awt.Font;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.RoyMenu;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;

@SuppressWarnings("serial")
public final class LoadingScreen extends RoyMenu implements Observer {

	public LoadingScreen() {
		Translator.getInstance().registerObserver(this);
		addComponent(new Renderer());
	}
	
	private final class Renderer extends DoaRenderer {

		private Font font;
		private String text;
		private String loadingStringKey = "LOADING";
		private int textWidth;
		private int textHeight;
		
		public Renderer() {
			text = Translator.getInstance().getTranslatedString(loadingStringKey);
		}
		
		@Override
		public void render() {
			if (font == null) {
				text = "LOADING"; // TODO REMOVE THIS WHEN "LOADING" is added to external strings
				int[] size = DoaGraphicsFunctions.warp(1920 * .70f, 1080 * .70f);
				font = UIUtils.adjustFontToFitInArea(text, new DoaVector(size[0], size[1]));
				textWidth = UIUtils.textWidth(font, text);
				textHeight = UIUtils.textHeight(font);
			}
			DoaGraphicsFunctions.setFont(font);
			
			float x = (1920 - textWidth) / 2f;
			float y = 1080 / 2f + textHeight / 4f;

			/* outline */
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor().brighter());
			DoaGraphicsFunctions.drawString(text, x - 1, y);
			DoaGraphicsFunctions.drawString(text, x + 1, y);
			DoaGraphicsFunctions.drawString(text, x, y - 1);
			DoaGraphicsFunctions.drawString(text, x, y + 1);
			DoaGraphicsFunctions.drawString(text, x - 2, y);
			DoaGraphicsFunctions.drawString(text, x + 2, y);
			DoaGraphicsFunctions.drawString(text, x, y - 2);
			DoaGraphicsFunctions.drawString(text, x, y + 2);	
			
			/* text itself */
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.drawString(text, x, y);
		}
	}

	@Override
	public void onNotify(Observable b) {
		getComponentByType(Renderer.class).ifPresent(r -> {
			r.text = Translator.getInstance().getTranslatedString(r.loadingStringKey);
			r.font = null;
		});
	}
}
