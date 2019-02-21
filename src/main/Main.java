package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaEngine;
import com.doa.engine.DoaHandler;
import com.doa.engine.DoaWindow;

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
		DoaCamera.enableMouseZoom(0.1f, 10f);

		w = DoaWindow.createWindow();
		e = new DoaEngine();

		DoaHandler.instantiateDoaObject(TestObject.class, 910f, 490f);

		SwingUtilities.invokeLater(() -> configureGUI());

		MapLoader.readMapData(null);
		//MapLoader.PROVINCES.forEach((name, province)->System.out.println(province.getName() + " " + Arrays.toString(province.getNeighbours().toArray())));
		for(String k : MapLoader.PROVINCES.keySet()) {
			System.out.println(MapLoader.PROVINCES.get(k).getContinent().getName() + "\t\t" + MapLoader.PROVINCES.get(k).getName());
		}
		System.exit(0);
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