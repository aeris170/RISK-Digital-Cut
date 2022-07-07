package com.pmnm.risk.map;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString(includeFieldNames = true)
public final class MeshCollection implements Serializable {
	
	private static final long serialVersionUID = -9077842709746246648L;

	@Getter
	@NonNull
	private final Vertex2D center;

	@NonNull
	@Singular
	private final ImmutableList<@NonNull Mesh2D> meshes;
	public UnmodifiableIterator<@NonNull Mesh2D> getMeshes() {
		return meshes.iterator();
	}

}
