package com.pmnm.roy.ui.menu;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.globals.localization.Translator.Language;
import com.pmnm.roy.IRoyContainer;
import com.pmnm.roy.IRoyElement;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyLanguageButton;
import com.pmnm.roy.RoyLanguageButtonGroup;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("serial")
public class SettingsMenu extends DoaObject implements IRoyContainer {
	
	@Getter
	private boolean isVisible = true;
	private transient List<IRoyElement> elements = new ArrayList<>();

	private RoyLanguageButtonGroup group;
	private RoyLanguageButton englishButton;
	private RoyLanguageButton deutchButton;
	private RoyLanguageButton espanolButton;
	private RoyLanguageButton franceButton;
	private RoyLanguageButton italianButton;
	private RoyLanguageButton russianButton;
	private RoyLanguageButton turkishButton;

	private RoyButton backButton;

	public SettingsMenu() {
		group = new RoyLanguageButtonGroup();
		englishButton = group.createButton(Language.EN);
		deutchButton = group.createButton(Language.DE);
		espanolButton = group.createButton(Language.ES);
		franceButton = group.createButton(Language.FR);
		italianButton = group.createButton(Language.IT);
		russianButton = group.createButton(Language.RU);
		turkishButton = group.createButton(Language.TR);
		
		englishButton.setPosition(new DoaVector(100, 650));
		deutchButton.setPosition(new DoaVector(240, 700));
		espanolButton.setPosition(new DoaVector(380, 650));
		franceButton.setPosition(new DoaVector(520, 700));
		italianButton.setPosition(new DoaVector(660, 650));
		russianButton.setPosition(new DoaVector(800, 700));
		turkishButton.setPosition(new DoaVector(940, 650));
		
		switch (Translator.getInstance().getCurrentLanguage()) {
			case EN: group.setSelected(englishButton); break;
			case DE: group.setSelected(deutchButton); break;
			case ES: group.setSelected(espanolButton); break;
			case FR: group.setSelected(franceButton); break;
			case IT: group.setSelected(italianButton); break;
			case RU: group.setSelected(russianButton); break;
			case TR: group.setSelected(turkishButton); break;	
		}
		elements.add(group);
		
		backButton = RoyButton.builder()
			.textKey("BACK")
			.action(() -> {
				setVisible(false);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(new DoaVector(1250, 750));
		elements.add(backButton);
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