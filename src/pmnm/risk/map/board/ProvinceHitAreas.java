package pmnm.risk.map.board;

import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.Scenes;

import doa.engine.input.DoaMouse;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.map.Mesh2D;
import pmnm.risk.map.Vertex2D;

public final class ProvinceHitAreas extends DoaObject {

	private static final long serialVersionUID = -2012137488480810514L;
	
	private IRiskGameContext context;
	private Iterable<@NonNull IProvince> provinces;
	
	@Getter	private ProvinceHitArea highlightedProvince;
	@Getter	private ProvinceHitArea selectedProvince;
	
	@Getter private ProvinceHitArea attackerProvince;
	@Getter private ProvinceHitArea defenderProvince;
	
	@Getter private ProvinceHitArea reinforcingProvince;
	@Getter private ProvinceHitArea reinforceeProvince;
	
	@Getter	private List<ProvinceHitArea> areas;
	
	public ProvinceHitAreas(IRiskGameContext context) {
		this.context = context;
		provinces = context.getProvinces();
		
		areas = new ArrayList<>();
		for (IProvince p : provinces) {
			areas.add(ProvinceHitArea.of(p));
		}
		
		addComponent(new ProvinceHitAreaHighlighter());
		addComponent(new ProvinceHitAreaSelector());
		
		Scenes.GAME_SCENE.add(this);
	}
	
	@Override
	public void onAddToScene(DoaScene scene) {
		super.onAddToScene(scene);
		for (ProvinceHitArea pha : areas) {
			scene.add(pha);
		}
	}
	
	@Override
	public void onRemoveFromScene(DoaScene scene) {
		super.onRemoveFromScene(scene);
		for (ProvinceHitArea pha : areas) {
			scene.remove(pha);
		}
	}
	
	public void selectAttackerProvinceAs(IProvince province) {
		if(attackerProvince != null) {
			attackerProvince.deselectAsAttacker();
		}
		attackerProvince = findHitAreaOf(province);
		attackerProvince.selectAsAttacker();
	}
	
	public void selectDefenderProvinceAs(IProvince province) {
		if(defenderProvince != null) {
			defenderProvince.deselectAsDefender();
		}
		defenderProvince = findHitAreaOf(province);
		defenderProvince.selectAsDefender();
	}
	
	public void selectReinforcingProvinceAs(IProvince province) {
		if(reinforcingProvince != null) {
			reinforcingProvince.deselectAsReinforcing();
		}
		reinforcingProvince = findHitAreaOf(province);
		reinforcingProvince.deselectAsReinforcing();
	}
	
	public void selectReinforceeProvinceAs(IProvince province) {
		if(defenderProvince != null) {
			defenderProvince.deselectAsReinforced();
		}
		defenderProvince = findHitAreaOf(province);
		defenderProvince.deselectAsReinforced();
	}
	
	public ProvinceHitArea findHitAreaOf(@NonNull IProvince province) {
		return areas.stream().filter(area -> area.getProvince().equals(province)).findFirst().get();
	}

	@SuppressWarnings("serial")
	private final class ProvinceHitAreaHighlighter extends DoaScript {

		@Override
		public void tick() {
			if (context.isPaused()) return;
			
			for (ProvinceHitArea area : areas) {
				boolean highlight = false;
				
				Iterable<@NonNull Mesh2D> meshes = area.getProvince().getMeshes();
				for (Mesh2D mesh : meshes) {
					if (mesh.encasesPoint(new Vertex2D((int) DoaMouse.X, (int) DoaMouse.Y))) {
						highlight = true;
						break;
					}
				}
				if (highlightedProvince != null) {
					highlightedProvince.setHighlighted(false);
					highlightedProvince = null;
				}
				highlightedProvince = area;
				area.setHighlighted(highlight);
			}
		}
	}
	
	@SuppressWarnings("serial")
	private final class ProvinceHitAreaSelector extends DoaScript {

		@Override
		public void tick() {
			if (context.isPaused()) return;
			
			if (DoaMouse.MB1) {
				if (highlightedProvince != null) {
					if (selectedProvince != null) {
						selectedProvince.setSelected(false);
						selectedProvince = null;
					}
					selectedProvince = highlightedProvince;
					selectedProvince.setSelected(true);
				} else {
					selectedProvince.setSelected(false);
					selectedProvince = null;
				}
			}
		}
	}
}
