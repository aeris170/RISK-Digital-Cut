package com.pmnm.roy.ui.menu;

import java.util.prefs.Preferences;

import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.localization.Language;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.LanguageButton;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.ui.panel.DoaUIPanel;

public class SettingsMenu extends DoaUIPanel {

	private static final long serialVersionUID = -7299297354577954327L;

	private static final DoaVector LANGUAGE_BUTTON_SIZE = new DoaVector(Main.WINDOW_WIDTH * 0.072f, Main.WINDOW_HEIGHT * 0.129f);

	LanguageButton englishButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.052f, Main.WINDOW_HEIGHT * 0.601f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("English")).instantiate();
	LanguageButton deutchButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.125f, Main.WINDOW_HEIGHT * 0.694f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Deutch")).instantiate();
	LanguageButton espanolButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.197f, Main.WINDOW_HEIGHT * 0.601f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Espanol")).instantiate();
	LanguageButton franceButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.270f, Main.WINDOW_HEIGHT * 0.694f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("France")).instantiate();
	LanguageButton italianButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.343f, Main.WINDOW_HEIGHT * 0.601f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Italian")).instantiate();
	LanguageButton russianButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.416f, Main.WINDOW_HEIGHT * 0.694f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Russian")).instantiate();
	LanguageButton turkishButton = Builders.LBB
	        .args(new DoaVector(Main.WINDOW_WIDTH * 0.489f, Main.WINDOW_HEIGHT * 0.601f), LANGUAGE_BUTTON_SIZE.x, LANGUAGE_BUTTON_SIZE.y, DoaSprites.get("Turkish")).instantiate();

	LanguageButton selectedButton;

	TextImageButton backButton = Builders.TIBB.args(new DoaVector(Main.WINDOW_WIDTH * 0.651f, Main.WINDOW_HEIGHT * 0.694f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "BACK", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();

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
			UIConstants.mm.show();
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