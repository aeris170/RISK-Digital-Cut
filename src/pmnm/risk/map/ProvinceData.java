package pmnm.risk.map;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@ToString(includeFieldNames = true)
public final class ProvinceData implements Serializable {

	private static final long serialVersionUID = 7160270823763073348L;

	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	@NonNull
	private String name;

	@Setter(value = AccessLevel.PACKAGE)
	@EqualsAndHashCode.Exclude
	private ImmutableList<ProvinceData> neighbors;
	public Iterable<ProvinceData> getNeighbors() {
		return neighbors;
	}
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private MeshCollection meshes;
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	@EqualsAndHashCode.Exclude
	private ContinentData continent;
}
