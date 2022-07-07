package com.pmnm.risk.map.board;

import java.awt.BasicStroke;
import java.awt.Color;

import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.map.province.ProvinceHitArea;
import com.pmnm.risk.map.province.ProvinceHitAreaBounds;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;

public final class ProvinceConnector extends DoaObject {

	private static final long serialVersionUID = -6230774776747052926L;

	private static ProvinceConnector _this = null;

	private ProvinceHitArea[] provinceHitAreas;
	private float dashPhase = 0;
	private float[] dashArray = new float[] { 9, 5 };

	private ProvinceConnector() {
		setzOrder(10);
		addComponent(new ProvinceConnectorScript());
		addComponent(new ProvinceConnectorRenderer());
	}
	

	public void setPath(ProvinceHitArea... provinceHitAreas) {
		this.provinceHitAreas = provinceHitAreas;
		dashPhase = 0;
	}

	public static ProvinceConnector getInstance() {
		if(_this == null) {
			_this = new ProvinceConnector();
			Scenes.GAME_SCENE.add(_this);
		}
		return _this;
	}

	public static void deserialize(ProvinceConnector pc) {
		if (_this != null) {
			Scenes.GAME_SCENE.remove(_this);
		}
		_this = pc;
		Scenes.GAME_SCENE.add(_this);
	}
	
	
	private class ProvinceConnectorScript extends DoaScript {
		
		private static final long serialVersionUID = 8252737118743603352L;

		@Override
		public void tick() {
			if (!GameManager.INSTANCE.isPaused && GameManager.INSTANCE.isSinglePlayer) {
				dashPhase += 0.05f;
			}
		}	
	}
	
	private class ProvinceConnectorRenderer extends DoaRenderer {
		
		private static final long serialVersionUID = -8982130745576574664L;

		@Override
		public void render() {
			if (provinceHitAreas != null && provinceHitAreas.length > 0) {
				DoaGraphicsFunctions.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashArray, dashPhase));
				Color ownerColor = provinceHitAreas[0].getProvince().getOwner().getColor();
				ownerColor = new Color(255 - ownerColor.getRed(), 255 - ownerColor.getGreen(), 255 - ownerColor.getBlue());
				for (int i = provinceHitAreas.length - 1; i > 0; i--) {
					ProvinceHitAreaBounds first = provinceHitAreas[i].getBounds();
					ProvinceHitAreaBounds second = provinceHitAreas[i - 1].getBounds();
					DoaGraphicsFunctions.setColor(Color.BLACK);
					DoaGraphicsFunctions.drawLine(first.centerX - 1, first.centerY, second.centerX, second.centerY);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY - 1, second.centerX, second.centerY);
					DoaGraphicsFunctions.drawLine(first.centerX + 1, first.centerY, second.centerX, second.centerY);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY + 1, second.centerX, second.centerY);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY, second.centerX - 1, second.centerY);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY, second.centerX, second.centerY - 1);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY, second.centerX + 1, second.centerY);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY, second.centerX, second.centerY + 1);
					DoaGraphicsFunctions.setColor(ownerColor);
					DoaGraphicsFunctions.drawLine(first.centerX, first.centerY, second.centerX, second.centerY);
				}
			}
		}
	}

}