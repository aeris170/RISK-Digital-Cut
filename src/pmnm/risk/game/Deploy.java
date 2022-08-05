package pmnm.risk.game;

import java.io.Serializable;

import com.pmnm.risk.globals.Globals;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Data
@ToString(includeFieldNames = true)
public final class Deploy implements Serializable {

	private static final long serialVersionUID = 4136304648761194882L;

	@Value
	@ToString(includeFieldNames = true)
	public static final class Result implements Serializable {
		
		private static final long serialVersionUID = -7679290613346465900L;

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
