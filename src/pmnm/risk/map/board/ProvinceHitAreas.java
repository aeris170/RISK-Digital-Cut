package pmnm.risk.map.board;

import java.util.ArrayList;
import java.util.List;

import doa.engine.input.DoaMouse;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.NonNull;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.map.Mesh2D;
import pmnm.risk.map.Vertex2D;

public final class ProvinceHitAreas extends DoaObject {

	private static final long serialVersionUID = -2012137488480810514L;
	
	private IRiskGameContext context;
	private Iterable<@NonNull IProvince> provinces;
	
	private ProvinceHitArea highlighted;
	private ProvinceHitArea selected;
	private List<ProvinceHitArea> areas;
	
	public ProvinceHitAreas(IRiskGameContext context) {
		this.context = context;
		provinces = context.getProvinces();
		
		areas = new ArrayList<>();
		for (IProvince p : provinces) {
			areas.add(ProvinceHitArea.of(p));
		}
		
		addComponent(new ProvinceHitAreaHighlighter());
		addComponent(new ProvinceHitAreaSelector());
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
				if (highlighted != null) {
					highlighted.setHighlighted(false);
					highlighted = null;
				}
				highlighted = area;
				area.setHighlighted(highlight);
			}
		}
	}
	
	@SuppressWarnings("serial")
	private final class ProvinceHitAreaSelector extends DoaScript {

		@Override
		public void tick() {
			if (context.isPaused()) return;
			if (highlighted == null) return;
			
			if (DoaMouse.MB1) {
				if(selected != null) {
					selected.setSelected(false);
					selected = null;
				}
				selected = highlighted;
				highlighted.setSelected(true);
			}
		}
	}
}
