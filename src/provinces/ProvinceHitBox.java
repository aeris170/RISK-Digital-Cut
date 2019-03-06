package provinces;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.List;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;

import toolkit.Utils;

public class ProvinceHitBox extends DoaObject {

	private static final long serialVersionUID = -6848368535793292243L;

	private Province owner;
	private GeneralPath ownerHitBoxPath;

	public ProvinceHitBox(Province owner, Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
		this.owner = owner;
		ownerHitBoxPath = new GeneralPath();
		List<DoaVectorF> ownerHitBoxVertices = owner.getVertices();
		DoaVectorF startPoint = ownerHitBoxVertices.get(0);
		ownerHitBoxPath.moveTo(startPoint.x, startPoint.y);
		for (int i = 1; i < ownerHitBoxVertices.size(); i++) {
			DoaVectorF nextPoint = ownerHitBoxVertices.get(i);
			ownerHitBoxPath.lineTo(nextPoint.x, nextPoint.y);
		}
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
		g.draw(ownerHitBoxPath);
	}

	@Override
	public Shape getBounds() {
		return ownerHitBoxPath;
	}
}
