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
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;
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

	public static ProvinceHitArea clickedHitArea;

	public GameManager() {
		super(0f, 0f);
		int startingTroopCount = Player.findStartingTroopCount(numberOfPlayers);
		for (int i = 0; i < numberOfPlayers; i++) {
			Player p = DoaHandler.instantiateDoaObject(Player.class, "Player" + i, PlayerColorBank.get(i));
			players.add(p);
			startingTroops.put(p, startingTroopCount);
		}
		currentPlayer = players.get(0);
		currentPlayer.turn();
		if (!manualPlacement) {
			randomPlacement();
		}
	}

	public static void nextPhase() {
		if (currentPhase == TurnPhase.DRAFT) {
			currentPhase = TurnPhase.ATTACK;
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
		}
	}

	@Override
	public void tick() {
		if (DoaMouse.MB1) {
			clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.isMouseClicked()).findFirst().orElse(null);
		}
		if (!isManualPlacementDone) {
			/* System.out.println("Game started!"); players = new Player[numberOfPlayers];
			 * for(int i = 0; i < numberOfPlayers; i++) { players[i] = new Player("Player" +
			 * (i + 1), Color.BLACK); } turns = new Player[numberOfPlayers]; Dice
			 * beginningDice = Dice.randomlyGenerate(numberOfPlayers); int[]
			 * beginninggDiceValues = beginningDice.getAllValues(); boolean[]
			 * valuesFinalized = new boolean[numberOfPlayers];
			 * System.out.println(Arrays.toString(beginninggDiceValues));
			 * System.out.println(Arrays.toString(valuesFinalized)); int turnsDetermined =
			 * 0; int max = 0; int numberOfMax = 0; ArrayList<Integer> indicesOfMax = new
			 * ArrayList<>(); while(turnsDetermined < numberOfPlayers) { for(int i = 0; i <
			 * numberOfPlayers; i++) { if(beginninggDiceValues[i] > max &&
			 * valuesFinalized[i] == false) { max = beginninggDiceValues[i]; } } for(int i =
			 * 0; i < numberOfPlayers; i++) { if(beginninggDiceValues[i] == max) {
			 * indicesOfMax.add(i); numberOfMax++; } } if(numberOfMax == 1) {
			 * turns[turnsDetermined] = players[indicesOfMax.get(0)];
			 * valuesFinalized[indicesOfMax.get(0)] = true; turnsDetermined++; } else
			 * if(numberOfMax > 1) { } max = 0; numberOfMax = 0; indicesOfMax.clear(); }
			 * for(int i = 0; i < numberOfPlayers; i++) {
			 * System.out.print(turns[i].getName() + ", "); } System.out.println(); */
			// numberOfOccupiedProvinces = 0;
			if (startingTroops.values().stream().allMatch(v -> v == 0)) {
				isManualPlacementDone = true;
				reinforcementForThisTurn = Player.calculateReinforcementsForThisTurn(currentPlayer);
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

	public static void reinforce(Province reinforced, int reinforcementCount) {
		reinforced.addTroops(reinforcementCount);
		if (!isManualPlacementDone) {
			startingTroops.put(currentPlayer, startingTroops.get(currentPlayer) - reinforcementCount);
			currentPlayer = players.get(++placementCounter % players.size());
			currentPlayer.turn();
		} else {
			reinforcementForThisTurn -= reinforcementCount;
			if (reinforcementForThisTurn == 0) {
				nextPhase();
			}
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
			attackerProvinceHitArea.deselectAsAttacker();
			attackerProvinceHitArea.setzOrder(DoaObject.GAME_OBJECTS);
			ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(
			        hitArea -> attackerProvinceHitArea.getProvince().getNeighbours().contains(hitArea.getProvince()) && !hitArea.getProvince().isOwnedBy(currentPlayer))
			        .collect(Collectors.toList()).forEach(hitArea -> {
				        hitArea.deemphasizeForAttack();
				        hitArea.setzOrder(DoaObject.GAME_OBJECTS);
			        });
		}
		attackerProvinceHitArea = province;
		if (attackerProvinceHitArea != null) {
			attackerProvinceHitArea.selectAsAttacker();
			attackerProvinceHitArea.setzOrder(DoaObject.FRONT);
			ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(
			        hitArea -> attackerProvinceHitArea.getProvince().getNeighbours().contains(hitArea.getProvince()) && !hitArea.getProvince().isOwnedBy(currentPlayer))
			        .collect(Collectors.toList()).forEach(hitArea -> {
				        hitArea.emphasizeForAttack();
				        hitArea.setzOrder(DoaObject.FRONT);
			        });
		}

	}

	public static void markDefenderProvince(ProvinceHitArea province) {
		if (defenderProvinceHitArea != null) {
			defenderProvinceHitArea.deselectAsDefender();
			defenderProvinceHitArea.setzOrder(DoaObject.GAME_OBJECTS);
		}
		defenderProvinceHitArea = province;
		if (defenderProvinceHitArea != null) {
			defenderProvinceHitArea.selectAsDefender();
			defenderProvinceHitArea.setzOrder(DoaObject.FRONT);
			dicePanel.show();
		} else {
			dicePanel.hide();
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
			int defenderTroopCount = defenderProvinceHitArea.getProvince().getTroops();
			if (defenderTroopCount > defenderCasualties) {
				defenderProvinceHitArea.getProvince().removeTroops(defenderCasualties);
			} else {
				defenderProvinceHitArea.getProvince().removeTroops(defenderTroopCount);
			}
			attackerProvinceHitArea.getProvince().removeTroops(attackerCasualties);
			if (attackerProvinceHitArea.getProvince().getTroops() == 1) {
				markAttackerProvince(null);
				markDefenderProvince(null);
			}
			if (defenderProvinceHitArea.getProvince().getTroops() <= 0) {
				// capture
				int remainingTroops = attackerProvinceHitArea.getProvince().getTroops();
				if (remainingTroops - diceAmount > 1) {
					defenderProvinceHitArea.getProvince().addTroops(diceAmount);
					attackerProvinceHitArea.getProvince().removeTroops(diceAmount);
				} else if (remainingTroops - diceAmount <= 1 && remainingTroops > 1) {
					defenderProvinceHitArea.getProvince().addTroops(remainingTroops - 1);
					attackerProvinceHitArea.getProvince().removeTroops(remainingTroops - 1);
				}
				// the attacking province cannot both win and have only 1 troop left... right?
				occupyProvince(defenderProvinceHitArea.getProvince(), diceAmount);
			}
		} else {
			// dice cannot be thrown because province didn't have enough troop
		}
	}

	private static void occupyProvince(Province occupied, int invadingTroopCount) {
		occupied.getOccupiedBy(currentPlayer, invadingTroopCount);
		defenderProvinceHitArea.deemphasizeForAttack();
		markAttackerProvince(null);
		markDefenderProvince(null);
	}

	private static void randomPlacement() {
		while (!Province.UNCLAIMED_PROVINCES.isEmpty()) {
			currentPlayer.endTurn();
			claimProvince(Province.getRandomUnclaimedProvince());
		}
		while (!startingTroops.values().stream().allMatch(v -> v == 0)) {
			List<Province> playerProvinces = Player.getPlayerProvinces(currentPlayer);
			currentPlayer.endTurn();
			reinforce(playerProvinces.get(ThreadLocalRandom.current().nextInt(playerProvinces.size())), 1);
		}
	}
}