package com.pmnm.risk.globals;

import java.awt.Color;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PlayerColorBank {

	public static final Color[] COLORS = new Color[] {
		Color.RED,
		Color.BLUE,
		new Color(0x008080),
		new Color(0x800080),
		new Color(0xFFFF00),
		new Color(0xFF9200)
	};
}
