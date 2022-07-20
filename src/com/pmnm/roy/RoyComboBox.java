package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;
import com.pmnm.roy.ui.ZOrders;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
public final class RoyComboBox extends DoaObject implements IRoyElement, Observable {

	private final DoaVector SELECTED_ELEMENT_CONTENT_OFFSET = new DoaVector(10, 30);
	private final DoaVector ELEMENT_CONTENT_OFFSET;
	
	@NonNull
	private transient BufferedImage mainBg;
	@NonNull
	private transient BufferedImage buttonIcon = UIConstants.getArrowDownIdleSprite();
	@NonNull
	private transient BufferedImage dropDownBg;
	
	private transient BufferedImage colorBorder;
	
	private transient BufferedImage colorBorderSelected;

	@Getter
	@Setter
	private boolean isVisible;
	private boolean isOpen = false;
	@Getter
	@Setter
	private int selectedIndex = 0;
	private Element[] elements;

	private Type type;
	private enum Type { STRING, COLOR, SPRITE; }
	
	private List<Integer> lockedIndices = new ArrayList<>();
	
	public RoyComboBox(String[] names) {
		type = Type.STRING;
		ELEMENT_CONTENT_OFFSET = new DoaVector(10, 5);
		
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
		ELEMENT_CONTENT_OFFSET = new DoaVector(3, 3);
		
		elements = new Element[colors.length];
		for(int i = 0; i < colors.length; i++) {
			elements[i] = new Element(i);
			elements[i].color = colors[i];
		}

		mainBg = DoaSprites.getSprite("ColorBorder");
		dropDownBg = DoaSprites.getSprite("DropDownColor");
		colorBorder = DoaSprites.getSprite("ColorBorder");
		colorBorderSelected = DoaSprites.getSprite("ColorBorderSelected");
		
		addComponent(new Script());
		addComponent(new Renderer());
	}
	
	public void setLockedIndices(List<Integer> lockedIndeces) {
		this.lockedIndices = lockedIndeces;
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
		
		int[] pos, size, pos2, size2;
		for(int i = 0; i < elements.length; i++) {
			pos = DoaGraphicsFunctions.warp(position.x,
											position.y + mainBg.getHeight() + dropDownBg.getHeight() / elements.length * i);
			
			size = DoaGraphicsFunctions.warp(dropDownBg.getWidth(),
											 dropDownBg.getHeight() / elements.length);
			
			pos2 = DoaGraphicsFunctions.warp(position.x + ELEMENT_CONTENT_OFFSET.x,
											 position.y + mainBg.getHeight() + dropDownBg.getHeight() / elements.length * i + ELEMENT_CONTENT_OFFSET.y);
			
			size2 = DoaGraphicsFunctions.warp(dropDownBg.getWidth() - ELEMENT_CONTENT_OFFSET.x * 2,
											  dropDownBg.getHeight() / elements.length - ELEMENT_CONTENT_OFFSET.y * 2);

			elements[i].contentArea = new Rectangle(pos2[0], pos2[1], size2[0], size2[1]);
			elements[i].elementArea = new Rectangle(pos[0], pos[1], size[0], size[1]);
		}
		
	}

	@Override
	public Rectangle getContentArea() {
		int[] pos = DoaGraphicsFunctions.warp(transform.position.x, transform.position.y);
		int[] size = DoaGraphicsFunctions.warp(mainBg.getWidth(), mainBg.getHeight());
		return new Rectangle(
			pos[0],
			pos[1],
			size[0],
			size[1]
		);
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
		
		private void setOpen(boolean open) {
			isOpen = open;
			if(isOpen)
				setzOrder(ZOrders.COMBOBOX_OPEN);
			else
				setzOrder(ZOrders.COMBOBOX_CLOSE);
				
		}
		
		@Override
		public void tick() {
			enableDebugRender = false;
			if (!isVisible) return;
			boolean isOpen = RoyComboBox.this.isOpen;
			if(DoaMouse.MB1_RELEASE) {
				if (buttonArea().contains(new Point((int) DoaMouse.X, (int) DoaMouse.Y))) {
					setOpen(!isOpen);
				} else {
					setOpen(false);
				}
			}
			
			if(!isOpen) return;
			for(int i = 0; i < elements.length; i++) {
				if(elementIsPressed(elements[i].elementArea) && !RoyComboBox.this.lockedIndices.contains(i)) {
					selectedIndex = i;
					setOpen(false);
					notifyObservers();
					DoaMouse.MB1_RELEASE = false;
				}
			}
		}
		
		@Override
		public void debugRender() {
			if(isVisible) {
				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				
				DoaGraphicsFunctions.setColor(Color.RED);
				DoaGraphicsFunctions.draw(elements[0].elementArea);
				DoaGraphicsFunctions.setColor(Color.GREEN);
				DoaGraphicsFunctions.draw(elements[1].elementArea);
				DoaGraphicsFunctions.setColor(Color.BLUE);
				DoaGraphicsFunctions.draw(elements[2].elementArea);
				
				DoaGraphicsFunctions.setColor(new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 100));
				DoaGraphicsFunctions.fill(elements[0].contentArea);
				DoaGraphicsFunctions.setColor(new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), 100));
				DoaGraphicsFunctions.fill(elements[1].contentArea);
				DoaGraphicsFunctions.setColor(new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 100));
				DoaGraphicsFunctions.fill(elements[2].contentArea);
				
				DoaGraphicsFunctions.popTransform();
			}
		}
	}
	
	private final class Renderer extends DoaRenderer {

		private transient Font font = null;

		@NonNull
		private transient BufferedImage buttonIcon2 = UIConstants.getArrowDownPressedSprite();
		
		@Override
		public void render() {
			if (!isVisible) return;
			
			if(type == Type.STRING) {
				stringCBRender();
			} else if(type == Type.COLOR) {
				colorCBRender();
			}
		}
		
		private void stringCBRender() {
			if (font == null) {
				int[] size = DoaGraphicsFunctions.warp(mainBg.getWidth() - 10, mainBg.getHeight() - 10);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				font = UIUtils.adjustFontToFitInArea("COMBOBOXboxbox", contentSize);
			}

			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawImage(mainBg, 0, 0, mainBg.getWidth(), mainBg.getHeight());
			DoaGraphicsFunctions.setColor(Color.WHITE);
			DoaGraphicsFunctions.drawString(elements[selectedIndex].name, SELECTED_ELEMENT_CONTENT_OFFSET.x, SELECTED_ELEMENT_CONTENT_OFFSET.y);
			
			if(isOpen) {
				DoaGraphicsFunctions.drawImage(dropDownBg, 0, mainBg.getHeight(), dropDownBg.getWidth(), dropDownBg.getHeight());
				DoaGraphicsFunctions.drawImage(buttonIcon2, mainBg.getWidth() - buttonIcon2.getWidth() - 4, 3, buttonIcon2.getWidth(), buttonIcon2.getHeight());
				
				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				DoaGraphicsFunctions.setColor(new Color(255, 255, 255, 40));
				DoaGraphicsFunctions.fill(elements[selectedIndex].elementArea);
				
				DoaGraphicsFunctions.setColor(Color.WHITE);
				for (int i = 0; i < elements.length; i++) {
					String text = UIUtils.limitString(font, elements[i].name, dropDownBg.getWidth() * 0.9f);
					DoaGraphicsFunctions.drawString(text, elements[i].contentArea.x, elements[i].contentArea.y + elements[i].contentArea.height - ELEMENT_CONTENT_OFFSET.y);
				}
				DoaGraphicsFunctions.popTransform();
			} else {
				DoaGraphicsFunctions.drawImage(buttonIcon, mainBg.getWidth() - buttonIcon.getWidth() - 3, 3, buttonIcon.getWidth(), buttonIcon.getHeight());
			}
		}
		
		private void colorCBRender() {
			DoaGraphicsFunctions.setColor(elements[selectedIndex].color);
			DoaGraphicsFunctions.fillRect(ELEMENT_CONTENT_OFFSET.x, ELEMENT_CONTENT_OFFSET.y, elements[0].elementArea.width - ELEMENT_CONTENT_OFFSET.x * 2, mainBg.getHeight() - ELEMENT_CONTENT_OFFSET.y * 2);
			DoaGraphicsFunctions.drawImage(mainBg, 0, 0, mainBg.getWidth(), mainBg.getHeight());
			
			if(isOpen) {
				DoaGraphicsFunctions.drawImage(dropDownBg, 0, mainBg.getHeight(), dropDownBg.getWidth(), dropDownBg.getHeight());
				DoaGraphicsFunctions.drawImage(buttonIcon2, mainBg.getWidth() - buttonIcon2.getWidth() - 3, 3, buttonIcon2.getWidth(), buttonIcon2.getHeight());

				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				for (int i = 0; i < elements.length; i++) {
					DoaGraphicsFunctions.setColor(elements[i].color);
					DoaGraphicsFunctions.fill(elements[i].contentArea);
					DoaGraphicsFunctions.drawImage(colorBorder, elements[i].elementArea.x, elements[i].elementArea.y, colorBorder.getWidth(), colorBorder.getHeight());
					if(i == selectedIndex)
						DoaGraphicsFunctions.drawImage(
								colorBorderSelected,
								elements[i].elementArea.x + 5,
								elements[i].elementArea.y + 5,
								colorBorder.getWidth() - 10,
								colorBorder.getHeight() - 10);

					DoaGraphicsFunctions.setColor(Color.GRAY);
					if(i != selectedIndex && lockedIndices.contains(i))
						DoaGraphicsFunctions.fill(elements[i].contentArea);
				}
				
				DoaGraphicsFunctions.popTransform();
			} else {
				DoaGraphicsFunctions.drawImage(buttonIcon, mainBg.getWidth() - buttonIcon.getWidth() - 3, 3, buttonIcon.getWidth(), buttonIcon.getHeight());
			}
		}
	}
	
	@ToString(includeFieldNames = true)
	@EqualsAndHashCode(callSuper = true)
	private static final class Element extends DoaObject{
		
		private Rectangle elementArea;
		private Rectangle contentArea;
		private String name;
		private Color color;
		
		public Element(int index) {}
	}

	List<Observer> observers = new ArrayList<>();
	
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObservers() {
		observers.forEach(o -> o.onNotify(this));
	}
}
