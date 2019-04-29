package com.pmnm.risk.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.dice.Dice;
import com.pmnm.risk.dice.exceptions.DiceException;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.map.board.ProvinceConnector;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.risk.ui.gameui.BottomPanel;
import com.pmnm.risk.ui.gameui.DicePanel;
import com.pmnm.risk.ui.gameui.RiskGameScreenUI;

public class GameManager extends DoaObject {

	private static final long serialVersionUID = -4928417050440420799L;

	public static final List<Player> players = new ArrayList<>();
	public static int numberOfPlayers = 2;
	public static boolean manualPlacement = false;

	public static boolean isManualPlacementDone = false;
	public static final Map<Player, Integer> startingTroops = new HashMap<>();
	public static int placementCounter = 0;

	public static TurnPhase currentPhase = TurnPhase.DRAFT;
	public static int reinforcementForThisTurn = 0;
	public static Player currentPlayer;
	public static int turnCount = 0;

	public static ProvinceHitArea attackerProvinceHitArea = null;
	public static ProvinceHitArea defenderProvinceHitArea = null;
	public static DicePanel dicePanel = RiskGameScreenUI.DicePanel;

	public static ProvinceHitArea moveAfterOccupySource = null;
	public static ProvinceHitArea moveAfterOccupyDestination = null;

	public static ProvinceHitArea reinforcingProvince = null;
	public static ProvinceHitArea reinforcedProvince = null;

	public static ProvinceHitArea clickedHitArea;

	private static Province draftReinforceProvince = null;

	public GameManager() {
		super(0f, 0f);
		int startingTroopCount = Player.findStartingTroopCount(numberOfPlayers);
		for (int i = 0; i < numberOfPlayers; i++) {
			Player p = DoaHandler.instantiate(Player.class, "Player" + i, PlayerColorBank.get(i), true);
			players.add(p);
			startingTroops.put(p, startingTroopCount);
		}
		/* AIPlayer aIP1 = new AIPlayer("AIPlayer1", PlayerColorBank.get(0), 1);
		 * DoaHandler.add(aIP1); players.add(aIP1); startingTroops.put(aIP1,
		 * startingTroopCount); AIPlayer aIP2 = new AIPlayer("AIPlayer2",
		 * PlayerColorBank.get(1), 1); players.add(aIP2); DoaHandler.add(aIP2);
		 * startingTroops.put(aIP2, startingTroopCount); */
		currentPlayer = players.get(0);
		currentPlayer.turn();
		if (!manualPlacement) {
			randomPlacement();
		}
	}

	public static void nextPhase() {
		if (currentPhase == TurnPhase.DRAFT) {
			currentPhase = TurnPhase.ATTACK;
			if (currentPlayer.isLocalPlayer()) {
				BottomPanel.nextPhaseButton.enable();
			}
			BottomPanel.nullSpinner();
		} else if (currentPhase == TurnPhase.ATTACK) {
			currentPhase = TurnPhase.REINFORCE;
			markAttackerProvince(null);
			markDefenderProvince(null);
		} else if (currentPhase == TurnPhase.REINFORCE) {
			currentPhase = TurnPhase.DRAFT;
			currentPlayer.endTurn();
			GameManager.turnCount++;
			currentPlayer = players.get(turnCount % players.size());
			currentPlayer.turn();
			reinforcementForThisTurn = Player.calculateReinforcementsForThisTurn(currentPlayer);
			BottomPanel.updateSpinnerValues(1, reinforcementForThisTurn);
			BottomPanel.nextPhaseButton.disable();
		}
	}

	@Override
	public void tick() {
		if (DoaMouse.MB1) {
			clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.isMouseClicked()).findFirst().orElse(null);
		}
		if (!isManualPlacementDone) {
			if (startingTroops.values().stream().allMatch(v -> v <= 0)) {
				isManualPlacementDone = true;
				reinforcementForThisTurn = Player.calculateReinforcementsForThisTurn(currentPlayer);
				BottomPanel.updateSpinnerValues(1, reinforcementForThisTurn);
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {}

	public static void claimProvince(Province claimed) {
		claimed.getClaimedBy(currentPlayer);
		startingTroops.put(currentPlayer, startingTroops.get(currentPlayer) - 1);
		currentPlayer = players.get(++placementCounter % players.size());
		currentPlayer.turn();
	}

	public static void draftReinforce(int reinforcementCount) {
		if (draftReinforceProvince != null) {
			draftReinforceProvince.addTroops(reinforcementCount);
			if (!isManualPlacementDone) {
				startingTroops.put(currentPlayer, startingTroops.get(currentPlayer) - reinforcementCount);
				currentPlayer = players.get(++placementCounter % players.size());
				currentPlayer.turn();
			} else {
				reinforcementForThisTurn -= reinforcementCount;
				if (reinforcementForThisTurn <= 0) {
					nextPhase();
				} else {
					BottomPanel.updateSpinnerValues(1, reinforcementForThisTurn);
				}
			}
			draftReinforceProvince.getProvinceHitArea().isSelected = false;
			draftReinforceProvince = null;
		}
	}

	public static int numberOfReinforcementsForThisTurn() {
		return reinforcementForThisTurn;
	}

	public static boolean areAllProvincesClaimed() {
		return Province.ALL_PROVINCES.stream().filter(province -> province.isClaimed()).count() == Province.ALL_PROVINCES.size();
	}

	public static void markAttackerProvince(ProvinceHitArea province) {
		if (attackerProvinceHitArea != null) {
			ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(
			        hitArea -> attackerProvinceHitArea.getProvince().getNeighbours().contains(hitArea.getProvince()) && !hitArea.getProvince().isOwnedBy(currentPlayer))
			        .collect(Collectors.toList()).forEach(hitArea -> hitArea.deemphasizeForAttack());
			attackerProvinceHitArea.deselectAsAttacker();
		}
		attackerProvinceHitArea = province;
		if (attackerProvinceHitArea != null) {
			ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(
			        hitArea -> attackerProvinceHitArea.getProvince().getNeighbours().contains(hitArea.getProvince()) && !hitArea.getProvince().isOwnedBy(currentPlayer))
			        .collect(Collectors.toList()).forEach(hitArea -> hitArea.emphasizeForAttack());
			attackerProvinceHitArea.selectAsAttacker();
		}

	}

	public static void markDefenderProvince(ProvinceHitArea province) {
		if (defenderProvinceHitArea != null) {
			defenderProvinceHitArea.deselectAsDefender();
		}
		defenderProvinceHitArea = province;
		if (defenderProvinceHitArea != null) {
			defenderProvinceHitArea.selectAsDefender();
			if (currentPlayer.isLocalPlayer()) {
				dicePanel.show();
			}
		} else {
			if (currentPlayer.isLocalPlayer()) {
				dicePanel.hide();
			}
		}
	}

	public static ProvinceHitArea getAttackerProvince() {
		return attackerProvinceHitArea;
	}

	public static void toss(int diceAmount) {
		Integer[] attackerDiceValues = null;
		Integer[] defenderDiceValues = null;
		if (defenderProvinceHitArea.getProvince().getTroops() == 1 || diceAmount == 1) {
			defenderDiceValues = Arrays.stream(Dice.DEFENCE_DICE_1.rollAllAndGetAll()).boxed().toArray(Integer[]::new);
		} else {
			defenderDiceValues = Arrays.stream(Dice.DEFENCE_DICE_2.rollAllAndGetAll()).boxed().toArray(Integer[]::new);
		}
		switch (diceAmount) {
			case 1:
				if (attackerProvinceHitArea.getProvince().getTroops() > 1) {
					attackerDiceValues = Arrays.stream(Dice.ATTACK_DICE_1.rollAllAndGetAll()).boxed().toArray(Integer[]::new);
				}
				break;
			case 2:
				if (attackerProvinceHitArea.getProvince().getTroops() > 2) {
					attackerDiceValues = Arrays.stream(Dice.ATTACK_DICE_2.rollAllAndGetAll()).boxed().toArray(Integer[]::new);
				}
				break;
			case 3:
				if (attackerProvinceHitArea.getProvince().getTroops() > 3) {
					attackerDiceValues = Arrays.stream(Dice.ATTACK_DICE_3.rollAllAndGetAll()).boxed().toArray(Integer[]::new);
				}
				break;
			default:
				throw new DiceException("diceAmount not in the set (1, 2, 3)");
		}
		if (attackerDiceValues != null) {
			Arrays.sort(attackerDiceValues, Collections.reverseOrder());
			Arrays.sort(defenderDiceValues, Collections.reverseOrder());
			int attackerCasualties = 0;
			int defenderCasualties = 0;
			for (int i = 0; i < Math.min(attackerDiceValues.length, defenderDiceValues.length); i++) {
				if (attackerDiceValues[i] > defenderDiceValues[i]) {
					defenderCasualties++;
				} else {
					attackerCasualties++;
				}
			}
			defenderProvinceHitArea.getProvince().removeTroops(defenderCasualties);
			attackerProvinceHitArea.getProvince().removeTroops(attackerCasualties);
			if (attackerProvinceHitArea.getProvince().getTroops() == 1) {
				markAttackerProvince(null);
				markDefenderProvince(null);
			}
			if (defenderProvinceHitArea.getProvince().getTroops() <= 0) {
				// capture
				defenderProvinceHitArea.getProvince().removeTroops(defenderProvinceHitArea.getProvince().getTroops() + 1);
				BottomPanel.updateSpinnerValues(diceAmount, attackerProvinceHitArea.getProvince().getTroops() - 1);
				occupyProvince(defenderProvinceHitArea.getProvince());
			}
		}
	}

	public static void blitz() {
		int attackerTroops = attackerProvinceHitArea.getProvince().getTroops();
		if (attackerTroops <= 1 || defenderProvinceHitArea.getProvince().getTroops() <= 0) {
			return;
		}
		switch (attackerTroops) {
			default:
				toss(3);
				break;
			case 3:
				toss(2);
				break;
			case 2:
				toss(1);
				break;
		}
		blitz();
	}

	private static void occupyProvince(Province occupied) {
		ProvinceConnector.getInstance().setPath(attackerProvinceHitArea, defenderProvinceHitArea);
		attackerProvinceHitArea.isSelected = false;
		defenderProvinceHitArea.isSelected = false;
		occupied.getOccupiedBy(currentPlayer);
		defenderProvinceHitArea.deemphasizeForAttack();
		moveAfterOccupySource = attackerProvinceHitArea;
		moveAfterOccupyDestination = defenderProvinceHitArea;
		markAttackerProvince(null);
		markDefenderProvince(null);
		BottomPanel.nextPhaseButton.disable();
	}

	public static ProvinceHitArea getReinforcingProvince() {
		return reinforcingProvince;
	}

	public static void markReinforcingProvince(ProvinceHitArea province) {
		if (reinforcingProvince != null) {
			reinforcingProvince.deselectAsReinforcing();
			Utils.connectedComponents(reinforcingProvince).forEach(hitArea -> {
				hitArea.deemphasizeForReinforcement();
			});
		}
		reinforcingProvince = province;
		if (reinforcingProvince != null) {
			reinforcingProvince.selectAsReinforcing();
			Utils.connectedComponents(reinforcingProvince).forEach(hitArea -> {
				hitArea.emphasizeForReinforcement();
			});
		}
	}

	public static ProvinceHitArea getReinforcedProvince() {
		return reinforcedProvince;
	}

	public static void markReinforcedProvince(ProvinceHitArea province) {
		if (reinforcedProvince != null) {
			reinforcedProvince.deselectAsReinforced();
		}
		reinforcedProvince = province;
		if (reinforcedProvince != null) {
			reinforcedProvince.selectAsReinforced();
			ProvinceConnector.getInstance().setPath(Utils.shortestPath(reinforcingProvince, reinforcedProvince));
			if (currentPlayer.isLocalPlayer()) {
				BottomPanel.updateSpinnerValues(1, reinforcingProvince.getProvince().getTroops() - 1);
			}
		} else {
			if (currentPlayer.isLocalPlayer()) {
				// hide spinner
			}
		}
	}

	public static void reinforce(int reinforcementCount) {
		if (reinforcingProvince != null && reinforcedProvince != null) {
			reinforcingProvince.getProvince().removeTroops(reinforcementCount);
			reinforcedProvince.getProvince().addTroops(reinforcementCount);
			Utils.connectedComponents(reinforcingProvince).forEach(p -> p.deemphasizeForReinforcement());
			ProvinceConnector.getInstance().setPath();
			reinforcingProvince.isSelected = false;
			reinforcedProvince.isSelected = false;
			nextPhase();
		}
	}

	private static void randomPlacement() {
		while (!Province.UNCLAIMED_PROVINCES.isEmpty()) {
			currentPlayer.endTurn();
			claimProvince(Province.getRandomUnclaimedProvince());
		}
		while (!startingTroops.values().stream().allMatch(v -> v == 0)) {
			List<Province> playerProvinces = Player.getPlayerProvinces(currentPlayer);
			currentPlayer.endTurn();
			draftReinforceProvince = playerProvinces.get(ThreadLocalRandom.current().nextInt(playerProvinces.size()));
			draftReinforce(1);
		}
	}

	public static void setDraftReinforceProvince(Province clickedProvince) {
		draftReinforceProvince = clickedProvince;
	}

	public static void moveTroopsAfterOccupying(int count) {
		// 1 is there because it was -1 before
		moveAfterOccupyDestination.getProvince().addTroops(1 + count);
		moveAfterOccupySource.getProvince().removeTroops(count);
		ProvinceConnector.getInstance().setPath();
	}
}