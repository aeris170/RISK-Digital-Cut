package com.pmnm.risk.map.board;

import java.io.Serializable;

import com.google.common.collect.UnmodifiableIterator;
import com.pmnm.risk.dice.Dice;
import com.pmnm.risk.main.Conflict;
import com.pmnm.risk.main.Deploy;
import com.pmnm.risk.main.IPlayer;

import lombok.NonNull;

public interface IRiskGameContext extends Serializable {
	
	/* Game API */
	public Conflict setUpConflict(@NonNull final IProvince attacker, @NonNull final IProvince defender, @NonNull final Dice attackerDice);
	public void applyConflictResult(@NonNull final Conflict.Result result);
	public Deploy setUpDeploy(@NonNull final IProvince source, @NonNull final IProvince defender, int amount);
	public void applyDeployResult(@NonNull final Deploy.Result result);
	
	/* Province API */
	public IContinent continentOf(@NonNull final IProvince province);
	public boolean hasOccupier(@NonNull final IProvince province);
	public IPlayer occupierOf(@NonNull final IProvince province);
	public UnmodifiableIterator<@NonNull IProvince> neighborsOf(@NonNull final IProvince province);
	public int numberOfTroopsOn(@NonNull final IProvince province);
	
	/* Continent API */
	public UnmodifiableIterator<@NonNull IProvince> provincesOf(@NonNull final IContinent continent);
}
