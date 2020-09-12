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

// Note: This table creation in production should not be done by each server. It should be done once and updates
// only made my admin developers.

// create table statements
const CREATE_ROOMS_TABLE = `CREATE TABLE IF NOT EXISTS Rooms (
  ID SERIAL PRIMARY KEY,
  name VARCHAR(24) NOT NULL,
  num_members INT NOT NULL,
  max_num_members INT NOT NULL,
  num_active_games INT NOT NULL,
  max_num_active_games INT NOT NULL,
  games_played INT DEFAULT 0,
  created_by INT NOT NULL,
  date_created TIMESTAMP NOT NULL DEFAULT NOW()
);`

const CREATE_TABLES_TABLE = `CREATE TABLE IF NOT EXISTS Tables (
  ID SERIAL PRIMARY KEY,
  game_id VARCHAR(255) NOT NULL,
  room_id INTEGER REFERENCES Rooms(ID)
);`

const CREATE_MEMBERS_TABLE = `CREATE TABLE IF NOT EXISTS Members (
  ID SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  room_id INTEGER REFERENCES Rooms(ID),
  date_created TIMESTAMP NOT NULL DEFAULT NOW()
);`


// creating 'Rooms' table
pool
  .query(CREATE_USERS_TABLE)
  .then()
  .catch(err => console.error(err.stack))

// creating 'Tables' table
pool
  .query(CREATE_TABLES_TABLE)
  .then()
  .catch(err => console.error(err.stack))

// creating 'Members' table
pool
  .query(CREATE_MEMBERS_TABLE)
  .then()
  .catch(err => console.error(err.stack))

// we also update defaults
const UPDATE_ROOM_MAX_NUM_MEMBERS = `ALTER TABLE Rooms ALTER COLUMN max_num_members SET DEFAULT $1;`;
const UPDATE_ROOM_MAX_ACTIVE_GAMES = `ALTER TABLE Rooms ALTER COLUMN max_num_active_games SET DEFAULT $1;`; 

pool
  .query(UPDATE_ROOM_MAX_NUM_MEMBERS, [global.gConfig.max_num_members])
  .then()
  .catch(err => console.error(err.stack))

pool
  .query(UPDATE_ROOM_MAX_ACTIVE_GAMES, [global.gConfig.max_num_active_games])
  .then()
  .catch(err => console.error(err.stack))


module.exports = {
	pool: pool
}