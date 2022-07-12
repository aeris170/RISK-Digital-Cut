package pmnm.risk.game.databasedimpl;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import doa.engine.maths.DoaVector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.IContinent;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;
import pmnm.risk.map.Mesh2D;
import pmnm.risk.map.ProvinceData;
import pmnm.risk.map.Vertex2D;

public final class Province implements IProvince {
	
	private static final long serialVersionUID = 9078321009537504009L;

	private final RiskGameContext context;
	
	@NonNull
	@Getter(value = AccessLevel.PACKAGE)
	private final ProvinceData data;
	
	public Province(@NonNull final RiskGameContext context, @NonNull final ProvinceData data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public String getName() { return data.getName(); }

	@Override
	public boolean isNeighborOf(@NonNull IProvince province) {
		Iterable<IProvince> neighbors = getNeighbors();
		for (IProvince neighbor : neighbors) {
			if (neighbor == province) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterable<IProvince> getNeighbors() { return context.neighborsOf(this); }

	@Override
	public Vertex2D getCenterPoint() { return data.getMeshes().getCenter(); }

	@Override
	public boolean encasesPoint(@NonNull Vertex2D point) { return data.getMeshes().encasesPoint(point); }

	@Override
	public boolean encasesPoint(@NonNull DoaVector point) { return encasesPoint(new Vertex2D((int)point.x, (int)point.y)); }
	
	@Override
	public Iterable<@NonNull Mesh2D> getMeshes() {
		List<@NonNull Mesh2D> paths = new ArrayList<>();
		data.getMeshes().getMeshes().forEachRemaining(paths::add);
		return ImmutableList.copyOf(paths);
	}

	@Override
	public IContinent getContinent() { return context.continentOf(this); }

	@Override
	public IPlayer getOccupier() { return context.occupierOf(this); }

	@Override
	public boolean isOccupied() { return getOccupier() != null; }

	@Override
	public boolean isOccupiedBy(@NonNull IPlayer player) { return getOccupier() == player; }

	@Override
	public int getNumberOfTroops() { return context.numberOfTroopsOn(this); }

	@Override
	public boolean canLaunchAttack() { return getNumberOfTroops() > 1; }

	@Override
	public boolean canReinforceAnotherProvince() { return getNumberOfTroops() > 1; }
}
