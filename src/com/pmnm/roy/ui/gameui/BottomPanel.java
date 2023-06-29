package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.ZOrders;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.RoyImageButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaMath;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import pmnm.risk.game.Deploy;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext.TurnPhase;
import pmnm.risk.game.Reinforce;
import pmnm.risk.game.databasedimpl.Province;
import pmnm.risk.game.databasedimpl.RiskGameContext;
import pmnm.risk.map.board.ProvinceHitArea;
import pmnm.risk.map.board.ProvinceHitAreas;

@SuppressWarnings("serial")
public class BottomPanel extends RoyMenu {

	private RoyImageButton nextPhaseButton;
	private RoyImageButton decrementButton;
	private RoyImageButton incrementButton;
	private RoyImageButton centerPieceButton;

	private final DoaVector NEXT_PHASE_POSITION		= new DoaVector(1200, 965);
	private final DoaVector DECREMENT_POSITION		= new DoaVector(700, 1035);
	private final DoaVector INCREMENT_POSITION		= new DoaVector(700, 985);
	private final DoaVector CENTER_PIECE_POSITION	= new DoaVector(615, 1000);

	private int maxTroopCount = Globals.UNKNOWN_TROOP_COUNT;
	private int selectedTroopCount = Globals.UNKNOWN_TROOP_COUNT;

	private final RiskGameContext context;

	private Province clickedProvince = null;

	private String garrisonText = "";
	private String ownerText = "";
	private String nameText = "";
	private String continentText = "";

	private String currentPhaseText = "";

	public BottomPanel(final RiskGameContext context) {
		this.context = context;

		nextPhaseButton = RoyImageButton.builder()
			.image(DoaSprites.getSprite("nextPhaseButtonIdle"))
			.hoverImage(DoaSprites.getSprite("nextPhaseButtonHover"))
			.pressImage(DoaSprites.getSprite("nextPhaseButtonPressed"))
			.disabledImage(DoaSprites.getSprite("nextPhaseButtonDisabled"))
			.action(source -> {
				context.goToNextPhase();
				maxTroopCount = Globals.UNKNOWN_TROOP_COUNT;
				selectedTroopCount = Globals.UNKNOWN_TROOP_COUNT;
				if (context.getCurrentPhase() == TurnPhase.DRAFT) {
					nextPhaseButton.setEnabled(false);
				}
			})
			.build();
		nextPhaseButton.setPosition(NEXT_PHASE_POSITION);
		nextPhaseButton.setEnabled(false);
		nextPhaseButton.setScale(.7f);
		addElement(nextPhaseButton);

		decrementButton = RoyImageButton.builder()
			.image(DoaSprites.getSprite("arrowDown"))
			.hoverImage(DoaSprites.getSprite("arrowDownHover"))
			.pressImage(DoaSprites.getSprite("arrowDownPress"))
			.action(source -> decrementTroopCount())
			.build();
		decrementButton.setPosition(DECREMENT_POSITION);
		addElement(decrementButton);

		incrementButton = RoyImageButton.builder()
			.image(DoaSprites.getSprite("arrowUp"))
			.hoverImage(DoaSprites.getSprite("arrowUpHover"))
			.pressImage(DoaSprites.getSprite("arrowUpPress"))
			.action(source -> incrementTroopCount())
			.build();
		incrementButton.setPosition(INCREMENT_POSITION);
		addElement(incrementButton);

		centerPieceButton = RoyImageButton.builder()
			.image(DoaSprites.getSprite("centerPiece"))
			.hoverImage(DoaSprites.getSprite("centerPieceHover"))
			.pressImage(DoaSprites.getSprite("centerPiecePress"))
			.disabledImage(DoaSprites.getSprite("centerPieceDisabled"))
			.action(source -> {
				updateSpinnerValues();
				ProvinceHitAreas areas = context.getAreas();
				switch(context.getCurrentPhase()) {
					case DRAFT:
						ProvinceHitArea selectedProvinceHitArea = areas.getSelectedProvince();
						if (selectedProvinceHitArea == null) { return; }
						IProvince selectedProvince = selectedProvinceHitArea.getProvince();
						Deploy deploy = context.setUpDeploy(selectedProvince, selectedTroopCount);
						if (context.applyDeployResult(deploy.calculateResult())) {
							if (context.getRemainingDeploys() == 0) {
								nextPhaseButton.setEnabled(true);
							}
						}
						break;
					case ATTACK_DEPLOY:
						ProvinceHitArea attackerProvinceHitArea = areas.getAttackerProvince();
						ProvinceHitArea defenderProvinceHitArea = areas.getDefenderProvince();
						if (attackerProvinceHitArea == null  || defenderProvinceHitArea == null) { return; }

						IProvince attacker = attackerProvinceHitArea.getProvince();
						IProvince defender = defenderProvinceHitArea.getProvince();

						{ /* to avoid local variable bleeding */
							Reinforce reinforce = context.setUpReinforce(attacker, defender, selectedTroopCount);
							if (context.applyReinforceResult(reinforce.calculateResult())) {
								maxTroopCount = Globals.UNKNOWN_TROOP_COUNT;
								selectedTroopCount = Globals.UNKNOWN_TROOP_COUNT;
								nextPhaseButton.setEnabled(true);
							}
						}
						break;
					case REINFORCE:
						ProvinceHitArea reinforcerProvinceHitArea = areas.getReinforcingProvince();
						ProvinceHitArea reinforceeProvinceHitArea = areas.getReinforceeProvince();
						if (reinforcerProvinceHitArea == null  || reinforceeProvinceHitArea == null) { return; }

						IProvince reinforcer = reinforcerProvinceHitArea.getProvince();
						IProvince reinforcee = reinforceeProvinceHitArea.getProvince();
						if (!reinforcer.canReinforceAnotherProvince()) { return; }

						{ /* to avoid local variable bleeding */
							Reinforce reinforce = context.setUpReinforce(reinforcer, reinforcee, selectedTroopCount);
							if (context.applyReinforceResult(reinforce.calculateResult())) {
								maxTroopCount = Globals.UNKNOWN_TROOP_COUNT;
								selectedTroopCount = Globals.UNKNOWN_TROOP_COUNT;
								nextPhaseButton.setEnabled(false);
							}
						}
						break;
					default:
						break;
				}
				updateSpinnerValues();
			})
			.build();
		centerPieceButton.setPosition(CENTER_PIECE_POSITION);
		centerPieceButton.setTextColor(Color.BLACK);
		centerPieceButton.setHoverTextColor(UIConstants.getTextColor());
		addElement(centerPieceButton);
	
		updateSpinnerValues();
		if (context.getCurrentPhase() == TurnPhase.SETUP) {
			centerPieceButton.setEnabled(false);
		} else if (context.getCurrentPhase() == TurnPhase.DRAFT) {
			centerPieceButton.setEnabled(true);
		}
		nextPhaseButton.setEnabled(false);
	
		setzOrder(ZOrders.GAME_UI_Z);
	
		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {
		
		int counter = Globals.DEFAULT_TIME_SLICE;
		private TurnPhase previousPhase;

		@Override
		public void tick() {
			if (!isVisible()) return;

			if (counter < Globals.DEFAULT_TIME_SLICE) {
				counter++;
				return;
			}
			
			TurnPhase currentPhase = context.getCurrentPhase();
			if (currentPhase != previousPhase) {
				previousPhase = currentPhase;
				updateSpinnerValues();

				//currentPhaseText = Translator.getInstance().getTranslatedString(currentPhase.toString());
				currentPhaseText = currentPhase.toString();
			}
			
			clickedProvince = context.getAreas().getSelectedProvince() != null ? (Province) context.getAreas().getSelectedProvince().getProvince() : null;
			if (clickedProvince != null) {
				garrisonText = clickedProvince.getNumberOfTroops() == Globals.UNKNOWN_TROOP_COUNT ?
					"???" :
					Integer.toString(clickedProvince.getNumberOfTroops());
				if (clickedProvince.isOccupied()) {
					ownerText = clickedProvince.getOccupier().getName();
				} else {
					ownerText = "";
				}
				nameText = clickedProvince.getName().toUpperCase(Translator.getInstance().getCurrentLanguage().getLocale());
				continentText = clickedProvince.getContinent().getName().toUpperCase(Translator.getInstance().getCurrentLanguage().getLocale());
			} else {
				garrisonText = "";
				ownerText = "";
				nameText = "";
				continentText = "";
			}
			counter = 0;
		}
	}

	private final class Renderer extends DoaRenderer {

		private transient BufferedImage bottomRing	= DoaSprites.getSprite("MainMenuBottomRing");

		private final BufferedImage MIDDLE	= DoaSprites.getSprite("gaugeBig");
		private final BufferedImage LEFT	= DoaSprites.getSprite("gaugeLeft");
		private final BufferedImage RIGHT 	= DoaSprites.getSprite("gaugeRight");
		
		private transient BufferedImage garrisonBG	= DoaSprites.getSprite("garrisonHolder");
		private transient BufferedImage ownerBG		= DoaSprites.getSprite("ownerHolder");
		private transient BufferedImage provinceBG	= DoaSprites.getSprite("provinceNameHolder");
		private transient BufferedImage continentBG = DoaSprites.getSprite("continentHolder");

		private transient BufferedImage garrisonIcon	= DoaSprites.getSprite("garrisonHolderIcon");
		private transient BufferedImage ownerIcon		= DoaSprites.getSprite("ownerHolderIcon");
		private transient BufferedImage provinceIcon	= DoaSprites.getSprite("provinceNameHolderIcon");
		private transient BufferedImage continentIcon	= DoaSprites.getSprite("continentHolderIcon");

		private final DoaVector GARRISON_BG_POSITION	= new DoaVector(870, 890);
		private final DoaVector OWNER_BG_POSITION		= new DoaVector(857, 932);
		private final DoaVector PROVINCE_BG_POSITION	= new DoaVector(837, 974);
		private final DoaVector CONTINENT_BG_POSITION	= new DoaVector(825, 1016);
		
		
		@Override
		public void render() {
			if (!isVisible()) return;
			
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());

			DoaGraphicsFunctions.drawImage(bottomRing, 0, (float) (Main.WINDOW_HEIGHT - bottomRing.getHeight() + 6d));

			DoaGraphicsFunctions.drawImage(LEFT, Main.WINDOW_WIDTH * 0.304f, (float) ((double) Main.WINDOW_HEIGHT - LEFT.getHeight()));
			DoaGraphicsFunctions.drawImage(RIGHT, Main.WINDOW_WIDTH * 0.585f, (float) ((double) Main.WINDOW_HEIGHT - RIGHT.getHeight()));

			DoaVector phaseArea = new DoaVector(Main.WINDOW_WIDTH * 0.070f, Main.WINDOW_HEIGHT * 0.046f);
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), phaseArea, currentPhaseText)));
			DoaGraphicsFunctions.drawString(currentPhaseText, Main.WINDOW_WIDTH * 0.615f, Main.WINDOW_HEIGHT * 0.993f);

			DoaGraphicsFunctions.drawImage(MIDDLE, (Main.WINDOW_WIDTH - MIDDLE.getWidth()) / 2f, (float) ((double) Main.WINDOW_HEIGHT - MIDDLE.getHeight()));

			DoaGraphicsFunctions.drawImage(garrisonBG, GARRISON_BG_POSITION.x, GARRISON_BG_POSITION.y);
			DoaGraphicsFunctions.drawImage(garrisonIcon, GARRISON_BG_POSITION.x + garrisonBG.getWidth() + 6, GARRISON_BG_POSITION.y + 8);
			
			DoaGraphicsFunctions.drawImage(ownerBG, OWNER_BG_POSITION.x, OWNER_BG_POSITION.y);
			DoaGraphicsFunctions.drawImage(ownerIcon, OWNER_BG_POSITION.x + ownerBG.getWidth() + 6, OWNER_BG_POSITION.y + 8);
			
			DoaGraphicsFunctions.drawImage(provinceBG, PROVINCE_BG_POSITION.x, PROVINCE_BG_POSITION.y);
			DoaGraphicsFunctions.drawImage(provinceIcon, PROVINCE_BG_POSITION.x + provinceBG.getWidth() + 6, PROVINCE_BG_POSITION.y + 8);
			
			DoaGraphicsFunctions.drawImage(continentBG, CONTINENT_BG_POSITION.x, CONTINENT_BG_POSITION.y);
			DoaGraphicsFunctions.drawImage(continentIcon, CONTINENT_BG_POSITION.x + continentBG.getWidth() + 6, CONTINENT_BG_POSITION.y + 4);

			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN, 25f));
			FontMetrics fm = DoaGraphicsFunctions.getFontMetrics();

			DoaGraphicsFunctions.drawString(garrisonText, GARRISON_BG_POSITION.x + (garrisonBG.getWidth() - fm.stringWidth(garrisonText)) / 2f, GARRISON_BG_POSITION.y * 1.031f);
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN, 30f));
			fm = DoaGraphicsFunctions.getFontMetrics();
			if (clickedProvince != null) {
				DoaGraphicsFunctions.setColor(clickedProvince.getOccupier().getColor());
			}
			DoaGraphicsFunctions.drawString(ownerText, OWNER_BG_POSITION.x + (ownerBG.getWidth() - fm.stringWidth(ownerText)) / 2f, OWNER_BG_POSITION.y * 1.03f);
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN,
			        Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), new DoaVector(provinceBG.getWidth() * 0.95f, provinceBG.getHeight()), nameText)));
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			fm = DoaGraphicsFunctions.getFontMetrics();
			DoaGraphicsFunctions.drawString(nameText, PROVINCE_BG_POSITION.x + (provinceBG.getWidth() - fm.stringWidth(nameText)) / 2f, PROVINCE_BG_POSITION.y * 1.03f);
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN,
			        Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), new DoaVector(continentBG.getWidth() * 0.95f, continentBG.getHeight()), continentText)));
			fm = DoaGraphicsFunctions.getFontMetrics();
			DoaGraphicsFunctions.drawString(continentText, CONTINENT_BG_POSITION.x + (continentBG.getWidth() - fm.stringWidth(continentText)) / 2f, CONTINENT_BG_POSITION.y * 1.03f);
		}
		
	}

	private void updateSpinnerValues() {
		maxTroopCount = context.getRemainingDeploys();
		if (selectedTroopCount == Globals.UNKNOWN_TROOP_COUNT || selectedTroopCount == 0) {
			selectedTroopCount = maxTroopCount;
		} else {
			selectedTroopCount = (int) DoaMath.clamp(selectedTroopCount, 0, maxTroopCount);
		}
		if (maxTroopCount != Globals.UNKNOWN_TROOP_COUNT && maxTroopCount != 0) {
			centerPieceButton.setEnabled(true);
			centerPieceButton.setText(Integer.toString(selectedTroopCount));
			nextPhaseButton.setEnabled(false);
		} else {
			centerPieceButton.setEnabled(false);
			centerPieceButton.setText("");
		}
	}

	private void incrementTroopCount() {
		selectedTroopCount = Math.min(selectedTroopCount + 1, maxTroopCount);
		if (centerPieceButton.isEnabled()) {
			centerPieceButton.setText(Integer.toString(selectedTroopCount));
		}
	}

	private void decrementTroopCount() {
		selectedTroopCount = Math.max(selectedTroopCount - 1, 1);
		if (centerPieceButton.isEnabled()) {
			centerPieceButton.setText(Integer.toString(selectedTroopCount));
		}
	}
}
