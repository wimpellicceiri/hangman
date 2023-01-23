package com.techelevator.hangman.controller;

import com.techelevator.hangman.dao.HangmanDao;
import com.techelevator.hangman.model.Game;
import com.techelevator.hangman.model.Guess;
import com.techelevator.hangman.model.Player;
import com.techelevator.hangman.services.NotificationService;
import com.techelevator.hangman.services.RandomWordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/games")
public class HangmanController {
    private final HangmanDao dao;
    private final RandomWordService randomWordService;
    private final NotificationService notificationService;

    private static final int MAX_WORD_LENGTH = 6;

    public HangmanController(HangmanDao dao, RandomWordService randomWordService, NotificationService notificationService) {
        this.dao = dao;
        this.randomWordService = randomWordService;
        this.notificationService = notificationService;
    }

    @GetMapping("")
    public List<Game> getAllGames() {
        return dao.getAllGames();
    }

    @GetMapping("/{gameId}")
    public Game getGameById(@PathVariable int gameId) {
        Game game = dao.getGameById(gameId);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with id of " + gameId + " not found");
        }

        return game;
    }

    @PostMapping("")
    public Game startNewGame(@RequestBody @Valid Player player) {
        String word = randomWordService.getRandomWord(MAX_WORD_LENGTH);

        if (word == null) {
            word = "hello";
        }
        Game game = new Game(player.getEmail(), word.toLowerCase());
        return dao.startNewGame(game);
    }

    @PostMapping("/{gameId}/guess")
    public Game makeGuess(@RequestBody @Valid Guess guess, @PathVariable int gameId) {
        Game game = dao.makeGuess(gameId, guess.getGuess());

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with id of " + gameId + " not found");
        }

        if (game.isGameOver() && dao.isOverallLeader(game.getId())) {
            // send notification to former leader
            Player player = dao.getSecondPlacePlayer();
            notificationService.sendCrownTakenNotificationTo(player);
        }

        return game;
    }
}
