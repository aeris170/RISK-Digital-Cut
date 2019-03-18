package com.pmnm.risk.globals;

import com.pmnm.risk.asset.AssetLoader;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;
import com.pmnm.risk.map.MapLoader;

public final class Globals {

	private Globals() {
		throw new RiskStaticInstantiationException(getClass());
	}
	
	public static void initilaizeGlobals() {
		MapLoader.readMapData();
		AssetLoader.initializeAssets();
	}
}
