package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.geom.Ellipse2D;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaAnimation;
import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.UIInit;

public class Water extends DoaObject {

	//serial Version UID needed
	int texW, texH;
	
	Water(){
		//x, y at 0, zOrder -1
		super(0f, 0f, -2);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		switch (Season.getCurrentSeason()) {
		case WINTER:
			DoaSprite winter = DoaSprites.get("winterTex");
			texW = DoaSprites.get("winterTex").getWidth() / 6;
			texH = DoaSprites.get("winterTex").getWidth() / 6;
			for (int i = -50; i < Main.WINDOW_WIDTH + 50; i += texW) {
				for(int j = -50; j < Main.WINDOW_HEIGHT + 50; j += texH) {
					g.drawImage(winter, i, j, texW, texH);
				}	
			}
			break;
		case SPRING:
			DoaSprite spring = DoaSprites.get("springTex");
			texW = DoaSprites.get("springTex").getWidth() / 6;
			texH = DoaSprites.get("springTex").getWidth() / 6;
			for (int i = -50; i < Main.WINDOW_WIDTH + 50; i += texW) {
				for(int j = -50; j < Main.WINDOW_HEIGHT + 50; j += texH) {
					g.drawImage(spring, i, j, texW, texH);
				}	
			}
			break;
		case SUMMER:
			DoaSprite summer = DoaSprites.get("summerTex");
			texW = DoaSprites.get("summerTex").getWidth() / 6;
			texH = DoaSprites.get("summerTex").getWidth() / 6;
			for (int i = -50; i < Main.WINDOW_WIDTH + 50; i += texW) {
				for(int j = -50; j < Main.WINDOW_HEIGHT + 50; j += texH) {
					g.drawImage(summer, i, j, texW, texH);
				}	
			}
			break;
		case FALL:
			DoaSprite fall = DoaSprites.get("fallTex");
			texW = DoaSprites.get("fallTex").getWidth() / 6;
			texH = DoaSprites.get("fallTex").getWidth() / 6;
			for (int i = -50; i < Main.WINDOW_WIDTH + 50; i += texW) {
				for(int j = -50; j < Main.WINDOW_HEIGHT + 50; j += texH) {
					g.drawImage(fall, i, j, texW, texH);
				}	
			}
			break;
	}
		
		
	}
}
