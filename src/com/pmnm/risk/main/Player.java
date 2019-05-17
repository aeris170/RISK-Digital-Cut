package com.pmnm.risk.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.card.Card;
import com.pmnm.risk.exceptions.RiskException;
import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class Player extends DoaObject {

	private static final long serialVersionUID = 1411773994871441922L;

	public static Map<String, Player> NAME_PLAYER = new HashMap<>();

	private static int number = 1;

	private Color playerColor;
	private String playerName;
	protected boolean isInTurn;
	private int id;
	private boolean isLocalPlayer;
	private List<Card> cards = new ArrayList<>();

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
		if(!GameManager.INSTANCE.isPaused) {
			if (isInTurn && isLocalPlayer) {
				ProvinceHitArea clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.isMouseClicked()).findFirst().orElse(null);
				if (clickedHitArea != null) {
					Province clickedProvince = clickedHitArea.getProvince();
					GameManager gm = GameManager.INSTANCE;
					if (!gm.isManualPlacementDone) {
						if (!clickedProvince.isClaimed()) {
							gm.claimProvince(clickedProvince);
							isInTurn = false;
						} else if (clickedProvince.isOwnedBy(this) && gm.areAllProvincesClaimed()) {
							gm.setDraftReinforceProvince(clickedProvince);
							gm.draftReinforce(1);
							isInTurn = false;
						}
					} else if (gm.currentPhase == TurnPhase.DRAFT) {
						if (gm.numberOfReinforcementsForThisTurn() > 0 && clickedProvince.isOwnedBy(this)) {
							gm.setDraftReinforceProvince(clickedProvince);
						}
					} else if (gm.currentPhase == TurnPhase.ATTACK) {
						if (clickedProvince.isOwnedBy(this) && clickedProvince.getTroops() > 1 && gm.moveAfterOccupySource == null) {
							gm.markAttackerProvince(clickedHitArea);
							gm.markDefenderProvince(null);
						} else if (gm.getAttackerProvince() != null && !clickedProvince.isOwnedBy(this)
						        && gm.getAttackerProvince().getProvince().getNeighbours().contains(clickedProvince)) {
							gm.markDefenderProvince(clickedHitArea);
						}
					} else if (gm.currentPhase == TurnPhase.REINFORCE) {
						if (clickedProvince.isOwnedBy(this)) {
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
			if (DoaMouse.MB3) {
				cards.forEach(c -> System.out.println(c.toString()));
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {}

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

	public void endTurn() {
		isInTurn = false;
	}

	public boolean isLocalPlayer() {
		return isLocalPlayer;
	}
	
	public List<Card> getCards() {//TODO find a better solution to not fuck up the encapsulation
		return cards;
	}

	public void addCard(Card c) {
		cards.add(c);
	}

	public void removeCard(Card c) {
		cards.remove(c);
	}
	
	public void removeAllCards() {
		for(int i = 0; i < cards.size(); i++) {
			cards.remove(i);
		}
	}

	@Override
	public String toString() {
		return "Player [playerColor=" + playerColor + ", playerName=" + playerName + ", isInTurn=" + isInTurn + ", id=" + id + ", isLocalPlayer=" + isLocalPlayer
		        + ", source=" + source + ", destination=" + destination + "]";
	}
}
