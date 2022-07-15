package com.pmnm.util;

public interface Observable {

	void registerObserver(Observer o);
	void notifyObservers();
}
