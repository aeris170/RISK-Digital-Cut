package main;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.SwingUtilities;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaEngine;
import com.doa.engine.DoaHandler;
import com.doa.engine.DoaWindow;
import com.doa.engine.input.DoaMouse;

import assets.AssetLoader;
import map.MapLoader;

public class Main {

	public static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int WINDOW_WIDTH = gd.getDisplayMode().getWidth();
	public static final int WINDOW_HEIGHT = gd.getDisplayMode().getHeight();

	static DoaWindow w;
	static DoaEngine e;

	public static void main(final String[] args) {
		DoaEngine.DEBUG_ENABLED = true;
		DoaEngine.MULTI_THREAD_ENABLED = true;
		DoaEngine.CLEAR_COLOR = new Color(71, 40, 11);
		DoaMouse.clampWheel(1d, 4d);

		AssetLoader.initializeAssets();

		w = DoaWindow.createWindow();
		e = new DoaEngine();

		DoaCamera.setTweenAmountX(1f);
		DoaCamera.setTweenAmountY(1f);
		DoaCamera.enableMouseZoom(null, 1f, 4f);
		DoaCamera.adjustCamera(DoaHandler.instantiateDoaObject(Camera.class, WINDOW_WIDTH / 2f, WINDOW_HEIGHT / 2f), -10000, -10000, 10000, 10000);
		DoaHandler.instantiateDoaObject(GameBoard.class, 0f, 0f, WINDOW_WIDTH, WINDOW_HEIGHT);
		DoaHandler.instantiateDoaObject(DebugPanel.class);

		SwingUtilities.invokeLater(() -> configureGUI());

		MapLoader.readMapData(null);
		MapLoader.CONTINENTS.forEach((s, c) -> System.out.println(c.toString()));
		// System.exit(0);
	}

	private static void configureGUI() {
		w.setTitle("DoaEngine Test Window!");
		w.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		w.setLocation(0, 0);
		w.setUndecorated(true);
		w.setResizable(false);
		w.setVisible(true);
		w.add(e);
	}
}