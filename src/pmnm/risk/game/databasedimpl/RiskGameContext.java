package pmnm.risk.game.databasedimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableList;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.util.CircularQueue;

import doa.engine.maths.DoaMath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import pmnm.risk.game.Conflict;
import pmnm.risk.game.Deploy;
import pmnm.risk.game.Dice;
import pmnm.risk.game.GameConfig;
import pmnm.risk.game.IContinent;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.game.Reinforce;
import pmnm.risk.map.ContinentData;
import pmnm.risk.map.MapData;
import pmnm.risk.map.ProvinceData;
import pmnm.risk.map.board.GameBoard;
import pmnm.risk.map.board.ProvinceHitAreas;

public class RiskGameContext implements IRiskGameContext {

	private static final long serialVersionUID = -7180240760865875029L;

	public static RiskGameContext of(final MapData data) {
		RiskGameContext gameContext = new RiskGameContext(data);
		return gameContext;
	}
	
	private MapData map;
	private CircularQueue<IPlayer> players;
	
	@Getter
	@Setter
	private boolean isPaused;
	
	private boolean isInitialized;
	
	/* Data <-> Implementation Association */
	private Map<IContinent, ContinentData> continentData;
	private Map<ContinentData, IContinent> dataContinent;
	private Map<IProvince, ProvinceData> provinceData;
	private Map<ProvinceData, IProvince> dataProvince;
	
	/* Continent <-> Province Association */
	private Map<IContinent, @NonNull ImmutableList<@NonNull IProvince>> continentProvinces;
	private Map<IProvince, IContinent> provinceContinents;
	
	/* Player <-> Province Association */
	private Map<IPlayer, @NonNull ArrayList<@NonNull IProvince>> playerProvinces;
	private Map<IProvince, IPlayer> provincePlayers;
	
	/* Province Runtime info */
	private Map<IProvince, @NonNull ImmutableList<@NonNull IProvince>> neighbors;
	private Map<IProvince, @NonNull Integer> numberOfTroops;
	
	/* Game Runtime info */
	private boolean isInitialPlacementComplete;
	private IPlayer currentPlayingPlayer;
	private TurnPhase currentTurnPhase;
	
	/* Visuals */
	@Getter
	private ProvinceHitAreas areas;
	
	private RiskGameContext(@NonNull final MapData data) {
		map = data;

		/* --- Step 1, create IContinents and associate them with their data --- */
		continentData = new HashMap<>();
		dataContinent = new HashMap<>();
		Iterable<ContinentData> continentDatas = map.getContinents();
		for (@NonNull ContinentData cData : continentDatas) {
			IContinent continent = new Continent(this, cData);
			continentData.put(continent, cData);
			dataContinent.put(cData, continent);
		}
		/* --------------------------------------------------------------------- */ 

		/* --------------- Step 2, do Step 1 but for IProvinces ---------------- */
		provinceData = new HashMap<>();
		dataProvince = new HashMap<>();
		continentDatas = map.getContinents();
		for (@NonNull ContinentData cData : continentDatas) {
			Iterable<ProvinceData> provinceDatas = cData.getProvinces();
			
			for (@NonNull ProvinceData pData : provinceDatas) {
				IProvince province = new Province(this, pData);
				provinceData.put(province, pData);
				dataProvince.put(pData, province);
			}
		}		
		/* --------------------------------------------------------------------- */ 

		/* ------ Step 3, data are realized sort provinces into continents ----- */
		continentProvinces = new HashMap<>();
		provinceContinents = new HashMap<>();
		dataContinent.keySet().forEach(cData -> {
			List<IProvince> provinces = new ArrayList<>();

			Iterable<ProvinceData> provinceDatas = cData.getProvinces();
			for (@NonNull ProvinceData pData : provinceDatas) {
				provinces.add(objectOf(pData));
				provinceContinents.put(objectOf(pData), objectOf(cData));
			}
			
			continentProvinces.put(objectOf(cData), ImmutableList.copyOf(provinces));
		});
		/* --------------------------------------------------------------------- */ 

		/* ------------------ Step 4, set player associations ------------------ */
		playerProvinces = new HashMap<>();
		provincePlayers = new HashMap<>();
		provinceData.keySet().forEach(province -> provincePlayers.put(province, null));
		/* --------------------------------------------------------------------- */ 

		/* ----------------- Step 5, set neigbors of provinces ----------------- */
		neighbors = new HashMap<>();
		dataProvince.keySet().forEach(pData -> {
			List<IProvince> provinces = new ArrayList<>();
			
			Iterable<ProvinceData> neighborDatas = pData.getNeighbors();
			for (@NonNull ProvinceData nData : neighborDatas) {
				provinces.add(objectOf(nData));
			}
			
			neighbors.put(objectOf(pData), ImmutableList.copyOf(provinces));
		});
		/* --------------------------------------------------------------------- */ 

		/* ----------------- Step 6, add provinces to this map ----------------- */
		numberOfTroops = new HashMap<>();
		provinceData
			.keySet()
			.forEach(province -> numberOfTroops.put(province, Globals.UNKNOWN_TROOP_COUNT));
		/* --------------------------------------------------------------------- */ 
		
		currentTurnPhase = TurnPhase.SETUP;
		
		areas = new ProvinceHitAreas(this);
	}
	
	/* Game API */
	@Override
	public void initiliazeGame(@NonNull final GameConfig gameConfig) {
		if (isInitialized) return;
		
		players = new CircularQueue<>();
		for (Player.Data data : gameConfig.getData()) {
			Player p = new Player(this, data);
			players.add(p);
			playerProvinces.put(p, new ArrayList<>());
			Scenes.GAME_SCENE.add(p);
		}
		Scenes.GAME_SCENE.add(new GameBoard(map));
		Scenes.GAME_SCENE.add(areas);
		currentPlayingPlayer = players.getNext();
		
		if (gameConfig.isRandomPlacementEnabled()) {
			/* occupy all provinces */ { 
				List<IProvince> unoccupiedProvinces = new ArrayList<>(provinceData.keySet());
				while (!unoccupiedProvinces.isEmpty()) {
					int randomIndex = DoaMath.randomIntBetween(0, unoccupiedProvinces.size());
					IProvince randomProvince = unoccupiedProvinces.get(randomIndex);
					
					occupyProvince(currentPlayingPlayer, randomProvince);
					Deploy deploy = setUpDeploy(randomProvince, 1);
					applyDeployResult(deploy.calculateResult());
					
					unoccupiedProvinces.remove(randomIndex);
					currentPlayingPlayer = players.getNext();
				}
			}
			/* deploy to all provinces */ {
				currentPlayingPlayer = players.getFirst();
				while (!isInitialPlacementComplete()) {
					List<IProvince> provinces = playerProvinces.get(currentPlayingPlayer);
					int randomIndex = DoaMath.randomIntBetween(0, provinces.size());
					IProvince randomProvince = provinces.get(randomIndex);

					Deploy deploy = setUpDeploy(randomProvince, 1);
					applyDeployResult(deploy.calculateResult());
					
					currentPlayingPlayer = players.getNext();
				}
			}
		}
		isInitialized = true;
	}
	@Override
	public IPlayer getCurrentPlayer() { return currentPlayingPlayer; }
	@Override
	public TurnPhase getCurrentPhase() { return currentTurnPhase; }
	@Override
	public void goToNextPhase() { 
		switch(getCurrentPhase()) {
			case SETUP:
				break;
			case DRAFT:
				currentTurnPhase = TurnPhase.ATTACK;
				break;
			case ATTACK:
				currentTurnPhase = TurnPhase.REINFORCE;
				break;
			case REINFORCE:
				break;
		}
	}
	@Override
	public void finishCurrentPlayerTurn() {
		if (getCurrentPhase() == TurnPhase.DRAFT ||
			getCurrentPhase() == TurnPhase.ATTACK) { return; }
		
		if (getCurrentPhase() == TurnPhase.SETUP) {
			currentTurnPhase = TurnPhase.SETUP;	
		} else {
			currentTurnPhase = TurnPhase.ATTACK;
		}
		currentPlayingPlayer = players.getNext();
	}
	@Override
	public Deploy setUpDeploy(@NonNull IProvince target, int amount) {
		return new Deploy(this, target, amount);
	}
	@Override
	public void applyDeployResult(@NonNull final Deploy.Result result) {
		Deploy deploy = result.getDeploy();
		
		IProvince target = deploy.getTarget();
		numberOfTroops.put(target, result.getRemainingTargetTroops());
	}
	@Override
	public Conflict setUpConflict(@NonNull final IProvince attacker, @NonNull final IProvince defender, @NonNull final Dice method) {
		return new Conflict(this, attacker, defender, method);
	}
	@Override
	public void applyConflictResult(@NonNull final Conflict.Result result) {
		Conflict conflict = result.getConflict();
		
		IProvince attacker = conflict.getAttacker();
		numberOfTroops.put(attacker, result.getRemainingAttackerTroops());
		
		IProvince defender = conflict.getDefender();
		numberOfTroops.put(defender, result.getRemainingDefenderTroops());
		
		if(numberOfTroops.get(defender) <= 0) {
			IPlayer attackerPlayer = provincePlayers.get(attacker);
			IPlayer defenderPlayer = provincePlayers.get(defender);
			
			playerProvinces.get(defenderPlayer).remove(defender);
			provincePlayers.put(defender, attackerPlayer);
			
			numberOfTroops.put(defender, Globals.UNKNOWN_TROOP_COUNT);
		}
	}
	@Override
	public Reinforce setUpReinforce(@NonNull final IProvince source, @NonNull final IProvince destination, int amount) {
		return new Reinforce(this, source, destination, amount);
	}
	@Override
	public void applyReinforceResult(@NonNull final Reinforce.Result result) {
		Reinforce reinforce = result.getReinforce();
		
		IProvince reinforcer = reinforce.getSource();
		numberOfTroops.put(reinforcer, result.getRemainingSourceTroops());

		IProvince reinforcee = reinforce.getDestination();
		numberOfTroops.put(reinforcee, result.getRemainingDestinationTroops());
	}
	@Override
	public int calculateStartingTroopCount() { return 50 - 5 * players.size(); }
	@Override
	public int calculateTurnReinforcementsFor(@NonNull IPlayer player) {
		List<@NonNull IProvince> playerProvincesList = StreamSupport.stream(provincesOf(player).spliterator(), false).toList();
		int provinceCount = playerProvincesList.size();
		int reinforcementsForThisTurn = Math.max(provinceCount / 3, 3);
		for (@NonNull IContinent continent : continentData.keySet()) {
			@NonNull ImmutableList<@NonNull IProvince> provinces = continentProvinces.get(continent);
			if (playerProvincesList.containsAll(provinces)) {
				reinforcementsForThisTurn += continent.getCaptureBonus();
			}
		}
		return reinforcementsForThisTurn;
	}
	@Override
	public boolean isInitialPlacementComplete() {
		if (!isInitialPlacementComplete) {
			int requiredAmount = calculateStartingTroopCount();
			for (IPlayer player : players) {
				int totalPlayerTroops = playerProvinces.get(player).stream().mapToInt(IProvince::getNumberOfTroops).sum();
				if (totalPlayerTroops != requiredAmount) {
					return false;
				}
			}
			isInitialPlacementComplete = true;
		}
		return isInitialPlacementComplete;
	}
	@Override
	public boolean isEveryProvinceOccupied() {
		for (IProvince province : provinceData.keySet()) {
			if (provincePlayers.get(province) == null) {
				return false;
			}
		}
		return true;
	}
	
	/* Player API */
	@Override
	public Iterable<IProvince> provincesOf(@NonNull final IPlayer player) { return playerProvinces.get(player); }
	@Override
	public void occupyProvince(@NonNull final IPlayer player, @NonNull IProvince province) {
		IPlayer previousOccupier = province.getOccupier();
		if (previousOccupier != null) {
			playerProvinces.get(previousOccupier).remove(province);
		}
		playerProvinces.get(player).add(province);
		provincePlayers.put(province, player);
	}
	
	/* Province API */
	@Override
	public Iterable<@NonNull IProvince> getProvinces() {
		return provinceData.keySet();
	}
	@Override
	public IContinent continentOf(@NonNull final IProvince province) {
		return provinceContinents.get(province);
	}
	@Override
	public boolean hasOccupier(@NonNull final IProvince province) {
		return occupierOf(province) != null;
	}
	@Override
	public IPlayer occupierOf(@NonNull final IProvince province) {
		return provincePlayers.get(province);
	}
	@Override
	public Iterable<IProvince> neighborsOf(@NonNull final IProvince province) {
		return neighbors.get(province);
	}
	@Override
	public int numberOfTroopsOn(@NonNull final IProvince province) {
		return numberOfTroops.get(province);
	}
	
	/* Continent API */
	@Override
	public Iterable<@NonNull IContinent> getContinents() {
		return continentData.keySet();
	}
	@Override
	public Iterable<IProvince> provincesOf(@NonNull final IContinent continent) {
		return continentProvinces.get(continent);
	}
	
	/* Private Getters */
	@SuppressWarnings("unused")
	private ProvinceData dataOf(@NonNull final IProvince province) {
		return provinceData.get(province);
	}
	@SuppressWarnings("unused")
	private ContinentData dataOf(@NonNull final IContinent continent) {
		return continentData.get(continent);
	}
	private IProvince objectOf(@NonNull final ProvinceData data) {
		return dataProvince.get(data);
	}
	private IContinent objectOf(@NonNull final ContinentData data) {
		return dataContinent.get(data);
	}
}
