package com.pmnm.risk.globals;

import java.awt.Color;

public class PlayerColorBank {

	private static final Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK, Color.MAGENTA };

	public static int size() {
		return colors.length;
	}

	public static Color get(int index) {
		return colors[index % colors.length];
	}
}
