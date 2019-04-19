package com.pmnm.risk.ui.gameui;

public enum Season {

	WINTER("winter"), SPRING("spring"), SUMMER("summer"), FALL("fall");

	private String lower;

	Season(String s) {
		lower = s;
	}

	@Override
	public String toString() {
		return lower;
	}
}
