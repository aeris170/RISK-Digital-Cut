package com.pmnm.risk.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.pmnm.risk.dice.Dice;
import com.pmnm.risk.map.province.Province;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class GameManager extends DoaObject {

	private static final long serialVersionUID = -4928417050440420799L;

	public static Player[] players;
	public static int numberOfPlayers = 2;
	
	public static boolean isBeginning = true;
	public static Player[] turns;
	public static TurnPhase currentPhase;
	public static Player currentPlayer;
	public static int turnCount = 0;

	public GameManager() {
		super(0f, 0f);
	}

	public static void nextPhase() {
		if (currentPhase == TurnPhase.DRAFT) {
			currentPhase = TurnPhase.ATTACK;
		} else if (currentPhase == TurnPhase.ATTACK) {
			currentPhase = TurnPhase.REINFORCE;
		} else if (currentPhase == TurnPhase.REINFORCE) {
			currentPhase = TurnPhase.DRAFT;
		}
	}

	@Override
	public void tick() {
		if (isBeginning) {
			/*System.out.println("Game started!");
			players = new Player[numberOfPlayers];
			for(int i = 0; i < numberOfPlayers; i++) {
				players[i] = new Player("Player" + (i + 1), Color.BLACK);
			}
			turns = new Player[numberOfPlayers];
			Dice beginningDice = Dice.randomlyGenerate(numberOfPlayers);
			int[] beginninggDiceValues = beginningDice.getAllValues();
			boolean[] valuesFinalized = new boolean[numberOfPlayers];
			System.out.println(Arrays.toString(beginninggDiceValues));
			System.out.println(Arrays.toString(valuesFinalized));
			
			int turnsDetermined = 0;
			int max = 0;
			int numberOfMax = 0;
			ArrayList<Integer> indicesOfMax = new ArrayList<>();
			while(turnsDetermined < numberOfPlayers) {
				for(int i = 0; i < numberOfPlayers; i++) {
					if(beginninggDiceValues[i] > max && valuesFinalized[i] == false) {
						max = beginninggDiceValues[i];
					}
				}
				for(int i = 0; i < numberOfPlayers; i++) {
					if(beginninggDiceValues[i] == max) {
						indicesOfMax.add(i);
						numberOfMax++;
					}
				}
				if(numberOfMax == 1) {
					turns[turnsDetermined] = players[indicesOfMax.get(0)];
					valuesFinalized[indicesOfMax.get(0)] = true;
					turnsDetermined++;
				} else if(numberOfMax > 1) {
					
				}
				
				max = 0;
				numberOfMax = 0;
				indicesOfMax.clear();
			}
			for(int i = 0; i < numberOfPlayers; i++) {
				System.out.print(turns[i].getName() + ", ");
			}
			System.out.println();
			*/
			players = new Player[numberOfPlayers];
			players[0] = new Player("Player1", Color.BLUE);
			players[1] = new Player("Player2", Color.GREEN);
			//players[2] = new Player("Player3", Color.RED);
			//players[3] = new Player("Player4", Color.YELLOW);
			currentPhase = TurnPhase.DRAFT;
			isBeginning = false;
		}
		if(currentPhase == TurnPhase.REINFORCE) {

			ProvinceHitArea.isReinforcementsForThisTurnCalculated = false;
		}
		
		currentPlayer = players[turnCount % numberOfPlayers];
		
	}

	@Override
	public void render(DoaGraphicsContext g) {
		// TODO Auto-generated method stub

	}
}