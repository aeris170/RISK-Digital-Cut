package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.UIUtils;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class RoyText implements IRoyElement, IRoyTextHandler {

	@Getter
	private String name;

	@Getter
	@Setter
	private boolean isVisible = true;

	@Getter
	@Setter
	private boolean isEnabled = true;

	@Getter
	@Setter
	private Shape parentArea;

	@Getter
	@Setter
	private Rectangle textArea;

	@Getter
	private Font font;

	@Getter
	private String text;

	@Getter
	@Setter
	private Color textColor;

	@Getter
	@Setter
	private Color textHoverColor;

	@Getter
	@Setter
	private Color textDisabledColor;

	@Getter
	@Setter
	private boolean centered;

	@Getter
	@Setter
	private boolean monoColor;

	private Script script;
	private Renderer renderer;

	@Builder
	public RoyText(@NonNull Shape parentArea, @NonNull Rectangle textArea, Font font,
			String text, Color textColor, Color textHoverColor,
			Color textDisabledColor, boolean centered, boolean monoColor) {
		this.parentArea = parentArea;
		this.textArea = textArea;
		this.font = Objects.requireNonNullElse(font, UIConstants.getFont());
		this.text = Objects.requireNonNullElse(text, "");
		this.textColor = Objects.requireNonNullElse(textColor, UIConstants.getTextColor());
		this.textHoverColor = Objects.requireNonNullElse(textHoverColor, UIConstants.getHoverTextColor());
		this.textDisabledColor = Objects.requireNonNullElse(textDisabledColor, Color.GRAY);
		this.centered = centered;
		this.monoColor = monoColor;

		name = "RoyText: " + this.text;

		script = new Script();
		renderer = new Renderer();
	}

	public void setText(@NonNull String text) {
		this.text = text;
		renderer.recalculateFont();
	}

	@Override
	public Shape getContentArea() { return parentArea; }

	@Override
	public void tick() { script.tick(); }
	@Override
	public void render() { renderer.render(); }
	@Override
	public void debugRender() { renderer.debugRender(); }

	private final class Script {
		private void tick() {
			if (!isVisible) { return; }
			if (monoColor) { return; }

			Shape area = getContentArea();
			int mouseX = (int) DoaGraphicsFunctions.unwarpX(DoaMouse.X);
			int mouseY = (int) DoaGraphicsFunctions.unwarpY(DoaMouse.Y);
			if (area.contains(new Point(mouseX, mouseY))) {
				renderer.textColor = textHoverColor;
			} else {
				renderer.textColor = textColor;
			}

			if (!RoyText.this.isEnabled) { renderer.textColor = textDisabledColor; }
		}
	}

	private final class Renderer {

		private Color textColor = RoyText.this.textColor;

		private boolean shouldRecalculateFont = false;

		private Font font;

		private float textWidth;
		private float textHeight;
		private Rectangle2D.Float preciseBounds;

		private void render() {
			if (!isVisible) { return; }
			DoaGraphicsFunctions.pushAll();
			DoaGraphicsFunctions.resetAll();
			shouldRecalculateFont = true;
			if (shouldRecalculateFont) {
				shouldRecalculateFont = false;

				DoaVector contentSize = new DoaVector(textArea.width, textArea.height);
				font = UIUtils.adjustFontToFitInArea(text, contentSize);

				textWidth = DoaGraphicsFunctions.unwarpX(UIUtils.textWidth(font, text));
				textHeight = DoaGraphicsFunctions.unwarpY(UIUtils.textHeight(font));

				preciseBounds = UIUtils.preciseTextBounds(font, text);
				preciseBounds.x = DoaGraphicsFunctions.unwarpX(preciseBounds.x);
				preciseBounds.y = DoaGraphicsFunctions.unwarpY(preciseBounds.y);
				preciseBounds.width = DoaGraphicsFunctions.unwarpX(preciseBounds.width);
				preciseBounds.height = DoaGraphicsFunctions.unwarpY(preciseBounds.height);
			}

			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(textColor);

			if (centered) {
				DoaGraphicsFunctions.drawString(
					text,
					parentArea.getBounds().x + parentArea.getBounds().width / 2f  - textWidth / 2f,
					textArea.y - preciseBounds.y + (parentArea.getBounds().height - preciseBounds.height) / 2f
				);
			} else {
				DoaGraphicsFunctions.drawString(
					text,
					textArea.x,
					textArea.y - preciseBounds.y + (parentArea.getBounds().height - preciseBounds.height) / 2f
				);
			}

			DoaGraphicsFunctions.popAll();
		}

		private void debugRender() {
			if (!isVisible) { return; }
			DoaGraphicsFunctions.pushAll();
			DoaGraphicsFunctions.resetAll();

			DoaGraphicsFunctions.setColor(Color.GREEN);
			if (centered) {
				DoaGraphicsFunctions.drawRect(
					parentArea.getBounds().x + (parentArea.getBounds().width - textWidth) / 2f,
					parentArea.getBounds().y + (parentArea.getBounds().height - textHeight) / 2f,
					textWidth,
					textHeight
				);
			} else {
				DoaGraphicsFunctions.drawRect(
					textArea.x,
					textArea.y - (textHeight - parentArea.getBounds().height) / 2f,
					textArea.width,
					textArea.height
				);
			}

			DoaGraphicsFunctions.setColor(Color.CYAN);
			if (centered) {
				DoaGraphicsFunctions.drawRect(
					parentArea.getBounds().x + (parentArea.getBounds().width - textWidth) / 2f,
					parentArea.getBounds().y - preciseBounds.height + (parentArea.getBounds().height + preciseBounds.height) / 2f,
					preciseBounds.width,
					preciseBounds.height
				);
			} else {
				DoaGraphicsFunctions.drawRect(
					textArea.x,
					textArea.y - preciseBounds.height + (parentArea.getBounds().height + preciseBounds.height) / 2f,
					preciseBounds.width,
					preciseBounds.height
				);
			}

			DoaGraphicsFunctions.popAll();
		}

		private void recalculateFont() {
			shouldRecalculateFont = true;
		}
	}

	@Override
	public void setPosition(DoaVector position) {
		throw new UnsupportedOperationException("Position is ill defined for RoyText's. Use parentArea and textArea for fine tuned controls!");
	}
}
