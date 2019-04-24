package com.pmnm.risk.toolkit;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaCamera;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;
import com.pmnm.risk.main.Camera;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public final class Utils {

	private Utils() {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static float mapXCoordinateByZoom(final float x) {
		return mapCoordinatesByZoom(x, 0).x;
	}

	public static float mapYCoordinateByZoom(final float y) {
		return mapCoordinatesByZoom(0, y).y;
	}

	public static DoaVectorF mapCoordinatesByZoom(final float x, final float y) {
		return mapCoordinatesByZoom(new DoaVectorF(x, y));
	}

	public static DoaVectorF mapCoordinatesByZoom(final DoaVectorF coordinateToBeMapped) {
		final float cx = Main.WINDOW_WIDTH / 2f;
		final float cy = Main.WINDOW_HEIGHT / 2f;
		final float z = DoaCamera.getZ();
		final float mx = coordinateToBeMapped.x - cx;
		final float my = coordinateToBeMapped.y - cy;
		return new DoaVectorF(mx * z + cx, my * z + cy);
	}

	// XXX why the flying f****** f*** the author of DoaEngine did not implement
	// this to his OWN ENGINE??????
	// P.S. the author is me.
	// TO DO: f*** myself
	// TODO CHECK IF ALL Utils FUNCTIONS WORKS FLAWLESSLY.
	// Assigned to: Fazilet Simge ER
	// in order to do this, look at the top left of the screen. there are text
	// showing the mouse position.
	// zoom in to arbitrary locations and check if the mouse coordinate is mapped
	// correctly.
	// <3
	public static DoaVectorF mapMouseCoordinatesByZoom() {
		final DoaVectorF mouseCoordinates = new DoaVectorF((float) DoaMouse.X, (float) DoaMouse.Y);
		final float cx = Main.WINDOW_WIDTH / 2f;
		final float cy = Main.WINDOW_HEIGHT / 2f;
		final float camx = Camera.INSTANCE.getPosition().x;
		final float camy = Camera.INSTANCE.getPosition().y;
		final float z = DoaCamera.getZ();
		final float mx = mouseCoordinates.x - camx;
		final float my = mouseCoordinates.y - camy;
		final DoaVectorF distance = new DoaVectorF(mx * z + cx, my * z + cy);
		return mouseCoordinates.add(distance.sub(mouseCoordinates).mul(-1f / DoaCamera.getZ()));
	}

	public static double computeRectangleArea(Rectangle2D rect) {
		return rect.getWidth() * rect.getHeight();
	}

	public static void paintImage(DoaSprite sp, Color c) {
		float cRed = c.getRed() / 255f;
		float cBlue = c.getBlue() / 255f;
		float cGreen = c.getGreen() / 255f;
		float cAlpha = c.getAlpha() / 255f;
		for (int xx = 0; xx < sp.getWidth(); xx++) {
			for (int yy = 0; yy < sp.getHeight(); yy++) {
				Color oldColor = new Color(sp.getRGB(xx, yy), true);
				Color newColor = new Color((int) ((cRed * oldColor.getRed() / 255f) * 255), (int) ((cGreen * oldColor.getGreen() / 255f) * 255),
				        (int) ((cBlue * oldColor.getBlue() / 255f) * 255), (int) ((cAlpha * oldColor.getAlpha() / 255f) * 255));
				sp.setRGB(xx, yy, newColor.getRGB());
			}
		}
	}

	public static float findMaxFontSizeToFitInArea(DoaGraphicsContext g, Font f, DoaVectorF r, String s) {
		float fontSize = 0;
		FontMetrics fm = g.getFontMetrics(f);
		do {
			f = new Font(f.getFontName(), Font.PLAIN, f.getSize() + 1);
			fontSize++;
			fm = g.getFontMetrics(f);
		} while (fm.stringWidth(s) < r.x && fm.getHeight() < r.y);
		return fontSize;
	}

	public static List<ProvinceHitArea> connectedComponents(ProvinceHitArea province) {
		List<ProvinceHitArea> connectedComponents = new ArrayList<>();
		connectedComponents.add(province);
		explodeFrom(province, connectedComponents);
		return connectedComponents;
	}

	private static void explodeFrom(ProvinceHitArea provinceHitArea, List<ProvinceHitArea> connectedComponents) {
		Province province = provinceHitArea.getProvince();
		for (Province p : provinceHitArea.getProvince().getNeighbours()) {
			if (p.getOwner() == province.getOwner()) {
				ProvinceHitArea pHitArea = p.getProvinceHitArea();
				if (!connectedComponents.contains(pHitArea)) {
					connectedComponents.add(pHitArea);
					explodeFrom(pHitArea, connectedComponents);
				}
			}
		}
	}
}