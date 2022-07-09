package pmnm.risk.game.databasedimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.map.board.Province;
import com.pmnm.risk.map.continent.Continent;

import lombok.NonNull;
import pmnm.risk.game.Conflict;
import pmnm.risk.game.Deploy;
import pmnm.risk.game.Dice;
import pmnm.risk.game.IContinent;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.IProvince;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.map.ContinentData;
import pmnm.risk.map.MapData;
import pmnm.risk.map.ProvinceData;

public class RiskGameContext implements IRiskGameContext {

	private static final long serialVersionUID = -7180240760865875029L;

	public static RiskGameContext of(final MapData data) {
		RiskGameContext gameContext = new RiskGameContext(data);
		return gameContext;
	}
	
	private MapData map;
	private IPlayer[] players;

	/* Data <-> Implementation Association */
	private Map<IContinent, ContinentData> continentData;
	private Map<ContinentData, IContinent> dataContinent;
	private Map<IProvince, ProvinceData> provinceData;
	private Map<ProvinceData, IProvince> dataProvince;
	
	/* Continent <-> Province Association */
	private Map<IContinent, @NonNull ImmutableList<IProvince>> continentProvinces;
	private Map<IProvince, IContinent> provinceContinents;
	
	/* Player <-> Province Association */
	private Map<IPlayer, @NonNull ArrayList<IProvince>> playerProvinces;
	private Map<IProvince, IPlayer> provincePlayers;
	
	/* Province Runtime info */
	private Map<IProvince, @NonNull ImmutableList<IProvince>> neighbors;
	private Map<IProvince, @NonNull Integer> numberOfTroops;
	
	/* Game Runtime info */
	private IPlayer currentPlayingPlayer;
	private TurnPhase currentTurnPhase;
	
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
		provinceData.keySet().forEach(province -> {
			numberOfTroops.put(province, Globals.UNKNOWN_TROOP_COUNT);
		});
		/* --------------------------------------------------------------------- */ 
	}
	
	/* Game API */
	@Override
	public IPlayer getCurrentPlayer() { return currentPlayingPlayer; }
	public TurnPhase getCurrentPhase() { return currentTurnPhase; }
	@Override
	public Conflict setUpConflict(@NonNull final IProvince attacker, @NonNull final IProvince defender, @NonNull final Dice attackerDice) {
		return new Conflict(this, attacker, defender, attackerDice);
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
	public Deploy setUpDeploy(@NonNull final IProvince source, @NonNull final IProvince defender, int amount) {
		return new Deploy(this, source, defender, amount);
	}
	@Override
	public void applyDeployResult(@NonNull final Deploy.Result result) {
		Deploy deploy = result.getDeploy();
		
		IProvince deployer = deploy.getSource();
		numberOfTroops.put(deployer, result.getRemainingSourceTroops());

		IProvince deployee = deploy.getDestination();
		numberOfTroops.put(deployee, result.getRemainingDestinationTroops());
	}
	
	/* Province API */
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
	public UnmodifiableIterator<IProvince> neighborsOf(@NonNull final IProvince province) {
		return neighbors.get(province).iterator();
	}
	@Override
	public int numberOfTroopsOn(@NonNull final IProvince province) {
		return numberOfTroops.get(province);
	}
	
	/* Continent API */
	@Override
	public UnmodifiableIterator<IProvince> provincesOf(@NonNull final IContinent continent) {
		return continentProvinces.get(continent).iterator();
	}
	
	/* Private Getters */
	private ProvinceData dataOf(@NonNull final IProvince province) {
		return provinceData.get(province);
	}
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
