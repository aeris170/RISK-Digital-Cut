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
import pmnm.risk.game.Reinforce;
import pmnm.risk.map.board.ProvinceHitArea;

public class Player extends DoaObject implements IPlayer {

	private static final long serialVersionUID = 1411773994871441922L;

	private static int ID = 1;

	private IRiskGameContext context;
	@Getter private int id;
	@Getter private String name;
	@Getter private Color color;
	@Getter private boolean isLocalPlayer;

	private Deploy deploy;
	private Conflict conflict;
	private Reinforce reinforce;
	
	public Player(IRiskGameContext context, String name, Color color, boolean isLocalPlayer) {
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
		deploy = context.setUpDeploy(province, amount);
		context.applyDeployResult(deploy.calculateResult());
	}

	@Override
	public void attackToProvince(IProvince source, IProvince destination, Dice method) {
		conflict = context.setUpConflict(source, destination, method);
		context.applyConflictResult(conflict.calculateResult());
	}
	
	@Override
	public void reinforceProvince(final IProvince source, final IProvince destination, final int amount) {
		reinforce = context.setUpReinforce(source, destination, amount);
		context.applyReinforceResult(reinforce.calculateResult());
	}

	@Override
	public boolean isHumanPlayer() { return true; }
	
	public class MouseController extends DoaScript {

		private static final long serialVersionUID = 5822331332308267554L;

		@Override
		public void tick() {
			if (!GameManager.INSTANCE.isPaused && GameManager.INSTANCE.isSinglePlayer) {
				if (!GameManager.INSTANCE.isPaused) {
					if (isInTurn && isLocalPlayer) {
						ProvinceHitArea clickedHitArea = ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.getcisMouseClicked()).findFirst().orElse(null);
						if (clickedHitArea != null) {
							Province clickedProvince = clickedHitArea.getProvince();
							GameManager gm = GameManager.INSTANCE;
							if (!gm.isManualPlacementDone) {
								if (!clickedProvince.isClaimed()) {
									gm.claimProvince(clickedProvince);
									isInTurn = false;
								} else if (clickedProvince.isOwnedBy(Player.this) && gm.areAllProvincesClaimed()) {
									gm.setDraftReinforceProvince(clickedProvince);
									gm.draftReinforce(1);
									isInTurn = false;
								}
							} else if (gm.currentPhase == TurnPhase.DRAFT) {
								if (gm.numberOfReinforcementsForThisTurn() > 0 && clickedProvince.isOwnedBy(Player.this)) {
									gm.setDraftReinforceProvince(clickedProvince);
								}
							} else if (gm.currentPhase == TurnPhase.ATTACK) {
								if (clickedProvince.isOwnedBy(Player.this) && clickedProvince.getTroops() > 1 && gm.moveAfterOccupySource == null) {
									gm.markAttackerProvince(clickedHitArea);
									gm.markDefenderProvince(null);
								} else if (gm.getAttackerProvince() != null && !clickedProvince.isOwnedBy(Player.this)
								        && gm.getAttackerProvince().getProvince().getNeighbours().contains(clickedProvince)) {
									gm.markDefenderProvince(clickedHitArea);
								}
							} else if (gm.currentPhase == TurnPhase.REINFORCE) {
								if (clickedProvince.isOwnedBy(Player.this)) {
									if (clickedProvince.getTroops() > 1 && gm.getReinforcingProvince() == null) {
										gm.markReinforcingProvince(clickedHitArea);
									} else if (gm.getReinforcingProvince() != null && destination == null) {
										gm.markReinforcedProvince(clickedHitArea);
									}
								}
							}
						} else if (DoaMouse.MB1) {
							GameManager gm = GameManager.INSTANCE;
							gm.clickedHitArea = null;
							if (gm.currentPhase == TurnPhase.DRAFT) {
								gm.setDraftReinforceProvince(null);
							} else if (gm.currentPhase == TurnPhase.ATTACK) {
								gm.markAttackerProvince(null);
								gm.markDefenderProvince(null);
							} else if (gm.currentPhase == TurnPhase.REINFORCE) {
								gm.markReinforcingProvince(null);
								gm.markReinforcedProvince(null);
							}
							if (ProvinceHitArea.selectedProvinceByMouse != null) {
								ProvinceHitArea.selectedProvinceByMouse.isSelected = false;
								ProvinceHitArea.selectedProvinceByMouse = null;
							}
						}
					}
				}
			}
		}
	}

}
