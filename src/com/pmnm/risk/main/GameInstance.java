package com.pmnm.risk.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pmnm.risk.map.continent.Continent;
import com.pmnm.risk.map.province.Province;

//Java code for serialization and deserialization  
//of a Java object 

public class GameInstance implements Serializable {

	private List<Player> players = new ArrayList<>();
	private int numberOfPlayers;
	private boolean manualPlacement;
	private  boolean isManualPlacementDone;
	private  int placementCounter;
	private  TurnPhase currentPhase;
	private  int reinforcementForThisTurn;
	private  Player currentPlayer;
	private  int turnCount;
	private  Province draftReinforceProvince;
	private String currentDate;
	private String saveName;
	public Map<String, Continent> NAME_CONTINENT = new LinkedHashMap<>();
	public List<Province> ALL_PROVINCES = new ArrayList<>();
	public List<Province> UNCLAIMED_PROVINCES = new ArrayList<>();
	private Set<Province> provinces;

	
	
// Default constructor 
	public GameInstance(String currentDate, String saveName) {
		this.currentDate = currentDate;
		this.saveName = saveName;
	}
	
	
//later save complete
	public boolean saveNow(GameInstance NewSave) throws IOException {
		GameInstance object2 = NewSave;
		String filename = NewSave.saveName + ".ser";

		if (nameControl(object2.getSaveName())) {

			System.out.println("Save is unsuccessfull, name already exist");

			return false;

		} else {
			// Save savegame name
			saveGameNames(NewSave.saveName);

			System.out.println("We controlled the save game names");
			// reached

			// Serialization
			try {

				// Saving of object in a file
				FileOutputStream file = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(file);

				// Method for serialization of object
				out.writeObject(NewSave);

				out.close();
				file.close();

				System.out.println("Object has been serialized or Game is Saved");

			}

			catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("IOException is caught");
			}

		} // save operation has been made

		return true;

	}

//later save complete
	public GameInstance loadNow(String loadName) {

		GameInstance object1 = null;
		String filename = loadName + ".ser";

		// Deserialization
		try {
			// Reading the object from a file
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			object1 = (GameInstance) in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized ");

		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}

		return object1;

	}
	
	
	
	 private void readObject(ObjectInputStream inputStream)
	            throws IOException, ClassNotFoundException
	    {
	        inputStream.defaultReadObject();
	        
	    }    

	public void saveGameNames(String saveName) throws IOException {

		PrintWriter writer = new PrintWriter("saveGameNames.txt");
		writer.println(saveName);
		writer.close();

	}

	public boolean nameControl(String saveName) throws IOException {

		File file = new File("saveGameNames.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String names;

		boolean nameExist = false;

		while ((names = br.readLine()) != null) {
			if (names.equals(saveName)) {
				nameExist = true;
			}
		}

		System.out.println("We controlled the save names");

		return nameExist;

	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public boolean isManualPlacement() {
		return manualPlacement;
	}

	public void setManualPlacement(boolean manualPlacement) {
		this.manualPlacement = manualPlacement;
	}

	public boolean isManualPlacementDone() {
		return isManualPlacementDone;
	}

	public void setManualPlacementDone(boolean isManualPlacementDone) {
		this.isManualPlacementDone = isManualPlacementDone;
	}

	public int getPlacementCounter() {
		return placementCounter;
	}

	public void setPlacementCounter(int placementCounter) {
		this.placementCounter = placementCounter;
	}

	public TurnPhase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(TurnPhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public int getReinforcementForThisTurn() {
		return reinforcementForThisTurn;
	}

	public void setReinforcementForThisTurn(int reinforcementForThisTurn) {
		this.reinforcementForThisTurn = reinforcementForThisTurn;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}


	public Province getDraftReinforceProvince() {
		return draftReinforceProvince;
	}

	public void setDraftReinforceProvince(Province draftReinforceProvince) {
		this.draftReinforceProvince = draftReinforceProvince;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Map<String, Continent> getNAME_CONTINENT() {
		return NAME_CONTINENT;
	}

	public void setNAME_CONTINENT(Map<String, Continent> nAME_CONTINENT) {
		NAME_CONTINENT = nAME_CONTINENT;
	}

	

	public List<Province> getALL_PROVINCES() {
		return ALL_PROVINCES;
	}

	public void setALL_PROVINCES(List<Province> aLL_PROVINCES) {
		ALL_PROVINCES = aLL_PROVINCES;
	}

	public List<Province> getUNCLAIMED_PROVINCES() {
		return UNCLAIMED_PROVINCES;
	}

	public void setUNCLAIMED_PROVINCES(List<Province> uNCLAIMED_PROVINCES) {
		UNCLAIMED_PROVINCES = uNCLAIMED_PROVINCES;
	}

	public Set<Province> getProvinces() {
		return provinces;
	}

	public void setProvinces(Set<Province> provinces) {
		this.provinces = provinces;
	}


	
		
}