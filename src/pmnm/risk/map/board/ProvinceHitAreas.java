package pmnm.risk.map.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.game.IRiskGameContext.TurnPhase;
import pmnm.risk.map.Mesh2D;
import pmnm.risk.map.Vertex2D;

public final class ProvinceHitAreas extends DoaObject {

	private static final long serialVersionUID = -2012137488480810514L;
	
	private IRiskGameContext context;
	private Iterable<@NonNull IProvince> provinces;
	
	@Getter private ProvinceHitArea highlightedProvince;
	@Getter private ProvinceHitArea selectedProvince;
	
	@Getter private ProvinceHitArea attackerProvince;
	@Getter private ProvinceHitArea defenderProvince;
	
	@Getter private ProvinceHitArea reinforcingProvince;
	@Getter private ProvinceHitArea reinforceeProvince;
	
	@Getter private List<ProvinceHitArea> areas;
	@Getter private ProvinceConnector connector;
	
	public ProvinceHitAreas(IRiskGameContext context) {
		this.context = context;
		provinces = context.getProvinces();
		
		areas = new ArrayList<>();
		for (IProvince p : provinces) {
			areas.add(ProvinceHitArea.of(p));
		}
		
		connector = new ProvinceConnector(context);
		
		addComponent(new ProvinceHitAreaHighlighter());
		addComponent(new ProvinceHitAreaSelector());
	}
	
	@Override
	public void onAddToScene(DoaScene scene) {
		super.onAddToScene(scene);
		for (ProvinceHitArea pha : areas) {
			scene.add(pha);
		}
		scene.add(connector);
	}
	
	@Override
	public void onRemoveFromScene(DoaScene scene) {
		super.onRemoveFromScene(scene);
		for (ProvinceHitArea pha : areas) {
			scene.remove(pha);
		}
		scene.remove(connector);
	}
	
	public void dehighlightHighlightedProvince() {
		if (highlightedProvince != null) {
			highlightedProvince.setHighlighted(false);
			highlightedProvince = null;
		}
	}
	
	public void deselectSelectedProvince() {
		if (selectedProvince != null) {
			selectedProvince.setSelected(false);
			selectedProvince = null;
		}
	}
	
	public void selectAttackerProvinceAs(IProvince province) {
		if(attackerProvince != null) {
			attackerProvince.deselectAsAttacker();
			findAllNeighborsWithDifferentOccupier(attackerProvince).forEach(ProvinceHitArea::deemphasizeForAttack);
		}
		attackerProvince = findHitAreaOf(province);
		if(attackerProvince != null) {
			attackerProvince.selectAsAttacker();
			findAllNeighborsWithDifferentOccupier(attackerProvince).forEach(ProvinceHitArea::emphasizeForAttack);
		}
	}
	
	public void selectDefenderProvinceAs(IProvince province) {
		if(defenderProvince != null) {
			defenderProvince.deselectAsDefender();
		}
		defenderProvince = findHitAreaOf(province);
		if(defenderProvince != null) {
			defenderProvince.selectAsDefender();
		}
	}
	
	public void selectReinforcingProvinceAs(IProvince province) {
		if(reinforcingProvince != null) {
			reinforcingProvince.deselectAsReinforcing();
			findAllNeighborsWithSameOccupier(reinforcingProvince).forEach(ProvinceHitArea::deemphasizeForReinforcement);
		}
		reinforcingProvince = findHitAreaOf(province);
		if(reinforcingProvince != null) {
			reinforcingProvince.deselectAsReinforcing();
			findAllNeighborsWithSameOccupier(reinforcingProvince).forEach(ProvinceHitArea::emphasizeForReinforcement);
		}
	}
	
	public void selectReinforceeProvinceAs(IProvince province) {
		if(reinforceeProvince != null) {
			reinforceeProvince.deselectAsReinforced();
		}
		reinforceeProvince = findHitAreaOf(province);
		if(reinforceeProvince != null) {
			reinforceeProvince.selectAsReinforced();
		}
	}
	
	public void resetAll() {
		deselectSelectedProvince();
		selectAttackerProvinceAs(null);
		selectDefenderProvinceAs(null);
		selectReinforcingProvinceAs(null);
		selectReinforceeProvinceAs(null);
		connector.setPath((ProvinceHitArea[])null);
		areas.forEach(area -> {
			area.deemphasizeForAttack();
			area.deemphasizeForReinforcement();
		});
	}
	
	public ProvinceHitArea findHitAreaOf(IProvince province) {
		if (province == null) return null;
		return areas.stream().filter(area -> area.getProvince().equals(province)).findFirst().orElseThrow();
	}

	// Functions regarding neighbor queries
	private Iterable<ProvinceHitArea> findAllNeighborsWithSameOccupier(ProvinceHitArea province) {
		if (province == null) return Collections.emptyList();
		return connectedComponents(province, p -> p.isOccupiedBy(province.getProvince().getOccupier()));
	}
	private Iterable<ProvinceHitArea> findAllNeighborsWithDifferentOccupier(ProvinceHitArea province) {
		if (province == null) return Collections.emptyList();
		return connectedComponents(province, p -> !p.isOccupiedBy(province.getProvince().getOccupier()));
	}
	private Iterable<ProvinceHitArea> connectedComponents(ProvinceHitArea province, Predicate<IProvince> filter) {
		List<ProvinceHitArea> connectedComponents = new ArrayList<>();
		connectedComponents.add(province);
		explodeFrom(province, connectedComponents, filter);
		return connectedComponents;
	}
	private void explodeFrom(ProvinceHitArea provinceHitArea, List<ProvinceHitArea> connectedComponents, Predicate<IProvince> filter) {
		IProvince province = provinceHitArea.getProvince();
		for (IProvince p : province.getNeighbors()) {
			if (filter.test(p)) {
				ProvinceHitArea pHitArea = findHitAreaOf(p);
				if (!connectedComponents.contains(pHitArea)) {
					connectedComponents.add(pHitArea);
					explodeFrom(pHitArea, connectedComponents, filter);
				}
			}
		}
	}
	// Functions regarding neighbor queries

	@SuppressWarnings("serial")
	private final class ProvinceHitAreaHighlighter extends DoaScript {

		@Override
		public void tick() {
			if (context.isPaused()) return;

			Vertex2D mousePoint = new Vertex2D(
				DoaGraphicsFunctions.warpX(DoaMouse.X),
				DoaGraphicsFunctions.warpY(DoaMouse.Y)
			);
			ProvinceHitArea candidate = null;
	 outer: for (ProvinceHitArea area : areas) {
				Iterable<@NonNull Mesh2D> meshes = area.getProvince().getMeshes();
				for (Mesh2D mesh : meshes) {
					if (mesh.encasesPoint(mousePoint)) {
						candidate = area;
						break outer;
					}
				}
			}
	 		if (candidate == null) {
	 			dehighlightHighlightedProvince();
	 		} else if (candidate != highlightedProvince) {
	 			dehighlightHighlightedProvince();
				highlightedProvince = candidate;
				candidate.setHighlighted(true);
	 		}
		}
	}
	
	@SuppressWarnings("serial")
	private final class ProvinceHitAreaSelector extends DoaScript {

		@Override
		public void tick() {
			if (context.isPaused()) return;
			if (context.getCurrentPhase() == TurnPhase.ATTACK_DEPLOY) { return; }

			if (DoaMouse.MB1_RELEASE) {
				deselectSelectedProvince();
				if (highlightedProvince != null && highlightedProvince != selectedProvince) {
					selectedProvince = highlightedProvince;
					selectedProvince.setSelected(true);
				}
			}
		}
	}
}
