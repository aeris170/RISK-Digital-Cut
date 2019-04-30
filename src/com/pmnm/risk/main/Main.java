package com.pmnm.risk.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Locale;

import javax.swing.SwingUtilities;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaEngine;
import com.doa.engine.DoaHandler;
import com.doa.engine.DoaRenderingMode;
import com.doa.engine.DoaWindow;
import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.map.board.GameBoard;

public class Main {

	public static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int WINDOW_WIDTH = gd.getDisplayMode().getWidth();
	public static final int WINDOW_HEIGHT = gd.getDisplayMode().getHeight();

	static DoaWindow w;
	static DoaEngine e;

	public static void main(final String[] args) {
		DoaEngine.TICK_RATE = 240;
		Locale.setDefault(Locale.ENGLISH);
		DoaEngine.DEBUG_ENABLED = true;
		DoaEngine.RENDERING_MODE = DoaRenderingMode.BALANCED;
		DoaMouse.clampWheel(0.8d, 10d);

		Globals.initilaizeGlobals();

		w = DoaWindow.createWindow();
		e = new DoaEngine();

		DoaCamera.setTweenAmountX(1f);
		DoaCamera.setTweenAmountY(1f);
		DoaCamera.enableMouseZoom(null, 0.8f, 10f);

		DoaCamera.adjustCamera(Camera.getInstance(), -10000, -10000, 10000, 10000);
		GameBoard.getInstance();
		DoaHandler.instantiate(DebugPanel.class);

		SwingUtilities.invokeLater(() -> configureGUI());
	}

	private static void configureGUI() {
		w.setTitle("CS319 RISK!");
		w.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		w.setLocation(0, 0);
		w.setUndecorated(true);
		w.setResizable(false);
		w.setVisible(true);
		w.add(e);
	}
}