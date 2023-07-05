package com.pmnm.risk.main;

import java.awt.Color;
import java.awt.Font;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.utils.hardwareinfo.DoaCPUInfo;
import doa.engine.utils.hardwareinfo.DoaDisplay;
import doa.engine.utils.hardwareinfo.DoaEngineInfo;
import doa.engine.utils.hardwareinfo.DoaGPU;
import doa.engine.utils.hardwareinfo.DoaMemoryInfo;
import doa.engine.utils.hardwareinfo.DoaOSInfo;
import doa.engine.utils.hardwareinfo.DoaRuntimeInfo;

@SuppressWarnings("serial")
public final class SystemSpecs extends DoaObject {

	public static boolean isVisible = false;

	private DoaRuntimeInfo runtime;
	private DoaOSInfo os;
	private DoaCPUInfo cpu;
	private DoaMemoryInfo memory;
	private DoaGPU gpu;
	private DoaDisplay display;

	public SystemSpecs() {
		if (!isVisible) { return; }

		DoaEngineInfo.initialize();
		runtime = DoaEngineInfo.getRuntimeInfo();
		display = DoaEngineInfo.getDisplayInfo().getDisplayDevices().get(0);
		gpu = DoaEngineInfo.getGpuInfo().getGraphicsCards().get(0);
		memory = DoaEngineInfo.getMemoryInfo();
		os = DoaEngineInfo.getOsInfo();
		cpu = DoaEngineInfo.getCpuInfo();
		setzOrder(Integer.MAX_VALUE);
		addComponent(new Renderer());
	}

	private class Renderer extends DoaRenderer {

		private int fontSize = 24;
		private Font font = new Font("Arial", Font.BOLD, fontSize);
		private int outlineThickness = 1;

		@Override
		public void render() {
			if (!isVisible) { return; }

			DoaGraphicsFunctions.setFont(font);
			DoaGraphicsFunctions.setColor(Color.green);

			renderText("DoaEngine Version: " + runtime.getVersion());
			renderText("FPS: " + runtime.getFPS());
			renderText("TICKS: " + runtime.getTPS());
			renderText("----");
			renderText("Operating System: " + os.getName());
			renderText("----");
			renderText("CPU: " + cpu.getModelName());
			renderText("C°: " + cpu.getTemperature());
			renderText("Clock Frequency: " + cpu.getMhz() + "MHz");
			renderText("RAM: " + memory.getTotalMemory());
			renderText("----");
			renderText("GPU: " + gpu.getName());
			renderText("C°: " + gpu.getTemperature());
			renderText("RESOLUTION: " + display.getCurrentResolution());
			renderText("REFRESH RATE: " + display.getRefreshRate());

			resetCounter();
		}

		private int counter;
		private void renderText(String text) {
			int y = fontSize * ++counter;

			DoaGraphicsFunctions.setColor(Color.black);
			for(int i = 1; i <= outlineThickness; i++) {
				DoaGraphicsFunctions.drawString(text, -i, y);
				DoaGraphicsFunctions.drawString(text, i, y);
				DoaGraphicsFunctions.drawString(text, 0, y - i);
				DoaGraphicsFunctions.drawString(text, 0, y + i);
			}

			DoaGraphicsFunctions.setColor(Color.green);
			DoaGraphicsFunctions.drawString(text, 0, y);
		}
		private void resetCounter() { counter = 0; }
	}
}
