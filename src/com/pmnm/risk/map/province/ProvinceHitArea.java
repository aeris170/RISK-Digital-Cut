package com.pmnm.risk.map.province;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
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
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.risk.ui.UIInit;

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

	public static final List<ProvinceHitArea> ALL_PROVINCE_HIT_AREAS = new ArrayList<>();

	private Province province;
	private List<GeneralPath> meshes = new ArrayList<>();

	private transient BufferedImage unoccupiedMesh;
	private Map<Color, BufferedImage> playerOwnedMeshes = new HashMap<>();
	private transient BufferedImage selectedBorder;
	private transient BufferedImage emphasizedBorder;
	private transient BufferedImage highlightBorder;

	private boolean isPathVisible = true;
	private boolean isPointsVisible = false;

	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;

	private double centerX;
	private double centerY;

	private boolean isAttacker = false;
	private boolean isDefender = false;
	private boolean isEmphasized = false;
	private boolean isHighlighted = false;
	private boolean isReinforcing = false;
	private boolean isReinforced = false;

	public ProvinceHitArea(Province province, Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
		this.province = province;
		province.getMeshes().forEach(mesh -> {
			GeneralPath hitArea = new GeneralPath();
			DoaVectorI startPoint = mesh.get(0);
			hitArea.moveTo(startPoint.x, startPoint.y);
			for (int i = 1; i < mesh.size(); i++) {
				DoaVectorI nextPoint = mesh.get(i);
				hitArea.lineTo(nextPoint.x, nextPoint.y);
			}
			hitArea.closePath();
			meshes.add(hitArea);
		});
		centerX = province.getCenter().x;
		centerY = province.getCenter().y;
		cacheMeshAsImage();
		ALL_PROVINCE_HIT_AREAS.add(this);
	}

	@Override
	public void tick() {
		DoaVectorF mappedMouseCoords = Utils.mapMouseCoordinatesByZoom();
		if (meshes.stream().anyMatch(mesh -> mesh.contains((int) mappedMouseCoords.x, (int) mappedMouseCoords.y))) {
			isHighlighted = true;
			// setzOrder(DoaObject.FRONT);
		} else {
			isHighlighted = false;
			// setzOrder(DoaObject.GAME_OBJECTS);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (isPathVisible) {
			if (province.isClaimed()) {
				g.drawImage(playerOwnedMeshes.get(province.getOwner().getColor()), minX - 4d, minY - 4d);
				if (isEmphasized) {
					g.drawImage(emphasizedBorder, minX - 4d, minY - 4d);
				}
				if (isHighlighted) {
					g.drawImage(highlightBorder, minX - 4d, minY - 4d);
				}
				if (isAttacker) {
					g.drawImage(selectedBorder, minX - 4d, minY - 4d);
				}
				if (isDefender) {
					g.drawImage(selectedBorder, minX - 4d, minY - 4d);
				}
			} else {
				g.drawImage(unoccupiedMesh, minX - 4d, minY - 4d);
			}
		}
		if (isPointsVisible) {
			g.setColor(Color.MAGENTA);
			for (GeneralPath gp : meshes) {
				double[][] points = getPoints(gp);
				for (int i = 0; i < points.length; i++) {
					g.fillRect(points[i][0] - 1, points[i][1] - 1, 1, 1);
				}
			}
		}
		if (province.isClaimed()) {
			g.setFont(UIInit.UI_FONT.deriveFont(Font.BOLD, 18f));
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.BLACK);
			DoaSprite ownerLogo = DoaSprites.get("p" + province.getOwner().getID() + "Pawn");
			DoaSprite continentLogo = DoaSprites.get(province.getContinent().getAbbreviation());
			g.drawImage(continentLogo, centerX - continentLogo.getWidth() * 0.33f, centerY - continentLogo.getHeight() * 0.33f, continentLogo.getWidth() * 0.66f,
			        continentLogo.getHeight() * 0.66f);
			g.drawImage(ownerLogo, centerX - ownerLogo.getWidth() * 0.33f, centerY - ownerLogo.getHeight() * 0.33f, ownerLogo.getWidth() * 0.66f,
			        ownerLogo.getHeight() * 0.66f);
			String troopCount = "" + province.getTroops();
			g.drawString(troopCount, centerX - fm.stringWidth(troopCount) / 2f, centerY + (fm.getHeight() - fm.getAscent()) / 2f);
		}
	}

	private void cacheMeshAsImage() {
		for (GeneralPath mesh : meshes) {
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
		List<Graphics2D> playerMeshRenderers = new ArrayList<>();
		unoccupiedMesh = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
		selectedBorder = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
		emphasizedBorder = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
		highlightBorder = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < PlayerColorBank.size(); i++) {
			BufferedImage meshTexture = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
			meshTexture.setAccelerationPriority(1);
			Graphics2D meshRenderer = meshTexture.createGraphics();
			meshRenderer.translate(-minX + 4, -minY + 4);
			meshRenderer.setRenderingHints(HINTS);
			meshRenderer.setStroke(new BasicStroke(2));
			playerMeshRenderers.add(meshRenderer);
			playerOwnedMeshes.put(PlayerColorBank.get(i), meshTexture);
		}
		unoccupiedMesh.setAccelerationPriority(1);
		selectedBorder.setAccelerationPriority(1);
		Graphics2D umr = unoccupiedMesh.createGraphics();
		Graphics2D sbr = selectedBorder.createGraphics();
		Graphics2D ebr = emphasizedBorder.createGraphics();
		Graphics2D hbr = highlightBorder.createGraphics();
		umr.translate(-minX + 4, -minY + 4);
		umr.setRenderingHints(HINTS);
		umr.setStroke(new BasicStroke(2));
		sbr.translate(-minX + 4, -minY + 4);
		sbr.setRenderingHints(HINTS);
		sbr.setStroke(new BasicStroke(2));
		ebr.translate(-minX + 4, -minY + 4);
		ebr.setRenderingHints(HINTS);
		ebr.setStroke(new BasicStroke(2));
		hbr.translate(-minX + 4, -minY + 4);
		hbr.setRenderingHints(HINTS);
		hbr.setStroke(new BasicStroke(2));
		for (GeneralPath gp : meshes) {
			Composite umrOldComposite = umr.getComposite();
			umr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
			umr.setColor(Globals.PROVINCE_UNOCCUPIED);
			umr.fill(gp);
			umr.setComposite(umrOldComposite);
			umr.setColor(Globals.PROVINCE_UNOCCUPIED_BORDER);
			umr.draw(gp);

			sbr.setColor(Globals.PROVINCE_SELECTED_BORDER);
			sbr.draw(gp);

			ebr.setColor(Globals.PROVINCE_EMPHASIZE);
			ebr.draw(gp);

			ebr.setColor(Globals.PROVINCE_HIGHLIGHT);
			hbr.draw(gp);
			for (int i = 0; i < playerMeshRenderers.size(); i++) {
				Graphics2D renderer = playerMeshRenderers.get(i);
				Composite oldComposite = renderer.getComposite();
				renderer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
				renderer.setColor(PlayerColorBank.get(i));
				renderer.fill(gp);
				renderer.setComposite(oldComposite);
				renderer.setColor(Globals.PROVINCE_UNOCCUPIED_BORDER);
				renderer.draw(gp);
			}
		}
		umr.dispose();
		sbr.dispose();
		ebr.dispose();
		playerMeshRenderers.forEach(renderer -> renderer.dispose());
	}

	public Province getProvince() {
		return province;
	}

	public boolean isMouseClicked() {
		DoaVectorF mappedMouseCoords = Utils.mapMouseCoordinatesByZoom();
		return meshes.stream().anyMatch(mesh -> mesh.contains((int) mappedMouseCoords.x, (int) mappedMouseCoords.y) && DoaMouse.MB1);
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

	public void selectAsAttacker() {
		isAttacker = true;
		setzOrder(DoaObject.FRONT);
	}

	public void selectAsDefender() {
		isDefender = true;
		setzOrder(DoaObject.FRONT);
	}

	public void deselectAsAttacker() {
		isAttacker = false;
		setzOrder(DoaObject.GAME_OBJECTS);
	}

	public void deselectAsDefender() {
		isDefender = false;
		setzOrder(DoaObject.GAME_OBJECTS);
	}

	public void emphasizeForAttack() {
		isEmphasized = true;
		setzOrder(DoaObject.FRONT);
	}

	public void deemphasizeForAttack() {
		isEmphasized = false;
		setzOrder(DoaObject.GAME_OBJECTS);
	}

	public void selectAsReinforcing() {
		isReinforcing = true;
		setzOrder(DoaObject.FRONT);
	}

	public void selectAsReinforced() {
		isReinforcing = true;
		setzOrder(DoaObject.FRONT);
	}

	public void deselectAsReinforcing() {
		isReinforced = false;
		setzOrder(DoaObject.GAME_OBJECTS);
	}

	public void deselectAsReinforced() {
		isReinforced = false;
		setzOrder(DoaObject.GAME_OBJECTS);
	}

	public void emphasizeForReinforcement() {
		isEmphasized = true;
	}

	public void deemphasizeForReinforcement() {
		isEmphasized = false;
	}
}
