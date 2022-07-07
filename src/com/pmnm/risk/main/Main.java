package com.pmnm.risk.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.ImageIcon;

import com.pmnm.risk.globals.Globals;

import doa.engine.core.DoaEngineSettings;
import doa.engine.core.DoaGame;
import doa.engine.core.DoaRenderingMode;
import doa.engine.core.DoaWindowSettings;

public class Main extends DoaGame {

	public static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int WINDOW_WIDTH = gd.getDisplayMode().getWidth();
	public static final int WINDOW_HEIGHT = gd.getDisplayMode().getHeight();

	public static void main(final String[] args) { launch(args); }

	@Override
	public void initialize(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		Locale.setDefault(Locale.ENGLISH);
		eSettings.TICK_RATE = 240;
		eSettings.RENDERING_MODE = DoaRenderingMode.BALANCED;

		wSettings.TITLE = "RISK Digital Cut!";
		wSettings.DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(Main.class.getResource("/ui/cursor1.png")).getImage(), new Point(0, 0),
		        "Kaan's Cursor");
		wSettings.ICON = new ImageIcon(Main.class.getResource("/ui/icon.png")).getImage();

		Globals.initilaizeGlobals();
	}
}
