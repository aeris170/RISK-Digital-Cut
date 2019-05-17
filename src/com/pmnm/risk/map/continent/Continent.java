package com.pmnm.risk.map.continent;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.pmnm.risk.main.Player;
import com.pmnm.risk.map.province.Province;

public class Continent implements Serializable {

	private static final long serialVersionUID = 2216078176595232966L;

	public static Map<String, Continent> NAME_CONTINENT = new LinkedHashMap<>();

	private String name;
	private Color color;
	private int captureBonus;
	private String abbreviation;
	private Set<Province> provinces;

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public int getCaptureBonus() {
		return captureBonus;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public Set<Province> getProvinces() {
		return provinces;
	}

	public Continent setName(String name) {
		NAME_CONTINENT.remove(this.name);
		this.name = name;
		NAME_CONTINENT.put(name, this);
		return this;
	}

	public Continent setColor(Color color) {
		this.color = color;
		return this;
	}

	public Continent setCaptureBonus(int captureBonus) {
		this.captureBonus = captureBonus;
		return this;
	}

	public Continent setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
		return this;
	}

	public Continent setProvinces(Set<Province> provinces) {
		this.provinces = provinces;
		for (Province p : provinces) {
			p.setContinent(this);
		}
		return this;
	}

	@Override
	public String toString() {
		return "[Continent] Name: " + name + "\n" + Arrays.toString(provinces.toArray(new Province[provinces.size()]))
				+ "\n";
	}

	public static void printAllContinents() {
		NAME_CONTINENT.forEach((s, c) -> System.out.println(c.toString()));
	}

	
	@Override
	public boolean equals(Object o) {
		//return super.equals(o);
		if (!(o instanceof Continent))
			return false;
		Continent contO = (Continent) o;
		if(contO.provinces != null && this.provinces != null) {
			if(!contO.provinces.equals(this.provinces)) {
				return false;
			}
		}
		if(contO.color != null && this.color != null) {
			if(!contO.color.equals(this.color)) {
				return false;
			}
		}
		return contO.name.equals(this.name)  && contO.captureBonus == this.captureBonus
				&& contO.abbreviation.equals(this.abbreviation);
	}
	
	 @Override
		public int hashCode() {
			//return super.hashCode();
			int hash = 17;
			// Suitable nullity checks etc, of course :)
			if(this.provinces!= null)
				hash = hash * 23 + provinces.hashCode();
			if(this.color!= null)
				hash = hash * 23 + color.hashCode();
			if(this.name!= null)
				hash = hash * 23 + name.hashCode();
			hash = 23 * hash + captureBonus;
			if(this.abbreviation!= null)
				hash = hash * 23 + abbreviation.hashCode();
			return hash; 
		}
}
