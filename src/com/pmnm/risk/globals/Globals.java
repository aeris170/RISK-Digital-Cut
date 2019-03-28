package com.pmnm.risk.globals;

import com.pmnm.risk.asset.AssetLoader;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;
import com.pmnm.risk.map.MapLoader;
import com.pmnm.risk.ui.UIInit;

public final class Globals {

	private Globals() {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void initilaizeGlobals() {
		MapLoader.readMapData();
		AssetLoader.initializeAssets();
		UIInit.initUI();
	}
}
