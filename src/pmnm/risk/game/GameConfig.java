package pmnm.risk.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import pmnm.risk.game.IRiskGameContext.GameType;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.map.MapConfig;

@Value
public class GameConfig {

	@Getter @NonNull private final Player.Data[] data;
	@Getter private final boolean isRandomPlacementEnabled;
	@Getter private final GameType gameType;
	@Getter private final MapConfig mapConfig;
}
