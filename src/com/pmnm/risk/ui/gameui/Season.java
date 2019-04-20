package com.pmnm.risk.ui.gameui;

import java.util.Calendar;

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

	private static final Season seasons[] = { WINTER, WINTER, SPRING, SPRING, SPRING, SUMMER, SUMMER, SUMMER, FALL, FALL, FALL, WINTER };

	public static Season getSeason() {
		return seasons[Calendar.getInstance().get(Calendar.MONTH)];
	}
}
