package com.pmnm.risk.map;
import java.io.File;
import java.nio.file.Path;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
public final class MapConfig {

	@Getter	private final String name;
	@Getter private final Path path;
	@Getter private final File provincesFile;
	@Getter private final File continentsFile;
	@Getter private final File neighborsFile;
	@Getter private final File verticesFile;
	@Getter private final File backgroundImageFile;
	
	public MapConfig(String mapName) {
		name = mapName;
		path = Path.of("res/maps", mapName);
		check(path.toFile());
		
		provincesFile = new File(path + "/provinces.xml");
		check(provincesFile);
		
		continentsFile = new File(path + "/continents.xml");
		check(continentsFile);
		
		neighborsFile = new File(path + "/neighbours.xml");
		check(neighborsFile);
		
		verticesFile = new File(path + "/vertices.xml");
		check(verticesFile);
		
		backgroundImageFile = new File(path + "/map.png");
		check(backgroundImageFile);
	}
	
	public void validate() {
		check(path.toFile());
		check(provincesFile);
		check(continentsFile);
		check(neighborsFile);
		check(verticesFile);
		check(backgroundImageFile);
	}
	
	private void check(File f) {
		if (!f.exists()) {
			throw new RuntimeException(f.getName() + " not found");
		}
	}
}
