package com.pmnm.risk.main;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.input.DoaMouse;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.scripts.DoaScript;
import pmnm.risk.game.databasedimpl.Continent;
import pmnm.risk.map.board.ProvinceHitArea;
import pmnm.risk.map.province.Province;

public class Player extends DoaObject {

	private static final long serialVersionUID = 1411773994871441922L;

	public static Map<String, Player> NAME_PLAYER = new HashMap<>();

	private static int number = 1;

	private Color playerColor;
	private String playerName;
	protected boolean isInTurn;
	private int id;
	private boolean isLocalPlayer;

	private Province source = null;
	private Province destination = null;

	public Player(String playerName, Color playerColor, boolean isLocalPlayer) {
		this.playerName = playerName;
		this.playerColor = playerColor;
		id = number;
		if (NAME_PLAYER.get(playerName) == null) {
			NAME_PLAYER.put(playerName, this);
		} else {
			throw new RuntimeException("Player names must be unique!");
		}
		number++;
		this.isLocalPlayer = isLocalPlayer;
		
		addComponent(new MouseController());
	}
	
	public class MouseController extends DoaScript {

		private static final long serialVersionUID = 5822331332308267554L;

		@Override
		public void tick() {
			if (!GameManager.INSTANCE.isPaused && GameManager.INSTANCE.isSinglePlayer) {
				if (!GameManager.INSTANCE.isPaused) {
					if (isInTurn && isLocalPlayer) {
						ProvinceHitArea clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.getcisMouseClicked()).findFirst().orElse(null);
						if (clickedHitArea != null) {
							Province clickedProvince = clickedHitArea.getProvince();
							GameManager gm = GameManager.INSTANCE;
							if (!gm.isManualPlacementDone) {
								if (!clickedProvince.isClaimed()) {
									gm.claimProvince(clickedProvince);
									isInTurn = false;
								} else if (clickedProvince.isOwnedBy(Player.this) && gm.areAllProvincesClaimed()) {
									gm.setDraftReinforceProvince(clickedProvince);
									gm.draftReinforce(1);
									isInTurn = false;
								}
							} else if (gm.currentPhase == TurnPhase.DRAFT) {
								if (gm.numberOfReinforcementsForThisTurn() > 0 && clickedProvince.isOwnedBy(Player.this)) {
									gm.setDraftReinforceProvince(clickedProvince);
								}
							} else if (gm.currentPhase == TurnPhase.ATTACK) {
								if (clickedProvince.isOwnedBy(Player.this) && clickedProvince.getTroops() > 1 && gm.moveAfterOccupySource == null) {
									gm.markAttackerProvince(clickedHitArea);
									gm.markDefenderProvince(null);
								} else if (gm.getAttackerProvince() != null && !clickedProvince.isOwnedBy(Player.this)
								        && gm.getAttackerProvince().getProvince().getNeighbours().contains(clickedProvince)) {
									gm.markDefenderProvince(clickedHitArea);
								}
							} else if (gm.currentPhase == TurnPhase.REINFORCE) {
								if (clickedProvince.isOwnedBy(Player.this)) {
									if (clickedProvince.getTroops() > 1 && gm.getReinforcingProvince() == null) {
										gm.markReinforcingProvince(clickedHitArea);
									} else if (gm.getReinforcingProvince() != null && destination == null) {
										gm.markReinforcedProvince(clickedHitArea);
									}
								}
							}
						} else if (DoaMouse.MB1) {
							GameManager gm = GameManager.INSTANCE;
							gm.clickedHitArea = null;
							if (gm.currentPhase == TurnPhase.DRAFT) {
								gm.setDraftReinforceProvince(null);
							} else if (gm.currentPhase == TurnPhase.ATTACK) {
								gm.markAttackerProvince(null);
								gm.markDefenderProvince(null);
							} else if (gm.currentPhase == TurnPhase.REINFORCE) {
								gm.markReinforcingProvince(null);
								gm.markReinforcedProvince(null);
							}
							if (ProvinceHitArea.selectedProvinceByMouse != null) {
								ProvinceHitArea.selectedProvinceByMouse.isSelected = false;
								ProvinceHitArea.selectedProvinceByMouse = null;
							}
						}
					}
				}
			}
		}
	}

	public void turn() {
		isInTurn = true;
	}

	public boolean isTurn() {
		return isInTurn;
	}

	public String getName() {
		return playerName;
	}

	public Color getColor() {
		return playerColor;
	}

	public void setColor(Color pColor) {
		playerColor = pColor;
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

	public static boolean hasProvinces(Player player) {
		List<Province> playerProvinces = Province.ALL_PROVINCES.stream().filter(province -> province.isOwnedBy(player)).collect(Collectors.toList());
		return !playerProvinces.isEmpty();
	}

	// TODO DOA OPTIMIZE
	public static List<Province> getPlayerProvinces(Player player) {
		return Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == player).collect(Collectors.toList());
	}

	public static void resetIDs() {
		number = 1;
	}

	public void endTurn() {
		isInTurn = false;
	}

	public boolean isLocalPlayer() {
		return isLocalPlayer;
	}

	@Override
	public String toString() {
		return "Player [playerColor=" + playerColor + ", playerName=" + playerName + ", isInTurn=" + isInTurn + ", id=" + id + ", isLocalPlayer=" + isLocalPlayer + ", source="
		        + source + ", destination=" + destination + "]";
	}
}
