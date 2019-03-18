package com.pmnm.risk.main;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pmnm.risk.exceptions.RiskException;
import com.pmnm.risk.map.province.Province;

public class Player {

	public static final Map<String, Player> NAME_PLAYER = new HashMap<>();

	private Set<Province> provinces = new HashSet<>();
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

	public Set<Province> getProvinces() {
		return provinces;
	}

	public boolean addProvince(Province p) {
		return provinces.add(p);
	}

	public boolean removeProvince(Province p) {
		return provinces.remove(p);
	}

	public int getTroopsIn(Province p) {
		return provinceTroops.get(p);
	}

	public String getName() {
		return playerName;
	}

	public Color getColor() {
		return playerColor;
	}
}
