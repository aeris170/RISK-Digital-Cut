package main;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Province {

	public static final Map<String, Province> NAMES = new ConcurrentHashMap<>();
	public static final Map<String, List<Province>> GROUPS = new ConcurrentHashMap<>();

	private String name = "";
	private String group = "";
	private Set<Province> connectedProvinces = new CopyOnWriteArraySet<>();

	public Province setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public Province setGroup(String group) {
		this.group = group;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public Province connectTo(Province p) {
		connectedProvinces.add(p);
		p.connectTo(this);
		return this;
	}
}
