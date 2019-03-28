package com.pmnm.risk.ui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.ui.actions.ExitButtonAction;
import com.pmnm.risk.ui.actions.PlayOfflineButtonAction;
import com.pmnm.risk.ui.actions.PlayOnlineButtonAction;
import com.pmnm.risk.ui.actions.RulesButtonAction;
import com.pmnm.risk.ui.actions.SettingsButtonAction;

public final class UIInit {

	private static final String BUTTON_IDLE_SPRITE = "ButtonIdle";
	private static final String BUTTON_HOVER_SPRITE = "ButtonHover";

	private static final DoaVectorI MM_BUTTON_SIZE = new DoaVectorI(387, 60);

	private static final DoaVectorF MM_PLAY_OFFLINE_LOCATION = new DoaVectorF(1377f, 511f);
	private static final DoaVectorF MM_PLAY_ONLINE_LOCATION = new DoaVectorF(1377f, 584f);
	private static final DoaVectorF MM_SETTING_LOCATION = new DoaVectorF(1377f, 657f);
	private static final DoaVectorF MM_RULES_LOCATION = new DoaVectorF(1377f, 730f);
	private static final DoaVectorF MM_EXIT_LOCATION = new DoaVectorF(1377f, 803f);

	private UIInit() {}

	public static void initUI() {
		MainMenu mm = DoaHandler.instantiateDoaObject(MainMenu.class);
		mm.add(DoaHandler.instantiateDoaObject(DoaImageButton.class, MM_PLAY_OFFLINE_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y, new PlayOfflineButtonAction(mm),
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE)));
		mm.add(DoaHandler.instantiateDoaObject(DoaImageButton.class, MM_PLAY_ONLINE_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y, new PlayOnlineButtonAction(),
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE)));
		mm.add(DoaHandler.instantiateDoaObject(DoaImageButton.class, MM_SETTING_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y, new SettingsButtonAction(),
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE)));
		mm.add(DoaHandler.instantiateDoaObject(DoaImageButton.class, MM_RULES_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y, new RulesButtonAction(),
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE)));
		mm.add(DoaHandler.instantiateDoaObject(DoaImageButton.class, MM_EXIT_LOCATION, MM_BUTTON_SIZE.x, MM_BUTTON_SIZE.y, new ExitButtonAction(),
		        DoaSprites.get(BUTTON_IDLE_SPRITE), DoaSprites.get(BUTTON_HOVER_SPRITE)));
		mm.setActive(true);
	}
}
