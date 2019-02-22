package assets;

import java.io.IOException;

import com.doa.engine.graphics.DoaSprites;

public class AssetLoader {
		
	public static void initializeAssets() {
		try {
			DoaSprites.createSprite("WorldMap", "/maps/ColorMapBig.png");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}