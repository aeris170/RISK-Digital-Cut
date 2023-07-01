package com.pmnm.risk.main;

import java.awt.Color;

import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.game.databasedimpl.RiskGameContext;

public class AIPlayer extends Player {

	private static final long serialVersionUID = 2380077283511125497L;

	public AIPlayer(@NonNull final RiskGameContext context, @NonNull final Player.Data data) {
		super(context, data);
	}
	
	private int getDifficulty() { return ((AIPlayer.Data) data).difficulty; }
	
	public static class Data extends Player.Data {

		private static final long serialVersionUID = 7791868093951740396L;
		
		@Getter private final int difficulty;
		
		public Data(String name, Color color, String pawnKey, boolean isLocalPlayer, boolean isHumanPlayer, int difficulty) {
			super(name, color, pawnKey, isLocalPlayer, isHumanPlayer);
			this.difficulty = difficulty;
		}
	}
}