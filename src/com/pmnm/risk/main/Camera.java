package com.pmnm.risk.main;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaHandler;
import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.input.DoaMouse;
import com.doa.engine.task.DoaTaskGuard;
import com.doa.engine.task.DoaTasker;
import com.doa.maths.DoaMath;
import com.doa.maths.DoaVectorF;
import com.pmnm.risk.toolkit.Utils;

public class Camera extends DoaObject {

	private static final long serialVersionUID = 1481819429670061069L;

	private static final float KEY_LOOK_SPEED = 4f;
	private static final float MOUSE_LOOK_SPEED = 2f;
	private static final float LOW_PERCENTAGE_FOR_MOUSE_CAMERA = 5;
	private static final float HIGH_PERCENTAGE_FOR_MOUSE_CAMERA = 95;

	private static Camera _this = null;
	static int value = 0;
	
	
	private DoaVectorF topLeftBound;
	private DoaVectorF bottomRightBound;
	private PrintWriter writer;
	private boolean isLoggingVertices = false;
	private DoaTaskGuard vertexLogKeyGuard = new DoaTaskGuard(true);

	private int createCount = 1;

	private Camera(float x, float y) {
		super(x, y, 1000);
		topLeftBound = position.clone();
		bottomRightBound = position.clone().add(new DoaVectorF(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
		setFixed(true);
	}

	public void creator() {
		try {
			writer = new PrintWriter("newHopes.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
	}

	public static Camera getInstance() {
		return _this == null ? _this = DoaHandler.instantiate(Camera.class, Main.WINDOW_WIDTH / 2f, Main.WINDOW_HEIGHT / 2f) : _this;
	}

	@Override
	public void tick() {
		if (createCount == 1) {
			creator();
			createCount--;
		}
		if (DoaKeyboard.W || DoaKeyboard.KEY_UP) {
			position.y -= KEY_LOOK_SPEED;
		}
		if (DoaKeyboard.A || DoaKeyboard.KEY_LEFT) {
			position.x -= KEY_LOOK_SPEED;
		}
		if (DoaKeyboard.S || DoaKeyboard.KEY_DOWN) {
			position.y += KEY_LOOK_SPEED;
		}
		if (DoaKeyboard.D || DoaKeyboard.KEY_RIGHT) {
			position.x += KEY_LOOK_SPEED;
		}
		if (DoaMouse.Y < Main.WINDOW_HEIGHT * LOW_PERCENTAGE_FOR_MOUSE_CAMERA / 100) {
			position.y -= MOUSE_LOOK_SPEED;
		}
		if (DoaMouse.X < Main.WINDOW_WIDTH * LOW_PERCENTAGE_FOR_MOUSE_CAMERA / 100) {
			position.x -= MOUSE_LOOK_SPEED;
		}
		if (DoaMouse.Y > Main.WINDOW_HEIGHT * HIGH_PERCENTAGE_FOR_MOUSE_CAMERA / 100) {
			position.y += MOUSE_LOOK_SPEED;
		}
		if (DoaMouse.X > Main.WINDOW_WIDTH * HIGH_PERCENTAGE_FOR_MOUSE_CAMERA / 100) {
			position.x += MOUSE_LOOK_SPEED;
		}
		topLeftBound = new DoaVectorF(Main.WINDOW_WIDTH / 2f + (Main.WINDOW_WIDTH / 2f - (Main.WINDOW_WIDTH / 2f * DoaCamera.getZ())) / DoaCamera.getZ(),
		        Main.WINDOW_HEIGHT / 2f + (Main.WINDOW_HEIGHT / 2f - (Main.WINDOW_HEIGHT / 2f * DoaCamera.getZ())) / DoaCamera.getZ());
		bottomRightBound = new DoaVectorF(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT).sub(topLeftBound);

		position.x = DoaMath.clamp(position.x, topLeftBound.x, bottomRightBound.x);
		position.y = DoaMath.clamp(position.y, topLeftBound.y, bottomRightBound.y);

		if (vertexLogKeyGuard.get() && DoaKeyboard.V) {
			vertexLogKeyGuard.set(false);
			DoaTasker.guard(vertexLogKeyGuard, 1000);
			if (!isLoggingVertices) {
				isLoggingVertices = true;
				writer.println("LOG::START " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
			} else {
				isLoggingVertices = false;
				writer.println("LOG::END " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
				writer.flush();
			}
		}
		
		if (vertexLogKeyGuard.get() && DoaKeyboard.L) {
			vertexLogKeyGuard.set(false);
			DoaTasker.guard(vertexLogKeyGuard, 1000);
			System.out.println("L is pressed");
			GameManager.loadGame("firstSave");
		}
		
		
		if (isLoggingVertices) {
			if (DoaMouse.MB1) {
				writeVertices();
			}
		}
	}

	public void writeVertices() {
		// <vertex><x>45</x><y>199</y></vertex>
		int x = Math.round(Utils.mapMouseCoordinatesByZoom().x);
		int y = Math.round(Utils.mapMouseCoordinatesByZoom().y);
		writer.println("<vertex><x>" + x + "</x><y>" + y + "</y></vertex>");
		System.out.println("<vertex><x>" + x + "</x><y>" + y + "</y></vertex>");
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("DCam Pos: " + DoaCamera.getX() + " " + DoaCamera.getY(), 0, 20);
		g.drawString("Cam Pos: " + position.toString(), 0, 40);
		g.drawString("Cam Top Left Bound: " + topLeftBound.toString(), 0, 60);
		g.drawString("Cam Bottom Right Bound: " + bottomRightBound.toString(), 0, 80);
		g.drawString("Absolute Mouse Pos: " + new DoaVectorF((float) DoaMouse.X, (float) DoaMouse.Y).toString(), 0, 100);
		g.drawString("Mapped Mouse Pos: " + Utils.mapMouseCoordinatesByZoom().toString(), 0, 120);
		if (isLoggingVertices) {
			if(value == 0) { 
		     	saveGameFromCamera();
		     	value++;
				}
			g.setColor(Color.RED);
			g.fillRect(0, 160, 290, 23);
			g.setColor(Color.BLACK);
			g.drawString("VERTEX LOGGING ENABLED!", 0, 180);
		}
		g.setColor(Color.WHITE);
		g.drawString("Phase: " + GameManager.currentPhase, 0, 200);
		if (GameManager.currentPlayer != null) {
			g.setColor(GameManager.currentPlayer.getColor());
			g.fillRect(0, 200, 130, 23);
			g.setColor(Color.WHITE);
			g.drawString("Turn: " + GameManager.currentPlayer.getName(), 0, 220);
		}
	}
	
	
	public void  saveGameFromCamera() {
		try {
			GameManager.saveGame("Ege");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GameManager.tester();
		
	}
	
}
