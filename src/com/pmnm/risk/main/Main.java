package com.pmnm.risk.main;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.doa.engine.DoaEngine;
import com.doa.engine.DoaRenderingMode;
import com.doa.engine.DoaWindow;
import com.doa.engine.log.LogLevel;
import com.pmnm.risk.globals.Globals;

public class Main {

	public static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int WINDOW_WIDTH = gd.getDisplayMode().getWidth();
	public static final int WINDOW_HEIGHT = gd.getDisplayMode().getHeight();

	static DoaWindow w;
	static DoaEngine e;

	public static void main(final String[] args) {
		DoaEngine.TICK_RATE = 240;
		Locale.setDefault(Locale.ENGLISH);
		DoaEngine.INTERNAL_LOG_LEVEL = LogLevel.FINE;
		DoaEngine.RENDERING_MODE = DoaRenderingMode.BALANCED;

		Globals.initilaizeGlobals();

		w = DoaWindow.createWindow();
		e = new DoaEngine();

		SwingUtilities.invokeLater(() -> configureGUI(true));
	}

	private static void configureGUI(boolean isFullscreen) {
		w.setTitle("CS319 RISK!");
		w.setUndecorated(true);
		w.setResizable(false);
		w.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(Main.class.getResource("/ui/cursor1.png")).getImage(), new Point(0, 0), "Kaan's Cursor"));
		w.setVisible(true);
		if (isFullscreen) {
			w.setExtendedState(Frame.MAXIMIZED_BOTH);
			gd.setFullScreenWindow(w);
		} else {
			w.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			w.setLocation(0, 0);
		}
		w.add(e);
	}
}