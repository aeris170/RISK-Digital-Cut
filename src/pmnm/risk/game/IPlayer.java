package pmnm.risk.game;

import com.google.common.collect.UnmodifiableIterator;

public interface IPlayer {
	
	String getName();

	void occupyProvince(final IProvince province);
	UnmodifiableIterator<IProvince> getOccupiedProvinces();
	
	void deployToProvince(final IProvince province, final int amount);
	void attackToProvince(final IProvince source, final IProvince destination, final Dice method);
	
	/* Other methods */
	boolean isLocalPlayer();
	boolean isHumanPlayer();
}
