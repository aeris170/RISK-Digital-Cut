package com.pmnm.risk.main;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import com.pmnm.risk.map.province.Province;

public class AIPlayer extends Player {

	public static final Map<String, Player> NAME_AI = new HashMap<>();
	public int difficulty;

	public AIPlayer(String playerName, Color playerColor, int difficulty) {
		super(playerName, playerColor, false);
		this.difficulty = difficulty;
	}

	@Override
	public void tick() {
		if (isInTurn) {
			if (!GameManager.isManualPlacementDone) {
				if (!GameManager.areAllProvincesClaimed()) {
					if (difficulty <= 0 || difficulty == 1) {
						Province provinceToClaim = Province.getRandomUnclaimedProvince();
						GameManager.claimProvince(provinceToClaim);
						isInTurn = false;
					} else if (difficulty == 2) {
						if (getPlayerProvinces(this).isEmpty()) {
							Province provinceToClaim = Province.getRandomUnclaimedProvince();
							GameManager.claimProvince(provinceToClaim);
							isInTurn = false;
						} else {
							// TODO - Simge - medium ai i
						}
					}
				} else {
					if (difficulty <= 2) {
						List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this)
								.collect(Collectors.toList());
						Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
						GameManager.setDraftReinforceProvince(p);
						GameManager.draftReinforce(1);
						isInTurn = false;
					}
				}
			} else if (GameManager.currentPhase == TurnPhase.DRAFT) {
				if (difficulty <= 2) {
					List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this)
							.collect(Collectors.toList());
					Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
					GameManager.setDraftReinforceProvince(p);
					GameManager.draftReinforce(1);
				}
			} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
				attack();
			} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
				if(difficulty == 2) {
					// TODO - Simge - medium ai reinforce
				}
				GameManager.nextPhase();
			}
		}
	}

	public void attack() {
		List<Province> ownedProvinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this)
				.collect(Collectors.toList());
		if (difficulty == 1 || difficulty == 2) {
			for (int i = 0; i < ownedProvinces.size(); i++) {
				if (ownedProvinces.get(i).troopCount() > 10) {
					List<Province> ownedProvinceNeighbours = ownedProvinces.get(i).getNeighbours().stream()
							.filter(p -> p.getOwner() != this).collect(Collectors.toList());
					for (int j = 0; j < ownedProvinceNeighbours.size(); j++) {
						if (ownedProvinces.get(i).troopCount() - ownedProvinceNeighbours.get(j).troopCount() > 5) {
							GameManager.markAttackerProvince(ownedProvinces.get(i).getProvinceHitArea());
							GameManager.markDefenderProvince(ownedProvinceNeighbours.get(j).getProvinceHitArea());
							GameManager.blitz();
							if (ownedProvinces.get(i).troopCount() > 3) {
								GameManager.moveTroopsAfterOccupying(3);
							} else {
								GameManager.moveTroopsAfterOccupying(1);
							}
						}
					}
				}
			}
		}
		GameManager.nextPhase();
	}
}