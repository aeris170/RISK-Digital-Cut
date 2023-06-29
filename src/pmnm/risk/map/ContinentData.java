package pmnm.risk.map;

import java.awt.Color;
import java.io.Serializable;

import com.google.common.collect.ImmutableList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
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
	@EqualsAndHashCode.Exclude
	private final ImmutableList<ProvinceData> provinces;
	public Iterable<ProvinceData> getProvinces() {
		return provinces;
	}
}
