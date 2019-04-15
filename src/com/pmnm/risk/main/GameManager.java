package com.pmnm.risk.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class GameManager extends DoaObject {

	private static final long serialVersionUID = -4928417050440420799L;

	public static final List<Player> players = new ArrayList<>();
	public static int numberOfPlayers = 6;

	public static boolean isManualPlacementDone = false;
	public static final Map<Player, Integer> startingTroops = new HashMap<>();

	public static TurnPhase currentPhase = TurnPhase.DRAFT;
	public static int reinforcementForThisTurn = 0;
	public static Player currentPlayer;
	public static int turnCount = 0;

	public static ProvinceHitArea attackerProvinceHitArea = null;
	public static ProvinceHitArea defenderProvinceHitArea = null;

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
	}

	public static void nextPhase() {
		if (currentPhase == TurnPhase.DRAFT) {
			currentPhase = TurnPhase.ATTACK;
		} else if (currentPhase == TurnPhase.ATTACK) {
			currentPhase = TurnPhase.REINFORCE;
		} else if (currentPhase == TurnPhase.REINFORCE) {
			currentPhase = TurnPhase.DRAFT;
		}
	}

	@Override
	public void tick() {
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

	public static void occupyProvince(Province occupied) {
		occupied.getOccupiedBy(currentPlayer);
		startingTroops.put(currentPlayer, startingTroops.get(currentPlayer) - 1);
		currentPlayer = players.get(++turnCount % players.size());
		currentPlayer.turn();
	}

	public static void reinforce(Province reinforced, int reinforcementCount) {
		reinforced.addTroops(reinforcementCount);
		if (!isManualPlacementDone) {
			startingTroops.put(currentPlayer, startingTroops.get(currentPlayer) - reinforcementCount);
			currentPlayer = players.get(++turnCount % players.size());
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

	public static boolean areAllProvincesCaptured() {
		return Province.NAME_PROVINCE.values().stream().filter(province -> province.isOccupied()).count() == Province.NAME_PROVINCE.size();
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
		}
	}

	public static ProvinceHitArea getAttackerProvince() {
		return attackerProvinceHitArea;
	}
}