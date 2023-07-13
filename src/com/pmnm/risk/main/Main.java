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
import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;
import doa.engine.maths.DoaVector;

public class Main extends DoaGame {

	public static final int WINDOW_WIDTH = 1600;
	public static final int WINDOW_HEIGHT = 900;

	public static void main(final String[] args) { launch(args); }

	@Override
	public void initializeEngine(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		Locale.setDefault(Locale.ENGLISH);
		DoaLogger.LOGGER.setLevel(LogLevel.FINEST);
		eSettings.REFERENCE_RESOLUTION = new DoaVector(1920, 1080);
		eSettings.TICK_RATE = Globals.TICK_RATE;
		eSettings.RENDERING_MODE = DoaRenderingMode.BALANCED;
		eSettings.AXIS_HELPERS = true;
		eSettings.GLOBAL_TICK = Globals.GlobalTicker::tick;

		wSettings.TITLE = "RISK Digital Cut!";
		wSettings.DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon(Main.class.getResource("/ui/cursor1.png")).getImage(),
			new Point(0, 0),
			"Risk Cursor"
		);
		wSettings.ICON = new ImageIcon(Main.class.getResource("/ui/icon.png")).getImage();
		wSettings.RESOLUTION_OD = new DoaVector(WINDOW_WIDTH, WINDOW_HEIGHT);
		wSettings.WM = DoaWindowMode.WINDOWED;
	}

	@Override
	public void initializeGame(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		Globals.initilaizeGlobals();
	}

	@Override
	public void onExit() {
		Globals.destroyGlobals();
	}
}
