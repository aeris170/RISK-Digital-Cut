package com.pmnm.risk.map.board;

import java.awt.BasicStroke;
import java.awt.Color;

import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.map.province.ProvinceHitArea;

public final class ProvinceConnector extends DoaObject {

	private static final long serialVersionUID = -6230774776747052926L;

	private static ProvinceConnector _this = null;

	private ProvinceHitArea[] provinceHitAreas;
	private float dashArray = 0;

	private ProvinceConnector() {
		super(0f, 0f, 0, 0, 10);
	}

	@Override
	public void tick() {
		if (!GameManager.INSTANCE.isPaused && GameManager.INSTANCE.isSinglePlayer) {
			dashArray += 0.05f;
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (provinceHitAreas != null && provinceHitAreas.length > 0) {
			g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[] { 9, 5 }, dashArray));
			Color ownerColor = provinceHitAreas[0].getProvince().getOwner().getColor();
			ownerColor = new Color(255 - ownerColor.getRed(), 255 - ownerColor.getGreen(), 255 - ownerColor.getBlue());
			for (int i = provinceHitAreas.length - 1; i > 0; i--) {
				ProvinceHitArea first = provinceHitAreas[i];
				ProvinceHitArea second = provinceHitAreas[i - 1];
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

	public void setPath(ProvinceHitArea... provinceHitAreas) {
		this.provinceHitAreas = provinceHitAreas;
		dashArray = 0;
	}

	public static ProvinceConnector getInstance() {
		return _this == null ? _this = DoaHandler.instantiate(ProvinceConnector.class) : _this;
	}

	public static void deserialize(ProvinceConnector pc) {
		if (_this != null) {
			DoaHandler.remove(_this);
		}
		_this = pc;
		DoaHandler.add(_this);
	}
}