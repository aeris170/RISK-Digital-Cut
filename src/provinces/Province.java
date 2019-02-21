package provinces;

import java.awt.Color;
import java.util.List;

import continents.Continent;

public class Province {

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
		return this;
	}

	public Province setColor(Color color) {
		this.color = color;
		return this;
	}

	public Province setNeighbours(List<Province> neighbours) {
		this.neighbours = neighbours;
		return this;
	}

	@Override
	public String toString() {
		return "\n\t[Province] Name: " + name + "\tColor: RGB(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
	}
}
