package pmnm.risk.map.board;

import java.awt.BasicStroke;
import java.awt.Color;

import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.GameManager;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import pmnm.risk.game.IRiskGameContext;

public final class ProvinceConnector extends DoaObject {

	private static final long serialVersionUID = -6230774776747052926L;

	private IRiskGameContext context;
	private ProvinceHitArea[] provinceHitAreas;
	private float dashPhase = 0;
	private float[] dashArray = new float[] { 9, 5 };

	ProvinceConnector(IRiskGameContext context) {
		this.context = context;
		setzOrder(10);
		addComponent(new ProvinceConnectorScript());
		addComponent(new ProvinceConnectorRenderer());
	}
	

	public void setPath(ProvinceHitArea... provinceHitAreas) {
		this.provinceHitAreas = provinceHitAreas;
		dashPhase = 0;
	}
	
	private class ProvinceConnectorScript extends DoaScript {
		
		private static final long serialVersionUID = 8252737118743603352L;

		@Override
		public void tick() {
			if (context.isPaused()) { return; }
			dashPhase += 0.05f;
		}	
	}
	
	private class ProvinceConnectorRenderer extends DoaRenderer {
		
		private static final long serialVersionUID = -8982130745576574664L;

		@Override
		public void render() {
			if (provinceHitAreas == null) { return; }
			if (provinceHitAreas.length <= 0) { return; }
			
			DoaGraphicsFunctions.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashArray, dashPhase));
			Color ownerColor = provinceHitAreas[0].getProvince().getOccupier().getColor();
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
