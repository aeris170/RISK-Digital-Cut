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
import java.util.concurrent.CopyOnWriteArrayList;

import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.GameManager;
import com.pmnm.roy.ui.UIInit;
import com.pmnm.roy.ui.ZOrders;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;

public class ProvinceHitArea extends DoaObject {
	
	public static ProvinceHitArea of(Province province) {
		return new ProvinceHitArea(province);
	}

	private static final long serialVersionUID = -6848368535793292243L;

	public static ProvinceHitArea selectedProvinceByMouse = null;

	List<GeneralPath> meshes = new ArrayList<>();
	Province province;

	transient BufferedImage unoccupiedMesh;
	transient Map<Color, BufferedImage> playerOwnedMeshes;
	transient BufferedImage selectedMesh;
	transient BufferedImage selectedBorder;
	transient BufferedImage emphasizedBorder;
	transient BufferedImage highlightBorder;

	private boolean isPathVisible = true;
	private boolean isPointsVisible = false;

	ProvinceHitAreaBounds bounds;

	private boolean isAttacker = false;
	private boolean isDefender = false;
	private boolean isEmphasized = false;
	private boolean isHighlighted = false;
	public boolean isSelected = false;
	private boolean isReinforcing = false;
	private boolean isReinforced = false;

	private float selectedMeshAlpha = 0f;
	private float selectedMeshAlphaDelta = 0.005f;

	private ProvinceHitArea(Province province) {
		this.province = province;
		addComponent(new Script());
		addComponent(new Renderer());
		
		constructMeshes();
		ProvinceHitAreaBounds.Calculate(this);
		ProvinceHitAreaCacher.Cache(this);
		
		ProvinceSymbol symbol = new ProvinceSymbol(this);
		Scenes.GAME_SCENE.add(symbol);
	}

	public class Script extends DoaScript {
		
		private static final long serialVersionUID = -7463836870534187697L;

		@Override
		public synchronized void tick() {
			if (!GameManager.INSTANCE.isPaused) {
				if (meshes.stream().anyMatch(mesh -> mesh.contains((int) DoaMouse.X, (int) DoaMouse.Y))) {
					isHighlighted = true;
				} else {
					isHighlighted = false;
				}
				if (isMouseClicked()) {
					if (selectedProvinceByMouse != null) {
						selectedProvinceByMouse.isSelected = false;
					}
					selectedProvinceByMouse = ProvinceHitArea.this;
					isSelected = true;
				}
	
				if (isHighlighted) {
					setzOrder(ZOrders.HIGHLIGHTED_PROVINCE_Z);
				} else if (isEmphasized) {
					setzOrder(ZOrders.EMPHASIZED_PROVINCE_Z);
				} else if (isAttacker || isDefender || isReinforcing || isReinforced) {
					setzOrder(ZOrders.MUTATING_PROVINCE_Z);
				} else if (getzOrder() != 1) {
					setzOrder(ZOrders.DEFAULT_PROVINCE_Z);
				}
				if (isSelected) {
					setzOrder(ZOrders.SELECTED_PROVINCE_Z);
					selectedMeshAlpha += selectedMeshAlphaDelta;
					if (selectedMeshAlpha >= 0.6f) {
						selectedMeshAlpha = 0.6f;
						selectedMeshAlphaDelta *= -1;
					} else if (selectedMeshAlpha <= 0f) {
						selectedMeshAlpha = 0f;
						selectedMeshAlphaDelta *= -1;
					}
				} else {
					selectedMeshAlpha = 0f;
					if (getzOrder() == ZOrders.SELECTED_PROVINCE_Z) {
						setzOrder(ZOrders.DEFAULT_PROVINCE_Z);
					}
				}
			}
		}
		
		private boolean isMouseClicked() {
			return meshes.stream().anyMatch(mesh -> mesh.contains((int) DoaMouse.X, (int) DoaMouse.Y) && DoaMouse.MB1);
		}
	}
	
	public class Renderer extends DoaRenderer {
		
		private static final long serialVersionUID = 3390229334254057350L;

		@Override
		public void render() {
			if (isPathVisible) {
				if (province.isClaimed()) {
					DoaGraphicsFunctions.drawImage(playerOwnedMeshes.get(province.getOwner().getColor()), bounds.minX - 4f, bounds.minY - 4f);
					if (isEmphasized) {
						DoaGraphicsFunctions.drawImage(emphasizedBorder, bounds.minX - 4f, bounds.minY - 4f);
					}
					if (isHighlighted) {
						DoaGraphicsFunctions.drawImage(highlightBorder, bounds.minX - 4f, bounds.minY - 4f);
					}
					if (isAttacker) {
						DoaGraphicsFunctions.drawImage(selectedBorder, bounds.minX - 4f, bounds.minY - 4f);
					}
					if (isDefender) {
						DoaGraphicsFunctions.drawImage(selectedBorder, bounds.minX - 4f, bounds.minY - 4f);
					}
					if (isSelected) {
						DoaGraphicsFunctions.pushComposite();
						DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, selectedMeshAlpha));
						DoaGraphicsFunctions.drawImage(selectedMesh, bounds.minX - 4f, bounds.minY - 4f);
						DoaGraphicsFunctions.popComposite();
						DoaGraphicsFunctions.drawImage(highlightBorder, bounds.minX - 4f, bounds.minY - 4f);
					}
				} else {
					DoaGraphicsFunctions.drawImage(unoccupiedMesh, bounds.minX - 4f, bounds.minY - 4f);
				}
			}
			if (isPointsVisible) {
				DoaGraphicsFunctions.setColor(Color.MAGENTA);
				for (GeneralPath gp : meshes) {
					double[][] points = getPoints(gp);
					for (int i = 0; i < points.length; i++) {
						DoaGraphicsFunctions.fillRect((float) points[i][0] - 1, (float) points[i][1] - 1, 1, 1);
					}
				}
			}
		}
	}
	
	private void constructMeshes() {
		province.getMeshes().forEach(mesh -> {
			GeneralPath hitArea = new GeneralPath();
			Vertex2D startPoint = mesh.get(0);
			hitArea.moveTo(startPoint.x, startPoint.y);
			for (int i = 1; i < mesh.size(); i++) {
				DoaVector nextPoint = mesh.get(i);
				hitArea.lineTo(nextPoint.x, nextPoint.y);
			}
			hitArea.closePath();
			meshes.add(hitArea);
		});
	}
	
	public ProvinceHitAreaBounds getBounds() {
		return bounds;
	}
	
	public List<GeneralPath> getMesh() {
		return meshes;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	// https://stackoverflow.com/questions/5803111/obtain-ordered-vertices-of-generalpath
	// by finnw
	public static double[][] getPoints(Path2D path) {
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
		setzOrder(ZOrders.MUTATING_PROVINCE_Z);
	}

	public void selectAsDefender() {
		isDefender = true;
		setzOrder(ZOrders.MUTATING_PROVINCE_Z);
	}

	public void deselectAsAttacker() {
		isAttacker = false;
		setzOrder(ZOrders.DESELECTED_PROVINCE_Z);
	}

	public void deselectAsDefender() {
		isDefender = false;
		setzOrder(ZOrders.DESELECTED_PROVINCE_Z);
	}

	public void emphasizeForAttack() {
		isEmphasized = true;
		setzOrder(ZOrders.EMPHASIZED_PROVINCE_Z);
	}

	public void deemphasizeForAttack() {
		isEmphasized = false;
		setzOrder(ZOrders.DESELECTED_PROVINCE_Z);
	}

	public void selectAsReinforcing() {
		isReinforcing = true;
		setzOrder(ZOrders.MUTATING_PROVINCE_Z);
	}

	public void selectAsReinforced() {
		isReinforced = true;
		setzOrder(ZOrders.MUTATING_PROVINCE_Z);
	}

	public void deselectAsReinforcing() {
		isReinforcing = false;
		setzOrder(ZOrders.DESELECTED_PROVINCE_Z);
	}

	public void deselectAsReinforced() {
		isReinforced = false;
		setzOrder(ZOrders.DESELECTED_PROVINCE_Z);
	}

	public void emphasizeForReinforcement() {
		isEmphasized = true;
	}

	public void deemphasizeForReinforcement() {
		isEmphasized = false;
	}

	@Override
	public String toString() {
		return "PHA:" + province.toString();
	}
}