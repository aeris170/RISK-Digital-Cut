package main;

public class ProvinceBuilder {

	private ProvinceBuilder() {}

	/* WORK_IN_PROGRESS */
	public static Province buildProvince(String name, String group) {
		return new Province().setName(name).setGroup(group);
	}
}
