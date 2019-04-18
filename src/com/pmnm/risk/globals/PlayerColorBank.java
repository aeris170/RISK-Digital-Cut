package com.pmnm.risk.globals;

import java.awt.Color;

public class PlayerColorBank {

	private static final Color[] colors = new Color[] { Color.RED, Color.BLUE, new Color(0x008080), new Color(0x800080), new Color(0xFFFF00), new Color(0xA52A2A) };

	public static int size() {
		return colors.length;
	}

	public static Color get(int index) {
		return colors[index % colors.length];
	}
}
