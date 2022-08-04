package pmnm.risk.game.databasedimpl;

import java.awt.Color;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pmnm.risk.game.IContinent;
import pmnm.risk.game.IProvince;
import pmnm.risk.map.ContinentData;

@EqualsAndHashCode
@ToString(includeFieldNames = true)
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
	public Iterable<IProvince> getProvinces() { return context.provincesOf(this); }

}
