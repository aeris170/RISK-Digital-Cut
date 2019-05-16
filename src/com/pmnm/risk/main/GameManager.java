package com.pmnm.risk.main;

import java.awt.Color;
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
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.card.Card;
import com.pmnm.risk.dice.Dice;
import com.pmnm.risk.dice.exceptions.DiceException;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.map.board.ProvinceConnector;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.gameui.BottomPanel;
import com.pmnm.roy.ui.gameui.CardPanel;
import com.pmnm.roy.ui.gameui.DicePanel;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

public class GameManager extends DoaObject {

	private static final long serialVersionUID = -4928417050440420799L;

	public static GameManager INSTANCE;

	public final List<Player> players = new ArrayList<>();

	public int numberOfPlayers;
	public boolean manualPlacement = false;

	public boolean isManualPlacementDone = false;
	public final Map<Player, Integer> startingTroops = new HashMap<>();
	public int placementCounter = 0;

	public TurnPhase currentPhase = TurnPhase.DRAFT;
	public int reinforcementForThisTurn = 0;
	public Player currentPlayer;
	public int turnCount = 0;

	public ProvinceHitArea moveAfterOccupySource = null;
	public ProvinceHitArea moveAfterOccupyDestination = null;

	public static ProvinceHitArea attackerProvinceHitArea = null;
	public static ProvinceHitArea defenderProvinceHitArea = null;
	public static DicePanel dicePanel = RiskGameScreenUI.DicePanel;
	public static CardPanel cardPanel = RiskGameScreenUI.CardPanel;
	public static boolean cardWillBeGiven = false;

	public ProvinceHitArea reinforcingProvince = null;
	public ProvinceHitArea reinforcedProvince = null;

	public ProvinceHitArea clickedHitArea;

	private Province draftReinforceProvince = null;

	public String currentMapName;

	public float timer = 0;

	public GameManager(String mapName, List<String> playerNames, List<Color> playerColors, List<String> aiNames, List<Color> aiColors) {
		super(0f, 0f);
		if (INSTANCE != null) {
			DoaHandler.remove(INSTANCE);
		}
		currentMapName = mapName;
		numberOfPlayers = playerNames.size() + aiNames.size();
		int startingTroopCount = Player.findStartingTroopCount(numberOfPlayers);
		for (int i = 0; i < playerNames.size(); i++) {
			Player p = DoaHandler.instantiate(Player.class, playerNames.get(i), playerColors.get(i), true);
			players.add(p);
			startingTroops.put(p, startingTroopCount);
		}
		for (int i = 0; i < aiNames.size(); i++) {
			Player p = DoaHandler.instantiate(AIPlayer.class, aiNames.get(i), aiColors.get(i), 0);
			players.add(p);
			startingTroops.put(p, startingTroopCount);
		}
		/*
		 * for (int i = 0; i < numberOfPlayers; i++) { Player p =
		 * DoaHandler.instantiate(AIPlayer.class, "AIPlayer" + i,
		 * PlayerColorBank.get(i), i); players.add(p); startingTroops.put(p,
		 * startingTroopCount); }
		 */
		currentPlayer = players.get(0);
		currentPlayer.turn();
		if (!manualPlacement) {
			randomPlacement();
		}
		INSTANCE = this;
	}

	public void nextPhase() {
		if (currentPhase == TurnPhase.DRAFT) {
			currentPhase = TurnPhase.ATTACK;
			if (currentPlayer.isLocalPlayer()) {
				cardPanel.hide();
				BottomPanel.nextPhaseButton.enable();
			}
			BottomPanel.nullSpinner();
		} else if (currentPhase == TurnPhase.ATTACK) {
			currentPhase = TurnPhase.REINFORCE;
			markAttackerProvince(null);
			markDefenderProvince(null);
		} else if (currentPhase == TurnPhase.REINFORCE) {
			currentPhase = TurnPhase.DRAFT;
			if (cardWillBeGiven) {
				currentPlayer.addCard(Card.getRandomCard());
				cardWillBeGiven = false;
			}
			currentPlayer.endTurn();
			turnCount++;
			currentPlayer = players.get(turnCount % players.size());
			currentPlayer.turn();
			reinforcementForThisTurn = Player.calculateReinforcementsForThisTurn(currentPlayer);
			markReinforcingProvince(null);
			markReinforcedProvince(null);
			BottomPanel.updateSpinnerValues(1, reinforcementForThisTurn);
			BottomPanel.nextPhaseButton.disable();
			if (currentPlayer.isLocalPlayer()) {
				cardPanel.updateCards();
				cardPanel.show();
			}
			timer = 0;
		}
	}

	@Override
	public void tick() {
		if (DoaMouse.MB1) {
			clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.isMouseClicked())
					.findFirst().orElse(null);
		}
		if (!isManualPlacementDone) {
			if (startingTroops.values().stream().allMatch(v -> v <= 0)) {
				isManualPlacementDone = true;
				reinforcementForThisTurn = Player.calculateReinforcementsForThisTurn(currentPlayer);
				BottomPanel.updateSpinnerValues(1, reinforcementForThisTurn);
			}
		}
		timer += 0.2f;
		if(timer > (Main.WINDOW_WIDTH - DoaSprites.get("seasonCircle").getWidth()) / 2) {
			currentPhase = TurnPhase.DRAFT;
			if (cardWillBeGiven) {
				currentPlayer.addCard(Card.getRandomCard());
				cardWillBeGiven = false;
			}
			currentPlayer.endTurn();
			++turnCount;
			currentPlayer = players.get(turnCount % players.size());
			currentPlayer.turn();
			reinforcementForThisTurn = Player.calculateReinforcementsForThisTurn(currentPlayer);
			markReinforcingProvince(null);
			markReinforcedProvince(null);
			BottomPanel.updateSpinnerValues(1, reinforcementForThisTurn);
			BottomPanel.nextPhaseButton.disable();
			if (currentPlayer.isLocalPlayer()) {
				cardPanel.updateCards();
				cardPanel.show();
			}
			timer = 0;
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
	}

	public void claimProvince(Province claimed) {
		claimed.getClaimedBy(currentPlayer);
		startingTroops.put(currentPlayer, startingTroops.get(currentPlayer) - 1);
		currentPlayer = players.get(++placementCounter % players.size());
		currentPlayer.turn();
	}

	public void draftReinforce(int reinforcementCount) {
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

	public int numberOfReinforcementsForThisTurn() {
		return reinforcementForThisTurn;
	}

	public boolean areAllProvincesClaimed() {
		return Province.ALL_PROVINCES.stream().filter(province -> province.isClaimed())
				.count() == Province.ALL_PROVINCES.size();
	}

	public void markAttackerProvince(ProvinceHitArea province) {
		if (attackerProvinceHitArea != null) {
			ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream()
					.filter(hitArea -> attackerProvinceHitArea.getProvince().getNeighbours()
							.contains(hitArea.getProvince()) && !hitArea.getProvince().isOwnedBy(currentPlayer))
					.collect(Collectors.toList()).forEach(hitArea -> hitArea.deemphasizeForAttack());
			attackerProvinceHitArea.deselectAsAttacker();
		}
		attackerProvinceHitArea = province;
		if (attackerProvinceHitArea != null) {
			ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream()
					.filter(hitArea -> attackerProvinceHitArea.getProvince().getNeighbours()
							.contains(hitArea.getProvince()) && !hitArea.getProvince().isOwnedBy(currentPlayer))
					.collect(Collectors.toList()).forEach(hitArea -> hitArea.emphasizeForAttack());
			attackerProvinceHitArea.selectAsAttacker();
		}
	}

	public void markDefenderProvince(ProvinceHitArea province) {
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

	public ProvinceHitArea getAttackerProvince() {
		return attackerProvinceHitArea;
	}

	public void toss(int diceAmount) {
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
				attackerDiceValues = Arrays.stream(Dice.ATTACK_DICE_1.rollAllAndGetAll()).boxed()
						.toArray(Integer[]::new);
			}
			break;
		case 2:
			if (attackerProvinceHitArea.getProvince().getTroops() > 2) {
				attackerDiceValues = Arrays.stream(Dice.ATTACK_DICE_2.rollAllAndGetAll()).boxed()
						.toArray(Integer[]::new);
			}
			break;
		case 3:
			if (attackerProvinceHitArea.getProvince().getTroops() > 3) {
				attackerDiceValues = Arrays.stream(Dice.ATTACK_DICE_3.rollAllAndGetAll()).boxed()
						.toArray(Integer[]::new);
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
			if (defenderProvinceHitArea != null && defenderProvinceHitArea.getProvince().getTroops() <= 0) {
				// capture
				defenderProvinceHitArea.getProvince()
						.removeTroops(defenderProvinceHitArea.getProvince().getTroops() + 1);
				BottomPanel.updateSpinnerValues(diceAmount, attackerProvinceHitArea.getProvince().getTroops() - 1);
				occupyProvince(defenderProvinceHitArea.getProvince());
			}
		}
	}

	public void blitz() {
		if (attackerProvinceHitArea != null && defenderProvinceHitArea != null) {
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
	}

	private void occupyProvince(Province occupied) {
		ProvinceConnector.getInstance().setPath(attackerProvinceHitArea, defenderProvinceHitArea);
		attackerProvinceHitArea.isSelected = false;
		defenderProvinceHitArea.isSelected = false;
		occupied.getOccupiedBy(currentPlayer);
		defenderProvinceHitArea.deemphasizeForAttack();
		moveAfterOccupySource = attackerProvinceHitArea;
		moveAfterOccupyDestination = defenderProvinceHitArea;
		Player defender = defenderProvinceHitArea.getProvince().getOwner();
		if (!Player.hasProvinces(defender)) {
			defender.removeAllCards();
		}
		markAttackerProvince(null);
		markDefenderProvince(null);
		BottomPanel.nextPhaseButton.disable();
		cardWillBeGiven = true;
	}

	public ProvinceHitArea getReinforcingProvince() {
		return reinforcingProvince;
	}

	public void markReinforcingProvince(ProvinceHitArea province) {
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

	public ProvinceHitArea getReinforcedProvince() {
		return reinforcedProvince;
	}

	public void markReinforcedProvince(ProvinceHitArea province) {
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
			BottomPanel.nullSpinner();
			ProvinceConnector.getInstance().setPath();
		}
	}

	public void reinforce(int reinforcementCount) {
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

	private void randomPlacement() {
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

	public void setDraftReinforceProvince(Province clickedProvince) {
		draftReinforceProvince = clickedProvince;
	}

	public void moveTroopsAfterOccupying(int count) {
		// 1 is there because it was -1 before
		moveAfterOccupyDestination.getProvince().addTroops(1 + count);
		moveAfterOccupySource.getProvince().removeTroops(count);
		moveAfterOccupyDestination = null;
		moveAfterOccupySource = null;
		ProvinceConnector.getInstance().setPath();
	}
}
