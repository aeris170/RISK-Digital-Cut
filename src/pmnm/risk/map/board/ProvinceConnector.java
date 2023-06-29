package pmnm.risk.map.board;

import java.awt.BasicStroke;
import java.awt.Color;

import com.pmnm.risk.globals.ZOrders;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Setter;
import pmnm.risk.game.IRiskGameContext;

@SuppressWarnings("serial")
public final class ProvinceConnector extends DoaObject {

	public enum Mode {
		ATTACK, REINFORCE;
	}
	
	private IRiskGameContext context;
	private ProvinceHitArea[] provinceHitAreas;
	private float dashPhase = 0;
	private float[] dashArray = new float[] { 9, 5 };
	@Setter private Mode mode;
	
	ProvinceConnector(IRiskGameContext context) {
		this.context = context;
		setzOrder(ZOrders.PROVINCE_CONNECTOR_Z);
		addComponent(new Script());
		addComponent(new Renderer());
	}
	

	public void setPath(ProvinceHitArea... provinceHitAreas) {
		this.provinceHitAreas = provinceHitAreas;
		dashPhase = 0;
	}

	private class Script extends DoaScript {
		@Override
		public void tick() {
			if (context.isPaused()) { return; }
			
			if (mode == Mode.ATTACK) {
				dashPhase += 0.02f;
			} else {
				dashPhase += 0.09f;
			}
		}	
	}
	
	private class Renderer extends DoaRenderer {
		@Override
		public void render() {
			if (provinceHitAreas == null) { return; }
			if (provinceHitAreas.length <= 0) { return; }

			DoaGraphicsFunctions.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashArray, dashPhase));
			Color ownerColor = provinceHitAreas[0].getProvince().getOccupier().getColor();
			if (mode == Mode.ATTACK) {
				ownerColor = ownerColor.brighter();
			} else {
				ownerColor = new Color(255 - ownerColor.getRed(), 255 - ownerColor.getGreen(), 255 - ownerColor.getBlue());
			}
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
				// TODO add points to a polyline and render polyline
			}
		}
	}
}
