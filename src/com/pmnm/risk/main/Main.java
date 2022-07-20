package com.pmnm.risk.main;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.ImageIcon;

import com.pmnm.risk.globals.Globals;

import doa.engine.core.DoaEngineSettings;
import doa.engine.core.DoaGame;
import doa.engine.core.DoaRenderingMode;
import doa.engine.core.DoaWindowMode;
import doa.engine.core.DoaWindowSettings;
import doa.engine.maths.DoaVector;

public class Main extends DoaGame {

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;

	public static void main(final String[] args) { launch(args); }

	@Override
	public void initializeEngine(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		Locale.setDefault(Locale.ENGLISH);
		//DoaLogger.LOGGER.setLevel(LogLevel.FINEST);
		eSettings.REFERENCE_RESOLUTION = new DoaVector(WINDOW_WIDTH, WINDOW_HEIGHT);
		eSettings.TICK_RATE = 240;
		eSettings.RENDERING_MODE = DoaRenderingMode.BALANCED;

		wSettings.TITLE = "RISK Digital Cut!";
		wSettings.DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon(Main.class.getResource("/ui/cursor1.png")).getImage(),
			new Point(0, 0),
	        "Kaan's Cursor"
		);
		wSettings.ICON = new ImageIcon(Main.class.getResource("/ui/icon.png")).getImage();
		wSettings.RESOLUTION_OD = new DoaVector(WINDOW_WIDTH, WINDOW_HEIGHT);
		wSettings.WM = DoaWindowMode.WINDOWED;

		Globals.initilaizeGlobals();
	}
	
	@Override
	public void initializeGame(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		Locale.setDefault(Locale.ENGLISH);
		//DoaLogger.LOGGER.setLevel(LogLevel.FINEST);
		eSettings.REFERENCE_RESOLUTION = new DoaVector(WINDOW_WIDTH, WINDOW_HEIGHT);
		eSettings.TICK_RATE = 240;
		eSettings.RENDERING_MODE = DoaRenderingMode.BALANCED;

		wSettings.TITLE = "RISK Digital Cut!";
		wSettings.DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon(Main.class.getResource("/ui/cursor1.png")).getImage(),
			new Point(0, 0),
	        "Kaan's Cursor"
		);
		wSettings.ICON = new ImageIcon(Main.class.getResource("/ui/icon.png")).getImage();
		wSettings.RESOLUTION_OD = new DoaVector(WINDOW_WIDTH, WINDOW_HEIGHT);
		wSettings.WM = DoaWindowMode.WINDOWED;

		Globals.initilaizeGlobals();
	}
}
