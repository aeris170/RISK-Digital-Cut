package pmnm.risk.game;

import java.awt.Color;
import java.io.Serializable;

public interface IPlayer extends Serializable {
	
	int getId();
	String getName();
	Color getColor();

	void occupyProvince(final IProvince province);
	Iterable<IProvince> getOccupiedProvinces();
	
	void deployToProvince(final IProvince province, final int amount);
	void attackToProvince(final IProvince source, final IProvince destination, final Dice method);
	void reinforceProvince(final IProvince source, final IProvince destination, final int amount);
	void finishTurn();
	
	/* Other methods */
	boolean isLocalPlayer();
	boolean isHumanPlayer();
}
