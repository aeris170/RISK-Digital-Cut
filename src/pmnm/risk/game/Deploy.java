package pmnm.risk.game;

import com.pmnm.risk.globals.Globals;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

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
		private final int remainingTargetTroops;		
	}
	
	@NonNull
	private final IRiskGameContext context;
	
	@Getter
	@NonNull
	private final IProvince target;
		
	@Getter
	private final int amount;
	
	private Result result;
	
	public Result calculateResult() {
		if (result != null) return result;
		
		int targetTroops = context.numberOfTroopsOn(target);
		if (targetTroops == Globals.UNKNOWN_TROOP_COUNT) {
			targetTroops = 0;
		}
		targetTroops += amount;

		Result rv = new Result(
			this,
			targetTroops
		);
		result = rv;
		return rv;
	}
}
