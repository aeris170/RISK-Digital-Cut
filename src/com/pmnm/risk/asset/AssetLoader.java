package com.pmnm.risk.asset;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaSprite;
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
			DoaSprites.createSprite("garrisonHolder", "/ui/gameScreenElements/garrisonHolder.png");
			DoaSprites.createSprite("garrisonHolderIcon", "/ui/gameScreenElements/garrisonHolderIcon.png");
			DoaSprites.createSprite("ownerHolder", "/ui/gameScreenElements/ownerHolder.png");
			DoaSprites.createSprite("ownerHolderIcon", "/ui/gameScreenElements/ownerHolderIcon.png");
			DoaSprites.createSprite("provinceNameHolder", "/ui/gameScreenElements/provinceNameHolder.png");
			DoaSprites.createSprite("provinceNameHolderIcon", "/ui/gameScreenElements/provinceNameHolderIcon.png");
			DoaSprites.createSprite("mini", "/ui/gameScreenElements/mini.png");

			List<DoaSprite> riskLogoKeyFrames = new ArrayList<>();
			for (int i = 1; i <= 11; i++) {
				riskLogoKeyFrames.add(DoaSprites.createSprite("KEYFRAME" + i, "/ui/logoKeyframes/" + i + ".png"));
			}
			DoaAnimations.createAnimation("RiskLogoAnim", riskLogoKeyFrames, 100);

			Font customFont = Font.createFont(Font.TRUETYPE_FONT, AssetLoader.class.getResourceAsStream("/ui/fonts/Constantia.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (IOException | FontFormatException ex) {
			ex.printStackTrace();
		}
	}
}