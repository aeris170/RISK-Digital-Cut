package com.pmnm.risk.map.board;
import java.io.Serializable;

import com.google.common.collect.UnmodifiableIterator;
import com.pmnm.risk.main.IPlayer;
import com.pmnm.risk.map.Vertex2D;

import doa.engine.maths.DoaVector;
import lombok.NonNull;

public interface IProvince extends Serializable {
	
	String getName();
	
	boolean isNeighborOf(@NonNull final IProvince province);
	UnmodifiableIterator<@NonNull IProvince> getNeighbors();
	
	Vertex2D getCenterPoint();
	boolean encasesPoint(@NonNull final Vertex2D point);
	boolean encasesPoint(@NonNull final DoaVector point);
	
	IContinent getContinent();

	IPlayer getOccupier();
	boolean isOccupied();
	boolean isOccupiedBy(@NonNull final IPlayer player);
	
	int getNumberOfTroops();
	boolean canLaunchAttack();
}
