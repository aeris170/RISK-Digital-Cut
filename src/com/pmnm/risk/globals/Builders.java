package com.pmnm.risk.globals;

import com.doa.engine.scene.DoaObjectBuilder;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.main.AIPlayer;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Player;
import com.pmnm.risk.map.board.GameBoard;
import com.pmnm.risk.map.board.ProvinceConnector;
import com.pmnm.risk.map.province.ProvinceHitArea;
import com.pmnm.risk.map.province.ProvinceHitArea.ProvinceSymbol;
import com.pmnm.roy.ui.ColorComboButton;
import com.pmnm.roy.ui.DifficultyComboButton;
import com.pmnm.roy.ui.LanguageButton;
import com.pmnm.roy.ui.RandomPlacementButton;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.TypeComboButton;
import com.pmnm.roy.ui.gameui.BlitzButton;
import com.pmnm.roy.ui.gameui.BottomPanel;
import com.pmnm.roy.ui.gameui.DicePanel;
import com.pmnm.roy.ui.gameui.GameScreenExitFadeToBlack;
import com.pmnm.roy.ui.gameui.GameScreenExitPopup;
import com.pmnm.roy.ui.gameui.PauseMenu;
import com.pmnm.roy.ui.gameui.SeasonEffect;
import com.pmnm.roy.ui.gameui.SpinnerCenterPiece;
import com.pmnm.roy.ui.gameui.TopPanel;
import com.pmnm.roy.ui.gameui.Water;
import com.pmnm.roy.ui.menu.ExitFadeToBlack;
import com.pmnm.roy.ui.menu.ExitPopup;
import com.pmnm.roy.ui.menu.FadingBackground;
import com.pmnm.roy.ui.menu.HostGameMenu;
import com.pmnm.roy.ui.menu.JoinGameMenu;
import com.pmnm.roy.ui.menu.LoadGameMenu;
import com.pmnm.roy.ui.menu.MainMenu;
import com.pmnm.roy.ui.menu.NewGameMenu;
import com.pmnm.roy.ui.menu.PlayOfflineMenu;
import com.pmnm.roy.ui.menu.PlayOnlineMenu;
import com.pmnm.roy.ui.menu.RulesMenu;
import com.pmnm.roy.ui.menu.SettingsMenu;

public final class Builders {

	public static final DoaObjectBuilder<TextImageButton> TIBB = new DoaObjectBuilder<>(TextImageButton.class);
	public static final DoaObjectBuilder<DoaImageButton> DIBB = new DoaObjectBuilder<>(DoaImageButton.class);
	public static final DoaObjectBuilder<RandomPlacementButton> RPBB = new DoaObjectBuilder<>(RandomPlacementButton.class);
	public static final DoaObjectBuilder<TypeComboButton> TCBB = new DoaObjectBuilder<>(TypeComboButton.class);
	public static final DoaObjectBuilder<DifficultyComboButton> DCBB = new DoaObjectBuilder<>(DifficultyComboButton.class);
	public static final DoaObjectBuilder<ColorComboButton> CCBB = new DoaObjectBuilder<>(ColorComboButton.class);
	public static final DoaObjectBuilder<SpinnerCenterPiece> SCPB = new DoaObjectBuilder<>(SpinnerCenterPiece.class);
	public static final DoaObjectBuilder<LanguageButton> LBB = new DoaObjectBuilder<>(LanguageButton.class);
	public static final DoaObjectBuilder<BlitzButton> BBB = new DoaObjectBuilder<>(BlitzButton.class);

	public static final DoaObjectBuilder<FadingBackground> FBB = new DoaObjectBuilder<>(FadingBackground.class);
	public static final DoaObjectBuilder<MainMenu> MMB = new DoaObjectBuilder<>(MainMenu.class);
	public static final DoaObjectBuilder<PlayOfflineMenu> POFMB = new DoaObjectBuilder<>(PlayOfflineMenu.class);
	public static final DoaObjectBuilder<PlayOnlineMenu> PONMB = new DoaObjectBuilder<>(PlayOnlineMenu.class);
	public static final DoaObjectBuilder<SettingsMenu> SMB = new DoaObjectBuilder<>(SettingsMenu.class);
	public static final DoaObjectBuilder<RulesMenu> RMB = new DoaObjectBuilder<>(RulesMenu.class);
	public static final DoaObjectBuilder<NewGameMenu> NGMB = new DoaObjectBuilder<>(NewGameMenu.class);
	public static final DoaObjectBuilder<LoadGameMenu> LGMB = new DoaObjectBuilder<>(LoadGameMenu.class);
	public static final DoaObjectBuilder<HostGameMenu> HGMB = new DoaObjectBuilder<>(HostGameMenu.class);
	public static final DoaObjectBuilder<JoinGameMenu> JGMB = new DoaObjectBuilder<>(JoinGameMenu.class);
	public static final DoaObjectBuilder<ExitPopup> EPB = new DoaObjectBuilder<>(ExitPopup.class);
	public static final DoaObjectBuilder<ExitFadeToBlack> EFTBB = new DoaObjectBuilder<>(ExitFadeToBlack.class);
	public static final DoaObjectBuilder<PauseMenu> PMB = new DoaObjectBuilder<>(PauseMenu.class);

	public static final DoaObjectBuilder<SeasonEffect> SEB = new DoaObjectBuilder<>(SeasonEffect.class);
	public static final DoaObjectBuilder<Water> WB = new DoaObjectBuilder<>(Water.class);
	public static final DoaObjectBuilder<DicePanel> DPB = new DoaObjectBuilder<>(DicePanel.class);
	public static final DoaObjectBuilder<TopPanel> TPB = new DoaObjectBuilder<>(TopPanel.class);
	public static final DoaObjectBuilder<BottomPanel> BPB = new DoaObjectBuilder<>(BottomPanel.class);
	public static final DoaObjectBuilder<GameManager> GMB = new DoaObjectBuilder<>(GameManager.class);
	public static final DoaObjectBuilder<GameBoard> GBB = new DoaObjectBuilder<>(GameBoard.class);
	public static final DoaObjectBuilder<ProvinceHitArea> PHAB = new DoaObjectBuilder<>(ProvinceHitArea.class);
	public static final DoaObjectBuilder<ProvinceSymbol> PSB = new DoaObjectBuilder<>(ProvinceSymbol.class);
	public static final DoaObjectBuilder<ProvinceConnector> PCB = new DoaObjectBuilder<>(ProvinceConnector.class);
	public static final DoaObjectBuilder<Player> PB = new DoaObjectBuilder<>(Player.class);
	public static final DoaObjectBuilder<AIPlayer> AIPB = new DoaObjectBuilder<>(AIPlayer.class);
	public static final DoaObjectBuilder<GameScreenExitPopup> GSEPB = new DoaObjectBuilder<>(GameScreenExitPopup.class);
	public static final DoaObjectBuilder<GameScreenExitFadeToBlack> GSEFTBB = new DoaObjectBuilder<>(GameScreenExitFadeToBlack.class);

	private Builders() {}
}
