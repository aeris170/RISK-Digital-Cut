package toolkit;

import com.doa.engine.DoaCamera;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;

import exceptions.RiskStaticInstantiationException;
import main.Camera;
import main.Main;

public final class Utils {

	private Utils() throws RiskStaticInstantiationException {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static float mapXCoordinateByZoom(final float x) {
		return mapCoordinatesByZoom(x, 0).x;
	}

	public static float mapYCoordinateByZoom(final float y) {
		return mapCoordinatesByZoom(0, y).y;
	}

	public static DoaVectorF mapCoordinatesByZoom(final float x, final float y) {
		return mapCoordinatesByZoom(new DoaVectorF(x, y));
	}

	public static DoaVectorF mapCoordinatesByZoom(final DoaVectorF coordinateToBeMapped) {
		final float cx = Main.WINDOW_WIDTH / 2f;
		final float cy = Main.WINDOW_HEIGHT / 2f;
		final float z = DoaCamera.getZ();
		final float mx = coordinateToBeMapped.x - cx;
		final float my = coordinateToBeMapped.y - cy;
		return new DoaVectorF(mx * z + cx, my * z + cy);
	}

	// XXX why the flying f****** f*** the author of DoaEngine did not implement
	// this to his OWN ENGINE??????
	// P.S. the author is me.
	// TO DO: f*** myself
	// TODO CHECK IF THIS FUNCTION WORKS FLAWLESSLY. Assigned to: Fazilet Simge ER
	// in order to do this, look at the top left of the screen. there are text that
	// shows the mouse position.
	// zoom in to arbitrary locations and check if the mouse coordinate is mapped
	// correctly.
	// <3
	public static DoaVectorF mapMouseCoordinatesByZoom() {
		final DoaVectorF mouseCoordinates = new DoaVectorF((float) DoaMouse.X, (float) DoaMouse.Y);
		final float cx = Main.WINDOW_WIDTH / 2f;
		final float cy = Main.WINDOW_HEIGHT / 2f;
		final float camx = Camera.INSTANCE.getPosition().x;
		final float camy = Camera.INSTANCE.getPosition().y;
		final float z = DoaCamera.getZ();
		final float mx = mouseCoordinates.x - camx;
		final float my = mouseCoordinates.y - camy;
		final DoaVectorF distance = new DoaVectorF(mx * z + cx, my * z + cy);
		return mouseCoordinates.add(distance.sub(mouseCoordinates).mul(-1f / DoaCamera.getZ()));
	}
}
