package com.pmnm.risk.main;

import java.awt.Color;
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
		Locale.setDefault(Locale.ENGLISH);
		DoaEngine.DEBUG_ENABLED = false;
		DoaEngine.MULTI_THREAD_ENABLED = false;
		DoaEngine.CLEAR_COLOR = new Color(3, 0, 47);
		DoaEngine.RENDERING_MODE = DoaRenderingMode.BALANCED;
		DoaMouse.clampWheel(0.8d, 10d);

		Globals.initilaizeGlobals();

		w = DoaWindow.createWindow();
		e = new DoaEngine();

		DoaCamera.setTweenAmountX(1f);
		DoaCamera.setTweenAmountY(1f);
		DoaCamera.enableMouseZoom(null, 0.8f, 10f);

		DoaCamera.adjustCamera(DoaHandler.instantiateDoaObject(Camera.class, WINDOW_WIDTH / 2f, WINDOW_HEIGHT / 2f), -10000, -10000, 10000, 10000);
		DoaHandler.instantiateDoaObject(GameBoard.class, 0f, 0f, WINDOW_WIDTH, WINDOW_HEIGHT);
		DoaHandler.instantiateDoaObject(DebugPanel.class);

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