package com.pmnm.risk.ui.gameui;

import java.awt.Color;
import java.util.Calendar;

import com.pmnm.risk.main.GameManager;

public enum Season {

	WINTER("winter", Color.CYAN), SPRING("spring", Color.GREEN), SUMMER("summer", Color.YELLOW), FALL("fall", new Color(205, 133, 63));

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
	private static Season currentSeason = SUMMER;// seasons[Calendar.getInstance().get(Calendar.MONTH)];

	public static void updateSeason() {
		currentSeason = seasons[(Calendar.MONTH + GameManager.turnCount / 4) % 12];
	}

	public static Season getCurrentSeason() {
		return currentSeason;
	}
}
