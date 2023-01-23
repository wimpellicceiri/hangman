package com.techelevator.hangman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Game {
    public static final char MASK_CHAR = '-';
    private int id;
    private String email;
    private List<Character> guesses = new ArrayList<>();

    @JsonIgnore
    private String wordToGuess;

    public Game() {

    }
    public Game(String email, String wordToGuess) {
        this.email = email;
        this.wordToGuess = wordToGuess;
    }

    public boolean isGameOver() {
        if (guesses.size() == 0) {
            return false;
        }

        if (getInvalidGuessesRemaining() == 0) {
            return true;
        }

        return getResult().equals(wordToGuess);
    }

    public String getResult() {
        // create character array
        char[] myResult = new char[wordToGuess.length()];

        // fill it with hyphens to mask
        Arrays.fill(myResult, MASK_CHAR);

        Map<Character, List<Integer>> charIndices = new HashMap<>();
        for (int i = 0; i < wordToGuess.length(); i++) {
            char currentChar = wordToGuess.charAt(i);
            List<Integer> indices = charIndices.getOrDefault(currentChar, new ArrayList<>());
            indices.add(i);
            charIndices.put(currentChar, indices);
        }
        // for each guess they get right, fill in the letters where the hyphens would otherwise mask the result be
        for (Character guess : guesses) {
            if (charIndices.containsKey(guess)) {
                for (int index : charIndices.get(guess)) {
                    myResult[index] = guess;
                }
            }
        }

        return new String(myResult);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Character> getGuesses() {
        return guesses;
    }

    public void addGuess(char guess) {
        this.guesses.add(guess);
    }

    public int getInvalidGuessesRemaining() {
        int guessesRemain = 7;

        for (char guess : guesses) {
            if (wordToGuess.indexOf(guess) == -1) {
                guessesRemain--;
            }
        }

        return guessesRemain;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public void setWordToGuess(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }
}
