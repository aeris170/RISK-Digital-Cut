package com.pmnm.risk.ui.gameui;

import java.awt.Font;
import java.awt.FontMetrics;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.risk.ui.UIInit;
import com.pmnm.risk.ui.gameui.actions.NextPhaseButtonAction;

public class BottomPanel extends DoaPanel {

	private static final long serialVersionUID = -59674351675589726L;

	private static final DoaSprite MIDDLE = DoaSprites.get("gaugeBig");
	private static final DoaSprite LEFT = DoaSprites.get("gaugeLeft");
	private static final DoaSprite RIGHT = DoaSprites.get("gaugeRight");

	public static DoaImageButton nextPhaseButton = DoaHandler.instantiateDoaObject(DoaImageButton.class, 0f, 0f, 300, 300, DoaSprites.get("nextPhaseButtonIdle"),
	        DoaSprites.get("nextPhaseButtonHover"), DoaSprites.get("nextPhaseButtonPressed"), DoaSprites.get("nextPhaseButtonDisabled"));

	public BottomPanel() {
		super(0f, 0f, 0, 0);
		nextPhaseButton.addAction(new NextPhaseButtonAction(nextPhaseButton));
		add(nextPhaseButton);
		nextPhaseButton.disable();

		show();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		Province clickedProvince = GameManager.clickedHitArea != null ? GameManager.clickedHitArea.getProvince() : null;

		String garrisonText = "";
		DoaSprite garrisonSprite = DoaSprites.get("garrisonHolder");
		DoaVectorF garrisonTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.454f, Main.WINDOW_HEIGHT * 0.836f);

		String ownerText = "";
		DoaSprite ownerSprite = DoaSprites.get("ownerHolder");
		DoaVectorF ownerTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.447f, Main.WINDOW_HEIGHT * 0.874f);

		String nameText = "";
		DoaSprite nameSprite = DoaSprites.get("provinceNameHolder");
		DoaVectorF nameTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.436f, Main.WINDOW_HEIGHT * 0.912f);

		String continentText = "";
		DoaSprite continentSprite = DoaSprites.get("continentHolder");
		DoaVectorF continentTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.430f, Main.WINDOW_HEIGHT * 0.950f);

		if (clickedProvince != null) {
			garrisonText += clickedProvince.getTroops();
			ownerText += clickedProvince.getOwner().getName();
			nameText += clickedProvince.getName().toUpperCase();
			continentText += clickedProvince.getContinent().getName().toUpperCase();
		}
		nextPhaseButton.setPosition(new DoaVectorF(1195, 970));
		nextPhaseButton.setWidth(70);
		nextPhaseButton.setHeight(70);

		g.setColor(UIInit.FONT_COLOR);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - DoaSprites.get("MainMenuBottomRing").getHeight() + 6);

		g.drawImage(LEFT, Main.WINDOW_WIDTH * 0.304f, Main.WINDOW_HEIGHT - LEFT.getHeight());
		g.drawImage(RIGHT, Main.WINDOW_WIDTH * 0.585f, Main.WINDOW_HEIGHT - RIGHT.getHeight());

		String phaseText = GameManager.currentPhase.name();
		DoaVectorF phaseArea = new DoaVectorF(Main.WINDOW_WIDTH * 0.070f, Main.WINDOW_HEIGHT * 0.046f);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, phaseArea, phaseText)));
		g.drawString(GameManager.currentPhase.name(), Main.WINDOW_WIDTH * 0.615f, Main.WINDOW_HEIGHT * 0.993f);

		g.drawImage(MIDDLE, (Main.WINDOW_WIDTH - MIDDLE.getWidth()) / 2f, Main.WINDOW_HEIGHT - MIDDLE.getHeight());

		g.drawImage(garrisonSprite, garrisonTopLeft.x, garrisonTopLeft.y);
		g.drawImage(DoaSprites.get("garrisonHolderIcon"), Main.WINDOW_WIDTH * 0.527f, Main.WINDOW_HEIGHT * 0.842f);
		g.drawImage(ownerSprite, ownerTopLeft.x, ownerTopLeft.y);
		g.drawImage(DoaSprites.get("ownerHolderIcon"), Main.WINDOW_WIDTH * 0.539f, Main.WINDOW_HEIGHT * 0.880f);
		g.drawImage(nameSprite, nameTopLeft.x, nameTopLeft.y);
		g.drawImage(DoaSprites.get("provinceNameHolderIcon"), Main.WINDOW_WIDTH * 0.552f, Main.WINDOW_HEIGHT * 0.918f);
		g.drawImage(continentSprite, continentTopLeft.x, continentTopLeft.y);
		g.drawImage(DoaSprites.get("continentHolderIcon"), Main.WINDOW_WIDTH * 0.559f, Main.WINDOW_HEIGHT * 0.953f);

		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, 25f));
		FontMetrics fm = g.getFontMetrics();

		g.drawString(garrisonText, garrisonTopLeft.x + (garrisonSprite.getWidth() - fm.stringWidth(garrisonText)) / 2f, garrisonTopLeft.y * 1.031f);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, 30f));
		fm = g.getFontMetrics();
		if (clickedProvince != null) {
			g.setColor(GameManager.clickedHitArea.getProvince().getOwner().getColor());
		}
		g.drawString(ownerText, ownerTopLeft.x + (ownerSprite.getWidth() - fm.stringWidth(ownerText)) / 2f, ownerTopLeft.y * 1.03f);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN,
		        Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, new DoaVectorF(nameSprite.getWidth() * 0.95f, nameSprite.getHeight()), nameText)));
		g.setColor(UIInit.FONT_COLOR);
		fm = g.getFontMetrics();
		g.drawString(nameText, nameTopLeft.x + (nameSprite.getWidth() - fm.stringWidth(nameText)) / 2f, nameTopLeft.y * 1.03f);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN,
		        Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, new DoaVectorF(continentSprite.getWidth() * 0.95f, continentSprite.getHeight()), continentText)));
		fm = g.getFontMetrics();
		g.drawString(continentText, continentTopLeft.x + (continentSprite.getWidth() - fm.stringWidth(continentText)) / 2f, continentTopLeft.y * 1.03f);
	}
}
