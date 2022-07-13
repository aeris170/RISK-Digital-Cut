package com.pmnm.roy;

import lombok.NonNull;

public interface IRoyContainer extends IRoyElement {

	Iterable<IRoyElement> getElements();
	void addElement(@NonNull IRoyElement element);
	boolean removeElement(@NonNull IRoyElement element);

}
