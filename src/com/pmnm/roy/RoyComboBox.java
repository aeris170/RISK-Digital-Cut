package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.risk.toolkit.Utils;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
public final class RoyComboBox extends DoaObject implements IRoyElement {

	private final DoaVector SELECTED_ELEMENT_CONTENT_OFFSET = new DoaVector(10, 30);
	private final DoaVector ELEMENT_OFFSET = new DoaVector(5, 5);
	private final DoaVector ELEMENT_CONTENT_OFFSET = new DoaVector(10, -4);
	
	@NonNull
	private transient BufferedImage mainBg;
	@NonNull
	private transient BufferedImage buttonIcon = UIConstants.getArrowDownIdleSprite();
	@NonNull
	private transient BufferedImage dropDownBg;
	
	private transient BufferedImage colorBorder;

	@Getter
	@Setter
	private boolean isVisible;
	private boolean isOpen = false;
	private int selectedIndex = 0;
	private Element[] elements;

	private Type type;
	private enum Type {
		STRING, COLOR, SPRITE;
	}
	
	
	public RoyComboBox(String[] names) {
		type = Type.STRING;
		
		elements = new Element[names.length];
		for(int i = 0; i < names.length; i++) {
			elements[i] = new Element(i);
			elements[i].name = names[i];
		}
		
		mainBg = DoaSprites.getSprite("PlayerTypeBorder");
		dropDownBg = DoaSprites.getSprite("DropDownTexType");
		
		addComponent(new Script());
		addComponent(new Renderer());
	}
	
	public RoyComboBox(Color[] colors) {
		type = Type.COLOR;
		
		elements = new Element[colors.length];
		for(int i = 0; i < colors.length; i++) {
			elements[i] = new Element(i);
			elements[i].name = colors[i].toString();
		}

		mainBg = DoaSprites.getSprite("ColorBorder");
		dropDownBg = DoaSprites.getSprite("DropDownColor");
		colorBorder = DoaSprites.getSprite("ColorBorder");
		
		addComponent(new Script());
		addComponent(new Renderer());
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
		
		for(int i = 0; i < elements.length; i++) {
			int[] pos = DoaGraphicsFunctions.warp(transform.position.x + ELEMENT_OFFSET.x, transform.position.y + mainBg.getHeight() + (mainBg.getHeight() - 13) * i + ELEMENT_OFFSET.y);
			int[] size = DoaGraphicsFunctions.warp(dropDownBg.getWidth() - ELEMENT_OFFSET.x * 2, mainBg.getHeight() - ELEMENT_OFFSET.y * 2 - 2);
			
			elements[i].contentArea = new Rectangle(pos[0], pos[1], size[0], size[1]);
		}
		
	}

	@Override
	public Rectangle getContentArea() {
		return null;
	}
	
	private final class Script extends DoaScript {

		public Rectangle buttonArea() {
			int[] pos = DoaGraphicsFunctions.warp(transform.position.x + mainBg.getWidth() - buttonIcon.getWidth() - 4, transform.position.y + 3);
			int[] size = DoaGraphicsFunctions.warp(buttonIcon.getWidth(), buttonIcon.getHeight());

			return new Rectangle(pos[0], pos[1], size[0], size[1]);
			
		}

		private boolean elementIsPressed(Rectangle pos) {
			if(pos.contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y)) && DoaMouse.MB1_RELEASE) {
				return true;
			}
			return false;
		}
		
		@Override
		public void tick() {
			enableDebugRender = true;
			if (!isVisible) return;
			
			if (buttonArea().contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
				if(DoaMouse.MB1_RELEASE) {
					isOpen = !isOpen;
				}
			}
			
			if(!isOpen) return;
			for(int i = 0; i < elements.length; i++) {
				if(elementIsPressed(elements[i].contentArea)) {
					selectedIndex = i;
					isOpen = false;
				}
			}
		}
		
		@Override
		public void debugRender() {
			DoaGraphicsFunctions.setColor(Color.RED);
			DoaGraphicsFunctions.draw(elements[0].contentArea);
			DoaGraphicsFunctions.setColor(Color.GREEN);
			DoaGraphicsFunctions.draw(elements[1].contentArea);
			DoaGraphicsFunctions.setColor(Color.BLUE);
			DoaGraphicsFunctions.draw(elements[2].contentArea);
		}
	}
	
	private final class Renderer extends DoaRenderer {

		private transient Font font = null;

		@NonNull
		private transient BufferedImage buttonIcon2 = UIConstants.getArrowDownPressedSprite();
		
		@Override
		public void render() {
			if (!isVisible) return;
			if (font == null) {
				int[] size = DoaGraphicsFunctions.warp(mainBg.getWidth() - 10, mainBg.getHeight() - 10);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				font = UIConstants.getFont().deriveFont(
					Font.PLAIN,
					Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), contentSize, "COMBOBOXboxbox")
				);
			}

			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawImage(mainBg, 0, 0, mainBg.getWidth(), mainBg.getHeight());
			
			if(type == Type.STRING) {
				DoaGraphicsFunctions.setColor(Color.WHITE);
				DoaGraphicsFunctions.drawString(elements[selectedIndex].name, SELECTED_ELEMENT_CONTENT_OFFSET.x, SELECTED_ELEMENT_CONTENT_OFFSET.y);
			} else if(type == Type.COLOR) {
				DoaGraphicsFunctions.drawImage(colorBorder, 0, 0, colorBorder.getWidth(), colorBorder.getHeight());
			}
			
			if(isOpen) {
				DoaGraphicsFunctions.drawImage(dropDownBg, 0, mainBg.getHeight(), dropDownBg.getWidth(), dropDownBg.getHeight());
				DoaGraphicsFunctions.drawImage(buttonIcon2, mainBg.getWidth() - buttonIcon2.getWidth() - 4, 3, buttonIcon2.getWidth(), buttonIcon2.getHeight());

				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				DoaGraphicsFunctions.setColor(Color.LIGHT_GRAY);
				DoaGraphicsFunctions.fill(elements[selectedIndex].contentArea);
				DoaGraphicsFunctions.popTransform();
				
				DoaGraphicsFunctions.setColor(Color.WHITE);
				for (int i = 0; i < elements.length; i++) {
					String text = elements[i].name;
					int len = elements[i].name.length();
					FontMetrics fm = DoaGraphicsFunctions.getFontMetrics(font);
					if(fm.stringWidth(elements[i].name) > dropDownBg.getWidth() * .9) {
						String sub = elements[i].name.substring(0, len);
						sub += "...";
						while(fm.stringWidth(sub) > dropDownBg.getWidth() * .9) {
							len--;
							sub = elements[i].name.substring(0, len);
							sub += "...";
						}
						text = sub;
					}
					
					DoaGraphicsFunctions.drawString(text, ELEMENT_CONTENT_OFFSET.x, mainBg.getHeight() + elements[i].contentArea.height * (i + 1) + ELEMENT_CONTENT_OFFSET.y);
				}
			} else {
				DoaGraphicsFunctions.drawImage(buttonIcon, mainBg.getWidth() - buttonIcon.getWidth() - 4, 3, buttonIcon.getWidth(), buttonIcon.getHeight());
			}
			
		}
	}
	
	@ToString(includeFieldNames = true)
	@EqualsAndHashCode(callSuper = true)
	private static final class Element extends DoaObject{
		
		private Rectangle contentArea;
		private String name;
		
		public Element(int index) {
		}
	}
}
