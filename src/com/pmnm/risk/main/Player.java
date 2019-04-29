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
	protected boolean isInTurn;
	private int id;
	private boolean isLocalPlayer;

	private Province source = null;
	private Province destination = null;

	public Player(String playerName, Color playerColor, boolean isLocalPlayer) {
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
		this.isLocalPlayer = isLocalPlayer;
	}

	@Override
	public void tick() {
		if (isInTurn && isLocalPlayer) {
			ProvinceHitArea clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.isMouseClicked()).findFirst().orElse(null);
			if (clickedHitArea != null) {
				Province clickedProvince = clickedHitArea.getProvince();
				if (!GameManager.isManualPlacementDone) {
					if (!clickedProvince.isClaimed()) {
						GameManager.claimProvince(clickedProvince);
						isInTurn = false;
					} else if (clickedProvince.isOwnedBy(this) && GameManager.areAllProvincesClaimed()) {
						GameManager.setDraftReinforceProvince(clickedProvince);
						GameManager.draftReinforce(1);
						isInTurn = false;
					}
				} else if (GameManager.currentPhase == TurnPhase.DRAFT) {
					if (GameManager.numberOfReinforcementsForThisTurn() > 0 && clickedProvince.isOwnedBy(this)) {
						GameManager.setDraftReinforceProvince(clickedProvince);
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
					if (clickedProvince.isOwnedBy(this)) {
						if (clickedProvince.getTroops() > 1 && GameManager.getReinforcingProvince() == null) {
							GameManager.markReinforcingProvince(clickedHitArea);
						} else if (destination == null) {
							GameManager.markReinforcedProvince(clickedHitArea);
						}
					}
				}
			} else if (DoaMouse.MB1) {
				GameManager.clickedHitArea = null;
				if (GameManager.currentPhase == TurnPhase.DRAFT) {
					GameManager.setDraftReinforceProvince(null);
				} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
					GameManager.markAttackerProvince(null);
					GameManager.markDefenderProvince(null);
				} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
					GameManager.markReinforcingProvince(null);
					GameManager.markReinforcedProvince(null);
				}
				ProvinceHitArea.selectedProvinceByMouse.isSelected = false;
				ProvinceHitArea.selectedProvinceByMouse = null;
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
		List<Province> playerProvinces = Province.ALL_PROVINCES.stream().filter(province -> province.isOwnedBy(player)).collect(Collectors.toList());
		int reinforcementsForThisTurn = Math.max(playerProvinces.size() / 3, 3);
		for (Entry<String, Continent> entry : Continent.NAME_CONTINENT.entrySet()) {
			Continent currentContinent = entry.getValue();
			if (playerProvinces.containsAll(currentContinent.getProvinces())) {
				reinforcementsForThisTurn += currentContinent.getCaptureBonus();
			}
		}
		return reinforcementsForThisTurn;
	}

	public static List<Province> getPlayerProvinces(Player player) {
		return Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == player).collect(Collectors.toList());
	}

	public void endTurn() {
		isInTurn = false;
	}

	public boolean isLocalPlayer() {
		return isLocalPlayer;
	}
}
