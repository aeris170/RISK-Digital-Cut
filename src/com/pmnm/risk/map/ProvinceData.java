package com.pmnm.risk.map;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
public final class ProvinceData implements Serializable {

	private static final long serialVersionUID = 7160270823763073348L;

	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	@NonNull
	private String name;

	@Setter(value = AccessLevel.PACKAGE)
	private ImmutableList<@NonNull ProvinceData> neighbors;
	public Iterable<@NonNull ProvinceData> getNeighbors() {
		return neighbors;
	}
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private MeshCollection meshes;
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private ContinentData continent;
}
