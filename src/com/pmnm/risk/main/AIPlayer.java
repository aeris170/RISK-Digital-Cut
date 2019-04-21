package com.pmnm.risk.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.engine.task.DoaTasker;
import com.doa.utils.DoaUtils;
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
					GameManager.claimProvince(provinceToClaim);
					isInTurn = false;
				} else {
					List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this)
							.collect(Collectors.toList());
					Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
					GameManager.reinforce(p, 1);
					isInTurn = false;
				}
			} else if (GameManager.currentPhase == TurnPhase.DRAFT) {
				List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this)
						.collect(Collectors.toList());
				Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
				if(provinces.size() == 2) {
					System.out.println("<3");
				}
				if (GameManager.numberOfReinforcementsForThisTurn() > 0) {
					GameManager.reinforce(p, 1);
				}
			} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
				if (difficulty == 0) {
					GameManager.nextPhase();
				} else if (difficulty == 1) {
					attackPhase(difficulty);
					GameManager.nextPhase();
				}
			} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
				GameManager.nextPhase();
			}
		}
	}

	public void attackPhase(int difficulty) {
		List<Province> ownedProvinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this)
				.collect(Collectors.toList());
		for (int i = 0; i < ownedProvinces.size(); i++) {
			if (ownedProvinces.get(i).troopCount() > 10) {
				List<Province> ownedProvinceNeighbours = ownedProvinces.get(i).getNeighbours().stream()
						.filter(p -> p.getOwner() != this).collect(Collectors.toList());
				for (int j = 0; j < ownedProvinceNeighbours.size(); j++) {
					if (ownedProvinces.get(i).troopCount() - ownedProvinceNeighbours.get(j).troopCount() > 5) {
						GameManager.markAttackerProvince(ownedProvinces.get(i).getProvinceHitArea());
						GameManager.markDefenderProvince(ownedProvinceNeighbours.get(j).getProvinceHitArea());
						GameManager.toss(3);
					}
				}
			}
		}
	}
}
