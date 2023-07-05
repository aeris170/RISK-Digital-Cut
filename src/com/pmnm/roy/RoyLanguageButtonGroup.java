package com.pmnm.roy;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.localization.Translator.Language;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("serial")
public class RoyLanguageButtonGroup extends DoaObject implements IRoyContainer {

	@Getter
	private boolean isVisible;

	private RoyLanguageButton selected;
	private List<RoyLanguageButton> buttons = new ArrayList<>();

	public RoyLanguageButton createButton(Language language) {
		RoyLanguageButton b = new RoyLanguageButton(this, language);
		buttons.add(b);
		return b;
	}

	public RoyLanguageButton getSelected() { return selected; }

	public void setSelected(RoyLanguageButton button) {
		if (buttons.contains(button)) {
			selected = button;
		}
	}

	@Override
	public void onAddToScene(DoaScene scene) {
		super.onAddToScene(scene);
		buttons.forEach(scene::add);
	}

	@Override
	public void onRemoveFromScene(DoaScene scene) {
		super.onRemoveFromScene(scene);
		buttons.forEach(scene::remove);
	}

	@Override
	public void setzOrder(int zOrder) {
		super.setzOrder(zOrder);
		buttons.forEach(b -> b.setzOrder(zOrder + 1));
	}

	@Override
	public void setVisible(boolean value) {
		isVisible = value;
		buttons.forEach(b -> b.setVisible(value));
	}

	@Override
	public void setPosition(DoaVector position) { throw new UnsupportedOperationException(); }

	@Override
	public Rectangle getContentArea() { return new Rectangle(0, 0, 1920, 1080); }

	@Override
	public Iterable<IRoyElement> getElements() { throw new UnsupportedOperationException(); }

	@Override
	public void addElement(@NonNull IRoyElement element) { throw new UnsupportedOperationException(); }

	@Override
	public boolean removeElement(@NonNull IRoyElement element) { throw new UnsupportedOperationException(); }

}
