package pmnm.risk.game.databasedimpl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import doa.engine.graphics.DoaSprites;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pmnm.risk.game.Conflict;
import pmnm.risk.game.Deploy;
import pmnm.risk.game.Dice;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext.TurnPhase;
import pmnm.risk.game.Reinforce;
import pmnm.risk.map.board.ProvinceHitArea;
import pmnm.risk.map.board.ProvinceHitAreas;

public class Player extends DoaObject implements IPlayer {

	private static final long serialVersionUID = 1411773994871441922L;

	private static int ID = 1;

	private RiskGameContext context;
	@Getter private int id;
	@NonNull private Player.Data data;

	private Deploy lastDeploy;
	private Conflict lastConflict;
	private Reinforce lastReinforce;
	
	public Player(@NonNull final RiskGameContext context, @NonNull final Player.Data data) {
		this.context = context;
		this.id = ID++;
		this.data = data;
		
		addComponent(new MouseController());
	}

	@Override
	public String getName() { return data.getName(); }

	@Override
	public Color getColor() { return data.getColor(); }

	@Override
	public boolean isLocalPlayer() { return data.isLocalPlayer(); }

	@Override
	public void occupyProvince(final IProvince province) {
		if (itIsNotMyTurn()) return;
		context.occupyProvince(this, province);
		deployToProvince(province, 1);
	}
	
	@Override
	public Iterable<IProvince> getOccupiedProvinces() { return context.provincesOf(this); }

	@Override
	public void deployToProvince(IProvince province, int amount) {
		if (itIsNotMyTurn()) return;
		lastDeploy = context.setUpDeploy(province, amount);
		context.applyDeployResult(lastDeploy.calculateResult());
	}

	@Override
	public void attackToProvince(IProvince source, IProvince destination, Dice method) {
		if (itIsNotMyTurn()) return;
		lastConflict = context.setUpConflict(source, destination, method);
		context.applyConflictResult(lastConflict.calculateResult());
	}
	
	@Override
	public void reinforceProvince(final IProvince source, final IProvince destination, final int amount) {
		if (itIsNotMyTurn()) return;
		lastReinforce = context.setUpReinforce(source, destination, amount);
		context.applyReinforceResult(lastReinforce.calculateResult());
	}

	@Override
	public boolean isHumanPlayer() { return true; }
	
	@Override
	public void finishTurn() {
		if (itIsNotMyTurn()) return;
		context.finishCurrentPlayerTurn();
	}
	
	public class MouseController extends DoaScript {

		private static final long serialVersionUID = 5822331332308267554L;

		private IProvince attackerProvince;

		private IProvince reinforcingProvince;

		@Override
		public void tick() {
			if (context.isPaused()) return;
			if (itIsNotMyTurn()) return;
			
			ProvinceHitAreas areas = context.getAreas();
			ProvinceHitArea selected = areas.getSelectedProvince();
			if(selected == null) { 
				attackerProvince = null;
				reinforcingProvince = null;
				return;
			}

			IProvince province = selected.getProvince();

			/* Step 1, initial placement */
			if (!context.isInitialPlacementComplete()) {
				/* if province is not occupied, occupy it. otherwise if it is occupied by me, deploy 1 troop */
				if (!province.isOccupied()) {
					occupyProvince(province);
					areas.deselectSelectedProvince();
					finishTurn();
				} else if (province.isOccupiedBy(Player.this) && context.isEveryProvinceOccupied()) {
					deployToProvince(province, 1);
					areas.deselectSelectedProvince();
					finishTurn();
				}
				return;
			}

			/* Step 2, playing the game */
			TurnPhase currentPhase = context.getCurrentPhase();
			if (currentPhase == TurnPhase.ATTACK) {
				if (province.isOccupiedBy(Player.this) && province.canLaunchAttack() && attackerProvince == null) {
					attackerProvince = province;
					areas.selectAttackerProvinceAs(province);
					areas.selectDefenderProvinceAs(null);
				} else if (attackerProvince != null && !province.isOccupiedBy(Player.this) && province.isNeighborOf(attackerProvince)) {
					areas.selectDefenderProvinceAs(province);
				}
				return;
			}

			if (currentPhase == TurnPhase.REINFORCE && province.isOccupiedBy(Player.this)) {
				if (province.canReinforceAnotherProvince() && reinforcingProvince == null) {
					reinforcingProvince = province;
					areas.selectReinforcingProvinceAs(province);
				} else if (reinforcingProvince != null) {
					areas.selectReinforceeProvinceAs(province);
				}
				return;
			}
		}
	}
	
	private boolean itIsNotMyTurn() { return !equals(context.getCurrentPlayer()); }
	
	@RequiredArgsConstructor
	public static class Data implements Serializable {

		private static final long serialVersionUID = 8565091325727904597L;
		
		@Getter @NonNull private final String name;
		@Getter @NonNull private final Color color;
		@NonNull private final String pawnKey;
		@Getter private final boolean isLocalPlayer;
		private transient BufferedImage pawn;
		
		public BufferedImage getPawn() {
			if (pawn == null) {
				pawn = DoaSprites.getSprite(pawnKey);
			}
			return pawn;
		}
	}
}
