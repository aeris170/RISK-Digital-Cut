package pmnm.risk.game;

import java.io.Serializable;

import com.google.common.collect.UnmodifiableIterator;

import lombok.NonNull;

public interface IRiskGameContext extends Serializable {
	
	/* Game API */
	IPlayer getCurrentPlayer();
	TurnPhase getCurrentPhase();
	Conflict setUpConflict(@NonNull final IProvince attacker, @NonNull final IProvince defender, @NonNull final Dice attackerDice);
	void applyConflictResult(@NonNull final Conflict.Result result);
	Deploy setUpDeploy(@NonNull final IProvince source, @NonNull final IProvince defender, int amount);
	void applyDeployResult(@NonNull final Deploy.Result result);
	
	/* Province API */
	IContinent continentOf(@NonNull final IProvince province);
	boolean hasOccupier(@NonNull final IProvince province);
	IPlayer occupierOf(@NonNull final IProvince province);
	UnmodifiableIterator<IProvince> neighborsOf(@NonNull final IProvince province);
	int numberOfTroopsOn(@NonNull final IProvince province);
	
	/* Continent API */
	UnmodifiableIterator<IProvince> provincesOf(@NonNull final IContinent continent);
	
	public enum TurnPhase { DRAFT, ATTACK, REINFORCE; }
}
