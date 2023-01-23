package com.techelevator.hangman;

import com.techelevator.hangman.model.Game;
import org.junit.*;

public class HangmanApplicationTests {

	@Test
	public void game_returns_game_over() {
		Game game = new Game("walt@techelevator.com", "elevator");
		game.addGuess('e');
		game.addGuess('l');
		game.addGuess('v');
		game.addGuess('a');
		game.addGuess('t');
		game.addGuess('r');
		game.addGuess('o');

		Assert.assertTrue(game.isGameOver());
	}

	@Test
	public void game_returns_masked_result() {
		Game game = new Game("walt@techelevator.com", "elevator");
		game.addGuess('e');
		game.addGuess('a');
		game.addGuess('t');
		game.addGuess('r');
		game.addGuess('o');

		Assert.assertEquals("e-e-ator", game.getResult());
	}

	@Test
	public void new_game_returns_fully_masked_word() {
		String wordToGuess = "elevator";
		String expected = String.valueOf(Game.MASK_CHAR).repeat(wordToGuess.length());

		Game game = new Game("walt@techelevator.com", wordToGuess);

		Assert.assertEquals(expected, game.getResult());
	}

	@Test
	public void all_guesses_exhausted() {
		// TODO
	}

}
