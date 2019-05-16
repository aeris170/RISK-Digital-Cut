package com.pmnm.risk.map.continent;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.pmnm.risk.map.province.Province;

public class Continent implements Serializable {

	private static final long serialVersionUID = 2216078176595232966L;

	public static Map<String, Continent> NAME_CONTINENT = new LinkedHashMap<>();

	private String name;
	private Color color;
	private int captureBonus;
	private String abbreviation;
	private Set<Province> provinces;

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public int getCaptureBonus() {
		return captureBonus;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public Set<Province> getProvinces() {
		return provinces;
	}

	public Continent setName(String name) {
		NAME_CONTINENT.remove(this.name);
		this.name = name;
		NAME_CONTINENT.put(name, this);
		return this;
	}

	public Continent setColor(Color color) {
		this.color = color;
		return this;
	}

	public Continent setCaptureBonus(int captureBonus) {
		this.captureBonus = captureBonus;
		return this;
	}

	public Continent setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
		return this;
	}

	public Continent setProvinces(Set<Province> provinces) {
		this.provinces = provinces;
		for (Province p : provinces) {
			p.setContinent(this);
		}
		return this;
	}

	@Override
	public String toString() {
		return "[Continent] Name: " + name + "\n" + Arrays.toString(provinces.toArray(new Province[provinces.size()])) + "\n";
	}

	public static void printAllContinents() {
		NAME_CONTINENT.forEach((s, c) -> System.out.println(c.toString()));
	}
}
