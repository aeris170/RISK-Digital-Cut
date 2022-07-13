package com.pmnm.roy;

import java.awt.Rectangle;

import doa.engine.maths.DoaVector;

public interface IRoyElement {

	boolean isVisible();
	void setVisible(boolean value);
	
	void setPosition(DoaVector position);
	Rectangle getContentArea();
}
