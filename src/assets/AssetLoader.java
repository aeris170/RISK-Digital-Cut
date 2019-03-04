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
			DoaSprites.createSprite("WorldMap", "/maps/ColorMapBig.png");
			DoaSprites.createSprite("WorldMapNames", "/maps/ColorMapNamesNew.png");
			DoaSprites.createSprite("DummyMap", "/maps/DummyMap2.png");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}