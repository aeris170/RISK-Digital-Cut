package map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.doa.engine.DoaHandler;
import com.doa.maths.DoaVectorI;

import continents.Continent;
import exceptions.RiskStaticInstantiationException;
import provinces.Province;
import provinces.ProvinceHitBox;

public final class MapLoader {

	private static final String PROVINCE_DATA_PATH = "res/mapdata/provinces.xml";
	private static final String CONTINENT_DATA_PATH = "res/mapdata/continents.xml";
	private static final String NEIGHBOURS_DATA_PATH = "res/mapdata/neighbours.xml";
	private static final String VERTICES_DATA_PATH = "res/mapdata/vertices.xml";

	private MapLoader() throws RiskStaticInstantiationException {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void readMapData() {
		try {
			createProvinces();
			groupProvinces();
			connectProvinces();
			solidifyProvinces();
		} catch (JDOMException | IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void createProvinces() throws JDOMException, IOException {
		Document provincesDocument = new SAXBuilder().build(new File(PROVINCE_DATA_PATH));
		Element provincesElement = provincesDocument.getRootElement();
		provincesElement.getChildren().forEach(province -> new Province().setName(province.getText()));
	}

	private static void groupProvinces() throws JDOMException, IOException {
		Document continentsDocument = new SAXBuilder().build(new File(CONTINENT_DATA_PATH));
		Element continentsElement = continentsDocument.getRootElement();
		continentsElement.getChildren().forEach(continentElement -> {
			final Continent continent = new Continent().setName(continentElement.getChildText("name"));
			final List<Province> provincesOfContinent = new ArrayList<>();
			continentElement.getChildren("province").forEach(child -> provincesOfContinent.add(Province.NAME_PROVINCE.get(child.getText())));
			continent.setProvinces(provincesOfContinent);
		});
	}

	private static void connectProvinces() throws JDOMException, IOException {
		Document neighboursDocument = new SAXBuilder().build(new File(NEIGHBOURS_DATA_PATH));
		Element neighboursElement = neighboursDocument.getRootElement();
		neighboursElement.getChildren().forEach(provinceElement -> {
			final Province province = Province.NAME_PROVINCE.get(provinceElement.getChildText("name"));
			final List<Province> neighboursOfProvince = new ArrayList<>();
			provinceElement.getChildren("neighbour").forEach(child -> neighboursOfProvince.add(Province.NAME_PROVINCE.get(child.getText())));
			province.setNeighbours(neighboursOfProvince);
		});
	}

	private static void solidifyProvinces() throws JDOMException, IOException {
		// TODO implement
		Document verticesDocument = new SAXBuilder().build(new File(VERTICES_DATA_PATH));
		Element verticesElement = verticesDocument.getRootElement();
		verticesElement.getChildren().forEach(provinceElement -> {
			final Province province = Province.NAME_PROVINCE.get(provinceElement.getChildText("name"));
			final List<DoaVectorI> verticesOfProvince = new ArrayList<>();
			provinceElement.getChildren("vertex").forEach(vertex -> {
				int vx = Integer.parseInt(vertex.getChildText("x"));
				int vy = Integer.parseInt(vertex.getChildText("y"));
				verticesOfProvince.add(new DoaVectorI(vx, vy));
			});
			province.setVertices(verticesOfProvince);
			DoaHandler.instantiateDoaObject(ProvinceHitBox.class, province, 0f, 0f, 0, 0);
		});
	}

	@Deprecated
	private static void readContinentsData() {
		try {
			readContinentData(new SAXBuilder().build(new File(CONTINENT_DATA_PATH)));
		} catch (IOException | JDOMException ex) {
			ex.printStackTrace();
		}
		Province.NAME_PROVINCE.forEach((name, province) -> {
			final List<Province> neighbours = new ArrayList<>();
			// NEIGHBOURS_INTERMEDIATE_DATA.get(province).forEach(neighbourName ->
			// neighbours.add(Province.NAME_PROVINCE.get(neighbourName)));
			province.setNeighbours(neighbours);
		});
	}

	@Deprecated
	private static void readContinentData(Document document) {
		Element continents = document.getRootElement();
		for (Element continent : continents.getChildren()) {
			Continent continentObject = new Continent().setName(continent.getChildText("name"));
			// continentObject.setColor(readContinentColor(continent));
			continentObject.setProvinces(readContinentProvinces(continent));
		}
	}

	@Deprecated
	private static Color readContinentColor(Element continent) {
		int[] continentColorChannels = new int[3];
		List<Element> continentColorElement = continent.getChild("color").getChildren();
		for (int i = 0; i < continentColorElement.size(); i++) {
			continentColorChannels[i] = Integer.parseInt(continentColorElement.get(i).getText());
		}
		return new Color(continentColorChannels[0], continentColorChannels[1], continentColorChannels[2]);
	}

	@Deprecated
	private static List<Province> readContinentProvinces(Element continent) {
		return readProvinceData(continent.getChild("provinces"));
	}

	@Deprecated
	private static List<Province> readProvinceData(Element provinces) {
		List<Province> provincesList = new ArrayList<>();
		for (Element province : provinces.getChildren()) {
			Province provinceObject = new Province().setName(province.getChildText("name"));
			provincesList.add(provinceObject);
			// provinceObject.setColor(readProvinceColor(province));
			// NEIGHBOURS_INTERMEDIATE_DATA.put(provinceObject,
			// readProvinceNeighbours(province));
		}
		return provincesList;
	}

	@Deprecated
	private static Color readProvinceColor(Element province) {
		int[] provinceColorChannels = new int[3];
		List<Element> provinceColorElement = province.getChild("color").getChildren();
		for (int i = 0; i < provinceColorElement.size(); i++) {
			provinceColorChannels[i] = Integer.parseInt(provinceColorElement.get(i).getText());
		}
		return new Color(provinceColorChannels[0], provinceColorChannels[1], provinceColorChannels[2]);
	}

	@Deprecated
	private static List<String> readProvinceNeighbours(Element province) {
		List<String> provinceNeighboursList = new ArrayList<>();
		for (Element neighbour : province.getChild("neighbours").getChildren()) {
			provinceNeighboursList.add(neighbour.getText());
		}
		return provinceNeighboursList;
	}
}