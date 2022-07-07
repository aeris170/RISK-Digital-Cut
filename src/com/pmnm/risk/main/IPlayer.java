package com.pmnm.risk.main;
import com.google.common.collect.UnmodifiableIterator;
import com.pmnm.risk.dice.Dice;
import com.pmnm.risk.map.board.IProvince;

import lombok.NonNull;

public interface IPlayer {
	
	String getName();

	void occupyProvince(final IProvince province);
	UnmodifiableIterator<@NonNull IProvince> getOccupiedProvinces();
	
	void deployToProvince(final IProvince province, final int amount);
	void attackToProvince(final IProvince source, final IProvince destination, final Dice method);
	
	/* Other methods */
	boolean isLocalPlayer();
	boolean isHumanPlayer();
}
