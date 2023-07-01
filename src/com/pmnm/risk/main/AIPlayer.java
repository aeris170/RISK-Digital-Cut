package com.pmnm.risk.main;

import lombok.NonNull;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.game.databasedimpl.RiskGameContext;

public class AIPlayer extends Player {

	private static final long serialVersionUID = 2380077283511125497L;

	public int difficulty;

	public AIPlayer(String playerName, Color playerColor, int difficulty) {
		super(playerName, playerColor, false);
		this.difficulty = difficulty;
	}

	@Override
}