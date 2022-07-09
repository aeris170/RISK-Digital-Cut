package pmnm.risk.map;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.StandardException;

@Value
@ToString(includeFieldNames = true)
public final class MapConfig implements Serializable {
	
	private static final long serialVersionUID = -6178254628641453859L;
	
	private static boolean INITIALIZED = false;
	private static ImmutableList<@NonNull MapConfig> CONFIGS = null;

	public static void readMapConfigs() {
		List<MapConfig> configs = new ArrayList<>();
		
		File[] maps = new File("res/maps/").listFiles(File::isDirectory);
		for (int i = 0; i < maps.length; i++) {
			MapConfig config = null;
			
			try {
				config = new MapConfig(maps[i].getName());
			} catch(MapValidationException ex) {
				ex.printStackTrace();
				/* swallow the exception? */
			}

			if(config != null) {
				configs.add(config);
			}
		}

		CONFIGS = ImmutableList.copyOf(CONFIGS);
		INITIALIZED = true;
	}
	
	public static Iterable<@NonNull MapConfig> getConfigs() { 
		if(!INITIALIZED) { readMapConfigs(); }
		return CONFIGS;
	}

	@Getter	private final String name;
	@Getter private final Path path;
	@Getter private final File provincesFile;
	@Getter private final File continentsFile;
	@Getter private final File neighborsFile;
	@Getter private final File verticesFile;
	@Getter private final File backgroundImageFile;
	
	public MapConfig(String mapName) throws MapValidationException {
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
	
	public void validate() throws MapValidationException {
		check(path.toFile());
		check(provincesFile);
		check(continentsFile);
		check(neighborsFile);
		check(verticesFile);
		check(backgroundImageFile);
	}
	
	private void check(File f) throws MapValidationException {
		if (!f.exists()) {
			throw new MapValidationException(f.getParentFile().getName() + " " + f.getName() + " not found");
		}
	}
	
	@StandardException
	@SuppressWarnings("serial")
	public class MapValidationException extends Exception {}
}
