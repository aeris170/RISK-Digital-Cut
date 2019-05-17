package com.pmnm.risk.card;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.pmnm.risk.main.Player;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.gameui.CardPanel;

public class Card implements Serializable {

	private static final long serialVersionUID = -3610958010376376612L;

	public static Map<Province, Card> PROVINCE_CARDS = new HashMap<>();

	public static List<Card> UNDISTRIBUTED_CARDS = new ArrayList<>();

	private static final Color strokeColor = new Color(68, 66, 41);

	private Province province;
	private CardType type;
	private transient BufferedImage provinceTex;

	public Card() {
		UNDISTRIBUTED_CARDS.add(this);
	}

	public Card setType(CardType type) {
		this.type = type;
		return this;
	}

	public Card setProvince(Province province) {
		this.province = province;
		cacheMeshAsImage();
		return this;
	}

	public Province getProvince() {
		return province;
	}

	public CardType getType() {
		return type;
	}

	public BufferedImage getTex() {
		return provinceTex;
	}

	public void cacheMeshAsImage() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		List<GeneralPath> meshes = province.getProvinceHitArea().getMesh();

		for (GeneralPath mesh : meshes) {
			double[][] vertices = ProvinceHitArea.getPoints(mesh);
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
		provinceTex = new BufferedImage(maxX - minX + 8, maxY - minY + 8, BufferedImage.TYPE_INT_ARGB);
		provinceTex.setAccelerationPriority(1);
		Graphics2D hbr = provinceTex.createGraphics();
		hbr.translate(-minX + 4, -minY + 4);
		hbr.setRenderingHints(ProvinceHitArea.HINTS);
		hbr.setColor(strokeColor);
		hbr.setStroke(new BasicStroke(2));
		for (GeneralPath gp : meshes) {
			hbr.fill(gp);
		}
		hbr.setColor(Color.RED);
		hbr.drawRect(0, 0, provinceTex.getWidth() - 1, provinceTex.getHeight() - 1);
		hbr.dispose();
		BufferedImage bf = CardPanel.CardBG;
		double scalex = (double) (bf.getWidth() * 0.9f) / provinceTex.getWidth();
		double scaley = (double) (bf.getHeight() * 0.3f) / provinceTex.getHeight();
		double r = Math.min(scalex, scaley);
		provinceTex = Utils.toBufferedImage(provinceTex.getScaledInstance((int) (provinceTex.getWidth() * r),
				(int) (provinceTex.getHeight() * r), Image.SCALE_SMOOTH));
	}

	public static Card getRandomCard() {
		return UNDISTRIBUTED_CARDS.remove(ThreadLocalRandom.current().nextInt(UNDISTRIBUTED_CARDS.size() - 1));
	}

	public static void printAllCards() {
		PROVINCE_CARDS.entrySet().forEach(entry -> System.out.println(entry.getValue().toString()));
	}

	@Override
	public String toString() {
		return "Card [province=" + province.getName() + ", type=" + type + "]";
	}
}
