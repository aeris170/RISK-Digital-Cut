package com.pmnm.risk.globals.discordrichpresence;

import java.time.Instant;

import doa.engine.utils.discordapi.DoaDiscordActivity;
import doa.engine.utils.discordapi.DoaDiscordService;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class DiscordRichPresenceAdapter {
	
	private static final long DISCORD_APP_ID = 1000135041311060039L;
	private static final String APP_DESCRIPTION = "Fighting over World Domination";
	private static final String APP_DETAIL = "Playing";
	private static final String LARGE_ICON = "icon512";
	
	private static boolean isInitialized = false;
	private static Instant startupTimestamp = null;
	
	public static void initialize() {
		if (isInitialized) { return; }
		
		startupTimestamp = Instant.now();
		
		DoaDiscordService.init(DISCORD_APP_ID);
		switchToDefaultActivity();
		
		isInitialized = true;
	}
	
	public static void switchToDefaultActivity() {
		DoaDiscordService.switchActivity(getDefaultActivity());
	}
	
	public static DoaDiscordActivity getDefaultActivity() {
		DoaDiscordActivity act = new DoaDiscordActivity();
		
		act.setName(APP_DESCRIPTION);
		act.setDescription(APP_DETAIL);

		act.setStart(startupTimestamp);
		
		act.setLargeImageName(LARGE_ICON);
		
		return act;
	}
}
