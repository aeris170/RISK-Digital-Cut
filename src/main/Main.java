package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.SwingUtilities;

import com.doa.engine.DoaEngine;
import com.doa.engine.DoaHandler;
import com.doa.engine.DoaWindow;

public class Main {

	public static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int WINDOW_WIDTH = gd.getDisplayMode().getWidth();
	public static final int WINDOW_HEIGHT = gd.getDisplayMode().getHeight();

	static DoaWindow w;
	static DoaEngine e;

	public static void main(final String[] args) {
		DoaEngine.DEBUG_ENABLED = true;
		DoaEngine.MULTI_THREAD_ENABLED = true;

		w = DoaWindow.createWindow();
		e = new DoaEngine();

		DoaHandler.instantiateDoaObject(TestObject.class, 200f, 200f);

		SwingUtilities.invokeLater(() -> configureGUI());
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