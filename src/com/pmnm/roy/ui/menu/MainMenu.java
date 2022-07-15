package com.pmnm.roy.ui.menu;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaAnimation;
import doa.engine.graphics.DoaAnimations;
import doa.engine.scene.DoaScene;
import doa.engine.scene.elements.renderers.DoaRenderer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.IRoyContainer;
import com.pmnm.roy.IRoyElement;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.ui.UIConstants;

@SuppressWarnings("serial")
public class MainMenu extends DoaObject implements IRoyContainer {

	private static final DoaVector PLAY_OFFLINE_LOCATION 	= new DoaVector(1377f, 511f);
	private static final DoaVector PLAY_ONLINE_LOCATION 	= new DoaVector(1377f, 584f);
	private static final DoaVector SETTINGS_LOCATION 		= new DoaVector(1377f, 657f);
	private static final DoaVector RULES_LOCATION 			= new DoaVector(1377f, 730f);
	private static final DoaVector EXIT_LOCATION 			= new DoaVector(1377f, 803f);
	
	@Getter
	private boolean isVisible;

	private ExitPopup ep;

	private List<IRoyElement> elements;

	public MainMenu() {
		elements = new ArrayList<>();
		
		RoyButton settingsButton = RoyButton
			.builder()
			.textKey("SETTINGS")
			.action(() -> {
				setVisible(false);
				UIConstants.getSettingsMenu().setVisible(true);
			})
			.build();
		settingsButton.setPosition(SETTINGS_LOCATION);
		elements.add(settingsButton);
			
		/* Rules Button */
		RoyButton rulesButton = RoyButton
			.builder()
			.textKey("RULES")
			.action(() -> {
				setVisible(false);
				UIConstants.getBackground().setVisible(false);
				UIConstants.getRulesMenu().setVisible(true);
			})
			.build();
		rulesButton.setPosition(RULES_LOCATION);
		elements.add(rulesButton);
		/* ------------ */
		
		/* Exit Button and related stuff */
		ep = new ExitPopup();
		elements.add(ep);
		
		RoyButton exitButton = RoyButton
			.builder()
			.textKey("EXIT")
			.action(() -> ep.setVisible(true))
			.build();
		exitButton.setPosition(EXIT_LOCATION);
		elements.add(exitButton);
		/* ----------------------------- */
			
		/*
		TextImageButton playOfflineButton = Builders.TIBB.args(PLAY_OFFLINE_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY_OFFLINE", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton playOnlineButton = Builders.TIBB.args(PLAY_ONLINE_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY_ONLINE", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton settingsButton = Builders.TIBB.args(SETTING_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "SETTINGS", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton rulesButton = Builders.TIBB.args(RULES_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "RULES", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		playOfflineButton.addAction(() -> {
			hide();
			UIInit.pofm.show();
		});
		playOnlineButton.addAction(() -> {
			hide();
			UIInit.ponm.show();
		});
		settingsButton.addAction(() -> {
			hide();
			UIInit.sm.show();
		});
		rulesButton.addAction(() -> {
			UIInit.rm.show();
		});
		add(playOfflineButton);
		add(playOnlineButton);
		add(settingsButton);
		add(rulesButton);
		 */
		
		addComponent(new Renderer());
	}
	
	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible) { return; }
			
			DoaAnimation riskLogoAnim = DoaAnimations.getAnimation("RiskLogoAnim");
			DoaGraphicsFunctions.drawAnimation(
				riskLogoAnim,
				1286,
				216,
				riskLogoAnim.current().getWidth(),
				riskLogoAnim.current().getHeight()
			);
		}
	}
	
	@Override
	public void onAddToScene(DoaScene scene) {
		super.onAddToScene(scene);
		for (IRoyElement e : elements) {
			if (e instanceof DoaObject) {
				scene.add((DoaObject) e);
			}
		}
	}

	@Override
	public void onRemoveFromScene(DoaScene scene) {
		super.onRemoveFromScene(scene);
		for (IRoyElement e : elements) {
			if (e instanceof DoaObject) {
				scene.remove((DoaObject) e);
			}
		}
	}
	
	@Override
	public void setzOrder(int zOrder) {
		super.setzOrder(zOrder);
		for (IRoyElement e : elements) {
			if(e instanceof DoaObject) {
				((DoaObject)e).setzOrder(zOrder + 1);
			}
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		elements.forEach(e -> e.setVisible(isVisible));
		ep.setVisible(false); /* Exit popups should not be affected */
	}

	@Override
	public void setPosition(DoaVector position) { throw new UnsupportedOperationException("not implemented"); }

	@Override
	public Rectangle getContentArea() { return new Rectangle(0, 0, 1920, 1080); }

	@Override
	public Iterable<IRoyElement> getElements() { return elements; }

	@Override
	public void addElement(@NonNull IRoyElement element) { elements.add(element); }

	@Override
	public boolean removeElement(@NonNull IRoyElement element) { return elements.remove(element); }
}