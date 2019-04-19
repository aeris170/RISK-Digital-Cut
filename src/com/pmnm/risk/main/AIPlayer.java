package com.pmnm.risk.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class AIPlayer extends Player {

	public static final Map<String, Player> NAME_AI = new HashMap<>();
	private static int number = 1;
	
	public static int difficulty;
	private boolean isInTurn;
	private int id;
	private Robot robot;

	private Province source = null;
	private Province destination = null;
	
	public AIPlayer(String playerName, Color playerColor, int difficulty) {
		super(playerName, playerColor);
		this.difficulty = difficulty;
		id = number;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void tick() {
		if (isInTurn) {
			Province p = Province.getRandomUnclaimedProvince();
			
			/*
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
			}*/
		}
	}
}
