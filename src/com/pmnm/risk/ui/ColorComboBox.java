package com.pmnm.risk.ui;

import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.toolkit.Utils;

public class ColorComboBox {

	private static final DoaSprite[] OPTIONS = new DoaSprite[PlayerColorBank.size()];

	static {
		for (int i = 0; i < PlayerColorBank.size(); i++) {
			DoaSprite w = DoaSprites.deepCopyDoaSprite(DoaSprites.get("White"));
			Utils.paintImage(w, PlayerColorBank.get(i));
			OPTIONS[i] = w;
		}
	}

	public ColorComboBox() {}

}
