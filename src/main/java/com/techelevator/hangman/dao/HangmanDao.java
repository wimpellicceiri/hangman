package com.techelevator.hangman.dao;

import com.techelevator.hangman.model.Game;
import com.techelevator.hangman.model.Player;

import java.util.List;

public interface HangmanDao {
    List<Game> getAllGames();

    Game getGameById(int gameId);

    Game startNewGame(Game game);

    Game makeGuess(int gameId, char guessToMake);

    boolean isOverallLeader(int gameId);

    Player getSecondPlacePlayer();
}
