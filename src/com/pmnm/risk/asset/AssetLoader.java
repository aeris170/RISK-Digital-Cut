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

			{// TOP INFO
				DoaSprites.createSprite("timerBottomRing", "/ui/gameScreenElements/topInfo/timerBottomRing.png");
				DoaSprites.createSprite("seasonCircle", "/ui/gameScreenElements/topInfo/turnCirc.png");
				DoaSprites.createSprite("summer", "/ui/gameScreenElements/topInfo/summer.png");
				DoaSprites.createSprite("spring", "/ui/gameScreenElements/topInfo/spring.png");
				DoaSprites.createSprite("fall", "/ui/gameScreenElements/topInfo/fall.png");
				DoaSprites.createSprite("winter", "/ui/gameScreenElements/topInfo/winter.png");
			}

			{// BOTTOM INFO
				DoaSprites.createSprite("gaugeBig", "/ui/gameScreenElements/bottomInfo/gaugeBig.png");
				DoaSprites.createSprite("gaugeRight", "/ui/gameScreenElements/bottomInfo/gaugeRight.png");
				DoaSprites.createSprite("gaugeLeft", "/ui/gameScreenElements/bottomInfo/gaugeLeft.png");
				DoaSprites.createSprite("garrisonHolder", "/ui/gameScreenElements/bottomInfo/garrisonHolder.png");
				DoaSprites.createSprite("garrisonHolderIcon", "/ui/gameScreenElements/bottomInfo/garrison.png");
				DoaSprites.createSprite("ownerHolder", "/ui/gameScreenElements/bottomInfo/ownerHolder.png");
				DoaSprites.createSprite("ownerHolderIcon", "/ui/gameScreenElements/bottomInfo/owner.png");
				DoaSprites.createSprite("provinceNameHolder", "/ui/gameScreenElements/bottomInfo/nameHolder.png");
				DoaSprites.createSprite("provinceNameHolderIcon", "/ui/gameScreenElements/bottomInfo/name.png");
				DoaSprites.createSprite("continentHolder", "/ui/gameScreenElements/bottomInfo/continentHolder.png");
				DoaSprites.createSprite("continentHolderIcon", "/ui/gameScreenElements/bottomInfo/continent.png");
				DoaSprites.createSprite("nextPhaseButtonIdle", "/ui/gameScreenElements/bottomInfo/nextPhaseNeutral.png");
				DoaSprites.createSprite("nextPhaseButtonHover", "/ui/gameScreenElements/bottomInfo/nextPhaseHovered.png");
				DoaSprites.createSprite("nextPhaseButtonPressed", "/ui/gameScreenElements/bottomInfo/nextPhasePressed.png");
				DoaSprites.createSprite("nextPhaseButtonDisabled", "/ui/gameScreenElements/bottomInfo/nextPhaseDisabled.png");
			}

			{// CONTINENT SYMBOLS
				DoaSprites.createSprite("AS", "/ui/gameScreenElements/continents/asia.png");
				DoaSprites.createSprite("AF", "/ui/gameScreenElements/continents/africa.png");
				DoaSprites.createSprite("EU", "/ui/gameScreenElements/continents/europe.png");
				DoaSprites.createSprite("NA", "/ui/gameScreenElements/continents/northAmerica.png");
				DoaSprites.createSprite("AU", "/ui/gameScreenElements/continents/australia.png");
				DoaSprites.createSprite("SA", "/ui/gameScreenElements/continents/southAmerica.png");
			}

			{// PAWNS
				DoaSprites.createSprite("p1Pawn", "/ui/gameScreenElements/pawns/p1.png");
				DoaSprites.createSprite("p2Pawn", "/ui/gameScreenElements/pawns/p2.png");
				DoaSprites.createSprite("p3Pawn", "/ui/gameScreenElements/pawns/p3.png");
				DoaSprites.createSprite("p4Pawn", "/ui/gameScreenElements/pawns/p4.png");
				DoaSprites.createSprite("p5Pawn", "/ui/gameScreenElements/pawns/p5.png");
				DoaSprites.createSprite("p6Pawn", "/ui/gameScreenElements/pawns/p6.png");
			}

			{// DICE
				DoaSprites.createSprite("diceScroll", "/ui/gameScreenElements/diceScroll/diceScroll.png");
				DoaSprites.createSprite("dice1Idle", "/ui/gameScreenElements/diceScroll/oneDieNormal.png");
				DoaSprites.createSprite("dice1Hover", "/ui/gameScreenElements/diceScroll/oneDieHover.png");
				DoaSprites.createSprite("dice2Idle", "/ui/gameScreenElements/diceScroll/twoDiceNormal.png");
				DoaSprites.createSprite("dice2Hover", "/ui/gameScreenElements/diceScroll/twoDiceHover.png");
				DoaSprites.createSprite("dice3Idle", "/ui/gameScreenElements/diceScroll/threeDiceNormal.png");
				DoaSprites.createSprite("dice3Hover", "/ui/gameScreenElements/diceScroll/threeDiceHover.png");
				DoaSprites.createSprite("blitzIdle", "/ui/gameScreenElements/diceScroll/blitzNormal.png");
				DoaSprites.createSprite("blitzHover", "/ui/gameScreenElements/diceScroll/blitzHover.png");
			}

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