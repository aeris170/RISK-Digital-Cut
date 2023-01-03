package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.RoyMenu;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.NonNull;
import pmnm.risk.game.GameConfig;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.map.MapConfig;

@SuppressWarnings("serial")
public final class LoadingScreen extends RoyMenu implements Observer {

	private BufferedImage selectedMapPreview;
	private String selectedMapName;
	
	private List<String> names = new ArrayList<String>();
	private List<Color> colors = new ArrayList<Color>();
	private List<BufferedImage> pawns = new ArrayList<BufferedImage>();
	
	public LoadingScreen() {
		Translator.getInstance().registerObserver(this);
		addComponent(new Renderer());
		
		getComponentByType(Renderer.class).ifPresent(renderer -> renderer.font = null);
	}
	
	public void setGameConfig(@NonNull final GameConfig config) {
		names.clear();
		colors.clear();
		pawns.clear();
		for (Player.Data data : config.getData()) {
			names.add(data.getName());
			colors.add(data.getColor());
			pawns.add(data.getPawn());
		}
		
		MapConfig mapConfig = MapConfig.getConfigs().get(config.getMapIndex());
		selectedMapName = mapConfig.getName().replace("_", " ").toUpperCase(Locale.ENGLISH); /* map names have _ instead of spaces */
		selectedMapPreview = mapConfig.getBackgroundImagePreview();
	}
	
	private final class Renderer extends DoaRenderer {

		private Font font;
		private Font loadingFont;
		private int stringWidth;
		private DoaVector textDimensions;
		private DoaVector textPosition;
		private String text;
		private String loadingStringKey = "LOADING";
		private int mapTextWidth;
		private int mapTextHeight;
		private int loadingTextWidth;
		private int loadingTextHeight;

		private transient BufferedImage mapChooserBg;
		private transient BufferedImage mapBorder;
		private transient BufferedImage mainScroll;
		private transient BufferedImage playerNameBg;
		private transient BufferedImage colorBg;
		private transient BufferedImage pawnBg;
		
		public Renderer() {
			mainScroll = DoaSprites.getSprite("MainScroll");
			text = Translator.getInstance().getTranslatedString(loadingStringKey);
			mapChooserBg = DoaSprites.getSprite("MapChooserBackground");
			mapBorder = DoaSprites.getSprite("MapBorder");
			playerNameBg = UIConstants.getPlayerTypeBorderSprite();
			colorBg = UIConstants.getColorBorderSprite();
			pawnBg = UIConstants.getColorBorderSprite();
		}
		
		@Override
		public void render() {
			if (font == null) {
				text = "LOADING"; // TODO REMOVE THIS WHEN "LOADING" is added to external strings
				textDimensions = new DoaVector(300f, 50f);
					
				font = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), textDimensions, "GAME OF THRONES"), 0)[0]
				);
				
				loadingFont = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), textDimensions, text), 0)[0]
				);
				
				mapTextWidth = UIUtils.textWidth(font, selectedMapName);
				mapTextHeight = UIUtils.textHeight(font);

				loadingTextWidth = UIUtils.textWidth(font, text);
				loadingTextHeight = UIUtils.textHeight(font);
				
				textPosition = new DoaVector(45 + (mapBorder.getWidth() - mapTextWidth) / 2, 325);
			}
			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawImage(mapChooserBg, 153, 259, mapChooserBg.getWidth(), mapChooserBg.getHeight());
			
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.drawString(
				selectedMapName,
				textPosition.x + (textDimensions.x - stringWidth) / 2f,
				textPosition.y
			);
		
			DoaGraphicsFunctions.drawImage(
				selectedMapPreview,
				200,
				360,
				mapBorder.getWidth() - 5f,
				mapBorder.getHeight() - 3f);
			DoaGraphicsFunctions.drawImage(mapBorder, 195, 357);
			
			DoaGraphicsFunctions.drawImage(mainScroll, 600, 170, mainScroll.getWidth(), mainScroll.getHeight());
			
			// names
			for(int i = 0; i < names.size(); i++) {
				DoaGraphicsFunctions.drawImage(playerNameBg, 725, 282 + i * 55, playerNameBg.getWidth(), playerNameBg.getHeight());
				
				DoaGraphicsFunctions.drawString(names.get(i), 735, 314 + i * 55);
			}

			// colors
			for(int i = 0; i < colors.size(); i++) {
				DoaGraphicsFunctions.setColor(colors.get(i));
				DoaGraphicsFunctions.fill(new Rectangle(983, 285 + 55 * i, colorBg.getWidth() - 6, colorBg.getHeight() - 6));
				
				DoaGraphicsFunctions.drawImage(colorBg, 980, 282 + 55 * i, colorBg.getWidth(), colorBg.getHeight());
			}

			// pawns
			for(int i = 0; i < pawns.size(); i++) {
				DoaGraphicsFunctions.drawImage(pawnBg, 1092, 282 + 55 * i, pawnBg.getWidth(), pawnBg.getHeight());
				
				DoaGraphicsFunctions.drawImage(pawns.get(i), 1122, 286 + 55 * i, pawnBg.getHeight() - 10, pawnBg.getHeight() - 10);
			}
			
			float x = (1920 - loadingTextWidth) / 2f;
			float y = 950 - loadingTextHeight / 4f;

			DoaGraphicsFunctions.setFont(loadingFont);
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
