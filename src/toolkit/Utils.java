package toolkit;

import com.doa.engine.DoaCamera;
import com.doa.maths.DoaVectorF;

import exceptions.RiskStaticInstantiationException;
import main.Main;

public final class Utils {

	private Utils() throws RiskStaticInstantiationException {
		throw new RiskStaticInstantiationException(getClass());
	}

	// FIXME
	public static float mapXCoordinateByZoom(final float x) {
		return mapCoordinateByZoom(x, 0).x;
	}

	// FIXME
	public static float mapYCoordinateByZoom(final float y) {
		return mapCoordinateByZoom(0, y).y;
	}

	// FIXME
	public static DoaVectorF mapCoordinateByZoom(final float x, final float y) {
		final DoaVectorF currentOffset = new DoaVectorF(
		        Main.WINDOW_WIDTH / 2f + (Main.WINDOW_WIDTH / 2f - (Main.WINDOW_WIDTH / 2f * DoaCamera.getZ())) / DoaCamera.getZ(),
		        Main.WINDOW_HEIGHT / 2f + (Main.WINDOW_HEIGHT / 2f - (Main.WINDOW_HEIGHT / 2f * DoaCamera.getZ())) / DoaCamera.getZ());
		return new DoaVectorF(x, y).sub(currentOffset);
	}

	// FIXME
	public static DoaVectorF mapCoordinateByZoom(final DoaVectorF coordinateToBeMapped) {
		final DoaVectorF currentOffset = new DoaVectorF(
		        Main.WINDOW_WIDTH / 2f + (Main.WINDOW_WIDTH / 2f - (Main.WINDOW_WIDTH / 2f * DoaCamera.getZ())) / DoaCamera.getZ(),
		        Main.WINDOW_HEIGHT / 2f + (Main.WINDOW_HEIGHT / 2f - (Main.WINDOW_HEIGHT / 2f * DoaCamera.getZ())) / DoaCamera.getZ());
		return coordinateToBeMapped.clone().sub(currentOffset);
	}
}
