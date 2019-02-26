package map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import continents.Continent;
import exceptions.RiskStaticInstantiationException;
import provinces.Province;

public final class MapLoader {

	public static final Map<String, Continent> CONTINENTS = new LinkedHashMap<>();
	public static final Map<String, Province> PROVINCES = new LinkedHashMap<>();

	private static final Map<Province, List<String>> NEIGHBOURS_INTERMEDIATE_DATA = new LinkedHashMap<>();

	private MapLoader() throws RiskStaticInstantiationException {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void readMapData(String continentsXMLFilePath) {
		try {
			File inputFile = new File("res/continents.xml");
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(inputFile);
			Element continents = document.getRootElement();
			for (Element continent : continents.getChildren()) {
				String continentName = continent.getChildText("name");
				CONTINENTS.put(continentName, new Continent().setName(continentName));
				int[] continentColorChannels = new int[3];
				List<Element> continentColorElement = continent.getChild("color").getChildren();
				for (int i = 0; i < continentColorElement.size(); i++) {
					continentColorChannels[i] = Integer.parseInt(continentColorElement.get(i).getText());
				}
				CONTINENTS.get(continentName).setColor(new Color(continentColorChannels[0], continentColorChannels[1], continentColorChannels[2]));
				List<Province> provincesList = new ArrayList<>();
				Element provinces = continent.getChild("provinces");
				for (Element province : provinces.getChildren()) {
					String provinceName = province.getChildText("name");
					PROVINCES.put(provinceName, new Province().setName(provinceName));
					int[] provinceColorChannels = new int[3];
					List<Element> provinceColorElement = province.getChild("color").getChildren();
					for (int i = 0; i < provinceColorElement.size(); i++) {
						provinceColorChannels[i] = Integer.parseInt(provinceColorElement.get(i).getText());
					}
					provincesList.add(PROVINCES.get(provinceName).setColor(new Color(provinceColorChannels[0], provinceColorChannels[1], provinceColorChannels[2])));
					List<String> provinceNeighboursList = new ArrayList<>();
					for (Element neighbour : province.getChild("neighbours").getChildren()) {
						provinceNeighboursList.add(neighbour.getText());
					}
					NEIGHBOURS_INTERMEDIATE_DATA.put(PROVINCES.get(provinceName), provinceNeighboursList);
				}
				CONTINENTS.get(continentName).setProvinces(provincesList);
			}
		} catch (IOException | JDOMException ex) {
			ex.printStackTrace();
		}
		assignNeighboursToProvinces();
	}

	private static void assignNeighboursToProvinces() {
		PROVINCES.forEach((name, province) -> {
			final List<Province> neighbours = new ArrayList<>();
			NEIGHBOURS_INTERMEDIATE_DATA.get(province).forEach(neighbourName -> neighbours.add(PROVINCES.get(neighbourName)));
			province.setNeighbours(neighbours);
		});
	}
}