package com.pmnm.roy;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.util.Observable;
import com.pmnm.util.Observer;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@SuppressWarnings("serial")
public final class RoyMiniButton extends DoaObject implements IRoyInteractableElement, Observer {

	@Getter
	@Setter
	private boolean isVisible = true;

	@Getter
	@Setter
	private boolean isEnabled = true;

	@Getter
	@Setter
	private String textKey;

	@NonNull
	private transient BufferedImage image;

	@NonNull
	private transient BufferedImage hoverImage;

	@NonNull
	private transient BufferedImage pressImage;

	@NonNull
	private transient BufferedImage disabledImage;

	@Setter
	private transient IRoyAction action = null;

	private transient BufferedImage currentImage;

	private int width = 0;
	private int height = 0;

	@Getter
	@Setter
	private IRoyTextHandler textHandler;

	@Builder
	RoyMiniButton(@NonNull String textKey, @NonNull IRoyAction action) {
		this.textKey = textKey;
		this.action = action;
		this.image = UIConstants.getMiniButtonIdleSprite();
		this.hoverImage = UIConstants.getMiniButtonHoverSprite();
		this.pressImage = UIConstants.getMiniButtonPressedSprite();
		this.disabledImage = image;
		this.textHandler = RoyText.builder()
			.parentArea(getContentArea())
			.textArea(new Rectangle(
				(int) (transform.position.x),
				(int) (transform.position.y),
				(int) (image.getWidth() * 0.70f),
				(int) (image.getHeight() * 0.70f)
			))
			.text(Translator.getInstance().getTranslatedString(textKey))
			.centered(true)
			.build();

		currentImage = image;

		width = image.getWidth();
		height = image.getHeight();

		addComponent(new Script());
		addComponent(new Renderer());

		Translator.getInstance().registerObserver(this);
	}

	@Override
	public void setPosition(DoaVector position) {
		transform.position.x = position.x;
		transform.position.y = position.y;
		textHandler.setParentArea(getContentArea());
		textHandler.setTextArea(new Rectangle(
			(int) (transform.position.x),
			(int) (transform.position.y),
			(int) (image.getWidth() * 0.70f),
			(int) (image.getHeight() * 0.70f)
		));
	}

	@Override
	public Rectangle getContentArea() {
		return new Rectangle(
			(int) transform.position.x,
			(int) transform.position.y,
			image.getWidth(),
			image.getHeight()
		);
	}

	@Override
	public void onNotify(Observable b) {
		textHandler.setText(Translator.getInstance().getTranslatedString(textKey));
	}

	private final class Script extends DoaScript {

		@Override
		public void tick() {
			if (!isVisible()) { return; }

			if (!RoyMiniButton.this.isEnabled()) {
				currentImage = disabledImage;
				return;
			}
			Rectangle area = getContentArea();
			int mouseX = (int) DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = (int) DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			if (area.contains(new Point(mouseX, mouseY))) {
				if(currentImage == pressImage && DoaMouse.MB1_RELEASE) {
					action.execute(RoyMiniButton.this);
				} else if(DoaMouse.MB1 || DoaMouse.MB1_HOLD) {
					currentImage = pressImage;
				} else {
					currentImage = hoverImage;
				}
			} else {
				currentImage = image;
			}
			getTextHandler().tick();
		}
	}

	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible) { return; }

			DoaGraphicsFunctions.drawImage(currentImage, 0, 0, image.getWidth(), image.getHeight());
			getTextHandler().render();
		}

		@Override
		public void debugRender() {
			if (!isVisible) { return; }

			DoaGraphicsFunctions.setColor(Color.RED);
			DoaGraphicsFunctions.drawRect(0, 0, width, height);
			getTextHandler().debugRender();
		}
	}
}
