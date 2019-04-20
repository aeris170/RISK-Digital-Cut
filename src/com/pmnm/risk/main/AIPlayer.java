package com.pmnm.risk.main;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class AIPlayer extends Player {

	public static final Map<String, Player> NAME_AI = new HashMap<>();

	public int difficulty;

	Province provinceToClaim;
	ProvinceHitArea areaToClick;

	public AIPlayer(String playerName, Color playerColor, int difficulty) {
		super(playerName, playerColor, false);
		this.difficulty = difficulty;
	}

	@Override
	public void tick() {
		if (isInTurn) {
			if (!GameManager.isManualPlacementDone) {
				if (!GameManager.areAllProvincesClaimed()) {
					Province provinceToClaim = Province.getRandomUnclaimedProvince();
					ProvinceHitArea areaToClick = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.getProvince().equals(provinceToClaim))
					        .findFirst().orElse(null);
					GameManager.claimProvince(provinceToClaim);
					isInTurn = false;
				} else {
					List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this).collect(Collectors.toList());
					Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
					GameManager.reinforce(p, 1);
					isInTurn = false;
				}
			} else if (GameManager.currentPhase == TurnPhase.DRAFT) {
				List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this).collect(Collectors.toList());
				Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
				GameManager.reinforce(p, 1);
			} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
				GameManager.nextPhase();
			} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
				GameManager.nextPhase();
			}
		}
	}
}