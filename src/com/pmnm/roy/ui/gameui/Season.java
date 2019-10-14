package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.util.Calendar;

public enum Season {

	WINTER("winter", new Color(218, 255, 255)), SPRING("spring", new Color(123, 215, 109)), SUMMER("summer", new Color(211, 242, 150)), FALL("fall", new Color(229, 178, 76));

	private String lower;
	private Color color;

	Season(String s, Color c) {
		lower = s;
		color = c;
	}

	@Override
	public String toString() {
		return lower;
	}

	public Color getSeasonColor() {
		return color;
	}

	private static final Season[] seasons = { WINTER, WINTER, SPRING, SPRING, SPRING, SUMMER, SUMMER, SUMMER, FALL, FALL, FALL, WINTER };
	public static Season currentSeason = seasons[Calendar.getInstance().get(Calendar.MONTH)];

	public static void updateSeason() {
		// currentSeason = FALL;
		// seasons[(Calendar.MONTH + GameManager.INSTANCE.turnCount) % 12];
	}

	public static Season getCurrentSeason() {
		return currentSeason;
	}
}
