package com.pmnm.roy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.RoyMenu;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaMath;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.NonNull;
import lombok.Setter;
import pmnm.risk.game.GameConfig;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.map.MapConfig;

@SuppressWarnings("serial")
public final class LoadingScreen extends RoyMenu{

	private BufferedImage selectedMapPreview;
	private String selectedMapName;
	private String hintText = "";
	private String loadingText = "";
	private boolean hintTextChanged = false;
	private boolean loadingTextChanged = false;
	
	@Setter
	private float loadingBarProgress = 0f;
	private float currentBarProgress = 0f;
	
	private List<String> names = new ArrayList<String>();
	private List<Color> colors = new ArrayList<Color>();
	private List<BufferedImage> pawns = new ArrayList<BufferedImage>();
	
	public LoadingScreen() {
		addComponent(new Script());
		addComponent(new Renderer());
		getComponentByType(Renderer.class).ifPresent(renderer -> renderer.mapNameFont = null);
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
		
		MapConfig mapConfig = config.getMapConfig();
		selectedMapName = mapConfig.getName().replace("_", " ").toUpperCase(Locale.ENGLISH); /* map names have _ instead of spaces */
		selectedMapPreview = mapConfig.getBackgroundImagePreview();
	}
	
	public void setHintText(String hintText) {
		this.hintText = hintText;
		hintTextChanged = true;
	}
	
	public void setLoadingText(String loadingText) {
		this.loadingText = loadingText;
		loadingTextChanged = true;
	}
	
	private final class Script extends DoaScript {
		private String[] hints = new String[] {"hint1", "hint2", "hint3 tehe ;P"};
		private int hintIndex = 0;
		private int timer = 0;
		private int timerMax = 600;
		
		public Script() {
			setHintText(hints[hintIndex]);
		}
		
		@Override
		public void tick() {
			timer++;

			if (currentBarProgress < loadingBarProgress && DoaMath.randomBetween(0, 1) < 0.15f) {
				currentBarProgress = DoaMath.clamp(currentBarProgress + 0.005f, 0, loadingBarProgress);
			}
			
			if(timer >= timerMax) {
				timer = 0;
				
				if(hintIndex >= hints.length - 1) {
					hintIndex = 0;
				} else {
					hintIndex++;
				}

				setHintText(hints[hintIndex]);
			}
		}
	}
	
	private final class Renderer extends DoaRenderer {

		private Font mapNameFont;
		private DoaVector mapNamePosition;
		private DoaVector mapNameDimensions;
		
		private Font hintFont;
		private DoaVector hintPosition;
		private DoaVector hintTextDimensions;
		
		private Font loadingFont;
		private DoaVector loadingPosition;
		private DoaVector loadingTextDimensions;

		private transient BufferedImage mapChooserBg;
		private transient BufferedImage mapBorder;
		private transient BufferedImage mainScroll;
		private transient BufferedImage playerNameBg;
		private transient BufferedImage colorBg;
		private transient BufferedImage pawnBg;
		
		public Renderer() {
			mainScroll = DoaSprites.getSprite("MainScroll");
			mapChooserBg = DoaSprites.getSprite("MapChooserBackground");
			mapBorder = DoaSprites.getSprite("MapBorder");
			playerNameBg = UIConstants.getPlayerTypeBorderSprite();
			colorBg = UIConstants.getColorBorderSprite();
			pawnBg = UIConstants.getColorBorderSprite();
		}
		
		@Override
		public void render() {
			if (mapNameFont == null) {
				mapNameDimensions = new DoaVector(300f, 50f);
				mapNameFont = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), mapNameDimensions, "GAME OF THRONES"), 0)[0]
				);

				mapNameDimensions = new DoaVector(UIUtils.textWidth(mapNameFont, selectedMapName), UIUtils.textHeight(mapNameFont));
				mapNamePosition = new DoaVector(195 + (mapBorder.getWidth() - mapNameDimensions.x) / 2f, 325);
			}
			
			if (hintFont == null || hintTextChanged) {
				hintTextDimensions = new DoaVector(1000f, 35f);
				
				hintFont = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), hintTextDimensions, hintText), 0)[0]
				);

				hintTextDimensions = new DoaVector(UIUtils.textWidth(hintFont, hintText), UIUtils.textHeight(hintFont));
				hintPosition = new DoaVector((1920 - hintTextDimensions.x) / 2f, 950 - hintTextDimensions.y / 4f);
				
				hintTextChanged = false;
			}
			
			if (loadingFont == null || loadingTextChanged) {
				loadingTextDimensions = new DoaVector(1000f, 50f);
				
				loadingFont = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), loadingTextDimensions, loadingText), 0)[0]
				);

				loadingTextDimensions = new DoaVector(UIUtils.textWidth(loadingFont, loadingText), UIUtils.textHeight(loadingFont));
				loadingPosition = new DoaVector((1920 - loadingTextDimensions.x) / 2f, 950 - loadingTextDimensions.y / 4f + 55);
				
				loadingTextChanged = false;
			}
			
			// MAP SECTION
			DoaGraphicsFunctions.drawImage(mapChooserBg, 153, 259, mapChooserBg.getWidth(), mapChooserBg.getHeight());
			
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			
			DoaGraphicsFunctions.setFont(mapNameFont);
			DoaGraphicsFunctions.drawString(selectedMapName, mapNamePosition.x, mapNamePosition.y);
		
			DoaGraphicsFunctions.drawImage(
				selectedMapPreview,
				200,
				360,
				mapBorder.getWidth() - 5f,
				mapBorder.getHeight() - 3f);
			DoaGraphicsFunctions.drawImage(mapBorder, 195, 357);
			
			DoaGraphicsFunctions.drawImage(mainScroll, 600, 170, mainScroll.getWidth(), mainScroll.getHeight());
			
			// names
			for (int i = 0; i < names.size(); i++) {
				DoaGraphicsFunctions.drawImage(playerNameBg, 725, 282 + i * 55, playerNameBg.getWidth(), playerNameBg.getHeight());
				
				DoaGraphicsFunctions.drawString(names.get(i), 735, 314 + i * 55);
			}

			// colors
			for (int i = 0; i < colors.size(); i++) {
				DoaGraphicsFunctions.setColor(colors.get(i));
				DoaGraphicsFunctions.fill(new Rectangle(983, 285 + 55 * i, colorBg.getWidth() - 6, colorBg.getHeight() - 6));
				
				DoaGraphicsFunctions.drawImage(colorBg, 980, 282 + 55 * i, colorBg.getWidth(), colorBg.getHeight());
			}

			// pawns
			for (int i = 0; i < pawns.size(); i++) {
				DoaGraphicsFunctions.drawImage(pawnBg, 1092, 282 + 55 * i, pawnBg.getWidth(), pawnBg.getHeight());
				
				DoaGraphicsFunctions.drawImage(pawns.get(i), 1122, 286 + 55 * i, pawnBg.getHeight() - 10, pawnBg.getHeight() - 10);
			}

			// HINT
			DoaGraphicsFunctions.setColor(Color.DARK_GRAY);
			DoaGraphicsFunctions.setFont(hintFont);
			DoaGraphicsFunctions.drawString(hintText, hintPosition.x, hintPosition.y);

			// LOADING BAR
			DoaGraphicsFunctions.setColor(Color.GRAY);
			DoaGraphicsFunctions.fillRect(350, 950, 1220, 50);

			DoaGraphicsFunctions.setColor(Color.ORANGE);
			DoaGraphicsFunctions.fillRect(353, 953, 1214 * currentBarProgress, 44);
			
			DoaGraphicsFunctions.setFont(loadingFont);
			/* outline */
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor().brighter());
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x - 1, loadingPosition.y);
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x + 1, loadingPosition.y);
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x, loadingPosition.y - 1);
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x, loadingPosition.y + 1);
			

			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x - 1, loadingPosition.y - 1);
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x + 1, loadingPosition.y + 1);
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x - 1, loadingPosition.y + 1);
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x + 1, loadingPosition.y - 1);
			
			//DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x - 2, loadingPosition.y);
			//DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x + 2, loadingPosition.y);
			//DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x, loadingPosition.y - 2);
			//DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x, loadingPosition.y + 2);	
			
			/* text itself */
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.drawString(loadingText, loadingPosition.x, loadingPosition.y);
		}
	}
}
