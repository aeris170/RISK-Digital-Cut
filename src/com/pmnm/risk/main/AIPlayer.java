package com.pmnm.risk.main;

import java.awt.Color;
import java.util.List;

import com.pmnm.risk.globals.Globals;

import doa.engine.maths.DoaMath;
import doa.engine.scene.DoaRoutine;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext.TurnPhase;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.game.databasedimpl.RiskGameContext;
import pmnm.risk.map.board.ProvinceHitArea;

public class AIPlayer extends Player {

	private static final long serialVersionUID = 2380077283511125497L;

	public AIPlayer(@NonNull final RiskGameContext context, @NonNull final Player.Data data) {
		super(context, data);
		
		addComponent(new AIController());
	}
	
	private int getDifficulty() { return ((AIPlayer.Data) data).difficulty; }
	
	public class AIController extends DoaScript {

		private static final long serialVersionUID = -4006899545069105835L;
		
		private boolean wait = false;

		private List<ProvinceHitArea> getProvinceHitAreas() {
			return context.getAreas().getAreas();
		}
		
		private List<ProvinceHitArea> getUnoccupiedProvinceHitAreas() {
			return getProvinceHitAreas().stream().filter(p -> p.getProvince().getOccupier() == null).toList();
		}
		
		private List<ProvinceHitArea> getMyProvinceHitAreas() {
			return getProvinceHitAreas().stream().filter(p -> p.getProvince().isOccupiedBy(AIPlayer.this)).toList();
		}
		
		private IProvince chooseRandomProvince(List<ProvinceHitArea> provinces) {
			int randomIndex = DoaMath.randomIntBetween(0, provinces.size());
			return provinces.get(randomIndex).getProvince();
		}
		
		private void delayedMove(DoaRoutine r) {
			wait = true;
			float randomFloat = DoaMath.randomBetween(.3f, .8f);
			doAfterTicks(() -> {
				r.doJob();
				wait = false;
			}, (int) (Globals.TICK_RATE * randomFloat));
		}

		@Override
		public void tick() {
			if (context.isPaused()) return;
			if (itIsNotMyTurn()) return;
			if (wait) return;

			/* Step 1, initial placement */
			if (!context.isInitialPlacementComplete()) {
				/* If every province is not occupied, occupy a random unoccupied province.
				 * Otherwise, deploy 1 troop to a random province which is occupied by me.
				 */
				
				if (!context.isEveryProvinceOccupied()) {
					List<ProvinceHitArea> unoccupiedProvinces = getUnoccupiedProvinceHitAreas();
					IProvince province = chooseRandomProvince(unoccupiedProvinces);

					delayedMove(() -> { occupyProvince(province); });
				} else {
					List<ProvinceHitArea> myProvinces = getMyProvinceHitAreas();
					IProvince province = chooseRandomProvince(myProvinces);
					
					delayedMove(() -> { deployToProvince(province, 1); });
				}
				
				return;
			}
			
			/* Step 2, playing the game */
			TurnPhase currentPhase = context.getCurrentPhase();
			
			if (currentPhase == TurnPhase.DRAFT) {
				List<ProvinceHitArea> myProvinces = getMyProvinceHitAreas();
				IProvince province = chooseRandomProvince(myProvinces);
				
				delayedMove(() -> { deployToProvince(province, context.getRemainingDeploys()); });
				return;
			}
			
			if (currentPhase == TurnPhase.ATTACK) {
				delayedMove(() -> { context.goToNextPhase(); });
				return;
			}
			
			if (currentPhase == TurnPhase.REINFORCE) {
				delayedMove(() -> { context.goToNextPhase(); });
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