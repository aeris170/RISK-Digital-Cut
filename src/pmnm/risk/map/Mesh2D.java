package pmnm.risk.map;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString(includeFieldNames = true)
public final class Mesh2D implements Serializable {
	
	private static final long serialVersionUID = 2730238773326738438L;

	@NonNull
	@Singular
	private final ImmutableList<@NonNull Vertex2D> vertices;
	public Iterable<@NonNull Vertex2D> getVertices() {
		return vertices;
	}
	
	@Getter
	private GeneralPath boundary = new GeneralPath();
	
	Mesh2D(@NonNull final ImmutableList<@NonNull Vertex2D> vertices) {
		this.vertices = vertices;
		calculateBoundary();
	}

	public boolean encasesPoint(@NonNull Vertex2D point) {
		return boundary.contains(new Point(point.getX(), point.getY()));
	}
	
	private void calculateBoundary() {
		Vertex2D startPoint = vertices.get(0);
		boundary.moveTo(startPoint.getX(), startPoint.getY());
		for (int i = 1; i < vertices.size(); i++) {
			Vertex2D nextPoint = vertices.get(i);
			boundary.lineTo(nextPoint.getX(), nextPoint.getY());
		}
		boundary.closePath();		
	}
}