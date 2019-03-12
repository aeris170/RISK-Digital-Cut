package com.pmnm.risk.map.province;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.main.DebugPanel;
import com.pmnm.risk.toolkit.Utils;

public class ProvinceHitArea extends DoaObject {

	private static final long serialVersionUID = -6848368535793292243L;

	private Province owner;
	private List<GeneralPath> ownerMeshes = new ArrayList<>();
	private boolean isVisible = false;

	public ProvinceHitArea(Province owner, Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
		this.owner = owner;
		owner.getMeses().forEach(mesh -> {
			GeneralPath hitArea = new GeneralPath();
			DoaVectorI startPoint = mesh.get(0);
			hitArea.moveTo(startPoint.x, startPoint.y);
			for (int i = 1; i < mesh.size(); i++) {
				DoaVectorI nextPoint = mesh.get(i);
				hitArea.lineTo(nextPoint.x, nextPoint.y);
			}
			hitArea.closePath();
			ownerMeshes.add(hitArea);
		});
	}

	@Override
	public void tick() {
		DoaVectorF mappedMouseCoords = Utils.mapMouseCoordinatesByZoom();
		ownerMeshes.forEach(mesh -> {
			if (mesh.contains((int) mappedMouseCoords.x, (int) mappedMouseCoords.y)) {
				if (DoaMouse.MB1) {
					isVisible = !isVisible;
				}
				DebugPanel.mouseOnProvinceName = owner.getName();
			}
		});
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isVisible) {
			g.setColor(Color.MAGENTA);
			g.setStroke(new BasicStroke(2));
			for (GeneralPath gp : ownerMeshes) {
				g.draw(gp);
			}
		}
	}

	@Override
	public Shape getBounds() {
		return null;
	}
}
