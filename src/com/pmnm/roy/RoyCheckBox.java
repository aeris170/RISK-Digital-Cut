package com.pmnm.roy;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class RoyCheckBox extends DoaObject implements IRoyElement {

	@Getter
	@Setter
	private boolean isVisible;

	@Getter
	private boolean isChecked = false;

	private final BufferedImage holder = DoaSprites.getSprite("ReadyCircle");
	private final BufferedImage blip = DoaSprites.getSprite("Ready");

	@Getter
	@Setter
	private String text;
	
	public RoyCheckBox() {
		addComponent(new Script());
		addComponent(new Renderer());
	}

	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
	}

	@Override
	public Rectangle getContentArea() {
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(holder.getWidth(), holder.getHeight());
		return new Rectangle(
			pos[0],
			pos[1],
			size[0],
			size[1]
		);
	}

	private final class Script extends DoaScript {

		@Override
		public void tick() {
			if(!isVisible()) return;
			if(!DoaMouse.MB1) { return; }
			
			Rectangle area = getContentArea();
			if (area.contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
				isChecked = !isChecked;
			}
		}
	}
	
	private final class Renderer extends DoaRenderer {
		@Override
		public void render() {
			if(!isVisible()) return;

			DoaGraphicsFunctions.drawImage(holder, 0, 0, holder.getWidth(), holder.getHeight());

			if(isChecked)
				DoaGraphicsFunctions.drawImage(blip, 3, 3, blip.getWidth(), blip.getHeight());
		}
	}
}
