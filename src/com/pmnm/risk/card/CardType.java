package com.pmnm.risk.card;

public enum CardType {

	INFANTRY, CAVALRY, ARTILLERY;
	
	public String toString(){
		return name().toLowerCase();
	}
}
