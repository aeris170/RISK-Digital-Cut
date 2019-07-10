package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIInit;
import com.pmnm.roy.ui.gameui.actions.CenterPieceButtonAction;
import com.pmnm.roy.ui.gameui.actions.DecrementButtonAction;
import com.pmnm.roy.ui.gameui.actions.IncrementButtonAction;
import com.pmnm.roy.ui.gameui.actions.NextPhaseButtonAction;

public class BottomPanel extends DoaPanel {

	public static BottomPanel INSTANCE;
	private static final long serialVersionUID = -59674351675589726L;

	private static final BufferedImage MIDDLE = DoaSprites.get("gaugeBig");
	private static final BufferedImage LEFT = DoaSprites.get("gaugeLeft");
	private static final BufferedImage RIGHT = DoaSprites.get("gaugeRight");

	public static DoaImageButton nextPhaseButton = DoaHandler.instantiate(DoaImageButton.class, Main.WINDOW_WIDTH * 0.622f, Main.WINDOW_HEIGHT * 0.898f, 70, 70,
	        DoaSprites.get("nextPhaseButtonIdle"), DoaSprites.get("nextPhaseButtonHover"), DoaSprites.get("nextPhaseButtonPressed"),
	        DoaSprites.get("nextPhaseButtonDisabled"));
	public static DoaImageButton decrementButton = DoaHandler.instantiate(DoaImageButton.class, Main.WINDOW_WIDTH * 0.363f, Main.WINDOW_HEIGHT * 0.958f, 41, 36,
	        DoaSprites.get("arrowDown"), DoaSprites.get("arrowDownHover"), DoaSprites.get("arrowDownPress"));
	public static DoaImageButton incrementButton = DoaHandler.instantiate(DoaImageButton.class, Main.WINDOW_WIDTH * 0.363f, Main.WINDOW_HEIGHT * 0.912f, 41, 36,
	        DoaSprites.get("arrowUp"), DoaSprites.get("arrowUpHover"), DoaSprites.get("arrowUpPress"));
	public static SpinnerCenterPiece centerPiece = DoaHandler.instantiate(SpinnerCenterPiece.class,
	        new DoaVectorF(Main.WINDOW_WIDTH * 0.320f, Main.WINDOW_HEIGHT * 0.925f), DoaSprites.get("centerPiece").getWidth(), DoaSprites.get("centerPiece").getHeight(),
	        DoaSprites.get("centerPiece"), DoaSprites.get("centerPiece"), "", Color.BLACK, Color.RED);

	public static List<Integer> spinnerValues;
	public static int index = 0;

	public BottomPanel() {
		super(0f, 0f, 0, 0);
		if (INSTANCE != null) {
			DoaHandler.remove(INSTANCE);
		}
		nextPhaseButton.addAction(new NextPhaseButtonAction(nextPhaseButton));
		decrementButton.addAction(new DecrementButtonAction(this));
		incrementButton.addAction(new IncrementButtonAction(this));
		centerPiece.addAction(new CenterPieceButtonAction());
		nextPhaseButton.disable();
		add(nextPhaseButton);
		add(decrementButton);
		add(incrementButton);
		add(centerPiece);
		show();
		INSTANCE = this;
	}

	@Override
	public void tick() {
		if (GameManager.INSTANCE.isPaused) {
			nextPhaseButton.disable();
			decrementButton.disable();
			incrementButton.disable();
			centerPiece.disable();
		}
		try {
			centerPiece.setText(spinnerValues != null ? "" + spinnerValues.get(index) : "");
		} catch (Exception ex) {
			// TODO have no idea why this catch block is here, if you come across an
			// exception getting thrown, feel free to fix.
			// and don't ask me how do i fix this, just fix it ffs.
			ex.printStackTrace();
		}
	}

	public static void signal() {
		BottomPanel.nextPhaseButton.enable();
		BottomPanel.decrementButton.enable();
		BottomPanel.incrementButton.enable();
		BottomPanel.centerPiece.enable();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		GameManager gm = GameManager.INSTANCE;
		Province clickedProvince = gm.clickedHitArea != null ? gm.clickedHitArea.getProvince() : null;

		String garrisonText = "";
		BufferedImage garrisonSprite = DoaSprites.get("garrisonHolder");
		DoaVectorF garrisonTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.454f, Main.WINDOW_HEIGHT * 0.836f);

		String ownerText = "";
		BufferedImage ownerSprite = DoaSprites.get("ownerHolder");
		DoaVectorF ownerTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.447f, Main.WINDOW_HEIGHT * 0.874f);

		String nameText = "";
		BufferedImage nameSprite = DoaSprites.get("provinceNameHolder");
		DoaVectorF nameTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.436f, Main.WINDOW_HEIGHT * 0.912f);

		String continentText = "";
		BufferedImage continentSprite = DoaSprites.get("continentHolder");
		DoaVectorF continentTopLeft = new DoaVectorF(Main.WINDOW_WIDTH * 0.430f, Main.WINDOW_HEIGHT * 0.950f);

		if (clickedProvince != null) {
			garrisonText += clickedProvince.getTroops() != -1 ? clickedProvince.getTroops() : "???";
			ownerText += clickedProvince.getOwner().getName();
			nameText += clickedProvince.getName().toUpperCase();
			continentText += clickedProvince.getContinent().getName().toUpperCase();
		}
		g.setColor(UIInit.FONT_COLOR);

		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - DoaSprites.get("MainMenuBottomRing").getHeight() + 6d);

		g.drawImage(LEFT, Main.WINDOW_WIDTH * 0.304f, (double) Main.WINDOW_HEIGHT - LEFT.getHeight());
		g.drawImage(RIGHT, Main.WINDOW_WIDTH * 0.585f, (double) Main.WINDOW_HEIGHT - RIGHT.getHeight());

		String phaseText = gm.currentPhase.name();
		DoaVectorF phaseArea = new DoaVectorF(Main.WINDOW_WIDTH * 0.070f, Main.WINDOW_HEIGHT * 0.046f);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, Utils.findMaxFontSizeToFitInArea(g, UIInit.UI_FONT, phaseArea, phaseText)));
		g.drawString(gm.currentPhase.name(), Main.WINDOW_WIDTH * 0.615f, Main.WINDOW_HEIGHT * 0.993f);

		g.drawImage(MIDDLE, (Main.WINDOW_WIDTH - MIDDLE.getWidth()) / 2f, (double) Main.WINDOW_HEIGHT - MIDDLE.getHeight());

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
			g.setColor(gm.clickedHitArea.getProvince().getOwner().getColor());
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

	public static void updateSpinnerValues(int lowerLimit, int upperLimit) {
		spinnerValues = new ArrayList<>();
		for (int i = lowerLimit; i <= upperLimit; i++) {
			spinnerValues.add(i);
		}
		index = spinnerValues.size() - 1;
		centerPiece.setText("" + spinnerValues.get(index));
	}

	public void incrementIndex() {
		index++;
		index += spinnerValues.size();
		index %= spinnerValues.size();
	}

	public void decrementIndex() {
		index--;
		index += spinnerValues.size();
		index %= spinnerValues.size();
	}

	public static void nullSpinner() {
		spinnerValues = null;
		centerPiece.setText("");
	}
}
