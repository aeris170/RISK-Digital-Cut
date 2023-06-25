package pmnm.risk.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import pmnm.risk.game.IRiskGameContext.GameType;
import pmnm.risk.game.databasedimpl.Player;
import pmnm.risk.map.MapConfig;

@Value
public class GameConfig implements Serializable {

	private static final long serialVersionUID = 405673275700219342L;

	@Getter @NonNull private final Player.Data[] data;
	@Getter private final boolean isRandomPlacementEnabled;
	@Getter private final GameType gameType;
	@Getter private final MapConfig mapConfig;
}
