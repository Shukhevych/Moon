-- Table: players

-- DROP TABLE players;

CREATE TABLE players
(
  id bigserial NOT NULL,
  name character varying,
  email character varying,
  password character varying,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

-- Table: cards

-- DROP TABLE cards;

CREATE TABLE cards
(
  player_id bigint NOT NULL,
  proto_id character varying NOT NULL,
  count integer,
  researched boolean NOT NULL DEFAULT false,
  exp bigint,
  CONSTRAINT pk_cards PRIMARY KEY (player_id, proto_id),
  CONSTRAINT fk_player FOREIGN KEY (player_id)
      REFERENCES players (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Index: fki_player

-- DROP INDEX fki_player;

CREATE INDEX fki_player
  ON cards
  USING btree
  (player_id);

-- Table: decks

-- DROP TABLE decks;

CREATE TABLE decks
(
  id bigserial NOT NULL,
  title character varying,
  player_id bigint,
  cards character varying[],
  CONSTRAINT fk_decks FOREIGN KEY (player_id)
      REFERENCES players (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Index: fki_decks

-- DROP INDEX fki_decks;

CREATE INDEX fki_decks
  ON decks
  USING btree
  (player_id);
