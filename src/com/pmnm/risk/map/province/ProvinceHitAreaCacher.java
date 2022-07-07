package com.pmnm.risk.map.province;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.PlayerColorBank;

public final class ProvinceHitAreaCacher {
	
	public static final Map<RenderingHints.Key, Object> HINTS = Map.of(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
	        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON, RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY,
	        RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE, RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON,
	        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC, RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY,
	        RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE, RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


	public static void Cache(ProvinceHitArea province) {
		province.playerOwnedMeshes = new HashMap<>();
		List<Graphics2D> playerMeshRenderers = new ArrayList<>();
		province.unoccupiedMesh = new BufferedImage(province.bounds.maxX - province.bounds.minX + 8, province.bounds.maxY - province.bounds.minY + 8, BufferedImage.TYPE_INT_ARGB);
		province.selectedBorder = new BufferedImage(province.bounds.maxX - province.bounds.minX + 8, province.bounds.maxY - province.bounds.minY + 8, BufferedImage.TYPE_INT_ARGB);
		province.selectedMesh = new BufferedImage(province.bounds.maxX - province.bounds.minX + 8, province.bounds.maxY - province.bounds.minY + 8, BufferedImage.TYPE_INT_ARGB);
		province.emphasizedBorder = new BufferedImage(province.bounds.maxX - province.bounds.minX + 8, province.bounds.maxY - province.bounds.minY + 8, BufferedImage.TYPE_INT_ARGB);
		province.highlightBorder = new BufferedImage(province.bounds.maxX - province.bounds.minX + 8, province.bounds.maxY - province.bounds.minY + 8, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < PlayerColorBank.colors.length; i++) {
			BufferedImage meshTexture = new BufferedImage(province.bounds.maxX - province.bounds.minX + 8, province.bounds.maxY - province.bounds.minY + 8, BufferedImage.TYPE_INT_ARGB);
			meshTexture.setAccelerationPriority(1);
			Graphics2D meshRenderer = meshTexture.createGraphics();
			meshRenderer.translate(-province.bounds.minX + 4, -province.bounds.minY + 4);
			meshRenderer.setRenderingHints(HINTS);
			meshRenderer.setStroke(new BasicStroke(2));
			playerMeshRenderers.add(meshRenderer);
			province.playerOwnedMeshes.put(PlayerColorBank.colors[i], meshTexture);
		}
		province.unoccupiedMesh.setAccelerationPriority(1);
		province.selectedBorder.setAccelerationPriority(1);
		province.selectedMesh.setAccelerationPriority(1);
		province.emphasizedBorder.setAccelerationPriority(1);
		province.highlightBorder.setAccelerationPriority(1);
		Graphics2D umr = province.unoccupiedMesh.createGraphics();
		Graphics2D sbr = province.selectedBorder.createGraphics();
		Graphics2D smr = province.selectedMesh.createGraphics();
		Graphics2D ebr = province.emphasizedBorder.createGraphics();
		Graphics2D hbr = province.highlightBorder.createGraphics();
		umr.translate(-province.bounds.minX + 4, -province.bounds.minY + 4);
		umr.setRenderingHints(HINTS);
		umr.setStroke(new BasicStroke(2));
		sbr.translate(-province.bounds.minX + 4, -province.bounds.minY + 4);
		sbr.setRenderingHints(HINTS);
		sbr.setStroke(new BasicStroke(2));
		smr.translate(-province.bounds.minX + 4, -province.bounds.minY + 4);
		smr.setRenderingHints(HINTS);
		smr.setStroke(new BasicStroke(2));
		ebr.translate(-province.bounds.minX + 4, -province.bounds.minY + 4);
		ebr.setRenderingHints(HINTS);
		ebr.setStroke(new BasicStroke(2));
		hbr.translate(-province.bounds.minX + 4, -province.bounds.minY + 4);
		hbr.setRenderingHints(HINTS);
		hbr.setStroke(new BasicStroke(2));
		for (GeneralPath gp : province.meshes) {
			Composite umrOldComposite = umr.getComposite();
			umr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
			umr.setColor(Globals.PROVINCE_UNOCCUPIED);
			umr.fill(gp);
			umr.setComposite(umrOldComposite);
			umr.setColor(Globals.PROVINCE_UNOCCUPIED_BORDER);
			umr.draw(gp);

			sbr.setColor(Globals.PROVINCE_SELECTED_BORDER);
			sbr.draw(gp);

			smr.setColor(Color.WHITE);
			smr.fill(gp);

			ebr.setColor(Globals.PROVINCE_EMPHASIZE);
			ebr.draw(gp);

			ebr.setColor(Globals.PROVINCE_HIGHLIGHT);
			hbr.draw(gp);
			for (int i = 0; i < playerMeshRenderers.size(); i++) {
				Graphics2D renderer = playerMeshRenderers.get(i);
				Composite oldComposite = renderer.getComposite();
				renderer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
				renderer.setColor(PlayerColorBank.colors[i]);
				renderer.fill(gp);
				renderer.setComposite(oldComposite);
				renderer.setColor(Globals.PROVINCE_UNOCCUPIED_BORDER);
				renderer.draw(gp);
			}
		}
		umr.dispose();
		sbr.dispose();
		ebr.dispose();
		hbr.dispose();
		playerMeshRenderers.forEach(Graphics2D::dispose);
	}
	
	private ProvinceHitAreaCacher() {}
}
