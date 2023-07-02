package com.pmnm.risk.main;

import java.awt.Color;
import java.util.List;

import doa.engine.maths.DoaMath;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.game.databasedimpl.RiskGameContext;
import pmnm.risk.map.board.ProvinceHitArea;
import pmnm.risk.map.board.ProvinceHitAreas;

public class AIPlayer extends Player {

	private static final long serialVersionUID = 2380077283511125497L;

	public AIPlayer(@NonNull final RiskGameContext context, @NonNull final Player.Data data) {
		super(context, data);
		
		addComponent(new AIController());
	}
	
	private int getDifficulty() { return ((AIPlayer.Data) data).difficulty; }
	
	public class AIController extends DoaScript {

		private static final long serialVersionUID = -4006899545069105835L;
		
		@Override
		public void tick() {
			if (context.isPaused()) return;
			if (itIsNotMyTurn()) return;

			List<ProvinceHitArea> areas = context.getAreas().getAreas();

			IProvince province = null;

			/* Step 1, initial placement */
			if (!context.isInitialPlacementComplete()) {
				/* If every province is not occupied, occupy a random unoccupied province.
				 * Otherwise, deploy 1 troop to a random province which is occupied by me.
				 */
				if(!context.isEveryProvinceOccupied()) {
					List<ProvinceHitArea> unoccupiedProvinces = areas.stream().filter(p -> p.getProvince().getOccupier() == null).toList();
					int randomIndex = DoaMath.randomIntBetween(0, unoccupiedProvinces.size());
					province = unoccupiedProvinces.get(randomIndex).getProvince();
					occupyProvince(province);
				} else {
					List<ProvinceHitArea> myProvinces = areas.stream().filter(p -> p.getProvince().isOccupiedBy(AIPlayer.this)).toList();
					int randomIndex = DoaMath.randomIntBetween(0, myProvinces.size());
					province = (IProvince) myProvinces.get(randomIndex).getProvince();
					deployToProvince(province, 1);
				}
				
				return;
			}
		}
	}
	
	public static class Data extends Player.Data {

		private static final long serialVersionUID = 7791868093951740396L;
		
		@Getter private final int difficulty;
		
		public Data(String name, Color color, String pawnKey, boolean isLocalPlayer, boolean isHumanPlayer, int difficulty) {
			super(name, color, pawnKey, isLocalPlayer, isHumanPlayer);
			this.difficulty = difficulty;
		}
	}
}