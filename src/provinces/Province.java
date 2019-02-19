package provinces;

import java.util.List;

import continents.Continent;

public class Province {

	private String name;
	private Continent continent;
	private List<Province> neighbours;

	public String getName() {
		return name;
	}

	public Continent getContinent() {
		return continent;
	}

	public List<Province> getNeighbours() {
		return neighbours;
	}

	public Province setName(String name) {
		this.name = name;
		return this;
	}

	public Province setContinent(Continent continent) {
		this.continent = continent;
		return this;
	}

	public Province setNeighbours(List<Province> neighbours) {
		this.neighbours = neighbours;
		return this;
	}

	public List<Province> addNeighbour(Province neighbour) {
		neighbours.add(neighbour);
		return neighbours;
	}
}
