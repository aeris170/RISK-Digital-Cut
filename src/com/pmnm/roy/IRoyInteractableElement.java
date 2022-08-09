package com.pmnm.roy;

public interface IRoyInteractableElement extends IRoyElement {

	void setAction(IRoyAction action);
	boolean isEnabled();
	void setEnabled(boolean value);
}
