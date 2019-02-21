package continents;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import provinces.Province;

public class Continent {

	private String name;
	private Color color;
	private List<Province> provinces;

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public List<Province> getProvinces() {
		return provinces;
	}

	public Continent setName(String name) {
		this.name = name;
		return this;
	}

	public Continent setColor(Color color) {
		this.color = color;
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
		return "[Continent] Name: " + name + "\tColor: RGB(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")\n\tProvinces:"
		        + Arrays.toString(provinces.toArray(new Province[provinces.size()]));
	}
}
