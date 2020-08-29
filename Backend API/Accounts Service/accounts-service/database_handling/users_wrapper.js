// users_wrapper.js - Wrapper exports utility promises that asyncronously perform non-blocking CRUD operations on the Users table of our database.

// importing our database connection handler
const pool = require('./conn').pool;

// inserts new user into Users table
function insertUser(username, email, hash) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Users (username, email, hash) VALUES ($1, $2, $3) RETURNING id', [username, email, hash], (err, results) => {
		    if (err) {
		      reject(err)
		    } else {
		      resolve(results.rows[0].id)
		    }
  		})
	})
}

// gets (id,hash) of user with given username
function getUserHash(username) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM Users WHERE username = $1', [username], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }

		    if (results.rows.length != 1) {
		    	resolve((-1,-1))
		    	return;
		    }

		    resolve([results.rows[0].id, results.rows[0].hash])
	  	})
	})
}

// deletes entry from Users table with given user id
function deleteEntry(user_id) {
	return new Promise(function (resolve, reject) {
		pool.query('DELETE FROM Users WHERE id = $1', [user_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }
		    resolve()
		})
	})
}

// checks if username exists (resolves to true if it does, false otherwise)
function isUsernameTaken(username) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM Users WHERE username = $1', [username], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }

		    if (results.rows.length != 1) {
		    	resolve(false)
		    }

		    resolve(true)
	  	})
	})
}

// sets hash for user with given email
// returns false if no user exists with given email
function resetPassword(email, new_hash) {
	return new Promise(function (resolve, reject) {
		pool.query('UPDATE Users SET (hash,password_reset_token,reset_token_expiration_date) = ($1,NULL,NULL) WHERE Users.email = $2', [new_hash, email], (err, result) => {
		    if (err) {
		      reject(err);
		      return;
		    }
		    if (result.rowCount != 1) {
		    	resolve(false)
		    	return;
		    }
		    resolve(true);
  		})
	})
}

// updates password reset token and expiration values for user with given email
// returns false if no such email exists, true otherwise (on success)
function updatePasswordResetTokenInformation(email, token, expiration_date) {
	return new Promise(function (resolve, reject) {
		pool.query('UPDATE Users SET (password_reset_token,reset_token_expiration_date) = ($1,$2) WHERE Users.email = $3', [token, expiration_date, email], (err, result) => {

		    if (err) {
		      reject(err);
		      return;
		    }
		    if (result.rowCount != 1) {
		    	resolve(false)
		    	return;
		    }

		    resolve(true);
  		})
	})
}

// gets password reset token and corresponding expiration date (as timestamp)
function getResetTokenInfo(email) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM Users WHERE email = $1', [email], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }

		    if (results.rows.length != 1) {
		    	resolve([-1,-1])
		    }

		    resolve([results.rows[0].password_reset_token, results.rows[0].reset_token_expiration_date])
	  	})
	})
}

module.exports = {
	insertUser: insertUser,
	getUserHash: getUserHash,
	deleteEntry: deleteEntry,
	isUsernameTaken: isUsernameTaken,
	resetPassword: resetPassword,
	updatePasswordResetTokenInformation: updatePasswordResetTokenInformation,
	getResetTokenInfo: getResetTokenInfo
}