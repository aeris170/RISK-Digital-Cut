package com.pmnm.risk.map.province;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.List;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.main.DebugPanel;
import com.pmnm.risk.toolkit.Utils;

public class ProvinceHitArea extends DoaObject {

	private static final long serialVersionUID = -6848368535793292243L;

	private Province owner;
	private GeneralPath ownerHitBoxPath;

	public ProvinceHitArea(Province owner, Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
		this.owner = owner;
		ownerHitBoxPath = new GeneralPath();
		List<DoaVectorI> ownerHitBoxVertices = owner.getVertices();
		DoaVectorI startPoint = ownerHitBoxVertices.get(0);
		ownerHitBoxPath.moveTo(startPoint.x, startPoint.y);
		for (int i = 1; i < ownerHitBoxVertices.size(); i++) {
			DoaVectorI nextPoint = ownerHitBoxVertices.get(i);
			ownerHitBoxPath.lineTo(nextPoint.x, nextPoint.y);
		}
		ownerHitBoxPath.closePath();
	}

	@Override
	public void tick() {
		DoaVectorF mappedMouseCoords = Utils.mapMouseCoordinatesByZoom();
		if (getBounds().contains((int) mappedMouseCoords.x, (int) mappedMouseCoords.y)) {
			DebugPanel.mouseOnProvinceName = owner.getName();
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.setColor(Color.MAGENTA);
		g.setStroke(new BasicStroke(2));
		g.draw(ownerHitBoxPath);
	}

	@Override
	public Shape getBounds() {
		return ownerHitBoxPath;
	}
}
