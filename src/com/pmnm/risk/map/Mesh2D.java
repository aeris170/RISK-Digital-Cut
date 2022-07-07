package com.pmnm.risk.map;

import java.awt.geom.GeneralPath;
import java.io.Serializable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import lombok.Builder;
import lombok.Data;
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
	public UnmodifiableIterator<@NonNull Vertex2D> getVertices() {
		return vertices.iterator();
	}
	
	@Getter
	private GeneralPath boundary = new GeneralPath();
	
	Mesh2D(@NonNull final ImmutableList<@NonNull Vertex2D> vertices) {
		this.vertices = vertices;
		calculateBoundary();
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