/*
 * File: Yahtzee.java
 * ------------------
 * Name:Hayan Khan
 * 
 * Stanford Programming Methodology Assignment #5
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	public void run() {
		setupPlayers();
		initDisplay();
		playGame();
	}
	
	/**
	 * Prompts the user for information about the number of players, then sets up the
	 * players array and number of players.
	 */
	private void setupPlayers() {
		nPlayers = chooseNumberOfPlayers();	
		
		/* Set up the players array by reading names for each player. */
		playerNames = new String[nPlayers];
		for (int i = 0; i < nPlayers; i++) {
			/* IODialog is a class that allows us to prompt the user for information as a
			 * series of dialog boxes.  We will use this here to read player names.
			 */
			IODialog dialog = getDialog();
			playerNames[i] = dialog.readLine("Enter name for player " + (i + 1));
		}
	}
	
	/**
	 * Prompts the user for a number of players in this game, reprompting until the user
	 * enters a valid number.
	 * 
	 * @return The number of players in this game.
	 */
	private int chooseNumberOfPlayers() {
		/* See setupPlayers() for more details on how IODialog works. */
		IODialog dialog = getDialog();
		
		while (true) {
			/* Prompt the user for a number of players. */
			int result = dialog.readInt("Enter number of players");
			
			/* If the result is valid, return it. */
			if (result > 0 && result <= MAX_PLAYERS)
				return result;
			
			dialog.println("Please enter a valid number of players.");
		}
	}
	
	/**
	 * Sets up the YahtzeeDisplay associated with this game.
	 */
	private void initDisplay() {
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
	}

	private void playGame(){
		/*
		 *This is where the main features of the game are set. The number of turns 
		 *each player has and the number of players is set. Each player in this part has three
		 *chances to reroll dice, to get most optimal roll. When the user is done rolling,
		 *the user can choose a category to pick and the score will then be updated.
		 */

		setup();
		for (int turnNum = 0 ; turnNum < N_SCORING_CATEGORIES ; turnNum++){
			for (playerNum = 0 ; playerNum < nPlayers ; playerNum++){
				firstRoll();
				secondRoll();
				thirdRoll();
				assignCategory();
			}
		}
		updateScore();
	}
	//sets up random generators, and arrays that will aid with the programming of other features
	
	private void setup(){
		rgen = RandomGenerator.getInstance();
		scoreArray = new int[nPlayers][N_CATEGORIES]; //is this array initialized to 0?
		hasCategoryBeenUsed = new boolean[nPlayers][N_CATEGORIES];
	}
	
	//rolls the five dies for the first time and displays the dies on the canvas
	private void firstRoll(){
		dice = new int[N_DICE];
		for (int i = 0 ; i < N_DICE ; i++){
			dice[i] = rgen.nextInt(1, 6);
		}
		display.printMessage(playerNames[playerNum] + " please roll the dice");
		display.waitForPlayerToClickRoll(playerNum);
		display.displayDice(dice);
	}
	
	//rolls the five dies for the second time and updates the dies on the canvas
	private void secondRoll(){
		display.printMessage(playerNames[playerNum] + " select your dice and please re-roll the dice");
		display.waitForPlayerToSelectDice();
		for (int i = 0 ; i<N_DICE ; i++){
			if (display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, 6);
			}
		}
		display.displayDice(dice);
	}
	// rolls the five dies for the last time and updates the dies on the canvas
	private void thirdRoll(){
		display.printMessage(playerNames[playerNum] + " select your dice and please re-roll the dice");
		display.waitForPlayerToSelectDice();
		for (int i = 0 ; i<N_DICE ; i++){
			if (display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, 6);
			}
		}
		display.displayDice(dice);
	}
	
	/*
	 * Gives power to the user to select the category he believes will give him
	 * the most points strategically.This piece of the program makes it so
	 * it so that a category already selected cannot be selected again.
	 */
	private void assignCategory(){
		display.printMessage(playerNames[playerNum] + "please pick a category");
		boolean pickedCategory = false;
		while (pickedCategory == false ){
			category = display.waitForPlayerToSelectCategory();
			if (hasCategoryBeenUsed[playerNum][category] == false){
				hasCategoryBeenUsed[playerNum][category] = true;
				pickedCategory = true;
				display.updateScorecard(category, playerNum, getScoreForCategory());
			} else {
				display.printMessage(playerNames[playerNum] + " pick a new category");
			}
		}	
	}
	/*
	 * The following checks all the categories sequentially to see what category
	 * the user decided to choose. When a category is selected, the program calculates
	 * the points that the user is to be awarded and displays it onto the canvas.
	 */
	private int getScoreForCategory(){
		scoreForCategory = 0;
		CheckOnes();
		CheckTwos();
		CheckThrees();
		CheckFours();
		CheckFives();
		CheckSixes();
		CheckThreeAndFourOfAKind(); 
		CheckFullHouse(); 
		CheckSmallStraight();
		CheckLargeStraight();
		CheckYahtzee(); 
		CheckChance();
		
		return (scoreForCategory);
	}
	private void CheckOnes(){
		if (category == ONES) {
			for (int i = 0 ; i < N_DICE; i++){
				if (dice[i] == 1){
					scoreForCategory ++;
				}
			}
			scoreArray[playerNum][ONES] = scoreForCategory;
		}
	}
	private void CheckTwos(){
		if (category == TWOS){
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 2){
					scoreForCategory += 2;
				}
			}
			scoreArray[playerNum][TWOS] = scoreForCategory;
		}
	}
	private void CheckThrees(){
		if (category == THREES){
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 3){
					scoreForCategory += 3;
				}
			}
			scoreArray[playerNum][THREES] = scoreForCategory;
		}
	}
	private void CheckFours(){
		if (category == FOURS){
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 4){
					scoreForCategory += 4;
				}
			}
			scoreArray[playerNum][FOURS] = scoreForCategory;
		}
	}
	private void CheckFives(){
		if (category == FIVES){
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 5){
					scoreForCategory += 5;
				}
			}
			scoreArray[playerNum][FIVES] = scoreForCategory;
		}
	}
	private void CheckSixes(){
		if (category == SIXES){
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 6){
					scoreForCategory += 6;
				}
			}
			scoreArray[playerNum][SIXES] = scoreForCategory;
		}
	}
	private void CheckThreeAndFourOfAKind(){
		if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND){
			scoreForCategory = 0;
			scoreArray[playerNum][THREE_OF_A_KIND] = scoreForCategory;
			int[] numOfSpecificIntegers= new int[NUMBER_OF_SIDES_ON_DICE];
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 1){
					numOfSpecificIntegers[ONES]++;
				}else if (dice[i] == 2){
					numOfSpecificIntegers[TWOS]++;
				}else if (dice[i] == 3){
					numOfSpecificIntegers[THREES]++;
				}else if (dice[i] == 4){
					numOfSpecificIntegers[FOURS]++;
				}else if (dice[i] == 5){
					numOfSpecificIntegers[FIVES]++;
				}else if (dice[i] == 6){
					numOfSpecificIntegers[SIXES]++;
				}
			}
			if (category == THREE_OF_A_KIND){
				for (int i = 0 ; i < NUMBER_OF_SIDES_ON_DICE ; i++){
					if (numOfSpecificIntegers[i] >= 3){
						scoreForCategory = (i + 1) * 3;
						scoreArray[playerNum][THREE_OF_A_KIND] = scoreForCategory;
					} 
				}
			} else if (category == FOUR_OF_A_KIND){
				for (int i = 0 ; i < NUMBER_OF_SIDES_ON_DICE ; i++){
					if (numOfSpecificIntegers[i] >= 4){
						scoreForCategory = (i + 1) * 4;
						scoreArray[playerNum][FOUR_OF_A_KIND] = scoreForCategory;
					}
				}
			}
		}
	}

	private void CheckFullHouse(){
		if (category == FULL_HOUSE){
			int[] numOfSpecificIntegers= new int[NUMBER_OF_SIDES_ON_DICE];
			for (int i = 0 ; i < N_DICE ; i++){
				if (dice[i] == 1){
					numOfSpecificIntegers[ONES]++;
				}else if (dice[i] == 2){
					numOfSpecificIntegers[TWOS]++;
				}else if (dice[i] == 3){
					numOfSpecificIntegers[THREES]++;
				}else if (dice[i] == 4){
					numOfSpecificIntegers[FOURS]++;
				}else if (dice[i] == 5){
					numOfSpecificIntegers[FIVES]++;
				}else if (dice[i] == 6){
					numOfSpecificIntegers[SIXES]++;
				}
			}
			for (int i = 0 ; i < NUMBER_OF_SIDES_ON_DICE; i++){
				if (numOfSpecificIntegers[i] == 3){
					for (int j = 0; j < NUMBER_OF_SIDES_ON_DICE; j++){
						if (numOfSpecificIntegers[j] == 2 ){
							scoreForCategory = 25;
							scoreArray[playerNum][FULL_HOUSE] = scoreForCategory;
						} else {
							scoreForCategory = 0;
							scoreArray[playerNum][FULL_HOUSE] = scoreForCategory;
						}
					}
				}
			}
		}
		
	}
	
	private void CheckSmallStraight(){
		if (category == SMALL_STRAIGHT){
			scoreForCategory = 0;
			for (int straightStartPosition = 1; straightStartPosition<=3 ; straightStartPosition++){// the max can only be 3 because the only possible straight for that largest number is 3 4 5 6, cant have 4 because 7 doesnt exist then
				int numInSeries = 0;
				for (int i = 0 ; i < N_DICE ; i++){
					if (dice[i] == straightStartPosition + numInSeries){// when it goes through without doin anything 
						numInSeries++;
						i = -1; // i is set to -1 because it increments after it finishes the loop, therefore i is set to 0 as it restarts the loop
					}
					if (numInSeries == 4){
						scoreForCategory = 30;
						break; //reposition out of loop 
					}
				}
				if (numInSeries==4) straightStartPosition=4; 
			}
			scoreArray[playerNum][SMALL_STRAIGHT] = scoreForCategory;
		}			
	}
	private void CheckLargeStraight(){
		if (category == LARGE_STRAIGHT){
			scoreForCategory = 0;
			for (int straightStartPosition = 1; straightStartPosition<=2 ; straightStartPosition++){ // 2 represents the maximum value 2 it can start at
				int numInSeries = 0;
				for (int i = 0 ; i < N_DICE ; i++){
					if (dice[i] == straightStartPosition + numInSeries){// when it goes through without doin anything 
						numInSeries++;
						i = -1; // i is set to -1 because it increments after it finishes the loop, therefore i is set to 0 as it restarts the loop
					}
					if (numInSeries == 5){
						scoreForCategory = 40;
						break; //reposition out of loop 
					}
				}
				if (numInSeries==5) straightStartPosition = 4; // straightStartPosition can be any value greater than 2 because this is only used to break out of the loop, dont wanna use two break statements 
			}
			scoreArray[playerNum][LARGE_STRAIGHT] = scoreForCategory;
		}			
	}
	
	private void CheckYahtzee(){
		if (category == YAHTZEE){
			scoreForCategory = 0;
			int numSame = 0;
			int yahtzeeNum = dice[0];
			for (int i = 0 ; i< N_DICE ; i++){
				if (yahtzeeNum == dice[i]){
					numSame ++;
				}
			}
			if (numSame == N_DICE){
				scoreForCategory = 50;
				scoreArray[playerNum][YAHTZEE] = scoreForCategory;
			}
		}
	}
		
	
	private void CheckChance(){
		if (category == CHANCE){
			scoreForCategory = 0;
			int total = 0 ;
			for (int i = 0; i < N_DICE ; i++){
				total += dice[i];
			}
			scoreForCategory = total;
			scoreArray[playerNum][CHANCE] = scoreForCategory;
		}
	}
	/*
	 * This part of the program is where all the points calculations are done,
	 * and the winner is determined.
	 */
	private void updateScore(){
		int totalUpperScore;
		int totalLowerScore;
		int totalScore;
		int upperBonusReward; //default reward if not over 63
		for (playerNum = 0; playerNum < nPlayers ; playerNum++ ){
			totalUpperScore = 0;
			totalLowerScore = 0;
			totalScore = 0;
			upperBonusReward = 0;
			for (int i = 0 ; i < 6 ; i++){ 
				totalUpperScore += scoreArray[playerNum][i]; 
			}
			display.updateScorecard(UPPER_SCORE, playerNum, totalUpperScore);
			if (totalUpperScore >= 63){
				upperBonusReward = 35;
			}
			display.updateScorecard(UPPER_BONUS, playerNum, upperBonusReward);
			//the following statements updates in the array
			scoreArray[playerNum][UPPER_SCORE] = totalUpperScore;
			scoreArray[playerNum][UPPER_BONUS] = upperBonusReward;
			
			//the following statements calculates the lower total
			for (int i = 8; i < 15; i++){ //reads from three of a kind to chance
				totalLowerScore += scoreArray[playerNum][i];
				scoreArray[playerNum][LOWER_SCORE] = totalLowerScore;
				display.updateScorecard(LOWER_SCORE, playerNum, scoreArray[playerNum][LOWER_SCORE]);
			}
			totalScore = totalUpperScore + totalLowerScore + upperBonusReward;
			display.updateScorecard(TOTAL, playerNum, totalScore);
		}
	}

	//private instance variables
	
	private int nPlayers;
	private int scoreForCategory;
	private boolean hasCategoryBeenUsed[][];
	private int playerNum;
	private int category;
	private int [] dice;
	private int [][] scoreArray;
	private String[] playerNames;
	private RandomGenerator rgen;
	private YahtzeeDisplay display;
}