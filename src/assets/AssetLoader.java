package assets;

import java.io.IOException;

import com.doa.engine.graphics.DoaSprites;

import exceptions.RiskStaticInstantiationException;

public final class AssetLoader {

	private AssetLoader() throws RiskStaticInstantiationException {
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