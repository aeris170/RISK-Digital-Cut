package com.pmnm.risk.map;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import java.awt.Color;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = true)
public final class ContinentData implements Serializable {

	private static final long serialVersionUID = -7206995728526896010L;

	@Getter
	@NonNull
	private final String name;
	
	@Getter
	@NonNull
	private final String abbreviation;
	
	@Getter
	private final int captureBonus;
	
	@Getter
	@NonNull
	private final Color color;
	
	@NonNull
	private final ImmutableList<@NonNull ProvinceData> provinces;
	public Iterable<@NonNull ProvinceData> getProvinces() {
		return provinces;
	}
}
