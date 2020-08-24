// db_wrapper.js - Wrapper exports utility promises that asyncronously perform non-blocking CRUD operations on our database.

// importing our database connection handler
const pool = require('./conn').pool;

// creates a new announcement and returns id of created announcement
function insertAnnouncement(body) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Announcements (body) VALUES ($1)', [body], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

// creates a new poll and returns id of created poll
function insertPoll(description) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Polls (description) VALUES ($1)', [description], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

// assigns a list of options to the poll with given id
// returns ids of options in a list
function appendOptionsToPoll(poll_id, options) {
	return new Promise(function (resolve, reject) {
		option_ids = []
		for (const option in options) {
			pool.query('INSERT INTO PollOptions (poll_id, description) VALUES ($1, $2)', [poll_id, options[option]], (err, result) => {
			    if (err) {
			      reject(error)
			    }

			    option_ids.push(result.insertId)
  			})
		}

		resolve(option_ids)
	});
}

// creates a new feedback element and returns id of created row in Feedback table
function insertFeedback(user_id, body) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Feedback (body, user_id) VALUES ($1, $2)', [body, user_id], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

// creates a new vote and returns id of created vote
function createVote(user_id, option_id) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Votes (option_id, cast_by) VALUES ($1, $2)', [option_id, user_id], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

// gets the number of votes for a specific option
function getVoteTotal(option_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT vote_total FROM PollOptions WHERE id = $1', [option_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    if (results.rows.length != 1) {
		    	resolve(-1)
		    }

		    resolve(results.rows[0][0])
	  	})
	})
}

// gets poll options for a given poll
function getPollOptions(poll_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM PollOptions WHERE poll_id = $1', [poll_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    resolve(results.rows)
	  	})
	})
}

// deletes entry from Announcements table with given id
function deleteAnnouncement(announcement_id) {
	return new Promise(function (resolve, reject) {
		pool.query('DELETE FROM Announcements WHERE id = $1', [announcement_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }
		    resolve()
		})
	})
}

// deletes all Poll information associated with a given poll_id
function deletePoll(poll_id) {
	return new Promise(function (resolve, reject) {
		pool.query('DELETE FROM Polls WHERE id = $1', [announcement_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }
		    resolve()
		})
	})
}

// fetches all announcements
function getAnnouncements() {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM Announcements', [], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    resolve(results.rows)
	  	})
	})
}

// fetches all polls
function getPolls() {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT * FROM Polls', [], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    resolve(results.rows)
	  	})
	})
}

// gets all votes a user has cast (returned as option id's)
function getUserVotes(user_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT option_id FROM Votes WHERE cast_by = $1', [user_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    resolve(results.rows)
	  	})
	})
}

module.exports = {
	insertAnnouncement: insertAnnouncement,
	insertPoll: insertPoll,
	createVote: createVote,
	getVoteTotal: getVoteTotal,
	getPollOptions: getPollOptions,
	deleteAnnouncement: deleteAnnouncement,
	deletePoll: deletePoll,
	getAnnouncements: getAnnouncements,
	getPolls: getPolls,
	insertFeedback: insertFeedback,
	getUserVotes: getUserVotes
}