package pmnm.risk.map.board;

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

import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.globals.ProvinceColorBank;

import lombok.experimental.UtilityClass;
import pmnm.risk.map.Mesh2D;

@UtilityClass
public final class ProvinceHitAreaCacher {
	
	private static final Map<RenderingHints.Key, Object> HINTS = Map.of(
		RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
        RenderingHints.KEY_ANTIALIASING, 		RenderingHints.VALUE_ANTIALIAS_ON,
        RenderingHints.KEY_COLOR_RENDERING, 	RenderingHints.VALUE_COLOR_RENDER_QUALITY,
        RenderingHints.KEY_DITHERING, 			RenderingHints.VALUE_DITHER_ENABLE,
        RenderingHints.KEY_FRACTIONALMETRICS, 	RenderingHints.VALUE_FRACTIONALMETRICS_ON,
        RenderingHints.KEY_INTERPOLATION, 		RenderingHints.VALUE_INTERPOLATION_BICUBIC,
        RenderingHints.KEY_RENDERING, 			RenderingHints.VALUE_RENDER_QUALITY,
        RenderingHints.KEY_STROKE_CONTROL, 		RenderingHints.VALUE_STROKE_NORMALIZE,
        RenderingHints.KEY_TEXT_ANTIALIASING, 	RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );


	public static void cache(ProvinceHitArea pha) {
		pha.playerOwnedMeshes = new HashMap<>();
		List<Graphics2D> playerMeshRenderers = new ArrayList<>();
		ProvinceHitAreaBounds bounds = pha.getBounds();
		int width 	= bounds.maxX - bounds.minX + 8;
		int height 	= bounds.maxY - bounds.minY + 8;
		int translateX = -bounds.minX + 4;
		int translateY = -bounds.minY + 4;
		
		pha.unoccupiedMesh 		= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pha.selectedBorder 		= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pha.selectedMesh 		= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pha.emphasizedBorder 	= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pha.highlightBorder 	= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < PlayerColorBank.COLORS.length; i++) {
			BufferedImage meshTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			meshTexture.setAccelerationPriority(1);
			Graphics2D meshRenderer = meshTexture.createGraphics();
			meshRenderer.translate(translateX, translateY);
			meshRenderer.setRenderingHints(HINTS);
			meshRenderer.setStroke(new BasicStroke(2));
			playerMeshRenderers.add(meshRenderer);
			pha.playerOwnedMeshes.put(PlayerColorBank.COLORS[i], meshTexture);
		}
		pha.unoccupiedMesh.setAccelerationPriority(1);
		pha.selectedBorder.setAccelerationPriority(1);
		pha.selectedMesh.setAccelerationPriority(1);
		pha.emphasizedBorder.setAccelerationPriority(1);
		pha.highlightBorder.setAccelerationPriority(1);
		Graphics2D umr = pha.unoccupiedMesh.createGraphics();
		Graphics2D sbr = pha.selectedBorder.createGraphics();
		Graphics2D smr = pha.selectedMesh.createGraphics();
		Graphics2D ebr = pha.emphasizedBorder.createGraphics();
		Graphics2D hbr = pha.highlightBorder.createGraphics();
		umr.translate(translateX, translateY);
		umr.setRenderingHints(HINTS);
		umr.setStroke(new BasicStroke(2));
		sbr.translate(translateX, translateY);
		sbr.setRenderingHints(HINTS);
		sbr.setStroke(new BasicStroke(2));
		smr.translate(translateX, translateY);
		smr.setRenderingHints(HINTS);
		smr.setStroke(new BasicStroke(2));
		ebr.translate(translateX, translateY);
		ebr.setRenderingHints(HINTS);
		ebr.setStroke(new BasicStroke(2));
		hbr.translate(translateX, translateY);
		hbr.setRenderingHints(HINTS);
		hbr.setStroke(new BasicStroke(2));
		for(Mesh2D mesh : pha.getProvince().getMeshes()) {
			GeneralPath gp = mesh.getBoundary();
			Composite umrOldComposite = umr.getComposite();
			umr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
			umr.setColor(ProvinceColorBank.UNOCCUPIED);
			umr.fill(gp);
			umr.setComposite(umrOldComposite);
			umr.setColor(ProvinceColorBank.UNOCCUPIED_BORDER);
			umr.draw(gp);

			sbr.setColor(ProvinceColorBank.SELECTED_BORDER);
			sbr.draw(gp);

			smr.setColor(Color.WHITE);
			smr.fill(gp);

			ebr.setColor(ProvinceColorBank.EMPHASIZE);
			ebr.draw(gp);

			hbr.setColor(ProvinceColorBank.HIGHLIGHT);
			hbr.draw(gp);
			for (int i = 0; i < playerMeshRenderers.size(); i++) {
				Graphics2D renderer = playerMeshRenderers.get(i);
				Composite oldComposite = renderer.getComposite();
				renderer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
				renderer.setColor(PlayerColorBank.COLORS[i]);
				renderer.fill(gp);
				renderer.setComposite(oldComposite);
				renderer.setColor(ProvinceColorBank.UNOCCUPIED_BORDER);
				renderer.draw(gp);
			}
		}
		umr.dispose();
		sbr.dispose();
		ebr.dispose();
		hbr.dispose();
		playerMeshRenderers.forEach(Graphics2D::dispose);
	}
}
