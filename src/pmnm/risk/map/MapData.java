package pmnm.risk.map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import com.google.common.collect.ImmutableList;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
public final class MapData implements Serializable {

	private static final long serialVersionUID = 3162242848938299845L;

	@Getter
	@NonNull
	private final MapConfig config;

	@Getter
	@NonNull
	private transient BufferedImage backgroundImage;

	@NonNull
	private final ImmutableList<ContinentData> continents;
	public Iterable<ContinentData> getContinents() {
		return continents;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(backgroundImage, "png", out);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		in.defaultReadObject();
		backgroundImage = ImageIO.read(in);
	}
}
