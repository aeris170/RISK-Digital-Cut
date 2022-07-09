package com.pmnm.risk.toolkit;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.pmnm.risk.main.Main;
import com.pmnm.risk.map.province.ProvinceHitArea;

import doa.engine.core.DoaCamera;
import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import pmnm.risk.map.province.Province;

public final class Utils {

	private Utils() {}

	public static float mapXCoordinateByZoom(final float x) {
		return mapCoordinatesByZoom(x, 0).x;
	}

	public static float mapYCoordinateByZoom(final float y) {
		return mapCoordinatesByZoom(0, y).y;
	}

	public static DoaVector mapCoordinatesByZoom(final float x, final float y) {
		return mapCoordinatesByZoom(new DoaVector(x, y));
	}

	public static DoaVector mapCoordinatesByZoom(final DoaVector coordinateToBeMapped) {
		final float cx = Main.WINDOW_WIDTH / 2f;
		final float cy = Main.WINDOW_HEIGHT / 2f;
		final float z = DoaCamera.getZ();
		final float mx = coordinateToBeMapped.x - cx;
		final float my = coordinateToBeMapped.y - cy;
		return new DoaVector(mx * z + cx, my * z + cy);
	}

	public static double computeRectangleArea(Rectangle2D rect) {
		return rect.getWidth() * rect.getHeight();
	}

	public static void paintImage(BufferedImage sp, Color c) {
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

	public static Color complementaryColor(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}

	public static float euclideanDistance(DoaVector first, DoaVector second) {
		return (float) Math.sqrt(Math.pow((second.x - first.x), 2) + Math.pow((second.y - first.y), 2));
	}

	public static float findMaxFontSizeToFitInArea(Font f, DoaVector r, String s) {
		float fontSize = 0;
		FontMetrics fm;
		do {
			f = new Font(f.getFontName(), Font.PLAIN, f.getSize() + 1);
			fontSize++;
			fm = DoaGraphicsFunctions.getFontMetrics(f);
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

	public static ProvinceHitArea[] shortestPath(ProvinceHitArea reinforcingProvince, ProvinceHitArea reinforcedProvince) {
		List<List<ProvinceHitArea>> paths = new ArrayList<>();
		doStuff(reinforcingProvince, new ArrayList<>(), paths, reinforcedProvince);
		List<ProvinceHitArea> shortestPath = paths.stream().min(Comparator.comparingInt(List::size)).orElse(new ArrayList<>());
		return shortestPath.toArray(new ProvinceHitArea[shortestPath.size()]);
	}

	private static void doStuff(ProvinceHitArea previous, List<ProvinceHitArea> path, List<List<ProvinceHitArea>> paths, ProvinceHitArea destination) {
		path.add(previous);
		if (previous == destination) {
			paths.add(path);
			return; // XXX if this function ever behaves buggy, comment the return and test again!
		}
		Province province = previous.getProvince();
		for (Province p : province.getNeighbours()) {
			if (p.getOwner() == province.getOwner() && !path.contains(p.getProvinceHitArea())) {
				doStuff(p.getProvinceHitArea(), new ArrayList<>(path), paths, destination);
			}
		}
	}

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}