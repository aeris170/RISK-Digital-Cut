package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;

public class Camera extends DoaObject {

	public static final float CAMERA_SPEED = 2f;

	public Camera(Float x, Float y) {
		super(x, y);
	}

	@Override
	public void tick() {
		if (DoaKeyboard.ESCAPE) {
			System.exit(0);
		}
		if (DoaCamera.getZ() != 1) {
			if (DoaKeyboard.A) {
				position.x -= CAMERA_SPEED / DoaCamera.getZ();
			}
			if (DoaKeyboard.D) {
				position.x += CAMERA_SPEED / DoaCamera.getZ();
			}
			if (DoaKeyboard.W) {
				position.y -= CAMERA_SPEED / DoaCamera.getZ();
			}
			if (DoaKeyboard.S) {
				position.y += CAMERA_SPEED / DoaCamera.getZ();
			}
		} else {
			position = new DoaVectorF((float)DoaMouse.X, (float)DoaMouse.Y);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Camera :" + position.toString(), 0, 40);
		g.setColor(Color.RED);
		g.fillRect(position.x - 1, position.y - 1, 2, 2);
	}

	@Override
	public Shape getBounds() {
		return null;
	}
}
