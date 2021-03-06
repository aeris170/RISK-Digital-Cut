package com.pmnm.risk.globals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.asset.AssetLoader;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.ui.UIInit;

public final class Globals {

	public static final Color PROVINCE_UNOCCUPIED = Color.WHITE;
	public static final Color PROVINCE_UNOCCUPIED_BORDER = Color.BLACK;
	public static final Color PROVINCE_SELECTED_BORDER = Color.CYAN;
	public static final Color PROVINCE_EMPHASIZE = Color.GREEN;
	public static final Color PROVINCE_HIGHLIGHT = Color.GRAY.darker().darker().darker().darker();
	public static final int MAX_NUM_PLAYERS = 6;
	public static String[] MAP_NAMES;
	public static BufferedImage[] MAP_IMAGES;

	private Globals() {}

	public static void initilaizeGlobals() {
		AssetLoader.initializeAssets();
		fetchMapNamesAndImages();
		Translator.getInstance();
		UIInit.initUI();
	}

	private static void fetchMapNamesAndImages() {
		File[] maps = new File("res/maps/").listFiles();
		MAP_NAMES = Arrays.stream(maps).filter(map -> map.isDirectory()).map(mapFolder -> mapFolder.getName().toUpperCase().replaceAll("_", " ").trim())
		        .toArray(String[]::new);
		MAP_IMAGES = new BufferedImage[MAP_NAMES.length];
		int i = 0;
		for (File f : maps) {
			if (f.isDirectory()) {
				try {
					System.out.println(f.getPath());
					String path = f.getPath().replaceAll("\\\\", "/");
					DoaSprites.createSprite("MAP#" + i, path.substring(path.indexOf("/"), path.length()) + "/map.png");
					i++;
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}