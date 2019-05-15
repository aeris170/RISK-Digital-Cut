package com.pmnm.risk.main;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import com.pmnm.risk.map.province.Province;

public class AIPlayer extends Player {

	private static final long serialVersionUID = 2380077283511125497L;

	public int difficulty;

	public AIPlayer(String playerName, Color playerColor, int difficulty) {
		super(playerName, playerColor, false);
		this.difficulty = difficulty;
	}

	@Override
	public void tick() {
		GameManager gm = GameManager.INSTANCE;
		if (isInTurn) {
			if (!gm.isManualPlacementDone) {
				if (!gm.areAllProvincesClaimed()) {
					if (difficulty <= 0 || difficulty == 1) {
						Province provinceToClaim = Province.getRandomUnclaimedProvince();
						gm.claimProvince(provinceToClaim);
						isInTurn = false;
					} else if (difficulty == 2) {
						if (getPlayerProvinces(this).isEmpty()) {
							Province provinceToClaim = Province.getRandomUnclaimedProvince();
							gm.claimProvince(provinceToClaim);
							//claimedProvince = provinceToClaim;
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
						gm.setDraftReinforceProvince(p);
						gm.draftReinforce(1);
						isInTurn = false;
					}
				}
			} else if (gm.currentPhase == TurnPhase.DRAFT) {
				if (difficulty <= 0 || difficulty == 1) {
					List<Province> provinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this).collect(Collectors.toList());
					Province p = provinces.get(ThreadLocalRandom.current().nextInt(provinces.size()));
					gm.setDraftReinforceProvince(p);
					gm.draftReinforce(1);
				}
			} else if (gm.currentPhase == TurnPhase.ATTACK) {
				attack();
			} else if (gm.currentPhase == TurnPhase.REINFORCE) {
				gm.nextPhase();
			}
		}
	}

	public void attack() {
		GameManager gm = GameManager.INSTANCE;
		List<Province> ownedProvinces = Province.ALL_PROVINCES.stream().filter(p -> p.getOwner() == this).collect(Collectors.toList());
		if (difficulty == 1) {
			for (int i = 0; i < ownedProvinces.size(); i++) {
				if (ownedProvinces.get(i).troopCount() > 10) {
					List<Province> ownedProvinceNeighbours = ownedProvinces.get(i).getNeighbours().stream()
							.filter(p -> p.getOwner() != this).collect(Collectors.toList());
					for (int j = 0; j < ownedProvinceNeighbours.size(); j++) {
						if (ownedProvinces.get(i).troopCount() - ownedProvinceNeighbours.get(j).troopCount() > 5) {
							gm.markAttackerProvince(ownedProvinces.get(i).getProvinceHitArea());
							gm.markDefenderProvince(ownedProvinceNeighbours.get(j).getProvinceHitArea());
							gm.blitz();
							if (ownedProvinces.get(i).troopCount() > 3) {
								gm.moveTroopsAfterOccupying(3);
							} else {
								gm.moveTroopsAfterOccupying(1);
							}
						}
					}
				}
			}
		}
		gm.nextPhase();
	}
}
