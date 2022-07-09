package com.pmnm.risk.main;
 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.map.board.ProvinceConnector; 
import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.network.Client;
import com.pmnm.risk.network.message.MessageBuilder; 
import com.pmnm.risk.network.message.MessageType; 
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.map.board.ProvinceHitArea; 
 
public final class GameInstance implements Serializable { 
	 
	private static final long serialVersionUID = 3374349513952165496L;
	 
	public static GameInstance from(IRiskGameContext context) {
		GameInstance instance = new GameInstance(context);
		return instance;
	}
	 
	private IRiskGameContext context; 
	private Date timestamp;
	private String version;
	 
	private GameInstance(IRiskGameContext context) { 
		this.context = context;
		timestamp = Date.from(Instant.now());
		version = Globals.GAME_VERSION;
	} 
	 
	public void saveToDisk() { 
		String mapName = GameManager.INSTANCE.currentMapName;
		File dir = Path.of(System.getProperty("user.home"), "Documents", "My Games", "Risk Digital Cut", "Saves", mapName).toFile();
		dir.mkdirs();
		try (
			FileOutputStream file = new FileOutputStream(dir + "save_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + mapName + ".sav"); 
			ObjectOutputStream out = new ObjectOutputStream(file)) {
			out.writeObject(this);
			System.out.println(context + " serialized!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	 
	 public static GameInstance loadGame() throws IOException, ClassNotFoundException {  
		 // TODO take input from UI String mapName =
		 //GameManager.INSTANCE.currentMapName;

		File dir = Path.of(System.getProperty("user.home"), "Documents", "My Games", "Risk Digital Cut", "Saves", mapName, ).toFile();
		 String dir = System.getProperty("user.home") + "\\Documents\\My Games\\RiskDigitalCut\\Saves\\" + mapName + "\\save_20190719_105218_CLASSIC.sav"; 
	     try (
    		 FileInputStream file = new FileInputStream(dir);
    		 ObjectInputStream in = new ObjectInputStream(file)) {
	    	 GameInstance loadedGame = (GameInstance) in.readObject();
	    	 System.out.println("deserialized!");
	    	 return loadedGame;
    	 }
     } 
	 
	 public static void deserializeGame(GameInstance gi) { 
		 ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.forEach(pha ->
	 DoaHandler.remove(pha)); ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.clear();
	 ProvinceHitArea.ALL_PROVINCE_SYMBOLS.forEach(ps -> DoaHandler.remove(ps));
	 ProvinceHitArea.ALL_PROVINCE_SYMBOLS.clear(); Player.NAME_PLAYER.values().forEach(p ->
	 DoaHandler.remove(p)); Player.NAME_PLAYER.clear(); DoaHandler.remove(GameManager.INSTANCE);
	 GameInstance loadedGame = gi; GameManager.INSTANCE = loadedGame.gm;
	 DoaHandler.add(GameManager.INSTANCE); Province.ALL_PROVINCES = loadedGame.provinces;
	 Continent.NAME_CONTINENT = loadedGame.continents; Player.NAME_PLAYER = loadedGame.NAME_PLAYER;
	 Province.UNCLAIMED_PROVINCES = loadedGame.UNCLAIMED_PROVINCES;
	 ProvinceHitArea.ALL_PROVINCE_HIT_AREAS = loadedGame.ALL_PROVINCE_HIT_AREAS;
	 ProvinceHitArea.ALL_PROVINCE_HIT_AREAS.forEach(pha -> { pha.cacheMeshAsImage();
	 DoaHandler.add(pha); }); ProvinceHitArea.ALL_PROVINCE_SYMBOLS = loadedGame.ALL_PROVINCE_SYMBOLS;
	 ProvinceHitArea.ALL_PROVINCE_SYMBOLS.forEach(ps -> DoaHandler.add(ps));
	 ProvinceConnector.deserialize(loadedGame.pc); Player.NAME_PLAYER.values().forEach(p ->
 DoaHandler.add(p)); } public static void loadLastStateAndCompare() throws FileNotFoundException,
 IOException, ClassNotFoundException { String mapName = GameManager.INSTANCE.currentMapName;
 String dir = System.getProperty("user.home") +
 "\\Documents\\My Games\\RiskDigitalCut\\Saves\\" + mapName + "\\.__last.sav"; try
 (FileInputStream file = new FileInputStream(dir)) { try (ObjectInputStream in = new
 ObjectInputStream(file)) { GameInstance loadedGame = (GameInstance) in.readObject(); currentState
 = new GameInstance(); if (currentState.equals(loadedGame)) {
 Client.getInstance().sendToServer(new
 MessageBuilder().setSender("Client").setData(loadedGame).setType(MessageType.COMPRESSED).build())
 ; currentState = loadedGame; } } } } public static void saveCurrentState() { new Thread(() -> {//
 save Task GameInstance gi = new GameInstance(); currentState = gi; String mapName =
 GameManager.INSTANCE.currentMapName; String dir = System.getProperty("user.home") +
 "\\Documents\\My Games\\RiskDigitalCut\\Saves\\" + mapName + "\\"; File f = new File(dir);
 f.mkdirs(); try (FileOutputStream file = new FileOutputStream(dir + ".__last.sav")) { try
 (ObjectOutputStream out = new ObjectOutputStream(file)) { out.writeObject(gi);
 System.out.println("Object has been serialized"); } } catch (IOException ex) {
 ex.printStackTrace(); } }).start(); } } 