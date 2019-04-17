package com.pmnm.risk.main;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.exceptions.RiskException;
import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class Player extends DoaObject {

	private static final long serialVersionUID = 1411773994871441922L;

	public static final Map<String, Player> NAME_PLAYER = new HashMap<>();
	private static int number = 1;

	private Color playerColor;
	private String playerName;
	private boolean isInTurn;
	private int id;

	private Province source = null;
	private Province destination = null;

	public Player(String playerName, Color playerColor) {
		super(0f, 0f);
		this.playerName = playerName;
		this.playerColor = playerColor;
		id = number;
		if (NAME_PLAYER.get(playerName) == null) {
			NAME_PLAYER.put(playerName, this);
		} else {
			throw new RiskException("Player names must be unique!");
		}
		number++;
	}

	@Override
	public void tick() {
		if (isInTurn) {
			ProvinceHitArea clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.isMouseClicked()).findFirst().orElse(null);
			if (clickedHitArea != null) {
				Province clickedProvince = clickedHitArea.getProvince();
				if (!GameManager.isManualPlacementDone) {
					if (!clickedProvince.isOccupied()) {
						GameManager.occupyProvince(clickedProvince);
						isInTurn = false;
					} else if (clickedProvince.isOwnedBy(this) && GameManager.areAllProvincesCaptured()) {
						GameManager.reinforce(clickedProvince, 1);
						isInTurn = false;
					}
				} else if (GameManager.currentPhase == TurnPhase.DRAFT) {
					if (GameManager.numberOfReinforcementsForThisTurn() > 0 && clickedProvince.isOwnedBy(this)) {
						GameManager.reinforce(clickedProvince, 1);
					}
				} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
					if (clickedProvince.isOwnedBy(this) && clickedProvince.getTroops() > 1) {
						GameManager.markAttackerProvince(clickedHitArea);
						GameManager.markDefenderProvince(null);
					} else if (GameManager.getAttackerProvince() != null && !clickedProvince.isOwnedBy(this)
					        && GameManager.getAttackerProvince().getProvince().getNeighbours().contains(clickedProvince)) {
						GameManager.markDefenderProvince(clickedHitArea);
					}
				} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
					if (source == null) {} else if (destination == null) {}
				}
			} else if (DoaMouse.MB1 && GameManager.currentPhase == TurnPhase.ATTACK) {
				GameManager.markAttackerProvince(null);
				GameManager.markDefenderProvince(null);
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {}

	public void turn() {
		isInTurn = true;
	}

	public String getName() {
		return playerName;
	}

	public Color getColor() {
		return playerColor;
	}

	public int getID() {
		return id;
	}

	public static int findStartingTroopCount(int numberOfPlayers) {
		return 50 - 5 * numberOfPlayers;
	}

	public static int calculateReinforcementsForThisTurn(Player player) {
		List<Province> playerProvinces = Province.NAME_PROVINCE.values().stream().filter(province -> province.isOwnedBy(player)).collect(Collectors.toList());
		int reinforcementsForThisTurn = playerProvinces.size() / 3;
		for (Entry<String, Continent> entry : Continent.NAME_CONTINENT.entrySet()) {
			Continent currentContinent = entry.getValue();
			if (playerProvinces.containsAll(currentContinent.getProvinces())) {
				reinforcementsForThisTurn += currentContinent.getCaptureBonus();
			}
		}
		return reinforcementsForThisTurn;
	}
}
