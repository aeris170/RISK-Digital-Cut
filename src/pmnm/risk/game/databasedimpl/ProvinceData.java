package pmnm.risk.game.databasedimpl;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;
import com.pmnm.risk.map.MeshCollection;

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
	private ImmutableList<pmnm.risk.game.databasedimpl.ProvinceData> neighbors;
	public Iterable<pmnm.risk.game.databasedimpl.ProvinceData> getNeighbors() {
		return neighbors;
	}
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private MeshCollection meshes;
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private ContinentData continent;
}
