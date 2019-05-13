package com.pmnm.roy.ui.gameui;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.main.Main;

public class Water extends DoaObject {

	private static final long serialVersionUID = -3289865017771805571L;

	BufferedImage tex = null;
	BufferedImage bigWinter = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);;
	BufferedImage bigSpring = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);;
	BufferedImage bigSummer = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);;
	BufferedImage bigFall = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);;

	Point2D[][] points = new Point2D.Double[18][11];
	long[][] startTime = new long[18][11];
	double[][] intensity = new double[18][11];

	List<TriangularSurface> mesh = new ArrayList<>();

	Water() {
		super(0f, 0f, -2);
		for (int y = 0; y < points[0].length; y++) {
			for (int x = 0; x < points.length; x++) {
				points[x][y] = new Point2D.Double(x * 120, y * 120);
				startTime[x][y] = ThreadLocalRandom.current().nextLong();
				intensity[x][y] = ThreadLocalRandom.current().nextInt(1, 2);
			}
		}
		for (int y = 0; y < points[0].length - 1; y++) {
			for (int x = 0; x < points.length - 1; x++) {
				Point2D pa1 = points[x][y];
				Point2D pa2 = points[x + 1][y];
				Point2D pa3 = points[x][y + 1];
				Point2D pb1 = points[x + 1][y];
				Point2D pb2 = points[x][y + 1];
				Point2D pb3 = points[x + 1][y + 1];
				mesh.add(new TriangularSurface(pa1, pa2, pa3));
				mesh.add(new TriangularSurface(pb1, pb2, pb3));
			}
		}
		DoaSprite winterSpr = DoaSprites.get("winterTex");
		DoaSprite springSpr = DoaSprites.get("springTex");
		DoaSprite summerSpr = DoaSprites.get("summerTex");
		DoaSprite fallSpr = DoaSprites.get("fallTex");
		Graphics2D bigWinterRenderer = bigWinter.createGraphics();
		Graphics2D bigSpringRenderer = bigSpring.createGraphics();
		Graphics2D bigSummerRenderer = bigSummer.createGraphics();
		Graphics2D bigFallRenderer = bigFall.createGraphics();
		int texW = winterSpr.getWidth() / 6;
		int texH = winterSpr.getHeight() / 6;
		for (int i = -50; i < Main.WINDOW_WIDTH + 50; i += texW) {
			for (int j = -50; j < Main.WINDOW_HEIGHT + 50; j += texH) {
				bigWinterRenderer.drawImage(winterSpr, i, j, texW, texH, null);
				bigSpringRenderer.drawImage(springSpr, i, j, texW, texH, null);
				bigSummerRenderer.drawImage(summerSpr, i, j, texW, texH, null);
				bigFallRenderer.drawImage(fallSpr, i, j, texW, texH, null);
			}
		}
	}

	@Override
	public void tick() {
		for (int y = 0; y < points[0].length; y++) {
			for (int x = 0; x < points.length; x++) {
				Point2D p = points[x][y];
				double px = x * 120 + (intensity[x][y]) * Math.sin((startTime[x][y] + System.nanoTime()) * 0.00000000473);
				double py = y * 120 + (intensity[x][y]) * Math.cos((startTime[x][y] + System.nanoTime()) * 0.00000000291);
				p.setLocation(px, py);
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		switch (Season.getCurrentSeason()) {
			case WINTER:
				tex = bigWinter;
				break;
			case SPRING:
				tex = bigSpring;
				break;
			case SUMMER:
				tex = bigSummer;
				break;
			case FALL:
				tex = bigFall;
				break;
		}
		g.pushTransform();
		g.translate(-50, -50);
		g.scale(1.1, 1.1);
		mesh.forEach(surface -> surface.render(g));
		g.popTransform();
	}

	private class TriangularSurface {

		Point2D[] points;
		Polygon polygon = new Polygon();
		AffineTransform t1 = new AffineTransform();
		AffineTransform t2 = new AffineTransform();

		TriangularSurface(Point2D first, Point2D second, Point2D third) {
			this.points = new Point2D[] { first, second, third };
			t1.setTransform(first.getX() - third.getX(), first.getY() - third.getY(), second.getX() - third.getX(), second.getY() - third.getY(), third.getX(),
			        third.getY());
			try {
				t1.invert();
			} catch (NoninvertibleTransformException ex) {
				ex.printStackTrace();
			}
		}

		public void render(DoaGraphicsContext g) {
			polygon.reset();
			for (Point2D p : points) {
				polygon.addPoint((int) p.getX(), (int) p.getY());
			}
			Shape originalClip = g.getClip();
			g.clip(polygon);
			Point2D a = points[0];
			Point2D b = points[1];
			Point2D c = points[2];
			t2.setTransform(a.getX() - c.getX(), a.getY() - c.getY(), b.getX() - c.getX(), b.getY() - c.getY(), c.getX(), c.getY());
			t2.concatenate(t1);
			g.drawImage(tex, t2);
			g.setClip(originalClip);
		}
	}
}
