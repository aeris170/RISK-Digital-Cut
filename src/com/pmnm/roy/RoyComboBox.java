package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.globals.ZOrders;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
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
	
	 /* used when combo needs to display something not on its list */
	private static final int OVERRIDEN_INDEX = -1979779;

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
	private boolean isVisible;
	
	@Getter
	@Setter
	private boolean isEditable = true;
	
	private boolean isOpen = false;
	
	@Getter
	@Setter
	private int selectedIndex = 0;
	
	private transient Element[] elements;

	private transient Object selectionOverride;

	@Setter
	private List<Integer> lockedIndices = new ArrayList<>();
	
	public RoyComboBox(String[] names) {
		ELEMENT_CONTENT_OFFSET = new DoaVector(10, 5);
		
		elements = new Element[names.length];
		for(int i = 0; i < names.length; i++) {
			elements[i] = new Element(i);
			elements[i].name = names[i];
		}
		
		mainBg = UIConstants.getPlayerTypeBorderSprite();
		dropDownBg = UIConstants.getPlayerTypeDropdownBgSprite();
		
		addComponent(new Script());
		addComponent(new StringComboRenderer());
	}
	
	public RoyComboBox(Color[] colors) {
		ELEMENT_CONTENT_OFFSET = new DoaVector(3, 3);
		
		elements = new Element[colors.length];
		for(int i = 0; i < colors.length; i++) {
			elements[i] = new Element(i);
			elements[i].color = colors[i];
		}

		mainBg = UIConstants.getColorBorderSprite();
		dropDownBg = UIConstants.getColorDropdownBgSprite();
		colorBorder = UIConstants.getColorBorderSprite();
		colorBorderSelected = UIConstants.getColorBorderSelectedSprite();
		
		addComponent(new Script());
		addComponent(new ColorComboRenderer());
	}
	
	public RoyComboBox(BufferedImage[] images) {
		ELEMENT_CONTENT_OFFSET = new DoaVector(5, 5);
		
		elements = new Element[images.length];
		for(int i = 0; i < images.length; i++) {
			elements[i] = new Element(i);
			elements[i].image = images[i];
		}

		mainBg = UIConstants.getColorBorderSprite();
		dropDownBg = UIConstants.getColorDropdownBgSprite();
		colorBorder = UIConstants.getColorBorderSprite();
		colorBorderSelected = UIConstants.getColorBorderSelectedSprite();
		
		addComponent(new Script());
		addComponent(new SpriteComboRenderer());
	}
	
	public int getElementCount() { return elements.length; }
	
	public void setSelectionOverride(Object o){
		selectionOverride = o;
		selectedIndex = OVERRIDEN_INDEX;
	}
	
	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
		
		for(int i = 0; i < elements.length; i++) {
			elements[i].elementArea = new Rectangle(
				(int) position.x,
				(int) (position.y + mainBg.getHeight() + dropDownBg.getHeight() / elements.length * i),
				dropDownBg.getWidth(),
				dropDownBg.getHeight() / elements.length
			);
			elements[i].contentArea = new Rectangle(
				(int) (position.x + ELEMENT_CONTENT_OFFSET.x),
				(int) (position.y + mainBg.getHeight() + dropDownBg.getHeight() / elements.length * i + ELEMENT_CONTENT_OFFSET.y),
				(int) (dropDownBg.getWidth() - ELEMENT_CONTENT_OFFSET.x * 2),
				(int) (dropDownBg.getHeight() / elements.length - ELEMENT_CONTENT_OFFSET.y * 2)
			);
		}
	}

	@Override
	public Rectangle getContentArea() {
		return new Rectangle(
			(int) transform.position.x,
			(int) transform.position.y,
			mainBg.getWidth(),
			mainBg.getHeight()
		);
	}
	
	@Override
	public void setVisible(boolean value) {
		isVisible = value;
		if (isVisible) {
			if (lockedIndices.contains(selectedIndex)) {
				for(int i = 0; i < elements.length; i++) {
					int currentIndex = i;
					if (!lockedIndices.contains(currentIndex)) {
						selectedIndex = currentIndex;
						break;
					}
				}
			}
		}
	}
	
	private final class Script extends DoaScript {

		public Rectangle buttonArea() {
			return new Rectangle(
				(int) (transform.position.x + mainBg.getWidth() - buttonIcon.getWidth() - 4),
				(int) (transform.position.y + 3),
				buttonIcon.getWidth(),
				buttonIcon.getHeight()
			);
		}

		private boolean elementIsPressed(Rectangle pos) {
			int mouseX = DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			return pos.contains(new Point(mouseX, mouseY)) && DoaMouse.MB1_RELEASE;
		}
		
		private void setOpen(boolean open) {
			if (!isEditable) { return; }
			isOpen = open;
			setzOrder(isOpen ? ZOrders.COMBOBOX_OPEN : ZOrders.COMBOBOX_CLOSE);
		}
		
		@Override
		public void tick() {
			if (!isVisible) { return; }
			if (!isEditable) { return; }
			boolean isOpen = RoyComboBox.this.isOpen;
			if (DoaMouse.MB1_RELEASE) {
				int mouseX = DoaGraphicsFunctions.unwarpX(DoaMouse.X);
				int mouseY = DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
				if (buttonArea().contains(new Point(mouseX, mouseY))) {
					setOpen(!isOpen);
				} else {
					setOpen(false);
				}
			}
			
			if (!isOpen) { return; }
			for (int i = 0; i < elements.length; i++) {
				if (elementIsPressed(elements[i].elementArea)) {
					DoaMouse.MB1 = false;
					DoaMouse.MB1_HOLD = false;
					DoaMouse.MB1_RELEASE = false;

					if(!RoyComboBox.this.lockedIndices.contains(i)) {
						selectedIndex = i;
						setOpen(false);
						notifyObservers();
					}
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
				
				DoaGraphicsFunctions.setColor(new Color(255, 0, 0, 100));
				DoaGraphicsFunctions.fill(elements[0].contentArea);
				DoaGraphicsFunctions.setColor(new Color(0, 255, 0, 100));
				DoaGraphicsFunctions.fill(elements[1].contentArea);
				DoaGraphicsFunctions.setColor(new Color(0, 0, 255, 100));
				DoaGraphicsFunctions.fill(elements[2].contentArea);
				
				DoaGraphicsFunctions.popTransform();
			}
		}
	}
	
	private final class StringComboRenderer extends DoaRenderer {

		private transient Font font = null;

		@NonNull
		private transient BufferedImage buttonIcon2 = UIConstants.getArrowDownPressedSprite();
		
		@Override
		public void render() {
			if (!isVisible) return;
			font = null;
			if (font == null) {
				DoaVector contentSize = new DoaVector(mainBg.getWidth() - 10, mainBg.getHeight() - 10);
				font = UIUtils.adjustFontToFitInArea("COMBOBOXboxbox", contentSize);
			}

			DoaGraphicsFunctions.setFont(font);
			
			DoaGraphicsFunctions.drawImage(mainBg, 0, 0, mainBg.getWidth(), mainBg.getHeight());
			DoaGraphicsFunctions.setColor(Color.WHITE);
			
			if (selectedIndex == OVERRIDEN_INDEX) {
				DoaGraphicsFunctions.drawString(selectionOverride.toString(), SELECTED_ELEMENT_CONTENT_OFFSET.x, SELECTED_ELEMENT_CONTENT_OFFSET.y);
			} else {
				DoaGraphicsFunctions.drawString(elements[selectedIndex].name, SELECTED_ELEMENT_CONTENT_OFFSET.x, SELECTED_ELEMENT_CONTENT_OFFSET.y);
			}
			
			if (isOpen) {
				DoaGraphicsFunctions.drawImage(dropDownBg, 0, mainBg.getHeight(), dropDownBg.getWidth(), dropDownBg.getHeight());
				DoaGraphicsFunctions.drawImage(buttonIcon2, mainBg.getWidth() - buttonIcon2.getWidth() - 4, 3, buttonIcon2.getWidth(), buttonIcon2.getHeight());
				
				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				
				if (selectedIndex != OVERRIDEN_INDEX) {
					DoaGraphicsFunctions.setColor(new Color(255, 255, 255, 40));
					DoaGraphicsFunctions.fill(elements[selectedIndex].elementArea);
				}
				
				DoaGraphicsFunctions.setColor(Color.WHITE);
				for (int i = 0; i < elements.length; i++) {
					String text = UIUtils.limitString(font, elements[i].name, dropDownBg.getWidth() * 0.9f);
					DoaGraphicsFunctions.drawString(
						text,
						elements[i].contentArea.x,
						elements[i].contentArea.y + elements[i].contentArea.height - ELEMENT_CONTENT_OFFSET.y
					);
				}
				DoaGraphicsFunctions.popTransform();
			} else {
				if (isEditable) {
					DoaGraphicsFunctions.drawImage(buttonIcon, mainBg.getWidth() - buttonIcon.getWidth() - 3, 3, buttonIcon.getWidth(), buttonIcon.getHeight());
				}
			}
		}
	}
	
	private final class ColorComboRenderer extends DoaRenderer {

		@NonNull
		private transient BufferedImage buttonIcon2 = UIConstants.getArrowDownPressedSprite();
		
		@Override
		public void render() {
			if (!isVisible) return;
			
			DoaGraphicsFunctions.setColor(elements[selectedIndex].color);
			DoaGraphicsFunctions.fillRect(ELEMENT_CONTENT_OFFSET.x, ELEMENT_CONTENT_OFFSET.y, elements[0].elementArea.width - ELEMENT_CONTENT_OFFSET.x * 2, mainBg.getHeight() - ELEMENT_CONTENT_OFFSET.y * 2);
			DoaGraphicsFunctions.drawImage(mainBg, 0, 0, mainBg.getWidth(), mainBg.getHeight());
			
			if (isOpen) {
				DoaGraphicsFunctions.drawImage(dropDownBg, 0, mainBg.getHeight(), dropDownBg.getWidth(), dropDownBg.getHeight());
				DoaGraphicsFunctions.drawImage(buttonIcon2, mainBg.getWidth() - buttonIcon2.getWidth() - 3, 3, buttonIcon2.getWidth(), buttonIcon2.getHeight());

				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				for (int i = 0; i < elements.length; i++) {
					DoaGraphicsFunctions.setColor(elements[i].color);
					DoaGraphicsFunctions.fill(elements[i].contentArea);
					DoaGraphicsFunctions.drawImage(colorBorder, elements[i].elementArea.x, elements[i].elementArea.y, colorBorder.getWidth(), colorBorder.getHeight());
					if (i == selectedIndex) {
						DoaGraphicsFunctions.drawImage(
							colorBorderSelected,
							elements[i].elementArea.x + 5,
							elements[i].elementArea.y + 5,
							colorBorder.getWidth() - 10,
							colorBorder.getHeight() - 10);
					}
					DoaGraphicsFunctions.setColor(Color.DARK_GRAY);
					if (i != selectedIndex && lockedIndices.contains(i)) {
						DoaGraphicsFunctions.fill(elements[i].contentArea);
					}
				}
				
				DoaGraphicsFunctions.popTransform();
			} else {
				if (isEditable) {
					DoaGraphicsFunctions.drawImage(buttonIcon, mainBg.getWidth() - buttonIcon.getWidth() - 3, 3, buttonIcon.getWidth(), buttonIcon.getHeight());
				}
			}
		}
	}
	
	private final class SpriteComboRenderer extends DoaRenderer {

		@NonNull
		private transient BufferedImage buttonIcon2 = UIConstants.getArrowDownPressedSprite();
		
		@Override
		public void render() {
			if (!isVisible) return;
			
			DoaGraphicsFunctions.drawImage(
				elements[selectedIndex].image,
				(mainBg.getWidth() - buttonIcon.getWidth()) / 2 - ELEMENT_CONTENT_OFFSET.x * 2,
				ELEMENT_CONTENT_OFFSET.y * 2,
				elements[selectedIndex].contentArea.height - ELEMENT_CONTENT_OFFSET.y * 2,
				elements[selectedIndex].contentArea.height - ELEMENT_CONTENT_OFFSET.y * 2
			);
			DoaGraphicsFunctions.drawImage(mainBg, 0, 0, mainBg.getWidth(), mainBg.getHeight());
			
			if(isOpen) {
				DoaGraphicsFunctions.drawImage(dropDownBg, 0, mainBg.getHeight(), dropDownBg.getWidth(), dropDownBg.getHeight());
				DoaGraphicsFunctions.drawImage(buttonIcon2, mainBg.getWidth() - buttonIcon2.getWidth() - 3, 3, buttonIcon2.getWidth(), buttonIcon2.getHeight());

				DoaGraphicsFunctions.pushTransform();
				DoaGraphicsFunctions.resetTransform();
				for (int i = 0; i < elements.length; i++) {
					DoaGraphicsFunctions.drawImage(
						elements[i].image,
						elements[i].elementArea.x + (elements[i].contentArea.width - (elements[i].contentArea.height - ELEMENT_CONTENT_OFFSET.y * 2)) / 2 + ELEMENT_CONTENT_OFFSET.x,
						elements[i].elementArea.y + ELEMENT_CONTENT_OFFSET.y * 2,
						elements[i].contentArea.height - ELEMENT_CONTENT_OFFSET.y * 2,
						elements[i].contentArea.height - ELEMENT_CONTENT_OFFSET.y * 2
					);
					DoaGraphicsFunctions.drawImage(colorBorder, elements[i].elementArea.x, elements[i].elementArea.y, colorBorder.getWidth(), colorBorder.getHeight());
					if(i == selectedIndex) {
						DoaGraphicsFunctions.drawImage(
							colorBorderSelected,
							elements[i].elementArea.x + 5,
							elements[i].elementArea.y + 5,
							colorBorder.getWidth() - 10,
							colorBorder.getHeight() - 10);
					}

					DoaGraphicsFunctions.setColor(Color.DARK_GRAY);
					if(i != selectedIndex && lockedIndices.contains(i)) {
						DoaGraphicsFunctions.fill(elements[i].contentArea);
					}
				}
				
				DoaGraphicsFunctions.popTransform();
			} else {
				if (isEditable) {
					DoaGraphicsFunctions.drawImage(buttonIcon, mainBg.getWidth() - buttonIcon.getWidth() - 3, 3, buttonIcon.getWidth(), buttonIcon.getHeight());
				}
			}
		}
	}
	
	@ToString(includeFieldNames = true)
	@EqualsAndHashCode
	private static final class Element {
		
		private Rectangle elementArea;
		private Rectangle contentArea;
		private String name;
		private Color color;
		private BufferedImage image;
		
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
