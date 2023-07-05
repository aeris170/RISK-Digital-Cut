package pmnm.risk.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.common.collect.ImmutableList;

import doa.engine.core.DoaGame;
import doa.engine.graphics.DoaSprites;
import lombok.experimental.UtilityClass;
import pmnm.risk.map.Mesh2D.Mesh2DBuilder;
import pmnm.risk.map.MeshCollection.MeshCollectionBuilder;

@UtilityClass
public final class MapLoader {

	private static final Map<String, ProvinceData> NAME_PROVINCE = new HashMap<>();
	private static final List<ContinentData> CONTINENTS = new ArrayList<>();

	public static MapData loadMap(MapConfig map) {
		NAME_PROVINCE.clear();
		CONTINENTS.clear();
		BufferedImage bgImg = null;

		try {
			createProvinces(map.getProvincesFile()); /* fetch all province names*/
			connectProvinces(map.getNeighborsFile()); /* set up adjacencies */
			solidifyProvinces(map.getVerticesFile()); /* set up meshes */
			groupProvinces(map.getContinentsFile()); /* create continents */
			bgImg = loadBackgroundImage(map.getName(), map.getBackgroundImageFile());
			return new MapData(map, bgImg, ImmutableList.copyOf(CONTINENTS));
		} catch (JDOMException | IOException ex) {
			DoaGame.exit();
			JOptionPane.showConfirmDialog(null,
			    ex.getMessage(),
			    "Map loader error",
			    JOptionPane.OK_OPTION,
			    JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}

	private static void createProvinces(File provincesFile) throws JDOMException, IOException {
		Document provincesDocument = createXMLDocumentFrom(provincesFile);
		Element provincesElement = provincesDocument.getRootElement();
		provincesElement.getChildren().forEach(province -> {
			String name = province.getText();
			ProvinceData provinceData = new ProvinceData(name);
			NAME_PROVINCE.put(name, provinceData);
		});
	}

	private static void connectProvinces(File neighboursFile) throws JDOMException, IOException {
		Document neighboursDocument = createXMLDocumentFrom(neighboursFile);
		Element neighboursElement = neighboursDocument.getRootElement();
		neighboursElement.getChildren().forEach(provinceElement -> {
			final String provinceName = provinceElement.getChildText("name");

			final ProvinceData province = NAME_PROVINCE.get(provinceName);

			List<ProvinceData> neighbors = new ArrayList<>();
			provinceElement.getChildren("neighbour").forEach(neighborElement -> {
				String neighborName = neighborElement.getText();
				ProvinceData neighbor = NAME_PROVINCE.get(neighborName);
				neighbors.add(neighbor);
			});
			province.setNeighbors(ImmutableList.copyOf(neighbors));
		});
	}

	private static void solidifyProvinces(File verticesFile) throws JDOMException, IOException {
		Document verticesDocument = createXMLDocumentFrom(verticesFile);
		Element verticesElement = verticesDocument.getRootElement();
		verticesElement.getChildren().forEach(provinceElement -> {
			final String provinceName = provinceElement.getChildText("name");

			final ProvinceData province = NAME_PROVINCE.get(provinceName);

			MeshCollectionBuilder builder = MeshCollection.builder();

			Element centerVertex = provinceElement.getChild("center").getChild("vertex");
			builder.center(new Vertex2D(Integer.parseInt(centerVertex.getChildText("x")), Integer.parseInt(centerVertex.getChildText("y"))));

			provinceElement.getChildren("mesh").forEach(meshElement -> {
				Mesh2DBuilder mBuilder = Mesh2D.builder();
				meshElement.getChildren("vertex").forEach(vertex -> {
					int vx = Integer.parseInt(vertex.getChildText("x"));
					int vy = Integer.parseInt(vertex.getChildText("y"));
					mBuilder.vertex(new Vertex2D(vx, vy));
				});

				builder.mesh(mBuilder.build());

			});

			province.setMeshes(builder.build());
		});
	}

	private static void groupProvinces(File continentsFile) throws JDOMException, IOException {
		Document continentsDocument = createXMLDocumentFrom(continentsFile);
		Element continentsElement = continentsDocument.getRootElement();
		continentsElement.getChildren().forEach(continentElement -> {
			String name = continentElement.getChildText("name");
			String abb = continentElement.getChildText("abbreviation");
			int captureBonus = Integer.parseInt(continentElement.getChildText("capture-bonus"));

			String[] channels = continentElement.getChildText("color").split(",");
			Color color = new Color(Integer.parseInt(channels[0].trim()), Integer.parseInt(channels[1].trim()), Integer.parseInt(channels[2].trim()));

			List<ProvinceData> provinces = new ArrayList<>();
			continentElement.getChildren("province").forEach(child -> {
				final String provinceName = child.getText();

				final ProvinceData province = NAME_PROVINCE.get(provinceName);

				provinces.add(province);
			});

			ContinentData c = new ContinentData(name, abb, captureBonus, color, ImmutableList.copyOf(provinces));
			CONTINENTS.add(c);

			provinces.forEach(province -> province.setContinent(c));
		});
	}

	private static BufferedImage loadBackgroundImage(String mapName, File backgroundImage) throws IOException {
		String p = backgroundImage.getPath();
		p = p.substring(p.indexOf(File.separator)).replace(File.separator, "/");
		return DoaSprites.createSprite(mapName + "MapBackground", p);
	}

	private static Document createXMLDocumentFrom(File file) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		builder.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		builder.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		return builder.build(file);
	}
}
