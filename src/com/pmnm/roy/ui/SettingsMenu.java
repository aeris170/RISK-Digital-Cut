package com.pmnm.roy.ui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.localization.Language;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.Main;

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

	LanguageButton selectedButton = englishButton;

	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.651f, Main.WINDOW_HEIGHT * 0.694f),
	        UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR,
	        UIInit.HOVER_FONT_COLOR);

	public SettingsMenu(MainMenu mm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		selectedButton.select();
		englishButton.addAction(() -> {
			englishButton.select();
			selectedButton.deselect();
			selectedButton = englishButton;
			Translator.getInstance().setCurrentLanguage(Language.EN);
		});
		deutchButton.addAction(() -> {
			deutchButton.select();
			selectedButton.deselect();
			selectedButton = deutchButton;
			Translator.getInstance().setCurrentLanguage(Language.DE);
		});
		espanolButton.addAction(() -> {
			espanolButton.select();
			selectedButton.deselect();
			selectedButton = espanolButton;
			Translator.getInstance().setCurrentLanguage(Language.ES);
		});
		franceButton.addAction(() -> {
			franceButton.select();
			selectedButton.deselect();
			selectedButton = franceButton;
			Translator.getInstance().setCurrentLanguage(Language.FR);
		});
		italianButton.addAction(() -> {
			italianButton.select();
			selectedButton.deselect();
			selectedButton = italianButton;
			Translator.getInstance().setCurrentLanguage(Language.IT);
		});
		russianButton.addAction(() -> {
			russianButton.select();
			selectedButton.deselect();
			selectedButton = russianButton;
			Translator.getInstance().setCurrentLanguage(Language.RU);
		});
		turkishButton.addAction(() -> {
			turkishButton.select();
			selectedButton.deselect();
			selectedButton = turkishButton;
			Translator.getInstance().setCurrentLanguage(Language.TR);
		});
		backButton.addAction(() -> {
			hide();
			mm.show();
		});
		add(englishButton);
		add(deutchButton);
		add(espanolButton);
		add(franceButton);
		add(italianButton);
		add(russianButton);
		add(turkishButton);
		add(backButton);
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("GenericBackground"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
	}
}