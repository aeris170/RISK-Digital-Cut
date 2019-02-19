package continents;

import java.util.List;

import provinces.Province;

public class Continent {

	private String name;
	private List<Province> provinces;

	public String getName() {
		return name;
	}

	public List<Province> getProvinces() {
		return provinces;
	}

	public Continent setName(String name) {
		this.name = name;
		return this;
	}

	public Continent setProvinces(List<Province> provinces) {
		this.provinces = provinces;
		return this;
	}
}
