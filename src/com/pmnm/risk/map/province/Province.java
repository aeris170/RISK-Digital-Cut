package com.pmnm.risk.map.province;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.doa.maths.DoaVectorI;
import com.pmnm.risk.main.Player;
import com.pmnm.risk.map.Mesh2D;
import com.pmnm.risk.map.continent.Continent;

public class Province implements Serializable {

	private static final long serialVersionUID = 157817527974605181L;

	public static List<Province> ALL_PROVINCES = new ArrayList<>();
	public static List<Province> UNCLAIMED_PROVINCES = new ArrayList<>();

	private Continent continent;
	private String name;
	private List<Province> neighbours;
	private List<Mesh2D> meshes = new ArrayList<>();
	private DoaVectorI center;
	private boolean isClaimed;
	private Player owner;
	private int troops;

	public Province() {
		ALL_PROVINCES.add(this);
		UNCLAIMED_PROVINCES.add(this);
	}

	public Continent getContinent() {
		return continent;
	}

	public String getName() {
		return name;
	}

	public List<Province> getNeighbours() {
		return neighbours;
	}

	public List<Mesh2D> getMeshes() {
		return meshes;
	}

	public DoaVectorI getCenter() {
		return center;
	}

	public void setCenter(DoaVectorI centerVertex) {
		center = centerVertex;
	}

	public Province setContinent(Continent continent) {
		this.continent = continent;
		return this;
	}

	public Province setName(String name) {
		this.name = name;
		return this;
	}

	public Province setNeighbours(List<Province> neighbours) {
		this.neighbours = neighbours;
		return this;
	}

	public Province setMeshes(List<Mesh2D> meshes) {
		this.meshes = meshes;
		return this;
	}

	public void addMesh(Mesh2D mesh) {
		meshes.add(mesh);
	}

	@Override
	public String toString() {
		return "Province [continent=" + continent + ", name=" + name + ", neighbours=" + neighbours + ", meshes="
				+ meshes + ", center=" + center + ", isClaimed=" + isClaimed + ", owner=" + owner + ", troops=" + troops
				+ "]";
	}

	public static void printAllProvinces() {
		ALL_PROVINCES.forEach(p -> System.out.print(p.toString()));
	}

	public boolean isClaimed() {
		return isClaimed;
	}

	public void getClaimedBy(Player player) {
		UNCLAIMED_PROVINCES.remove(this);
		owner = player;
		isClaimed = true;
		troops = 1;
	}

	public int getTroops() {
		return troops;
	}

	public void addTroops(int troops) {
		this.troops += troops;
	}

	public void removeTroops(int troops) {
		this.troops -= troops;
	}

	public boolean isOwnedBy(Player player) {
		return owner.equals(player);
	}

	public Player getOwner() {
		return owner;
	}

	public static Province getRandomUnclaimedProvince() {
		return UNCLAIMED_PROVINCES.get(ThreadLocalRandom.current().nextInt(UNCLAIMED_PROVINCES.size()));
	}

	public void getOccupiedBy(Player player) {
		owner = player;
	}

	public int troopCount() {
		return troops;
	}

	public ProvinceHitArea getProvinceHitArea() {
		return ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.stream().filter(hitArea -> hitArea.getProvince().equals(this))
				.findFirst().orElse(null);
	}

	@Override
	public boolean equals(Object o) {
		return this.name.equals(((Province) o).name) && ((Province) o).troops == this.troops
				&& ((owner != null && ((Province) o).owner != null) ? ((Province) o).owner.equals(this.owner) : true);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 23 + (owner != null ? owner.hashCode() : 0);
		hash = 23 * hash + troops;
		return hash;
	}
}
