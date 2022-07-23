package pmnm.risk.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import pmnm.risk.game.databasedimpl.Player;

@Value
public class GameConfig {

	@Getter @NonNull private final Player.Data[] data;
	@Getter private final boolean isRandomPlacementEnabled;
	
}
