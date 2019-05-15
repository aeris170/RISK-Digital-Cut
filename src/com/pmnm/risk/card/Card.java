package com.pmnm.risk.card;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class Card {

	public static final Map<Province, Card> PROVINCE_CARDS = new HashMap<>();

	private static final List<Card> UNDISTRIBUTED_CARDS = new ArrayList<>();
	
	private static final Color strokeColor = Color.RED;
			//new Color(68, 66, 41);

	private Province province;
	private CardType type;
	private BufferedImage provinceTex;

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
		int minX = 0;
		int minY = 0;
		int maxX = 0;
		int maxY = 0;
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
		//hbr.setRenderingHints(HINTS);
		hbr.setColor(strokeColor);
		hbr.setStroke(new BasicStroke(20));
		for (GeneralPath gp : meshes) {
			hbr.draw(gp);
		}
		hbr.dispose();
	}

	/**
	 * Gets a random, undistributed card.
	 * 
	 * @return a random card
	 */
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
