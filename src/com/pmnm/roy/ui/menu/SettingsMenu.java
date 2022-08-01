package com.pmnm.roy.ui.menu;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.globals.localization.Translator.Language;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyLanguageButton;
import com.pmnm.roy.RoyLanguageButtonGroup;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.maths.DoaVector;

@SuppressWarnings("serial")
public class SettingsMenu extends RoyMenu {

	private RoyLanguageButtonGroup group;
	private RoyLanguageButton englishButton;
	private RoyLanguageButton deutchButton;
	private RoyLanguageButton espanolButton;
	private RoyLanguageButton franceButton;
	private RoyLanguageButton italianButton;
	private RoyLanguageButton russianButton;
	private RoyLanguageButton turkishButton;

	private RoyButton backButton;

	public SettingsMenu() {
		group = new RoyLanguageButtonGroup();
		englishButton = group.createButton(Language.EN);
		deutchButton = group.createButton(Language.DE);
		espanolButton = group.createButton(Language.ES);
		franceButton = group.createButton(Language.FR);
		italianButton = group.createButton(Language.IT);
		russianButton = group.createButton(Language.RU);
		turkishButton = group.createButton(Language.TR);
		
		englishButton.setPosition(new DoaVector(100, 650));
		deutchButton.setPosition(new DoaVector(240, 700));
		espanolButton.setPosition(new DoaVector(380, 650));
		franceButton.setPosition(new DoaVector(520, 700));
		italianButton.setPosition(new DoaVector(660, 650));
		russianButton.setPosition(new DoaVector(800, 700));
		turkishButton.setPosition(new DoaVector(940, 650));
		
		switch (Translator.getInstance().getCurrentLanguage()) {
			case EN: group.setSelected(englishButton); break;
			case DE: group.setSelected(deutchButton); break;
			case ES: group.setSelected(espanolButton); break;
			case FR: group.setSelected(franceButton); break;
			case IT: group.setSelected(italianButton); break;
			case RU: group.setSelected(russianButton); break;
			case TR: group.setSelected(turkishButton); break;	
		}
		addElement(group);
		
		backButton = RoyButton.builder()
			.textKey("BACK")
			.action(source -> {
				setVisible(false);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(new DoaVector(1250, 750));
		addElement(backButton);
	}
}