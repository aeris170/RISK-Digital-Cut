package com.pmnm.risk.map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.Player;
import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public final class MapLoader {

	private static final Map<String, Province> NAME_PROVINCE = new HashMap<>();

	private MapLoader() {}

	public static void readMapData(File mapFolder) {
		try {
			String path = "res/maps/" + mapFolder.getPath().replaceAll("\\\\", "/");
			clearExistingMapData();
			createProvinces(new File(path + "/provinces.xml"));
			groupProvinces(new File(path + "/continents.xml"));
			connectProvinces(new File(path + "/neighbours.xml"));
			solidifyProvinces(new File(path + "/vertices.xml"));
			DoaSprites.createSprite("MapBackground", path.substring(path.indexOf('/'), path.length()) + "/map.png");
		} catch (JDOMException | IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void clearExistingMapData() {
		Province.ALL_PROVINCES.clear();
		Continent.NAME_CONTINENT.clear();
		ProvinceHitArea.ALL_PROVINCE_SYMBOLS.clear();
		ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.clear();
		Player.NAME_PLAYER.clear();
		Player.resetIDs();
	}

	private static void createProvinces(File provincesFile) throws JDOMException, IOException {
		Document provincesDocument = new SAXBuilder().build(provincesFile);
		Element provincesElement = provincesDocument.getRootElement();
		provincesElement.getChildren().forEach(province -> NAME_PROVINCE.put(province.getText(), new Province().setName(province.getText())));
	}

	private static void groupProvinces(File continentsFile) throws JDOMException, IOException {
		Document continentsDocument = new SAXBuilder().build(continentsFile);
		Element continentsElement = continentsDocument.getRootElement();
		continentsElement.getChildren().forEach(continentElement -> {
			final Continent continent = new Continent().setName(continentElement.getChildText("name"))
			        .setCaptureBonus(Integer.parseInt(continentElement.getChildText("capture-bonus"))).setAbbreviation(continentElement.getChildText("abbreviation"));
			String[] parsedColor = continentElement.getChildText("color").split(",");
			continent.setColor(new Color(Integer.parseInt(parsedColor[0].trim()), Integer.parseInt(parsedColor[1].trim()), Integer.parseInt(parsedColor[2].trim())));
			final Set<Province> provincesOfContinent = new HashSet<>();
			continentElement.getChildren("province").forEach(child -> provincesOfContinent.add(NAME_PROVINCE.get(child.getText())));
			continent.setProvinces(provincesOfContinent);
		});
	}

	private static void connectProvinces(File neighboursFile) throws JDOMException, IOException {
		Document neighboursDocument = new SAXBuilder().build(neighboursFile);
		Element neighboursElement = neighboursDocument.getRootElement();
		neighboursElement.getChildren().forEach(provinceElement -> {
			final Province province = NAME_PROVINCE.get(provinceElement.getChildText("name"));
			final List<Province> neighboursOfProvince = new ArrayList<>();
			provinceElement.getChildren("neighbour").forEach(child -> neighboursOfProvince.add(NAME_PROVINCE.get(child.getText())));
			province.setNeighbours(neighboursOfProvince);
		});
	}

	private static void solidifyProvinces(File verticesFile) throws JDOMException, IOException {
		Document verticesDocument = new SAXBuilder().build(verticesFile);
		Element verticesElement = verticesDocument.getRootElement();
		verticesElement.getChildren().forEach(provinceElement -> {
			final Province province = NAME_PROVINCE.get(provinceElement.getChildText("name"));
			Element centerVertex = provinceElement.getChild("center").getChild("vertex");
			province.setCenter(new Vertex2D(Integer.parseInt(centerVertex.getChildText("x")), Integer.parseInt(centerVertex.getChildText("y"))));
			provinceElement.getChildren("mesh").forEach(meshElement -> {
				final Mesh2D mesh = new Mesh2D();
				meshElement.getChildren("vertex").forEach(vertex -> {
					int vx = Integer.parseInt(vertex.getChildText("x"));
					int vy = Integer.parseInt(vertex.getChildText("y"));
					mesh.add(new Vertex2D(vx, vy));
				});
				province.addMesh(mesh);
			});
			Builders.PHAB.args(province).scene(Scenes.GAME_SCENE).instantiate();
		});
	}
}