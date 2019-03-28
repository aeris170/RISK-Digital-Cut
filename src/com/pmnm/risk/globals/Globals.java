package com.pmnm.risk.globals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pmnm.risk.asset.AssetLoader;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;
import com.pmnm.risk.map.MapLoader;
import com.pmnm.risk.ui.UIInit;

public final class Globals {

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	private Globals() {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void initilaizeGlobals() {
		try {
			EXECUTOR.execute(() -> MapLoader.readMapData());
			EXECUTOR.execute(() -> AssetLoader.initializeAssets());
			EXECUTOR.shutdown();
			EXECUTOR.awaitTermination(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
		UIInit.initUI();
	}
}
