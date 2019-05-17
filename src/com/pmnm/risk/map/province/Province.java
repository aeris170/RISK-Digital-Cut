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

	/*
	 * 
	 * private boolean isClaimed; private int troops;
	 */

	@Override
	public boolean equals(Object o) {
		//return super.equals(o);
		if (!(o instanceof Province))
			return false;
		Province playO = (Province) o;
		if (playO.continent != null && this.continent != null) {
			if (!playO.continent.equals(this.continent)) 
				return false;
		}
		if (playO.name != null && this.name != null) {
			if (!playO.name.equals(this.name)) 
				return false;
		}
		if (playO.neighbours != null && this.neighbours != null) {
			if (!playO.neighbours.equals(this.neighbours))
				return false;
		}
		if (playO.owner != null && this.owner != null) {
			if (!playO.owner.equals(this.owner)) 
				return false;
		}
		return playO.troops == this.troops && playO.isClaimed == this.isClaimed;
	}

	@Override
	public int hashCode() {
		//return super.hashCode();
		int hash = 17;
		// Suitable nullity checks etc, of course :)
		if (this.continent != null)
			hash = hash * 23 + continent.hashCode();
		if (this.name != null)
			hash = hash * 23 + name.hashCode();
		if (this.neighbours != null)
			hash = hash * 23 + neighbours.hashCode();
		if (this.owner != null)
			hash = hash * 23 + owner.hashCode();
		hash = 23 * hash + troops;
		hash = 23 * hash + (isClaimed ? 1 : 0);
		return hash;
	}
}
