package com.pmnm.risk.map.province;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.doa.maths.DoaVectorI;
import com.pmnm.risk.main.Player;
import com.pmnm.risk.map.Mesh2D;
import com.pmnm.risk.map.continent.Continent;

public class Province implements Serializable {

	private static final long serialVersionUID = 157817527974605181L;

	public static final Map<String, Province> NAME_PROVINCE = new LinkedHashMap<>();

	private Continent continent;
	private String name;
	private List<Province> neighbours;
	private List<Mesh2D> meshes = new ArrayList<>();
	private DoaVectorI center;
	private boolean isOccupied;
	private Player owner;
	private int troops;

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
		NAME_PROVINCE.remove(this.name);
		this.name = name;
		NAME_PROVINCE.put(name, this);
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
		return "[Province] Continent: " + continent.getName() + "\tName: " + name + "\n";
	}

	public static void printAllProvinces() {
		NAME_PROVINCE.forEach((s, p) -> System.out.print(p.toString()));
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void getOccupiedBy(Player player) {
		owner = player;
		isOccupied = true;
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
}
