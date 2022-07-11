package com.pmnm.risk.globals.localization;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.pmnm.roy.ui.UIInit;

public class Translator {

	public enum Language { EN, DE, IT, RU, TR, FR, ES; }

	private static final String LANGUAGE_DATA_PATH = "res/languages/";
	
	private static Translator _this;
	public static Translator getInstance() { return _this == null ? _this = new Translator() : _this; }	

	private Language currentLanguage;
	private Map<Language, Map<String, String>> languages = new EnumMap<>(Language.class);

	private Translator() {
		for (Language l : Language.values()) {
			try {
				Map<String, String> languageData = new HashMap<>();
				Document languageDocument = new SAXBuilder().build(new File(LANGUAGE_DATA_PATH + l.name() + ".xml"));
				Element root = languageDocument.getRootElement();
				root.getChildren().forEach(pair -> languageData.put(pair.getChildText("key").trim(), pair.getChildText("value").trim()));
				languages.put(l, languageData);
			} catch (JDOMException | IOException ex) {
				System.err.println("Exception while reading language data for Language: " + l.name() + " " + ex.getMessage());
			}
		}
		setCurrentLanguageIndex(Preferences.userNodeForPackage(UIInit.class).getInt("language", 0));
	}

	public Language getCurrentLanguage() { return currentLanguage; }
	public int getCurrentLanguageIndex() { return Arrays.asList(Language.values()).indexOf(currentLanguage); }

	public void setCurrentLanguage(Language newLanguage) { currentLanguage = newLanguage; }
	public void setCurrentLanguageIndex(int newLanguage) { currentLanguage = Language.values()[newLanguage]; }

	public String getTranslatedString(String key) {
		String rv = languages.get(currentLanguage).get(key);
		return (rv != null && rv.length() != 0) ? rv : "ROY::UNMAPPED_STR";
	}
}