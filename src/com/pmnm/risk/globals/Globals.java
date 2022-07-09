package com.pmnm.risk.globals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.pmnm.risk.asset.AssetLoader;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.map.MapConfig;
import com.pmnm.roy.ui.UIInit;

import doa.engine.graphics.DoaSprites;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Globals {
	
	public static final String GAME_VERSION = "ALPHA1";
	
	public static final int UNKNOWN_TROOP_COUNT = -38145124;

	public static final Color PROVINCE_UNOCCUPIED = Color.WHITE;
	public static final Color PROVINCE_UNOCCUPIED_BORDER = Color.BLACK;
	public static final Color PROVINCE_SELECTED_BORDER = Color.CYAN;
	public static final Color PROVINCE_EMPHASIZE = Color.GREEN;
	public static final Color PROVINCE_HIGHLIGHT = Color.GRAY.darker().darker().darker().darker();
	public static final int MAX_NUM_PLAYERS = 6;

	public static void initilaizeGlobals() {
		AssetLoader.initializeAssets();
		MapConfig.readMapConfigs();
		Translator.getInstance();
		UIInit.initUI();
	}

}