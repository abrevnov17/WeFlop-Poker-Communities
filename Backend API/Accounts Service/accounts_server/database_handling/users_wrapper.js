// users_wrapper.js - Wrapper exports utility promises that asyncronously perform non-blocking CRUD operations on the Users table of our database.

// importing our database connection handler
const pool = require('./conn').pool;

// inserts new user into Users table
function insertUser(username, email, hash) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Users (username, email, hash) VALUES ($1, $2, $3)', [username, email, hash], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

// validates that user does or does not exist in database with given credentials
// (resolves to user id or -1 if no such user exists) 
function getUserId(username, hash) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM Users WHERE username = $1 AND hash = $2', [username, hash], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    if (results.rows.length != 1) {
		    	resolve(-1)
		    }

		    resolve(results.rows[0][id])
	  	})
	})
}

// deletes entry from Users table with given user id
function deleteEntry(user_id) {
	return new Promise(function (resolve, reject) {
		pool.query('DELETE FROM Users WHERE id = $1', [user_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }
		    resolve()
		})
	})
}

module.exports = {
	insertUser: insertUser,
	getUserId: getUserId,
	deleteEntry: deleteEntry,
}