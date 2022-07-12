package pmnm.risk.game.databasedimpl;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.pmnm.risk.main.GameManager;

import doa.engine.input.DoaMouse;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import pmnm.risk.game.Conflict;
import pmnm.risk.game.Deploy;
import pmnm.risk.game.Dice;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.game.IRiskGameContext.TurnPhase;
import pmnm.risk.game.Reinforce;
import pmnm.risk.map.board.ProvinceHitArea;
import pmnm.risk.map.board.ProvinceHitAreas;

public class Player extends DoaObject implements IPlayer {

	private static final long serialVersionUID = 1411773994871441922L;

	private static int ID = 1;

	private RiskGameContext context;
	@Getter private int id;
	@Getter private String name;
	@Getter private Color color;
	@Getter private boolean isLocalPlayer;

	private Deploy lastDeploy;
	private Conflict lastConflict;
	private Reinforce lastReinforce;
	
	public Player(RiskGameContext context, String name, Color color, boolean isLocalPlayer) {
		this.context = context;
		this.id = ID++;
		this.name = name;
		this.color = color;
		this.isLocalPlayer = isLocalPlayer;
		
		addComponent(new MouseController());
	}

	@Override
	public void occupyProvince(final IProvince province) {
		context.occupyProvince(this, province);
	}
	
	@Override
	public Iterable<IProvince> getOccupiedProvinces() { return context.provincesOf(this); }

	@Override
	public void deployToProvince(IProvince province, int amount) {
		lastDeploy = context.setUpDeploy(province, amount);
		context.applyDeployResult(lastDeploy.calculateResult());
	}

	@Override
	public void attackToProvince(IProvince source, IProvince destination, Dice method) {
		lastConflict = context.setUpConflict(source, destination, method);
		context.applyConflictResult(lastConflict.calculateResult());
	}
	
	@Override
	public void reinforceProvince(final IProvince source, final IProvince destination, final int amount) {
		lastReinforce = context.setUpReinforce(source, destination, amount);
		context.applyReinforceResult(lastReinforce.calculateResult());
	}

	@Override
	public boolean isHumanPlayer() { return true; }
	
	@Override
	public void finishTurn() {
		context.
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
				} else if (province.isOccupiedBy(Player.this) && context.isEveryProvinceOccupied()) {
					deployToProvince(province, 1);
				}
				finishTurn();
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
}
