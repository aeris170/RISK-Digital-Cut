package com.pmnm.risk.map.continent;

import java.awt.Color;

import com.google.common.collect.UnmodifiableIterator;
import com.pmnm.risk.map.ContinentData;
import com.pmnm.risk.map.board.IContinent;
import com.pmnm.risk.map.board.IProvince;
import com.pmnm.risk.map.board.RiskGameContext;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

public final class Continent implements IContinent  {

	private static final long serialVersionUID = 4714708201174941610L;

	private final RiskGameContext context;
	
	@NonNull
	@Getter(value = AccessLevel.PACKAGE)
	private final ContinentData data;

	public Continent(@NonNull final RiskGameContext context, @NonNull final ContinentData data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public String getName() { return data.getName(); }

	@Override
	public String getAbbreviation() { return data.getAbbreviation(); }

	@Override
	public int getCaptureBonus() { return data.getCaptureBonus(); }

	@Override
	public Color getColor() { return data.getColor(); }

	@Override
	public boolean containsProvince(@NonNull IProvince province) { return equals(context.continentOf(province)); }

	@Override
	public UnmodifiableIterator<@NonNull IProvince> getProvinces() { return context.provincesOf(this); }

}
