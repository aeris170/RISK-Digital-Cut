package provinces;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import continents.Continent;

public class Province {

	public static final Map<String, Province> NAME_PROVINCE = new LinkedHashMap<>();
	public static final Map<Color, Province> COLOR_PROVINCE = new HashMap<>();

	private Continent continent;
	private String name;
	private Color color;
	private List<Province> neighbours;

	public Continent getContinent() {
		return continent;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public List<Province> getNeighbours() {
		return neighbours;
	}

	public Province setContinent(Continent continent) {
		this.continent = continent;
		return this;
	}

	public Province setName(String name) {
		this.name = name;
		NAME_PROVINCE.put(name, this);
		return this;
	}

	public Province setColor(Color color) {
		this.color = color;
		COLOR_PROVINCE.put(color, this);
		return this;
	}

	public Province setNeighbours(List<Province> neighbours) {
		this.neighbours = neighbours;
		return this;
	}

	@Override
	public String toString() {
		return "[Province] Continent: " + continent.getName() + "\tName: " + name + "\tColor: RGB(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue()
		        + ")";
	}

	public static void printAllProvinces() {
		NAME_PROVINCE.forEach((s, c) -> System.out.println(c.toString()));
	}
}
