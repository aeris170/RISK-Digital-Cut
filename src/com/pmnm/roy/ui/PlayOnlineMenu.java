package com.pmnm.roy.ui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.network.Client;
import com.pmnm.risk.network.SocServer;

public class PlayOnlineMenu extends DoaPanel {

	private static final long serialVersionUID = 1834343492692029824L;

	TextImageButton hostGameButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 511f),
			UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
			DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "HOST_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
	TextImageButton joinGameButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 584f),
			UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
			DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "JOIN_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 803f),
			UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
			DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);

	MainMenu mm;
	MultiPlayerMenuHost mpmh;

	public PlayOnlineMenu(MainMenu mm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

		this.mm = mm;

		hostGameButton.addAction(() -> {
			hide();
			mm.hide();
			mpmh = DoaHandler.instantiate(MultiPlayerMenuHost.class, this);
			SocServer.startServer(2);
		});

		joinGameButton.addAction(() ->{
			Client c = new Client("Host", "139.179.124.195");
		});

		backButton.addAction(() -> {
			hide();
			mm.show();
			SocServer.stopServer();
		});

		add(hostGameButton);
		add(joinGameButton);
		add(backButton);
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("BG1"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH,
					UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d,
					UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT,
					UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0,
				Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		g.drawAnimation(DoaAnimations.get("RiskLogoAnim"), 1286, 220);
	}

}
