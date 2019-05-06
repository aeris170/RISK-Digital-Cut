package com.pmnm.risk.ui;

import java.util.ArrayList;
import java.util.List;

import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.globals.PlayerColorBank;
import com.pmnm.risk.toolkit.Utils;

public class ColorComboButton extends DoaImageButton {

	private static final DoaSprite[] OPTIONS = new DoaSprite[PlayerColorBank.size()];

	static {
		for (int i = 0; i < PlayerColorBank.size(); i++) {
			DoaSprite w = DoaSprites.deepCopyDoaSprite(DoaSprites.get("White"));
			Utils.paintImage(w, PlayerColorBank.get(i));
			OPTIONS[i] = w;
		}
	}

	private static final List<TypeComboButton> COMBO_BUTTONS = new ArrayList<>();

	int index = 0;

	public ColorComboButton() {
		super(0f, 0f, 0, 0, null);
	}

}
