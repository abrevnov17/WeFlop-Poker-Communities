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
const CREATE_USERS_TABLE = `CREATE TABLE IF NOT EXISTS Users (
  ID SERIAL PRIMARY KEY,
  username VARCHAR(24) UNIQUE NOT NULL,
  email VARCHAR(254) UNIQUE NOT NULL,
  password VARCHAR(24) NOT NULL,
  password_reset_token text,
  reset_token_expiration_date timestamp,
  date_created timestamp NOT NULL DEFAULT NOW()
);`

// creating users table
pool
  .query(CREATE_USERS_TABLE)
  .then()
  .catch(err => console.error(err.stack))

module.exports = {
	pool: pool
}