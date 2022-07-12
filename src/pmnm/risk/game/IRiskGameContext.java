package pmnm.risk.game;

import java.io.Serializable;

import lombok.NonNull;

public interface IRiskGameContext extends Serializable {
	
	/* Game API */
	boolean isPaused();
	IPlayer getCurrentPlayer();
	TurnPhase getCurrentPhase();
	Deploy setUpDeploy(@NonNull final IProvince target, int amount);
	void applyDeployResult(@NonNull final Deploy.Result result);
	Conflict setUpConflict(@NonNull final IProvince attacker, @NonNull final IProvince defender, @NonNull final Dice attackerDice);
	void applyConflictResult(@NonNull final Conflict.Result result);
	Reinforce setUpReinforce(@NonNull final IProvince source, @NonNull final IProvince defender, int amount);
	void applyReinforceResult(@NonNull final Reinforce.Result result);
	int calculateStartingTroopCount();
	int calculateTurnReinforcementsFor(@NonNull IPlayer player);
	
	boolean isInitialPlacementComplete();
	boolean isEveryProvinceOccupied();
	
	/* Player API */
	public Iterable<IProvince> provincesOf(@NonNull final IPlayer player);
	public void occupyProvince(@NonNull final IPlayer player, @NonNull IProvince province);
	
	/* Province API */
	Iterable<@NonNull IProvince> getProvinces();
	IContinent continentOf(@NonNull final IProvince province);
	boolean hasOccupier(@NonNull final IProvince province);
    IPlayer occupierOf(@NonNull final IProvince province);
	Iterable<@NonNull IProvince> neighborsOf(@NonNull final IProvince province);
	int numberOfTroopsOn(@NonNull final IProvince province);
	
	/* Continent API */
	Iterable<@NonNull IContinent> getContinents();
	Iterable<@NonNull IProvince> provincesOf(@NonNull final IContinent continent);
	
	public enum TurnPhase { DRAFT, ATTACK, REINFORCE; }
}
