package pmnm.risk.game;

import com.pmnm.risk.globals.Globals;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import pmnm.risk.game.databasedimpl.RiskGameContext;

@Data
@ToString(includeFieldNames = true)
public final class Deploy {

	@Value
	@ToString(includeFieldNames = true)
	public static final class Result {
		
		@Getter
		@NonNull
		private final Deploy deploy;

		@Getter
		private final int remainingSourceTroops;
		
		@Getter
		private final int remainingDestinationTroops;
		
	}
	
	@NonNull
	private final RiskGameContext context;
	
	@Getter
	@NonNull
	private final IProvince source;
	
	@Getter
	@NonNull
	private final IProvince destination;
	
	@Getter
	private final int amount;
	
	private Result result;
	
	public Result calculateResult() {
		if (result != null) return result;
		
		int sourceTroops = context.numberOfTroopsOn(source);
		if (sourceTroops <= amount) { throw new IllegalArgumentException(""); }
		
		int destinationTroops = context.numberOfTroopsOn(destination);
		if (destinationTroops == Globals.UNKNOWN_TROOP_COUNT) {
			destinationTroops = 0;
		}
		destinationTroops += amount;
		sourceTroops -= amount;
		
		Result rv = new Result(
			this,
			sourceTroops,
			destinationTroops
		);
		result = rv;
		return rv;
	}
}
