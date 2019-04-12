package com.pmnm.risk.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.pmnm.risk.exceptions.RiskException;
import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.map.province.Province;

public class Player {

	public static final Map<String, Player> NAME_PLAYER = new HashMap<>();

	private Set<Province> provinces = new HashSet<>();
	@SuppressWarnings("unused")
	private Set<Card> cards = new HashSet<>();
	private Map<Province, Integer> provinceTroops = new HashMap<>();
	private Color playerColor;
	private String playerName;

	public Player(String playerName, Color playerColor) {
		this.playerName = playerName;
		this.playerColor = playerColor;
		if (NAME_PLAYER.get(playerName) == null) {
			NAME_PLAYER.put(playerName, this);
		} else {
			throw new RiskException("Player names must be unique!");
		}
	}

	public static int findStartingTroopCount(int numberOfPlayers) {
		return 50 - 5 * numberOfPlayers;
	}

	public static int calculateReinforcementsForThisTurn(Player player) {
		int reinforcementsForThisTurn = player.provinces.size() / 3;
		for (Entry<String, Continent> entry : Continent.NAME_CONTINENT.entrySet()) {
			Continent currentContinent = entry.getValue();
			if (player.provinces.containsAll(currentContinent.getProvinces())) {
				reinforcementsForThisTurn += currentContinent.getCaptureBonus();
			}
		}
		return reinforcementsForThisTurn;
	}

	public Set<Province> getProvinces() {
		return provinces;
	}

	public boolean addProvince(Province p) {
		boolean rv = provinces.add(p);
		provinceTroops.put(p, 1);
		return rv;
	}

	public boolean removeProvince(Province p) {
		boolean rv = provinces.remove(p);
		provinceTroops.remove(p);
		return rv;
	}

	public int getTroopsIn(Province p) {
		return provinceTroops.get(p);
	}
	
	public void modifyProvinceTroopsBy(Province p, int amount) {
		provinceTroops.put(p, provinceTroops.get(p) + amount);
	}

	public String getName() {
		return playerName;
	}

	public Color getColor() {
		return playerColor;
	}
}
