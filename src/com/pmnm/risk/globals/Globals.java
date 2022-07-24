package com.pmnm.risk.globals;

import com.pmnm.risk.globals.discordrichpresence.DiscordRichPresenceAdapter;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.ui.UIConstants;

import lombok.experimental.UtilityClass;
import pmnm.risk.map.MapConfig;

@UtilityClass
public final class Globals {
	
	public static final String GAME_VERSION = "ALPHA1";
	public static final int TICK_RATE = 240;
	
	public static final int UNKNOWN_TROOP_COUNT = -38145124;

	public static final int MAX_NUM_PLAYERS = 6;

	public static void initilaizeGlobals() {
		AssetLoader.initializeAssets();
		MapConfig.readMapConfigs();
		Translator.getInstance();
		DiscordRichPresenceAdapter.initialize();
		UIConstants.initUI();
	}

}