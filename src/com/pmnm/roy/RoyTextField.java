package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

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
public class RoyTextField extends DoaObject implements IRoyElement, Observable {

	@Getter
	@Setter
	private boolean isVisible;
	
	private boolean clicked;
	
	@Getter
	@Setter
	private String marker = "|";
	
	@Getter
	private String text = "";
	
	private int width;
	private int height;
	
	@Setter
	@NonNull
	private String placeholder = "";
	
	@Getter
	@Setter
	private int maxCharacters = Integer.MAX_VALUE;
	
	private Renderer renderer;
	
	public RoyTextField(int width, int height) {
		this.width = width;
		this.height = height;
		
		addComponent(new InputScript());
		renderer = new Renderer();
		addComponent(renderer);
		addComponent(new MarkerBlinkScript(renderer));
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
		
		getComponentByType(Renderer.class).ifPresent(r -> {
			Rectangle area = getContentArea();
			r.textArea = new Rectangle(
				area.x + 3,
				area.y + 3,
				area.width - 6,
				area.height - 6
			);
		});
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
		
		@Override
		public void tick() {
			if (!isVisible) return;
			if (DoaMouse.MB1_RELEASE) {
				if (getContentArea().contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
					clicked = true;
				} else {
					clicked = false;
				}
			}
			
			if (clicked) {
				boolean changed = false;
				
				List<Character> chars = DoaKeyboard.getTypedChars();
				for (Character c : chars) {
					if (text.length() < maxCharacters && 
						(('0' <= c && c <= '9') ||	// numbers
						('A' <= c && c <= 'Z') ||	// upper case letters
						('a' <= c && c <= 'z') ||	// lower case letters
						(c == ' '))					// space
					) {
						text += c;
						changed = true;
					} else if(c == '\b' && text.length() > 0) {	// backspace
						text = text.substring(0, text.length() - 1);
						changed = true;
					}
				}
				
				if (changed) {
					renderer.textChanged();
					notifyObservers();
				}
			}
			
		}

		@Override
		public void debugRender() {
			if (isVisible) {
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
		
		private Font font;
		private int stringWidth;
		private int stringHeight;
		private boolean isMarkerVisible = true;
		
		private Rectangle textArea;
		private Rectangle textPadding;
		
		public Renderer() {
			Rectangle area = getContentArea();
			textArea = new Rectangle(area.x + 3, area.y + 3, area.width - 6, area.height - 6);
			textPadding = new Rectangle(7, 0, 0, 0);
		}
		
		@Override
		public void render() {
			if (!isVisible()) return;
			if (font == null) {
				font = UIConstants.getFont().deriveFont(
					Font.BOLD,
					DoaGraphicsFunctions.warp(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), new DoaVector(textArea.width, textArea.height), text), 0)[0]
				);
				
				stringWidth = DoaGraphicsFunctions.unwarpX(UIUtils.textWidth(font, text));
				stringHeight = DoaGraphicsFunctions.unwarpY(UIUtils.textHeight(font));
			}
			
			DoaGraphicsFunctions.pushTransform();
			DoaGraphicsFunctions.resetTransform();
			
			DoaGraphicsFunctions.setColor(Color.DARK_GRAY);
			DoaGraphicsFunctions.fill(getContentArea());
			
			if (clicked) {
				DoaGraphicsFunctions.setColor(Color.GRAY);
				DoaGraphicsFunctions.fill(textArea);
			}
			
			DoaGraphicsFunctions.pushClip();
			DoaGraphicsFunctions.setClip(textArea);
			
			DoaGraphicsFunctions.setFont(font);
			
			if (text.length() > 0) {
				DoaGraphicsFunctions.setColor(Color.WHITE);
				DoaGraphicsFunctions.drawString(text, textArea.x + textPadding.x, textArea.y + textArea.height - stringHeight / 4f);
			} else {
				DoaGraphicsFunctions.setColor(Color.WHITE.darker().darker());
				DoaGraphicsFunctions.drawString(placeholder, textArea.x + textPadding.x, textArea.y + textArea.height - stringHeight / 4f);
			}
			
			DoaGraphicsFunctions.popClip();

			if (clicked && isMarkerVisible) {
				DoaGraphicsFunctions.setColor(Color.WHITE);
				DoaGraphicsFunctions.drawString(marker, textArea.x + stringWidth, textArea.y + textArea.height - stringHeight / 4f);
			}

			DoaGraphicsFunctions.popTransform();
		}
		
		public void textChanged() {
			font = null;
		}
	}

	private List<Observer> observers = new ArrayList<>();
	@Override public void registerObserver(Observer o) { observers.add(o); }
	@Override public void notifyObservers() { observers.forEach(o -> o.onNotify(this)); }
}
