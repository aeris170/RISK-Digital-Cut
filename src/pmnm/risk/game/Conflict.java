package pmnm.risk.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import org.javatuples.Pair;

import doa.engine.log.DoaLogger;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import pmnm.risk.game.databasedimpl.RiskGameContext;

@Data
@ToString(includeFieldNames = true)
public final class Conflict implements Serializable {

	private static final long serialVersionUID = -4261217185340912826L;

	@Value
	@ToString(includeFieldNames = true)
	public static final class Result implements Serializable {

		private static final long serialVersionUID = -2491016186457306137L;

		@Getter
		@NonNull
		private final Conflict conflict;

		@Getter
		private final int remainingAttackerTroops;

		@Getter
		private final int remainingDefenderTroops;

		@Getter
		private final boolean isDecisive;

		private final IProvince winner;

		public Optional<IProvince> tryGetWinner() {
			if (!isDecisive) { return Optional.empty(); }
			return Optional.of(winner);
		}
	}

	@NonNull
	private final RiskGameContext context;

	@Getter
	@NonNull
	private final IProvince attacker;

	@Getter
	@NonNull
	private final IProvince defender;

	@Getter
	@NonNull
	private final Dice attackerDice;

	private Result result;

	public Result calculateResult() {
		if (result != null) { return result; }

		Pair<Integer[], Integer[]> diceResult = rollAppropriateDice();
		Pair<Integer, Integer> casualtyResult = calculateCasualties(diceResult.getValue0(), diceResult.getValue1());

		int attackerCasualties = casualtyResult.getValue0();
		int defenderCasualties = casualtyResult.getValue1();

		int attackerTroopsAfterConflict = context.numberOfTroopsOn(attacker) - attackerCasualties;
		int defenderTroopsAfterConflict = context.numberOfTroopsOn(defender) - defenderCasualties;
		boolean isDecisive = attackerTroopsAfterConflict == 1 || defenderTroopsAfterConflict == 0;
		IProvince winner = null;
		if (isDecisive) {
			if (attackerTroopsAfterConflict == 1) {
				winner = defender;
			}
			if (defenderTroopsAfterConflict == 0) {
				winner = attacker;
			}
			if (winner == null) { DoaLogger.getInstance().severe("something is horribly wrong! @conflict line:89"); }
		}

		Result rv = new Result(
			this,
			attackerTroopsAfterConflict,
			defenderTroopsAfterConflict,
			isDecisive,
			winner
		);
		result = rv;
		return rv;
	}

	private Pair<Integer[], Integer[]> rollAppropriateDice() {
		Dice defenderDice;
		if (context.numberOfTroopsOn(defender) >= 2) {
			defenderDice = Dice.DEFENCE_DICE_2;
		} else {
			defenderDice = Dice.DEFENCE_DICE_1;
		}
		Integer[] defenders = IntStream.of(defenderDice.rollAllAndGetAll()).boxed().toArray(Integer[]::new);
		Integer[] attackers = IntStream.of(attackerDice.rollAllAndGetAll()).boxed().toArray(Integer[]::new);

		return new Pair<>(attackers, defenders);
	}

	private Pair<Integer, Integer> calculateCasualties(Integer[] attackers, Integer[] defenders) {
		Arrays.sort(attackers, Collections.reverseOrder());
		Arrays.sort(defenders, Collections.reverseOrder());

		int attackerCasualties = 0;
		int defenderCasualties = 0;

		int diceCount = Math.min(attackers.length, defenders.length);
		for(int i = 0; i < diceCount; i++) {
			if(attackers[i] > defenders[i]) {
				defenderCasualties++;
			} else {
				attackerCasualties++;
			}
		}
		return new Pair<>(attackerCasualties, defenderCasualties);
	}
}
