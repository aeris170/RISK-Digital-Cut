package pmnm.risk.map.board;

import java.io.Serializable;

import lombok.NonNull;
import pmnm.risk.map.Mesh2D;
import pmnm.risk.map.Vertex2D;

public final class ProvinceHitAreaBounds implements Serializable {

	private static final long serialVersionUID = 5855949010693537574L;

	public int minX = Integer.MAX_VALUE;
	public int minY = Integer.MAX_VALUE;
	public int maxX = Integer.MIN_VALUE;
	public int maxY = Integer.MIN_VALUE;
	
	public float centerX = Float.NaN;
	public float centerY = Float.NaN;
	
	public static ProvinceHitAreaBounds of(ProvinceHitArea province) {
		ProvinceHitAreaBounds bounds = new ProvinceHitAreaBounds();
		bounds.centerX = province.getProvince().getCenterPoint().getX();
		bounds.centerY = province.getProvince().getCenterPoint().getY();
		
		Iterable<@NonNull Mesh2D> boundaries = province.getProvince().getMeshes();
		for (Mesh2D boundary : boundaries) {
			Iterable<@NonNull Vertex2D> vertices = boundary.getVertices();
			for (Vertex2D vertex : vertices) {
				int x = vertex.getX();
				int y = vertex.getY();
				if (x < bounds.minX) {
					bounds.minX = x;
				}
				if (x > bounds.maxX) {
					bounds.maxX = x;
				}
				if (y < bounds.minY) {
					bounds.minY = y;
				}
				if (y > bounds.maxY) {
					bounds.maxY = y;
				}
			}
		}
		return bounds;
	}
	
	private ProvinceHitAreaBounds() {}
}
