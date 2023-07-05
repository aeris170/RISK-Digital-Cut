package com.pmnm.roy.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.pmnm.risk.toolkit.Utils;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UIUtils {

	public static final int textWidth(Font font, String string) {
		return DoaGraphicsFunctions.getFontMetrics(font).stringWidth(string);
	}
	public static final int textHeight(Font font) {
		return DoaGraphicsFunctions.getFontMetrics(font).getHeight();
	}
	public static final DoaVector textArea(Font font, String string) {
		return new DoaVector(textWidth(font, string), textHeight(font));
	}
	
	public static final Font adjustFontToFitInArea(String text, float width, float height) {
		return adjustFontToFitInArea(text, new DoaVector(width, height));
	}
	
	public static final Font adjustFontToFitInArea(String text, Rectangle area) {
		return adjustFontToFitInArea(text, new DoaVector(area.width, area.height));
	}
	public static final Font adjustFontToFitInArea(String text, DoaVector area) {
		return UIConstants.getFont().deriveFont(
			Font.PLAIN,
			DoaGraphicsFunctions.warpX(Utils.findMaxFontSizeToFitInArea(UIConstants.getFont(), area, text))
		);
	}
	
	public static final String limitString(Font font, String string, float width) {
		return limitString(font, string, width, "...");
	}
	public static final String limitString(Font font, String string, float width, String limiter) {
		width = DoaGraphicsFunctions.warpX(width);
		if (textWidth(font, string) < width) { return string; }
		
		int limiterWidth = textWidth(font, limiter);
		int len = string.length();
		String sub;
		do {
			len--;
			sub = string.substring(0, len);
		} while(textWidth(font, sub) + limiterWidth > width);
		return sub + limiter;
	}
	
	public static final String[] wrapString(Font font, String string, float width) {
		List<String> strings = new ArrayList<>();
		
		String[] words = string.split(" ");
		int begin = 0;
		int end = words.length - 1;
		
		while(begin != end) {
			String currentStr = joinStrings(words, begin, end);
			while(textWidth(font, currentStr) > width) {
				end--;
			}
			strings.add(currentStr);
			begin = end;
			end = words.length - 1;
		}
		
		return strings.toArray(String[]::new);
	}
	
	public static final String joinStrings(String[] words, int begin, int end) {
		StringBuilder str = new StringBuilder(words[begin]);
		for(int i = begin + 1; i < end; i++) {
			str.append(" ");
			str.append(words[i]);
		}
		return str.toString();
	}
}
