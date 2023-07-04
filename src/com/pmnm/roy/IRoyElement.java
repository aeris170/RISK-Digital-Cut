package com.pmnm.roy;

import java.awt.Shape;

import doa.engine.maths.DoaVector;

public interface IRoyElement {

	boolean isVisible();
	void setVisible(boolean value);
	
	void setPosition(DoaVector position);
	Shape getContentArea();
	default Shape getInteractionArea() { return getContentArea(); }
}
