package com.pmnm.risk.map.board;

import com.google.common.collect.UnmodifiableIterator;

import doa.engine.maths.DoaVector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import pmnm.risk.game.IContinent;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.databasedimpl.RiskGameContext;
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
		UnmodifiableIterator<pmnm.risk.game.IProvince> neighbors = getNeighbors();
		while (neighbors.hasNext()) {
			IProvince neighbor = neighbors.next();
			if (neighbor == province) {
				return true;
			}
		}
		return false;
	}

	@Override
	public UnmodifiableIterator<pmnm.risk.game.IProvince> getNeighbors() {
		return context.neighborsOf(this);
	}

	@Override
	public Vertex2D getCenterPoint() { return data.getMeshes().getCenter(); }

	@Override
	public boolean encasesPoint(@NonNull Vertex2D point) { return data.getMeshes().encasesPoint(point); }

	@Override
	public boolean encasesPoint(@NonNull DoaVector point) { return encasesPoint(new Vertex2D((int)point.x, (int)point.y)); }

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

}
