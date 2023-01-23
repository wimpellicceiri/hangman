package com.techelevator.hangman.dao;

import com.techelevator.hangman.model.Game;
import com.techelevator.hangman.model.Player;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcHangmanDao implements HangmanDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcHangmanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Game> getAllGames() {
        List<Game> allGames = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM game " +
                "ORDER BY game_id ASC;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next()) {
            Game game = mapRowToGame(rowSet);
            addGuessesToGame(game);
            allGames.add(game);
        }

        return allGames;
    }

    @Override
    public Game getGameById(int gameId) {
        String sql = "SELECT * " +
                "FROM game " +
                "WHERE game_id = ? " +
                "ORDER BY game_id ASC;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, gameId);

        if (rowSet.next()) {
            Game game = mapRowToGame(rowSet);
            addGuessesToGame(game);
            return game;
        }

        return null;
    }

    @Override
    public Game startNewGame(Game newGame) {
        String sql = "" +
                "INSERT INTO game " +
                "   (email, word_to_guess) " +
                "VALUES (?, ?) " +
                "RETURNING game_id;";

        Integer gameId = jdbcTemplate.queryForObject(sql, Integer.class,
                newGame.getEmail(),
                newGame.getWordToGuess());

        return getGameById(gameId);
    }

    @Override
    public Game makeGuess(int gameId, char guessToMake) {
        Game game = getGameById(gameId);
        if(game == null || game.isGameOver()) {
            return game;
        }

        String sql = "" +
                "INSERT INTO game_guess (game_id, guess) " +
                "SELECT game_id, ? " +
                "FROM game " +
                "WHERE game_id = ?;";

        jdbcTemplate.update(sql, guessToMake, gameId);

        game = getGameById(gameId);

        if (game != null && game.isGameOver()) {
            // update game to set isGameOver to true
            String updateSql = "UPDATE game SET is_game_over = true WHERE game_id = ?;";
            jdbcTemplate.update(updateSql, gameId);
        }

        return game;
    }

    @Override
    public boolean isOverallLeader(int gameId) {
        String sql = "" +
                "SELECT game_id, (MAX(created_at)-MIN(created_at)) AS time_to_finish " +
                "FROM game_guess " +
                "JOIN game USING (game_id) " +
                "WHERE is_game_over IS TRUE " +
                "GROUP BY game_id " +
                "ORDER BY time_to_finish ASC " +
                "LIMIT 1;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        return rowSet.next() && rowSet.getInt("game_id") == gameId;
    }

    @Override
    public Player getSecondPlacePlayer() {
        String sql = "" +
                "SELECT email " +
                "FROM game " +
                "WHERE game_id = (" +
                "SELECT game_id " +
                "FROM game_guess " +
                "JOIN game USING (game_id) " +
                "WHERE is_game_over IS TRUE " +
                "GROUP BY game_id " +
                "ORDER BY (MAX(created_at)-MIN(created_at)) ASC " +
                "OFFSET 1 ROWS FETCH NEXT 1 ROWS ONLY" +
                ");";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        if (rowSet.next()) {
            Player player = new Player();
            player.setEmail(rowSet.getString("email"));
            return player;
        }

        return null;
    }

    private void addGuessesToGame(Game game) {
        String sql = "SELECT guess " +
                "FROM game_guess " +
                "WHERE game_id = ? " +
                "ORDER BY created_at ASC;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, game.getId());

        while (rowSet.next()) {
            String guess = rowSet.getString("guess");
            if (guess != null && guess.length() > 0) {
                game.addGuess(guess.charAt(0));
            }
        }
    }

    private Game mapRowToGame(SqlRowSet rowSet) {
        Game game = new Game();

        game.setId(rowSet.getInt("game_id"));
        game.setWordToGuess(rowSet.getString("word_to_guess"));
        game.setEmail(rowSet.getString("email"));


        return game;
    }
}