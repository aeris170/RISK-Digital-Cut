package com.pmnm.risk.asset;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;

public final class AssetLoader {

	private AssetLoader() {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void initializeAssets() {
		try {
			DoaSprites.createSprite("WorldMapOld", "/maps/legacy/ColorMapBig.png");
			DoaSprites.createSprite("WorldMapNames", "/maps/legacy/ColorMapNamesNew.png");
			DoaSprites.createSprite("DummyMap", "/maps/legacy/DummyMap2.png");
			DoaSprites.createSprite("WorldMap", "/maps/GeographicMap.png");

			DoaSprites.createSprite("MainMenuBackground", "/ui/MainMenuBackground.png");
			DoaSprites.createSprite("MainMenuTopRing", "/ui/TopRing.png");
			DoaSprites.createSprite("MainMenuBottomRing", "/ui/BottomRing.png");
			DoaSprites.createSprite("RiskLogo", "/ui/RiskLogo.png");
			DoaSprites.createSprite("FleurDeLis", "/ui/FleurDeLis.png");
			DoaSprites.createSprite("FleurDeLisGold", "/ui/FleurDeLisGold.png");
			DoaSprites.createSprite("ButtonIdle", "/ui/ButtonIdle.png");
			DoaSprites.createSprite("ButtonHover", "/ui/ButtonHover.png");

			DoaSprites.createSprite("pieceGauge", "/ui/gameScreenElements/pieceGauge.png");
			DoaSprites.createSprite("gaugeNeedle", "/ui/gameScreenElements/needle.png");
			DoaSprites.createSprite("infoPanel", "/ui/gameScreenElements/infoPanel.png");

			DoaAnimations.createAnimation("RiskLogoAnim", "/ui/RiskGIF.gif", 200);

			Font customFont = Font.createFont(Font.TRUETYPE_FONT, AssetLoader.class.getResourceAsStream("/ui/fonts/Constantia.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (IOException | FontFormatException ex) {
			ex.printStackTrace();
		}
	}
}