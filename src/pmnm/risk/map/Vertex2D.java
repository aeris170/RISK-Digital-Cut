package pmnm.risk.map;

import java.io.Serializable;

import lombok.Getter;
import lombok.Value;

@Value
public final class Vertex2D implements Serializable {

	private static final long serialVersionUID = 1123855301381378720L;

	@Getter
	private final int x;

	@Getter
	private final int y;

	public Vertex2D(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
}
