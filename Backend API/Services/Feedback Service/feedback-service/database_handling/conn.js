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

module.exports = {
	pool: pool
}