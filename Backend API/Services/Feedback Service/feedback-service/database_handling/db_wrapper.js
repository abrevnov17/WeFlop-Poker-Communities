// db_wrapper.js - Wrapper exports utility promises that asyncronously perform non-blocking CRUD operations on our database.

// importing our database connection handler
const pool = require('./conn').pool;

// creates a new announcement and returns id of created announcement
function insertAnnouncement(title, body) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO announcements (title, body) VALUES ($1, $2) RETURNING id', [title, body], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }
		    
		    resolve(results.rows[0].id)
  		})
	})
}

// creates a new poll and returns id of created poll
function insertPoll(title, description) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Polls (title, description) VALUES ($1, $2) RETURNING id', [title, description], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }

		    resolve(results.rows[0].id)
  		})
	})
}

// assigns a list of options to the poll with given id
// returns ids of options in a list
function appendOptionsToPoll(poll_id, options) {
	return new Promise(function (resolve, reject) {
		option_ids = []

		for (let index = 0; index < options.length; index++) {
			const option = options[index];
			pool.query('INSERT INTO PollOptions (poll_id, description) VALUES ($1, $2) RETURNING id', [poll_id, option], (err, results) => {
			    if (err) {
			      reject(err)
			      return;
			    }

			    option_ids.push(results.rows[0].id)
  			})
		}

		resolve(option_ids)
	});
}

// creates a new feedback element and returns id of created row in Feedback table
function insertFeedback(user_id, body) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Feedback (body, user_id) VALUES ($1, $2) RETURNING id', [body, user_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }
		    resolve(results.rows[0].id)
  		})
	})
}

// creates a new vote and returns id of created vote
function createVote(user_id, option_id, poll_id) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Votes (option_id, cast_by, poll_id) VALUES ($1, $2, $3) RETURNING id', [option_id, user_id, poll_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }
		    resolve(results.rows[0].id)
  		})
	})
}

// gets the number of votes for a specific option
function getVoteTotal(option_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT vote_total FROM PollOptions WHERE id = $1', [option_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }

		    if (results.rows.length != 1) {
		    	resolve(-1)
		    	return;
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
		      return;
		    }

		    resolve(results.rows)
	  	})
	})
}

// gets poll_id corresponding to poll_option
function getPollIdFromOptionId(option_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT poll_id FROM PollOptions WHERE id = $1', [option_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }

		    if (results.rows.length !== 1) {
		    	reject("Invalid poll/option pair.")
		    	return;
		    }

		    resolve(results.rows[0].poll_id)
	  	})
	})
}

// deletes entry from Announcements table with given id
function deleteAnnouncement(announcement_id) {
	return new Promise(function (resolve, reject) {
		pool.query('DELETE FROM Announcements WHERE id = $1', [announcement_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
		    }
		    resolve()
		})
	})
}

// deletes all Poll information associated with a given poll_id
function deletePoll(poll_id) {
	return new Promise(function (resolve, reject) {
		pool.query('DELETE FROM Polls WHERE id = $1', [poll_id], (err, results) => {
		    if (err) {
		      reject(err)
		      return;
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
		      return;
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
		      return;
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
		      return;
		    }

		    resolve(results.rows)
	  	})
	})
}

module.exports = {
	insertAnnouncement: insertAnnouncement,
	insertPoll: insertPoll,
	appendOptionsToPoll: appendOptionsToPoll,
	createVote: createVote,
	getVoteTotal: getVoteTotal,
	getPollOptions: getPollOptions,
	getPollIdFromOptionId: getPollIdFromOptionId,
	deleteAnnouncement: deleteAnnouncement,
	deletePoll: deletePoll,
	getAnnouncements: getAnnouncements,
	getPolls: getPolls,
	insertFeedback: insertFeedback,
	getUserVotes: getUserVotes
}