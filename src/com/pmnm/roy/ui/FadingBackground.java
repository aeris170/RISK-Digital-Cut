package com.pmnm.roy.ui;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.task.DoaTaskGuard;
import com.pmnm.risk.main.Main;

public class FadingBackground extends DoaObject {
	
	private float alpha = 1f;
	private float alphaDelta = 0.001f;
	
	private DoaTaskGuard fadeGuard = new DoaTaskGuard();

	public FadingBackground() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, 998);
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(DoaGraphicsContext g) {

	}
}