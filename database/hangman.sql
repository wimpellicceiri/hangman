BEGIN TRANSACTION;

DROP TABLE IF EXISTS game_guess, game;


CREATE TABLE game
(
    game_id serial,
    email varchar(50) not null,
    word_to_guess varchar(50) not null,
    invalid_guesses_remaining int not null default(7),
    is_game_over boolean not null default(false),

    constraint pk_game primary key (game_id)
);

CREATE TABLE game_guess
(
    game_guess_id serial,
    game_id integer not null,
    guess char(1) not null,
    created_at timestamp not null default(current_timestamp),

    constraint pk_game_guess primary key (game_guess_id),
    constraint fk_game_guess foreign key (game_id) references game (game_id)
);

COMMIT;
