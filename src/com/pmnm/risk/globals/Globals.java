package com.pmnm.risk.globals;

import com.pmnm.risk.globals.discordrichpresence.DiscordRichPresenceAdapter;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.input.DoaKeyboard;
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.task.DoaTaskGuard;
import doa.engine.task.DoaTasker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import pmnm.risk.map.MapConfig;

@UtilityClass
public final class Globals {

	public static final String GAME_VERSION = "ALPHA1";
	public static final int TICK_RATE = 240;
	public static final int DEFAULT_TIME_SLICE = TICK_RATE / 10;

	public static final int UNKNOWN_TROOP_COUNT = -38145124;

	public static final int MAX_NUM_PLAYERS = 6;

	public static void initilaizeGlobals() {
		AssetLoader.initializeAssets();
		MapConfig.readMapConfigs();
		Translator.getInstance();
		DiscordRichPresenceAdapter.initialize();
		UIConstants.initUI();
	}

	public static void destroyGlobals() {
		DiscordRichPresenceAdapter.destroy();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class GlobalTicker {

		private static DoaTaskGuard guard = new DoaTaskGuard();

		@SuppressWarnings("all")
		public static void tick(Object userData) {
			if (guard.get() && DoaKeyboard.CTRL && DoaKeyboard.ALT && DoaKeyboard.F3) {
				DoaTasker.guard(guard, 1_000);

				DoaSceneHandler.getLoadedScene().getObjects().forEach(o ->
					o.getComponentsView().stream()
						.filter(DoaRenderer.class::isInstance)
						.map(c -> (DoaRenderer) c)
						.forEach(r -> r.enableDebugRender = !r.enableDebugRender)
				);
			}
		}
	}
}
