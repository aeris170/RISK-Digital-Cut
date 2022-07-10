package pmnm.risk.map.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.pmnm.roy.ui.UIInit;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;

public final class ProvinceSymbol extends DoaObject {

	private static final long serialVersionUID = -5094307362006544586L;
	
	private ProvinceHitArea province;
	
	public ProvinceSymbol(ProvinceHitArea province) {
		this.province = province;
		setzOrder(9);
		transform.position = new DoaVector(province.bounds.centerX, province.bounds.centerY);
		addComponent(new Renderer());
	}
	
	private class Renderer extends DoaRenderer {

		private static final long serialVersionUID = -6525932365611103854L;

		@Override
		public void render() {
			Province p = province.getProvince();
			if (p.isClaimed()) {
				DoaGraphicsFunctions.setFont(UIInit.UI_FONT.deriveFont(Font.BOLD, 18f));
				FontMetrics fm = DoaGraphicsFunctions.getFontMetrics();
				DoaGraphicsFunctions.setColor(Color.BLACK);
				BufferedImage ownerLogo = DoaSprites.getSprite("p" + p.getOwner().getID() + "Pawn");
				BufferedImage continentLogo = DoaSprites.getSprite(p.getContinent().getAbbreviation());
				DoaGraphicsFunctions.drawImage(
					continentLogo, 
					province.bounds.centerX - continentLogo.getWidth() * 0.33f, 
					province.bounds.centerY - continentLogo.getHeight() * 0.33f, 
					continentLogo.getWidth() * 0.66f, 
					continentLogo.getHeight() * 0.66f
				);
				DoaGraphicsFunctions.drawImage(
					ownerLogo, 
					province.bounds.centerX - ownerLogo.getWidth() * 0.33f, 
					province.bounds.centerY - ownerLogo.getHeight() * 0.33f,
					ownerLogo.getWidth() * 0.66f,
			        ownerLogo.getHeight() * 0.66f
		        );
				int troopCount = p.getTroops();
				String troops = "";
				if (troopCount == -1) {
					troops = "???";
				} else {
					troops = "" + troopCount;
				}
				DoaGraphicsFunctions.drawString(
					troops, 
					province.bounds.centerX - fm.stringWidth(troops) / 2f, 
					province.bounds.centerY + (fm.getHeight() - fm.getAscent()) / 2f
				);
			}
		}
	}		
}