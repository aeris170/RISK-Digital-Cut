package pmnm.risk.map;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;

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
	private ImmutableList<pmnm.risk.map.ProvinceData> neighbors;
	public Iterable<pmnm.risk.map.ProvinceData> getNeighbors() {
		return neighbors;
	}
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private MeshCollection meshes;
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private ContinentData continent;
}
