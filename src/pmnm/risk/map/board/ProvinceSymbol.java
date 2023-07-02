package pmnm.risk.map.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.ZOrders;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import pmnm.risk.game.IContinent;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;

public final class ProvinceSymbol extends DoaObject {

	private static final long serialVersionUID = -5094307362006544586L;
	
	static ProvinceSymbol of(ProvinceHitArea province) {
		return new ProvinceSymbol(province);
	}
	
	private ProvinceHitArea province;
	private BufferedImage continentLogo;
	private BufferedImage continentLogoUnoccupied;
	
	private ProvinceSymbol(ProvinceHitArea province) {
		this.province = province;

		IContinent provinceContinent = province.getProvince().getContinent();
		continentLogo = DoaSprites.getSprite(provinceContinent.getAbbreviation());
		continentLogoUnoccupied = new BufferedImage(continentLogo.getWidth(), continentLogo.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int yy = 0; yy < continentLogo.getHeight(); yy++) {
			for (int xx = 0; xx < continentLogo.getWidth(); xx++) {
				Color pixel = new Color(continentLogo.getRGB(xx, yy), true);
				Color continentColor = provinceContinent.getColor();
				Color newPixel = new Color(
					continentColor.getRed(),
					continentColor.getGreen(),
					continentColor.getBlue(),
					pixel.getAlpha()
				);
				
				continentLogoUnoccupied.setRGB(xx, yy, newPixel.getRGB());
			}
		}
		
		setzOrder(ZOrders.PROVINCE_SYMBOL_Z);
		addComponent(new Renderer());
	}
	
	private class Renderer extends DoaRenderer {

		private static final long serialVersionUID = -6525932365611103854L;

		@Override
		public void render() {
			IProvince p = province.getProvince();
			float tx = province.getBounds().centerX;
			float ty = province.getBounds().centerY;
			
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.BOLD, 18f));
			FontMetrics fm = DoaGraphicsFunctions.getFontMetrics();
			DoaGraphicsFunctions.setColor(new Color(1, 1, 1));
			DoaGraphicsFunctions.drawImage(
				p.isOccupied() ? continentLogo : continentLogoUnoccupied, 
				tx - continentLogo.getWidth() * 0.33f, 
				ty - continentLogo.getHeight() * 0.33f, 
				continentLogo.getWidth() * 0.66f, 
				continentLogo.getHeight() * 0.66f
			);

			if (!p.isOccupied()) { return; }
			IPlayer occupier = p.getOccupier();
			BufferedImage ownerLogo = DoaSprites.getSprite("p" + occupier.getId() + "Pawn");
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
				ty + (fm.getHeight() - fm.getAscent())
			);
		}
	}		
}