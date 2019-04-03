package gameui;

import java.awt.Rectangle;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;

public class InfoPanel extends DoaObject {

	private static final long serialVersionUID = -59674351675589726L;

	private static final DoaSprite TEXTURE = DoaSprites.get("infoPanel");

	public InfoPanel() {
		super(-227f, 732f, TEXTURE.getWidth() - 100, TEXTURE.getHeight() - 100, DoaObject.STATIC_FRONT);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(TEXTURE, position.x, position.y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) position.x, (int) position.y, width, height);
	}
}
