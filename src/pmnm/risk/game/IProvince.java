package pmnm.risk.game;

import java.io.Serializable;

import com.google.common.collect.UnmodifiableIterator;

import doa.engine.maths.DoaVector;
import lombok.NonNull;
import pmnm.risk.map.Vertex2D;

public interface IProvince extends Serializable {
	
	String getName();
	
	boolean isNeighborOf(@NonNull final IProvince province);
	UnmodifiableIterator<IProvince> getNeighbors();
	
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
