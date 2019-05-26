package com.pmnm.roy.ui.menu;

import java.util.prefs.Preferences;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.localization.Language;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.LanguageButton;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;

public class SettingsMenu extends DoaPanel {

	private static final long serialVersionUID = -7299297354577954327L;

	private static final DoaVectorI LANGUAGE_BUTTON_SIZE = new DoaVectorI((int) (Main.WINDOW_WIDTH * 0.072f), (int) (Main.WINDOW_HEIGHT * 0.129f));

	LanguageButton englishButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.052f, Main.WINDOW_HEIGHT * 0.601f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("English"));
	LanguageButton deutchButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.125f, Main.WINDOW_HEIGHT * 0.694f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Deutch"));
	LanguageButton espanolButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.197f, Main.WINDOW_HEIGHT * 0.601f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Espanol"));
	LanguageButton franceButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.270f, Main.WINDOW_HEIGHT * 0.694f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("France"));
	LanguageButton italianButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.343f, Main.WINDOW_HEIGHT * 0.601f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Italian"));
	LanguageButton russianButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.416f, Main.WINDOW_HEIGHT * 0.694f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Russian"));
	LanguageButton turkishButton = DoaHandler.instantiate(LanguageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.489f, Main.WINDOW_HEIGHT * 0.601f),
	        LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Turkish"));

	LanguageButton selectedButton;

	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.651f, Main.WINDOW_HEIGHT * 0.694f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	public SettingsMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		switch (Translator.getInstance().getCurrentLanguage()) {
			case EN:
				selectedButton = englishButton;
				break;
			case DE:
				selectedButton = deutchButton;
				break;
			case IT:
				selectedButton = italianButton;
				break;
			case RU:
				selectedButton = russianButton;
				break;
			case TR:
				selectedButton = turkishButton;
				break;
			case FR:
				selectedButton = franceButton;
				break;
			case ES:
				selectedButton = espanolButton;
				break;
			default:
				selectedButton = englishButton;
				break;
		}
		selectedButton.select();
		englishButton.addAction(() -> selectButton(englishButton, Language.EN));
		deutchButton.addAction(() -> selectButton(deutchButton, Language.DE));
		espanolButton.addAction(() -> selectButton(espanolButton, Language.ES));
		franceButton.addAction(() -> selectButton(franceButton, Language.FR));
		italianButton.addAction(() -> selectButton(italianButton, Language.IT));
		russianButton.addAction(() -> selectButton(russianButton, Language.RU));
		turkishButton.addAction(() -> selectButton(turkishButton, Language.TR));
		backButton.addAction(() -> {
			hide();
			UIInit.mm.show();
		});
		add(englishButton);
		add(deutchButton);
		add(espanolButton);
		add(franceButton);
		add(italianButton);
		add(russianButton);
		add(turkishButton);
		add(backButton);
		hide();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {}

	private void selectButton(LanguageButton b, Language l) {
		b.select();
		selectedButton.deselect();
		selectedButton = b;
		Translator.getInstance().setCurrentLanguage(l);
		Preferences.userNodeForPackage(getClass()).putInt("language", Translator.getInstance().getCurrentLanguageIndex());
	}
}