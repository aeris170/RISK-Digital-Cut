package java_dom_parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Translater {

	public static List<LanguageType> lanuageTypesList;
	public static List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
	/*
	public static void main(String[] args) {
		//currEnum bana seçilen dili vericek static olarak
		LanguageType currEnum = LanguageType.ger; 
		// call for the reading of the languages
	    //bu method xml dosyalarýný okuyup tüm dilleri okuyup liste yazýyor
		List<Map<String, String>> returnedListOfLanguages = readLanguagesData();  
		

		// call for selected language
		//Bu method Listten istenilen dilin mapini çekiyor
		Map<String, String> languageHash = returnHashOfWantedLanguage(currEnum, returnedListOfLanguages);

		//bu da istenilen kelimeyi çekiyor 
		returner("SETTINGS", languageHash)
		// call for words
		System.out.println(returner("SETTINGS", languageHash));

	}
	*/

	public static Map<String, String> returnHashOfWantedLanguage(LanguageType currEnum,
			List<Map<String, String>> returnedListOfLanguages) {

		int placeOfLanguageHash = 0;

		for (int i = 0; i < newList.size(); i++) {
			if (currEnum == lanuageTypesList.get(i)) {
				// prints the language
				// System.out.println(newList.get(i).toString());
				placeOfLanguageHash = i;
			}
		}
		return newList.get(placeOfLanguageHash);
	}

	public static String returner(String word, Map<String, String> returnedHashMapForLanguage) {
		return returnedHashMapForLanguage.get(word);
	}

	public static List<Map<String, String>> readLanguagesData() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			lanuageTypesList = new ArrayList<LanguageType>(EnumSet.allOf(LanguageType.class));

			// System.out.println(lanuageTypesList.size());

			for (int i = 0; i < lanuageTypesList.size(); i++) {

				HashMap<String, String> languagesHashes = new HashMap<>();

				Document doc = builder.parse(lanuageTypesList.get(i).toString() + ".xml");
				System.out.println(lanuageTypesList.get(i).toString() + ".xml");

				NodeList pairList = doc.getElementsByTagName("pair");
				for (int j = 0; j < pairList.getLength(); j++) {
					Node p = pairList.item(j);
					if (p.getNodeType() == Node.ELEMENT_NODE) {
						Element e = (Element) p;
						NodeList keyList = e.getElementsByTagName("key");
						NodeList valueList = e.getElementsByTagName("value");
						String key = keyList.item(0).getTextContent();
						String value = valueList.item(0).getTextContent();
						languagesHashes.put(key, value);
					}
				}

				newList.add(i, languagesHashes);
			}

		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newList;
	}

}