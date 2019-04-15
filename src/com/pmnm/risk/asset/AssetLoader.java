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
			DoaSprites.createSprite("garrisonHolder", "/ui/gameScreenElements/smallGarrisonHolder.png");
			DoaSprites.createSprite("garrisonHolderIcon", "/ui/gameScreenElements/smallGarrisonHolderIcon.png");
			DoaSprites.createSprite("ownerHolder", "/ui/gameScreenElements/smallOwnerHolder.png");
			DoaSprites.createSprite("ownerHolderIcon", "/ui/gameScreenElements/smallOwnerHolderIcon.png");
			DoaSprites.createSprite("provinceNameHolder", "/ui/gameScreenElements/smallProvinceNameHolder.png");
			DoaSprites.createSprite("provinceNameHolderIcon", "/ui/gameScreenElements/smallProvinceNameHolderIcon.png");
			DoaSprites.createSprite("mini", "/ui/gameScreenElements/smallMiniBG.png");

			DoaSprites.createSprite("scroll", "/ui/gameScreenElements/smallGameScreenScroll.png");
			DoaSprites.createSprite("nextPhaseButtonIdle", "/ui/gameScreenElements/smallNextPhaseButton.png");
			DoaSprites.createSprite("nextPhaseButtonHover", "/ui/gameScreenElements/smallPressedNextPhaseButton.png");
			DoaSprites.createSprite("cardsButtonIdle", "/ui/gameScreenElements/smallProvinceCardsButton.png");
			DoaSprites.createSprite("cardsButtonHover", "/ui/gameScreenElements/smallPressedProvinceCardsButton.png");
			DoaSprites.createSprite("winConditionButtonIdle", "/ui/gameScreenElements/smallWinningConditionsButton.png");
			DoaSprites.createSprite("winConditionButtonHover", "/ui/gameScreenElements/smallPressedWinningConditionsButton.png");

			DoaSprites.createSprite("p1Pawn", "/ui/gameScreenElements/pawns/p1.png");
			DoaSprites.createSprite("p2Pawn", "/ui/gameScreenElements/pawns/p2.png");
			DoaSprites.createSprite("p3Pawn", "/ui/gameScreenElements/pawns/p3.png");
			DoaSprites.createSprite("p4Pawn", "/ui/gameScreenElements/pawns/p4.png");
			DoaSprites.createSprite("p5Pawn", "/ui/gameScreenElements/pawns/p5.png");
			DoaSprites.createSprite("p6Pawn", "/ui/gameScreenElements/pawns/p6.png");

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