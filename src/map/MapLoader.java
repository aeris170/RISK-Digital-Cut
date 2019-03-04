package map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import continents.Continent;
import exceptions.RiskStaticInstantiationException;
import provinces.Province;

public final class MapLoader {

	private static final String CONTINENT_DATA_PATH = "res/continents.xml";
	private static final String HITBOX_DATA_PATH = "/maps/ColorMapNew.png";

	private static final Map<Province, List<String>> NEIGHBOURS_INTERMEDIATE_DATA = new LinkedHashMap<>();
	private static int count = 0;

	private MapLoader() throws RiskStaticInstantiationException {
		throw new RiskStaticInstantiationException(getClass());
	}

	public static void readMapData() {
		readContinentsData();
		readColorMapData();
	}

	private static void readContinentsData() {
		try {
			readContinentData(new SAXBuilder().build(new File(CONTINENT_DATA_PATH)));
		} catch (IOException | JDOMException ex) {
			ex.printStackTrace();
		}
		Province.NAME_PROVINCE.forEach((name, province) -> {
			final List<Province> neighbours = new ArrayList<>();
			NEIGHBOURS_INTERMEDIATE_DATA.get(province).forEach(neighbourName -> neighbours.add(Province.NAME_PROVINCE.get(neighbourName)));
			province.setNeighbours(neighbours);
		});
	}

	private static void readContinentData(Document document) {
		Element continents = document.getRootElement();
		for (Element continent : continents.getChildren()) {
			Continent continentObject = new Continent().setName(continent.getChildText("name"));
			continentObject.setColor(readContinentColor(continent));
			continentObject.setProvinces(readContinentProvinces(continent));
		}
	}

	private static Color readContinentColor(Element continent) {
		int[] continentColorChannels = new int[3];
		List<Element> continentColorElement = continent.getChild("color").getChildren();
		for (int i = 0; i < continentColorElement.size(); i++) {
			continentColorChannels[i] = Integer.parseInt(continentColorElement.get(i).getText());
		}
		return new Color(continentColorChannels[0], continentColorChannels[1], continentColorChannels[2]);
	}

	private static List<Province> readContinentProvinces(Element continent) {
		return readProvinceData(continent.getChild("provinces"));
	}

	private static List<Province> readProvinceData(Element provinces) {
		List<Province> provincesList = new ArrayList<>();
		for (Element province : provinces.getChildren()) {
			Province provinceObject = new Province().setName(province.getChildText("name"));
			provincesList.add(provinceObject);
			provinceObject.setColor(readProvinceColor(province));
			NEIGHBOURS_INTERMEDIATE_DATA.put(provinceObject, readProvinceNeighbours(province));
		}
		return provincesList;
	}

	private static Color readProvinceColor(Element province) {
		int[] provinceColorChannels = new int[3];
		List<Element> provinceColorElement = province.getChild("color").getChildren();
		for (int i = 0; i < provinceColorElement.size(); i++) {
			provinceColorChannels[i] = Integer.parseInt(provinceColorElement.get(i).getText());
		}
		return new Color(provinceColorChannels[0], provinceColorChannels[1], provinceColorChannels[2]);
	}

	private static List<String> readProvinceNeighbours(Element province) {
		List<String> provinceNeighboursList = new ArrayList<>();
		for (Element neighbour : province.getChild("neighbours").getChildren()) {
			provinceNeighboursList.add(neighbour.getText());
		}
		return provinceNeighboursList;
	}

	private static void readColorMapData() {
		try {
			final BufferedImage levelImage = ImageIO.read(MapLoader.class.getResourceAsStream(HITBOX_DATA_PATH));
			final int w = levelImage.getWidth();
			final int h = levelImage.getHeight();
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					final int pixel = levelImage.getRGB(x, y);
					final int red = pixel >> 16 & 0xff;
					final int green = pixel >> 8 & 0xff;
					final int blue = pixel & 0xff;
					final float xx = x;
					final float yy = y;
					Province p = Province.COLOR_PROVINCE.get(new Color(red, green, blue));
					if (p != null) {
						// DoaHandler.instantiateDoaObject(ProvinceHitBox.class, p, xx * 24, yy * 24,
						// 24, 24);
					}
				}
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}

	}
}