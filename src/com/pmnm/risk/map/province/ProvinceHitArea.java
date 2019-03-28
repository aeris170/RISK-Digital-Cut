package com.pmnm.risk.map.province;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.main.DebugPanel;
import com.pmnm.risk.toolkit.Utils;

public class ProvinceHitArea extends DoaObject {

	private static final long serialVersionUID = -6848368535793292243L;

	private static final Map<RenderingHints.Key, Object> HINTS = new HashMap<>();
	static {
		HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		HINTS.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	private Province owner;
	private List<GeneralPath> ownerMeshes = new ArrayList<>();
	private transient BufferedImage cachedMesh;
	private boolean isPathVisible = true;
	private boolean isPointsVisible = false;

	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;

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
		cacheMeshAsImage();
	}

	@Override
	public void tick() {
		DoaVectorF mappedMouseCoords = Utils.mapMouseCoordinatesByZoom();
		ownerMeshes.forEach(mesh -> {
			if (mesh.contains((int) mappedMouseCoords.x, (int) mappedMouseCoords.y)) {
				if (DoaMouse.MB1) {
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
			g.drawImage(cachedMesh, minX - 4d, minY - 4d);
			/* g.setStroke(new BasicStroke(2)); for (GeneralPath gp : ownerMeshes) {
			 * g.setColor(new Color(90, 90, 90)); g.fill(gp);
			 * g.setColor(owner.getContinent().getColor()); g.draw(gp); } */
		}
		if (isPointsVisible) {
			g.setColor(Color.MAGENTA);
			for (GeneralPath gp : ownerMeshes) {
				double[][] points = getPoints(gp);
				for (int i = 0; i < points.length; i++) {
					g.fillRect(points[i][0], points[i][1], 1, 1);
				}
			}
		}
	}

	private void cacheMeshAsImage() {
		for (GeneralPath mesh : ownerMeshes) {
			double[][] vertices = getPoints(mesh);
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i][0] < minX) {
					minX = (int) vertices[i][0];
				}
				if (vertices[i][0] > maxX) {
					maxX = (int) vertices[i][0];
				}
				if (vertices[i][1] < minY) {
					minY = (int) vertices[i][1];
				}
				if (vertices[i][1] > maxY) {
					maxY = (int) vertices[i][1];
				}
			}
		}
		Color borderColor = owner.getContinent().getColor();
		Color fillColor = new Color(borderColor.getRed() / 3, borderColor.getGreen() / 3, borderColor.getBlue() / 3);
		cachedMesh = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
		cachedMesh.setAccelerationPriority(1);
		Graphics2D meshRenderer = cachedMesh.createGraphics();
		meshRenderer.translate(-minX + 4, -minY + 4);
		meshRenderer.setRenderingHints(HINTS);
		meshRenderer.setStroke(new BasicStroke(2));
		for (GeneralPath gp : ownerMeshes) {
			meshRenderer.setColor(fillColor);
			meshRenderer.fill(gp);
			meshRenderer.setColor(borderColor);
			meshRenderer.draw(gp);
		}
		meshRenderer.dispose();
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
