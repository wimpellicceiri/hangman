package com.techelevator.hangman.model;

public class Guess {
    private int id;
    private int gameId;
    private char guess;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public char getGuess() {
        return guess;
    }

    public void setGuess(char guess) {
        this.guess = guess;
    }
}
