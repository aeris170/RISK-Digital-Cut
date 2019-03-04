package continents;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import provinces.Province;

public class Continent {

	public static final Map<String, Continent> NAME_CONTINENT = new LinkedHashMap<>();
	public static final Map<Color, Continent> COLOR_CONTINENT = new HashMap<>();

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
		NAME_CONTINENT.put(name, this);
		return this;
	}

	public Continent setColor(Color color) {
		this.color = color;
		COLOR_CONTINENT.put(color, this);
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
		return "[Continent] Name: " + name + "\tColor: RGB(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")\n\t"
		        + Arrays.toString(provinces.toArray(new Province[provinces.size()]));
	}

	public static void printAllContinents() {
		NAME_CONTINENT.forEach((s, c) -> System.out.println(c.toString()));
	}
}
