package pmnm.risk.map.board;

import java.awt.geom.GeneralPath;

public final class ProvinceHitAreaBounds {

	public int minX = Integer.MAX_VALUE;
	public 	int minY = Integer.MAX_VALUE;
	public int maxX = Integer.MIN_VALUE;
	public int maxY = Integer.MIN_VALUE;
	
	public float centerX = Float.NaN;
	public float centerY = Float.NaN;
	
	public static void Calculate(ProvinceHitArea province) {
		province.bounds = new ProvinceHitAreaBounds();
		province.bounds.centerX = province.province.getCenter().x;
		province.bounds.centerY = province.province.getCenter().y;
		
		for (GeneralPath mesh : province.meshes) {
			double[][] vertices = ProvinceHitArea.getPoints(mesh);
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i][0] < province.bounds.minX) {
					province.bounds.minX = (int) vertices[i][0];
				}
				if (vertices[i][0] > province.bounds.maxX) {
					province.bounds.maxX = (int) vertices[i][0];
				}
				if (vertices[i][1] < province.bounds.minY) {
					province.bounds.minY = (int) vertices[i][1];
				}
				if (vertices[i][1] > province.bounds.maxY) {
					province.bounds.maxY = (int) vertices[i][1];
				}
			}
		}
	}
	
	private ProvinceHitAreaBounds() {}
}
