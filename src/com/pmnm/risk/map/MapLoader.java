package com.pmnm.risk.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.doa.engine.DoaHandler;
import com.pmnm.risk.exceptions.RiskStaticInstantiationException;
import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public final class MapLoader {

	private static final String PROVINCE_DATA_PATH = "res/mapdata/provinces.xml";
	private static final String CONTINENT_DATA_PATH = "res/mapdata/continents.xml";
	private static final String NEIGHBOURS_DATA_PATH = "res/mapdata/neighbours.xml";
	private static final String VERTICES_DATA_PATH = "res/mapdata/vertices.xml";

	private MapLoader() {
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
		Document verticesDocument = new SAXBuilder().build(new File(VERTICES_DATA_PATH));
		Element verticesElement = verticesDocument.getRootElement();
		verticesElement.getChildren().forEach(provinceElement -> {
			final Province province = Province.NAME_PROVINCE.get(provinceElement.getChildText("name"));
			provinceElement.getChildren("mesh").forEach(meshElement -> {
				final Mesh2D mesh = new Mesh2D();
				meshElement.getChildren("vertex").forEach(vertex -> {
					int vx = Integer.parseInt(vertex.getChildText("x"));
					int vy = Integer.parseInt(vertex.getChildText("y"));
					mesh.add(new Vertex2D(vx, vy));
				});
				province.addMesh(mesh);
			});
			DoaHandler.instantiateDoaObject(ProvinceHitArea.class, province, 0f, 0f, 0, 0);
		});
	}
}