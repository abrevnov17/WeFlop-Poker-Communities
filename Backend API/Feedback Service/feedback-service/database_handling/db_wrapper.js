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

// creates a new vote and returns id of created vote
function createVote(user_id, option_id) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Votes (option_id, cast_by) VALUES ($1)', [option_id, user_id], (err, result) => {
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
		pool.query('SELECT vote_total FROM PollOptions WHERE poll_id = $1', [poll_id], (err, results) => {
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

function getPollOptions(poll_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT vote_total FROM PollOptions WHERE poll_id = $1', [poll_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    resolve(results.rows)
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

module.exports = {
	insertAnnouncement: insertAnnouncement,
	insertPoll: insertPoll,
	createVote: createVote,
	getVoteTotal: getVoteTotal,
	getPollOptions: getPollOptions,
	deleteAnnouncement: deleteAnnouncement,
	deletePoll: deletePoll,
	getPollOptions: getPollOptions,
	getAnnouncements: getAnnouncements,
	getPolls: getPolls
}