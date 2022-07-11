package pmnm.risk.game;

import java.awt.Color;
import java.io.Serializable;

import com.google.common.collect.UnmodifiableIterator;

import lombok.NonNull;

public interface IContinent extends Serializable {

	String getName();
	
	String getAbbreviation();
	
	int getCaptureBonus();
	
	Color getColor();
	
	boolean containsProvince(@NonNull final IProvince province);
	Iterable<IProvince> getProvinces();
}
