package com.pmnm.risk.globals;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import doa.engine.graphics.DoaAnimations;
import doa.engine.graphics.DoaSprites;
import doa.engine.sound.DoaSounds;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class AssetLoader {

	public static void initializeAssets() {
		try {
			DoaSprites.createSprite("BG0", "/ui/Background1.png");
			DoaSprites.createSprite("BG1", "/ui/Background2.png");
			DoaSprites.createSprite("BG2", "/ui/Background3.png");
			DoaSprites.createSprite("BG3", "/ui/Background4.png");
			DoaSprites.createSprite("BG4", "/ui/Background5.png");
			DoaSprites.createSprite("BG5", "/ui/Background6.png");
			DoaSprites.createSprite("MainMenuTopRing", "/ui/TopRing.png");
			DoaSprites.createSprite("MainMenuBottomRing", "/ui/BottomRing.png");
			DoaSprites.createSprite("RiskLogo", "/ui/RiskLogo.png");
			DoaSprites.createSprite("FleurDeLis", "/ui/FleurDeLis.png");
			DoaSprites.createSprite("FleurDeLisGold", "/ui/FleurDeLisGold.png");
			DoaSprites.createSprite("ButtonIdle", "/ui/ButtonIdle.png");
			DoaSprites.createSprite("ButtonHover", "/ui/ButtonHover.png");
			DoaSprites.createSprite("escapeMenu", "/ui/gameScreenElements/escapeMenu.png");

			{// SINGLEPLAYER MENU
				DoaSprites.createSprite("ArrowLeftIdle", "/ui/playOffline/arrowUnpressedLeft.png");
				DoaSprites.createSprite("ArrowRightIdle", "/ui/playOffline/arrowUnpressedRight.png");
				DoaSprites.createSprite("ArrowDownIdle", "/ui/playOffline/arrowUnpressedDown.png");
				DoaSprites.createSprite("ArrowLeftClick", "/ui/playOffline/arrowPressedLeft.png");
				DoaSprites.createSprite("ArrowRightClick", "/ui/playOffline/arrowPressedRight.png");
				DoaSprites.createSprite("ArrowDownClick", "/ui/playOffline/arrowPressedDown.png");
				DoaSprites.createSprite("White", "/ui/playOffline/colour.png");
				DoaSprites.createSprite("ColorBorder", "/ui/playOffline/colourRect.png");
				DoaSprites.createSprite("MapChooserBackground", "/ui/playOffline/mapChooserContainer.png");
				DoaSprites.createSprite("MapBorder", "/ui/playOffline/mapContainer.png");
				DoaSprites.createSprite("PlayerTypeBorder", "/ui/playOffline/playerType.png");
				DoaSprites.createSprite("MainScroll", "/ui/playOffline/playersScroll.png");
				DoaSprites.createSprite("DifficultyBorder", "/ui/playOffline/difficuty.png");
				DoaSprites.createSprite("DropDown", "/ui/playOffline/droppedComboBoxPlain.png");
				DoaSprites.createSprite("DropDownTex", "/ui/playOffline/droppedComboBox.png");
				DoaSprites.createSprite("DropDownType", "/ui/playOffline/droppedComboBoxPlainType.png");
				DoaSprites.createSprite("DropDownOnlineType", "/ui/playOnline/onlineDroppedComboBoxPlainType.png");
				DoaSprites.createSprite("DropDownTexType", "/ui/playOffline/droppedComboBoxType.png");
				DoaSprites.createSprite("DropDownOnlineTexType", "/ui/playOnline/onlineDroppedComboBoxType.png");
				DoaSprites.createSprite("DropDownColor", "/ui/playOffline/droppedComboBoxPlainColor.png");
				DoaSprites.createSprite("DropDownColorType", "/ui/playOffline/droppedComboBoxColor.png");
				DoaSprites.createSprite("RandomPlacementBorder", "/ui/playOffline/randomBox.png");

				{ // SAVE/LOAD MENU
					DoaSprites.createSprite("SaveScroll", "/ui/saveLoad/savedGameScroll.png");
					DoaSprites.createSprite("SaveMapContainter", "/ui/saveLoad/savedMapContainer.png");
				}
			}

			{// MULTIPLAYER MENU
				DoaSprites.createSprite("Ready", "/ui/playOnline/readyBlip.png");
				DoaSprites.createSprite("ReadyCircle", "/ui/playOnline/readyHolder.png");
			}

			{// SETTINGS MENU
				DoaSprites.createSprite("Lens", "/ui/settings/lensEffect.png");
				DoaSprites.createSprite("LensHover", "/ui/settings/lensEffectHover.png");
				DoaSprites.createSprite("LensSelect", "/ui/settings/lensEffectSelected.png");
				DoaSprites.createSprite("English", "/ui/settings/englis.png");
				DoaSprites.createSprite("Deutch", "/ui/settings/deutsc.png");
				DoaSprites.createSprite("Espanol", "/ui/settings/espano.png");
				DoaSprites.createSprite("France", "/ui/settings/franc.png");
				DoaSprites.createSprite("Italian", "/ui/settings/itali.png");
				DoaSprites.createSprite("Russian", "/ui/settings/rusk.png");
				DoaSprites.createSprite("Turkish", "/ui/settings/turkis.png");
			}

			{// RULES
				DoaSprites.createSprite("pt0", "/ui/rules/turnpt0.png");
				DoaSprites.createSprite("pt1", "/ui/rules/turnpt1.png");
				DoaSprites.createSprite("pt2", "/ui/rules/turnpt2.png");
				DoaSprites.createSprite("pt3", "/ui/rules/turnpt3.png");
				DoaSprites.createSprite("pt4", "/ui/rules/turnpt4.png");
				DoaSprites.createSprite("pt5", "/ui/rules/turnpt5.png");
			}

			{// EXIT POPUP
				DoaSprites.createSprite("ExitPopupBackground", "/ui/popupBox.png");
				DoaSprites.createSprite("MiniButtonIdle", "/ui/MiniButtonIdle.png");
				DoaSprites.createSprite("MiniButtonHover", "/ui/MiniButtonHover.png");
				DoaSprites.createSprite("MiniButtonClick", "/ui/MiniButtonPressed.png");
			}

			{// TOP INFO
				DoaSprites.createSprite("timerBottomRing", "/ui/gameScreenElements/topInfo/timerBottomRing.png");
				DoaSprites.createSprite("seasonCircle", "/ui/gameScreenElements/topInfo/turnCirc.png");
				DoaSprites.createSprite("summer", "/ui/gameScreenElements/topInfo/summer.png");
				DoaSprites.createSprite("spring", "/ui/gameScreenElements/topInfo/spring.png");
				DoaSprites.createSprite("fall", "/ui/gameScreenElements/topInfo/fall.png");
				DoaSprites.createSprite("winter", "/ui/gameScreenElements/topInfo/winter.png");
			}

			{// WEATHER EFFECTS
				DoaSprites.createSprite("godray", "/ui/gameScreenElements/topInfo/godray.png");
				DoaAnimations.createAnimation("Snowfall", "/ui/gameScreenElements/topInfo/snowfall.gif", 50);
				DoaAnimations.createAnimation("BetterFallingLeaves", "/ui/gameScreenElements/topInfo/betterFallingLeaves.gif", 50);
				DoaAnimations.createAnimation("FallingLeaves", "/ui/gameScreenElements/topInfo/fallingLeavesFixed.gif", 100);
				DoaAnimations.createAnimation("FloatingLeaves", "/ui/gameScreenElements/topInfo/floatingLeaves.gif", 100);
				DoaAnimations.createAnimation("CherryPetals", "/ui/gameScreenElements/topInfo/cherryPetals.gif", 60);
				DoaAnimations.createAnimation("Rain", "/ui/gameScreenElements/topInfo/rainLight.gif", 40);
				DoaSprites.createSprite("fallTex", "/ui/gameScreenElements/waterTextures/fallWaterTexture.jpg");
				DoaSprites.createSprite("springTex", "/ui/gameScreenElements/waterTextures/springWaterTexture.jpg");
				DoaSprites.createSprite("summerTex", "/ui/gameScreenElements/waterTextures/summerWaterTexture.jpg");
				DoaSprites.createSprite("winterTex", "/ui/gameScreenElements/waterTextures/winterWaterTexture.jpg");
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
				DoaSprites.createSprite("arrowUp", "/ui/gameScreenElements/bottomInfo/arrowUp.png");
				DoaSprites.createSprite("arrowUpHover", "/ui/gameScreenElements/bottomInfo/arrowUpHovered.png");
				DoaSprites.createSprite("arrowUpPress", "/ui/gameScreenElements/bottomInfo/arrowUpPressed.png");
				DoaSprites.createSprite("arrowDown", "/ui/gameScreenElements/bottomInfo/arrowDown.png");
				DoaSprites.createSprite("arrowDownHover", "/ui/gameScreenElements/bottomInfo/arrowDownHovered.png");
				DoaSprites.createSprite("arrowDownPress", "/ui/gameScreenElements/bottomInfo/arrowDownPressed.png");
				DoaSprites.createSprite("centerPiece", "/ui/gameScreenElements/bottomInfo/ArmySelector.png");
			}

			{// CONTINENT SYMBOLS
				DoaSprites.createSprite("AS", "/ui/gameScreenElements/continents/asia.png");
				DoaSprites.createSprite("AF", "/ui/gameScreenElements/continents/africa.png");
				DoaSprites.createSprite("EU", "/ui/gameScreenElements/continents/europe.png");
				DoaSprites.createSprite("NA", "/ui/gameScreenElements/continents/northAmerica.png");
				DoaSprites.createSprite("AU", "/ui/gameScreenElements/continents/australia.png");
				DoaSprites.createSprite("SA", "/ui/gameScreenElements/continents/southAmerica.png");
			}

			{// PLAYER SYMBOLS
				DoaSprites.createSprite("p1Pawn", "/ui/gameScreenElements/pawns/p1.png");
				DoaSprites.createSprite("p2Pawn", "/ui/gameScreenElements/pawns/p2.png");
				DoaSprites.createSprite("p3Pawn", "/ui/gameScreenElements/pawns/p3.png");
				DoaSprites.createSprite("p4Pawn", "/ui/gameScreenElements/pawns/p4.png");
				DoaSprites.createSprite("p5Pawn", "/ui/gameScreenElements/pawns/p5.png");
				DoaSprites.createSprite("p6Pawn", "/ui/gameScreenElements/pawns/p6.png");
			}

			{// DICE PANEL
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

			{// CARD PANEL
				DoaSprites.createSprite("artillery", "/ui/gameScreenElements/cardScroll/artillery.png");
				DoaSprites.createSprite("cavalry", "/ui/gameScreenElements/cardScroll/cavalry.png");
				DoaSprites.createSprite("infantry", "/ui/gameScreenElements/cardScroll/footman.png");
				DoaSprites.createSprite("card", "/ui/gameScreenElements/cardScroll/card.png");
				DoaSprites.createSprite("cardHover", "/ui/gameScreenElements/cardScroll/cardHover.png");
				DoaSprites.createSprite("cardSelected", "/ui/gameScreenElements/cardScroll/cardSelected.png");
				DoaSprites.createSprite("trump", "/ui/gameScreenElements/cardScroll/eyeOnBoxTrump.png");
				DoaSprites.createSprite("scroll", "/ui/gameScreenElements/cardScroll/scroll.png");
				DoaSprites.createSprite("cardButtonHover", "/ui/gameScreenElements/cardScroll/buttonHover.png");
				DoaSprites.createSprite("cardButtonIdle", "/ui/gameScreenElements/cardScroll/buttonIdle.png");
				DoaSprites.createSprite("cardButtonLocked", "/ui/gameScreenElements/cardScroll/buttonLocked.png");
				DoaSprites.createSprite("cardButtonPressed", "/ui/gameScreenElements/cardScroll/buttonPressed.png");
			}

			{// MUSIC
				DoaSounds.createSoundClip("track1", "/sounds/music/Ritual.wav");
				// DoaSounds.get("track1").loop(Clip.LOOP_CONTINUOUSLY);
				// DoaSounds.setGlobalVolume(1);
			}

			List<BufferedImage> riskLogoKeyFrames = new ArrayList<>();
			for (int i = 1; i <= 11; i++) {
				riskLogoKeyFrames.add(DoaSprites.createSprite("KEYFRAME" + i, "/ui/logoKeyframes/" + i + ".png"));
			}
			DoaAnimations.createAnimation("RiskLogoAnim", riskLogoKeyFrames, 100);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, AssetLoader.class.getResourceAsStream("/ui/fonts/Constantia.ttf")).deriveFont(12f);
			ge.registerFont(customFont);
			Font customFont2 = Font.createFont(Font.TRUETYPE_FONT, AssetLoader.class.getResourceAsStream("/ui/fonts/BookAntiqua.ttf")).deriveFont(12f);
			ge.registerFont(customFont2);
		} catch (IOException | FontFormatException | UnsupportedAudioFileException | LineUnavailableException ex) {
			ex.printStackTrace();
		}
	}
}