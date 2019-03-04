package provinces;

import java.awt.Rectangle;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;

import toolkit.Utils;

public class ProvinceHitBox extends DoaObject {

	private static final long serialVersionUID = -6848368535793292243L;

	private Province owner;

	public ProvinceHitBox(Province owner, Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
		this.owner = owner;
	}

	@Override
	public void tick() {
		DoaVectorF mappedMouseCoords = Utils.mapMouseCoordinatesByZoom();
		if (getBounds().contains((int) mappedMouseCoords.x, (int) mappedMouseCoords.y)) {
			System.out.println(owner.toString());
			System.out.println(position);
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.setColor(owner.getColor());
		g.fill(getBounds());
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) position.x, (int) position.y, width, height);
	}
}
