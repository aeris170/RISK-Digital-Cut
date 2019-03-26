package com.pmnm.risk.map.province;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
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
	private boolean isPathVisible = true;
	private boolean isPointsVisible = false;

	public ProvinceHitArea(Province owner, Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
		this.owner = owner;
		owner.getMeshes().forEach(mesh -> {
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
				if (DoaMouse.MB2) {
					isPathVisible = !isPathVisible;
				}
				if (DoaMouse.MB3) {
					isPointsVisible = !isPointsVisible;
				}
				DebugPanel.mouseOnProvinceName = owner.getName();
			}
		});
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isPathVisible) {
			g.setColor(Color.WHITE);
			for (GeneralPath gp : ownerMeshes) {
				g.fill(gp);
			}
			g.setColor(owner.getContinent().getColor());
			for (GeneralPath gp : ownerMeshes) {
				g.draw(gp);
			}
		}
		if (isPointsVisible) {
			g.setColor(Color.MAGENTA);
			for (GeneralPath gp : ownerMeshes) {
				double[][] points = getPoints(gp);
				for (int i = 0; i < points.length; i++) {
					g.fillRect(points[i][0] - 1, points[i][1] - 1, 1, 1);
				}
			}
		}
	}

	@Override
	public Shape getBounds() {
		return null;
	}

	// https://stackoverflow.com/questions/5803111/obtain-ordered-vertices-of-generalpath
	// by finnw
	private static double[][] getPoints(Path2D path) {
		List<double[]> pointList = new ArrayList<>();
		double[] coords = new double[6];
		int numSubPaths = 0;
		for (PathIterator pi = path.getPathIterator(null); !pi.isDone(); pi.next()) {
			switch (pi.currentSegment(coords)) {
				case PathIterator.SEG_MOVETO:
					pointList.add(Arrays.copyOf(coords, 2));
					++numSubPaths;
					break;
				case PathIterator.SEG_LINETO:
					pointList.add(Arrays.copyOf(coords, 2));
					break;
				case PathIterator.SEG_CLOSE:
					if (numSubPaths > 1) {
						throw new IllegalArgumentException("Path contains multiple subpaths");
					}
					return pointList.toArray(new double[pointList.size()][]);
				default:
					throw new IllegalArgumentException("Path contains curves");
			}
		}
		throw new IllegalArgumentException("Unclosed path");
	}
}
