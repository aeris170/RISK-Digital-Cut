package com.pmnm.risk.map.board;

import java.awt.BasicStroke;
import java.awt.Color;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class ProvinceConnector extends DoaObject {

	private static final long serialVersionUID = -6230774776747052926L;

	private ProvinceHitArea[] provinceHitAreas;
	private float dashArray = 100000;

	public ProvinceConnector() {
		super(0f, 0f, 0, 0, DoaObject.FRONT);
	}

	public void setPath(ProvinceHitArea... provinceHitAreas) {
		this.provinceHitAreas = provinceHitAreas;
		dashArray = 100000;
	}

	@Override
	public void tick() {
		dashArray -= 0.05f;
		if (dashArray <= 10) {
			dashArray = 100000;
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (provinceHitAreas != null) {
			g.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, dashArray));
			Color ownerColor = provinceHitAreas[0].getProvince().getOwner().getColor();
			ownerColor = new Color(255 - ownerColor.getRed(), 255 - ownerColor.getGreen(), 255 - ownerColor.getBlue());
			for (int i = 0; i < provinceHitAreas.length - 1; i++) {
				ProvinceHitArea first = provinceHitAreas[i];
				ProvinceHitArea second = provinceHitAreas[i + 1];
				g.setColor(Color.BLACK);
				g.drawLine(first.centerX() - 1, first.centerY(), second.centerX(), second.centerY());
				g.drawLine(first.centerX(), first.centerY() - 1, second.centerX(), second.centerY());
				g.drawLine(first.centerX() + 1, first.centerY(), second.centerX(), second.centerY());
				g.drawLine(first.centerX(), first.centerY() + 1, second.centerX(), second.centerY());
				g.drawLine(first.centerX(), first.centerY(), second.centerX() - 1, second.centerY());
				g.drawLine(first.centerX(), first.centerY(), second.centerX(), second.centerY() - 1);
				g.drawLine(first.centerX(), first.centerY(), second.centerX() + 1, second.centerY());
				g.drawLine(first.centerX(), first.centerY(), second.centerX(), second.centerY() + 1);
				g.setColor(ownerColor);
				g.drawLine(first.centerX(), first.centerY(), second.centerX(), second.centerY());
			}
		}
	}
}