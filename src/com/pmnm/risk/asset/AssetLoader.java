package com.pmnm.risk.asset;

import java.io.IOException;

import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;

public final class AssetLoader {

	private AssetLoader() {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void initializeAssets() {
		try {
			DoaSprites.createSprite("WorldMapOld", "/maps/legacy/ColorMapBig.png");
			DoaSprites.createSprite("WorldMapNames", "/maps/legacy/ColorMapNamesNew.png");
			DoaSprites.createSprite("DummyMap", "/maps/legacy/DummyMap2.png");
			DoaSprites.createSprite("WorldMap", "/maps/WorldMapNoIslands1.png");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}