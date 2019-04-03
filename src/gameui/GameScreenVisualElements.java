package gameui;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.main.Main;

public class GameScreenVisualElements extends DoaObject {

	private static final long serialVersionUID = -71765518001943590L;

	private static final int OFFSET = 6;

	public GameScreenVisualElements() {
		super(0f, 0f, DoaObject.STATIC_FRONT);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, 0 - OFFSET);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - DoaSprites.get("MainMenuBottomRing").getHeight() + OFFSET);
	}
}
