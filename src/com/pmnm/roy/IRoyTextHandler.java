package com.pmnm.roy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;

import lombok.NonNull;

public interface IRoyTextHandler {

	public @NonNull Shape getParentArea();
	public void setParentArea(@NonNull Shape parentArea);

	public @NonNull Rectangle getTextArea();
	public void setTextArea(@NonNull Rectangle textArea);

	public @NonNull Font getFont();

	public @NonNull String getText();
	public void setText(@NonNull String text);

	public @NonNull Color getTextColor();
	public void setTextColor(@NonNull Color textColor);

	public @NonNull Color getTextHoverColor();
	public void setTextHoverColor(@NonNull Color textHoverColor);

	public @NonNull Color getTextDisabledColor();
	public void setTextDisabledColor(@NonNull Color textDisabledColor);

	public boolean isCentered();
	public void setCentered(boolean centered);

	public boolean isMonoColor();
	public void setMonoColor(boolean monoColor);

	public void tick();
	public void render();
	public void debugRender();
}
