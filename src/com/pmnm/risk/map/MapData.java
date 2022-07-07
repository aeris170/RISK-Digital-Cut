package com.pmnm.risk.map;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = true)
public final class MapData implements Serializable {

	private static final long serialVersionUID = 3162242848938299845L;
	
	@NonNull
	private final ImmutableList<@NonNull ContinentData> continents;
	public UnmodifiableIterator<@NonNull ContinentData> getContinents() {
		return continents.iterator();
	}

}
