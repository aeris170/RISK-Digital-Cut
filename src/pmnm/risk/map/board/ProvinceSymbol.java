package pmnm.risk.map.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.pmnm.risk.globals.Globals;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;

public final class ProvinceSymbol extends DoaObject {

	private static final long serialVersionUID = -5094307362006544586L;
	
	static ProvinceSymbol of(ProvinceHitArea province) {
		return new ProvinceSymbol(province);
	}
	
	private ProvinceHitArea province;
	
	private ProvinceSymbol(ProvinceHitArea province) {
		this.province = province;
		setzOrder(9);
		addComponent(new Renderer());
		//transform.position = new DoaVector(province.getBounds().centerX, province.getBounds().centerY);
	}
	
	private class Renderer extends DoaRenderer {

		private static final long serialVersionUID = -6525932365611103854L;

		@Override
		public void render() {
			IProvince p = province.getProvince();
			if (!p.isOccupied()) { return; }
			IPlayer occupier = p.getOccupier();
			float tx = province.getBounds().centerX;
			float ty = province.getBounds().centerY;
			
			DoaGraphicsFunctions.setFont(UIConstants.UI_FONT.deriveFont(Font.BOLD, 18f));
			FontMetrics fm = DoaGraphicsFunctions.getFontMetrics();
			DoaGraphicsFunctions.setColor(Color.BLACK);
			BufferedImage ownerLogo = DoaSprites.getSprite("p" + occupier.getId() + "Pawn");
			BufferedImage continentLogo = DoaSprites.getSprite(p.getContinent().getAbbreviation());
			DoaGraphicsFunctions.drawImage(
				continentLogo, 
				tx - continentLogo.getWidth() * 0.33f, 
				ty - continentLogo.getHeight() * 0.33f, 
				continentLogo.getWidth() * 0.66f, 
				continentLogo.getHeight() * 0.66f
			);
			DoaGraphicsFunctions.drawImage(
				ownerLogo, 
				tx - ownerLogo.getWidth() * 0.33f, 
				ty - ownerLogo.getHeight() * 0.33f,
				ownerLogo.getWidth() * 0.66f,
		        ownerLogo.getHeight() * 0.66f
	        );
			int troopCount = p.getNumberOfTroops();
			String troops = "";
			if (troopCount != Globals.UNKNOWN_TROOP_COUNT) {
				troops = Integer.toString(troopCount);
			} else {
				troops = "???";
			}
			DoaGraphicsFunctions.drawString(
				troops, 
				tx - fm.stringWidth(troops) / 2f, 
				ty + (fm.getHeight() - fm.getAscent()) / 2f
			);
		}
	}		
}