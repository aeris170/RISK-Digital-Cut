package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.io.IOException;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.task.DoaTaskGuard;
import com.doa.engine.task.DoaTasker;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.GameInstance;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.actions.ExitButtonAction;
import com.pmnm.roy.ui.actions.LoadButtonAction;
import com.pmnm.roy.ui.actions.RulesButtonAction;
import com.pmnm.roy.ui.actions.SettingsButtonAction;

public final class EscPopup extends DoaPanel {

	// needs Serial Version UID

	private DoaVectorF bounds = new DoaVectorF(Main.WINDOW_WIDTH * 0.300f, Main.WINDOW_HEIGHT * 0.036f);

	// pos width height
	TextImageButton saveButton = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.342f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "SAVE",
			UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton loadButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.407f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "LOAD",
			UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton rulesButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.472f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "RULES",
			UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton settingsButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.537f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"),
			"SETTINGS", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton exitButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.602f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "QUIT",
			UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	
	private boolean hidden = false;	
	DoaTaskGuard escGuard = new DoaTaskGuard();
	
	public EscPopup(MainMenu mm, SettingsMenu sm, RulesMenu rm, LoadMenu lm, ExitPopup ep) {
		super(Main.WINDOW_WIDTH * 0.376f, Main.WINDOW_HEIGHT * 0.291f, (int) (Main.WINDOW_WIDTH * 0.240f),
				(int) (Main.WINDOW_HEIGHT * 0.419f));

		exitButtonPop.addAction(new ExitButtonAction(ep));
		settingsButtonPop.addAction(new SettingsButtonAction(mm, sm, ep));
		rulesButtonPop.addAction(new RulesButtonAction(mm, rm, ep));
		// loadButtonPop.addAction(new LoadButtonAction(mm, lm, ep, pom));
		saveButton.addAction(() -> {
			try {
				GameInstance.saveGame();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// I do not know what to catch here tbh
				e.printStackTrace();
			}
		});
		show();
	}

	@Override
	public void tick() {
		//TODO it fucking sucks
		super.tick();
		DoaTasker.guardExecution(()->{
			if (DoaKeyboard.ESCAPE) {
				hidden = !hidden;			
			}
			if(!hidden) {
				exitButtonPop.tick();
				settingsButtonPop.tick();
				rulesButtonPop.tick();
				loadButtonPop.tick();
				saveButton.tick();
			}
		}, escGuard, 1000);
		
	}
	
	@Override
	public void render(DoaGraphicsContext g) {
		if(!hidden) {
			Composite oldComposite = g.getComposite();
			g.setColor(Color.darkGray);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
			g.setComposite(oldComposite);

			g.drawImage(DoaSprites.get("escapeMenu"), position.x, position.y, width, height);
			
			exitButtonPop.render(g);
			settingsButtonPop.render(g);
			rulesButtonPop.render(g);
			loadButtonPop.render(g);
			saveButton.render(g);
		}
	}
	
	@Override
	public void show(){
		super.show();
		hidden = false;
	}
	
	@Override
	public void hide(){
		super.hide();
		hidden = true;
	}
}