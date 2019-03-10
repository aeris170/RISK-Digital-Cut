package com.pmnm.risk.map.continent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pmnm.risk.map.province.Province;

public class Continent implements Serializable {

	private static final long serialVersionUID = 2216078176595232966L;

	public static final Map<String, Continent> NAME_CONTINENT = new LinkedHashMap<>();

	private String name;
	private List<Province> provinces;

	public String getName() {
		return name;
	}

	public List<Province> getProvinces() {
		return provinces;
	}

	public Continent setName(String name) {
		NAME_CONTINENT.remove(this.name);
		this.name = name;
		NAME_CONTINENT.put(name, this);
		return this;
	}

	public Continent setProvinces(List<Province> provinces) {
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
