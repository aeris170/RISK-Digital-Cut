package com.pmnm.roy;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("serial")
public class RoyMenu extends DoaObject implements IRoyContainer {
	@Getter
	private boolean isVisible;
	
	private List<IRoyElement> elements = new ArrayList<>();
	
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
