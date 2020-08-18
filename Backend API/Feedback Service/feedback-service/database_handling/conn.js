// conn.js - Manages connection to database

// importing our config module
const config = require('./../config/config');

// used to manage PostgreSQL connection
const Pool = require('pg').Pool
const pool = new Pool({
  host: global.gConfig.db_host,
  database: global.gConfig.db_identifier,
  password: global.gConfig.db_password,
  port: global.gConfig.db_port,
})

// create table statements
const CREATE_ANNOUNCEMENTS_TABLE = `CREATE TABLE IF NOT EXISTS Announcements (
  id SERIAL PRIMARY KEY,
  body text[],
  date_created timestamp NOT NULL DEFAULT NOW()
);`

const CREATE_POLLS_TABLE = `CREATE TABLE IF NOT EXISTS Polls (
  id SERIAL PRIMARY KEY,
  description text[],
  date_created timestamp NOT NULL DEFAULT NOW()
);`

const CREATE_VOTES_TABLE = `CREATE TABLE IF NOT EXISTS Votes (
  id SERIAL PRIMARY KEY,
  option_id integer NOT NULL, REFERENCES PollOptions(id) ON DELETE CASCADE,
  cast_by integer NOT NULL,
  date_created timestamp NOT NULL DEFAULT NOW()
);`

const UPDATE_VOTE_TOTAL_FUNCTION = `CREATE OR REPLACE FUNCTION update_vote_total()
              RETURNS TRIGGER AS 
              $BODY$
              BEGIN
                  UPDATE PollOptions
                      SET vote_total = vote_total + 1
                      WHERE id = new.id
                      VALUES(new.id,new.name);

                         RETURN new;
              END;
              $BODY$
              language plpgsql`;

const UPDATE_VOTE_TOTAL_TRIGGER = `CREATE TRIGGER update_poll_option_vote_total BEFORE UPDATE
    ON Votes FOR EACH ROW EXECUTE PROCEDURE 
    update_vote_total();`

const CREATE_POLL_OPTIONS_TABLE = `CREATE TABLE IF NOT EXISTS PollOptions (
  id SERIAL PRIMARY KEY,
  poll_id int references Polls(id) ON DELETE CASCADE,
  option_number integer,
  description text,
  vote_total integer,
  UNIQUE (poll_id, description)
);`

// // creating users table
// pool
//   .query(CREATE_USERS_TABLE)
//   .then()
//   .catch(err => console.error(err.stack))

module.exports = {
	pool: pool
}