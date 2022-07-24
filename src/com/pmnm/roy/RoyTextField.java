package com.pmnm.roy;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.pmnm.risk.globals.Globals;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaKeyboard;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@SuppressWarnings("serial")
public class RoyTextField extends DoaObject implements IRoyElement{

	@Getter
	@Setter
	private boolean isVisible;
	private boolean clicked = false;
	private String marker = "|";
	private String text = "";
	
	private int width;
	private int height;
	@Setter
	@NonNull
	private String placeholder = "";
	@Setter
	private int maxCharacters = Integer.MAX_VALUE;
	
	public RoyTextField(int width, int height) {
		this.width = width;
		this.height = height;
		
		addComponent(new InputScript());
		Renderer r = new Renderer();
		addComponent(r);
		addComponent(new MarkerBlinkScript(r));
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
		
		getComponentByType(Renderer.class).ifPresent(
			r -> r.textArea = new Rectangle(
				getContentArea().x + 3,
				getContentArea().y + 3,
				getContentArea().width - 6,
				getContentArea().height - 6
			)
		);
		
	}

	@Override
	public Rectangle getContentArea() {
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(width, height);
		return new Rectangle(
			pos[0],
			pos[1],
			size[0],
			size[1]
		);
	}

	private final class InputScript extends DoaScript {
		
		int counter = 40;
		boolean locked = false;
		
		@Override
		public void tick() {
			enableDebugRender = true;
			if (!isVisible) return;
			if(DoaMouse.MB1_RELEASE) {
				if (getContentArea().contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
					clicked = true;
				} else {
					clicked = false;
				}
			}
			
			if(clicked && !locked && text.length() < maxCharacters) {
				if(DoaKeyboard.A) {
					text += "A";
					locked = true;
				}
			}
			
			if(locked) {
				if(counter > 0)
					counter--;
				else {
					counter = 40;
					locked = false;
				}
			}
			
		}
		
		@Override
		public void debugRender() {
			if(isVisible) {
				DoaGraphicsFunctions.setColor(Color.RED);
				DoaGraphicsFunctions.draw(getContentArea());
			}
		}
	
	}
	
	private final class MarkerBlinkScript extends DoaScript {

		int counter = 0;
		int number = Globals.TICK_RATE / 2;
		Renderer r;
		
		private MarkerBlinkScript(Renderer r) {
			this.r = r;
		}
		
		@Override
		public void tick() {
			counter++;
			r.isMarkerVisible = counter > 0;
			
			if(counter > number) {
				counter = -number;
			}
		}
		
	}
	
	private final class Renderer extends DoaRenderer {
		
		boolean isMarkerVisible = true;
		
		private Rectangle textArea;
		
		public Renderer() {
			Rectangle area = getContentArea();
			textArea = new Rectangle(area.x + 3, area.y + 3, area.width - 6, area.height - 6);
		}
		
		@Override
		public void render() {
			if(!isVisible()) return;
			
			DoaGraphicsFunctions.pushTransform();
			DoaGraphicsFunctions.resetTransform();
			
			DoaGraphicsFunctions.setColor(Color.DARK_GRAY);
			DoaGraphicsFunctions.fill(getContentArea());
			
			if(clicked) {
				DoaGraphicsFunctions.setColor(Color.GRAY);
				DoaGraphicsFunctions.fill(textArea);
			}
			
			DoaGraphicsFunctions.pushClip();
			DoaGraphicsFunctions.setClip(textArea);
			
			if(text.length() > 0) {
				DoaGraphicsFunctions.setColor(Color.WHITE);
				DoaGraphicsFunctions.drawString(text, getContentArea().x + 10, (float) getContentArea().y + 35);
			}
			else {
				DoaGraphicsFunctions.setColor(Color.WHITE.darker().darker());
				DoaGraphicsFunctions.drawString(placeholder, getContentArea().x + 10, (float) getContentArea().y + 35);
			}
			
			DoaGraphicsFunctions.popClip();
			
			if(clicked && isMarkerVisible) {
				DoaGraphicsFunctions.setColor(Color.WHITE);
				DoaGraphicsFunctions.drawString(marker, getContentArea().x + 10 + text.length() * 25, (float) getContentArea().y + 35);
			}

			DoaGraphicsFunctions.popTransform();
		}
		
	}
}
