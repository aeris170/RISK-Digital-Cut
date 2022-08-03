package com.pmnm.roy;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
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
	private boolean isChecked;

	private final BufferedImage circle;
	private final BufferedImage blip;

	@Getter
	@Setter
	private String text;
	
	public RoyCheckBox() {
		circle = UIConstants.getReadyCircleSprite();
		blip = UIConstants.getReadyBlipSprite();
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
		int[] size = DoaGraphicsFunctions.warp(circle.getWidth(), circle.getHeight());
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

			DoaGraphicsFunctions.drawImage(circle, 0, 0, circle.getWidth(), circle.getHeight());

			if(isChecked)
				DoaGraphicsFunctions.drawImage(blip, 3, 3, blip.getWidth(), blip.getHeight());
		}
	}
}
