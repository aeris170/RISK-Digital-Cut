package com.pmnm.risk.main;

import lombok.NonNull;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.game.databasedimpl.RiskGameContext;

public class AIPlayer extends Player {

	private static final long serialVersionUID = 2380077283511125497L;

	public int difficulty;

	public AIPlayer(@NonNull final RiskGameContext context, @NonNull final Player.Data data, int difficulty) {
		super(context, data);
		this.difficulty = difficulty;
	}

	@Override
}