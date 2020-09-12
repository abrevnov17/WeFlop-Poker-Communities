// users_wrapper.js - Wrapper exports utility promises that asyncronously perform non-blocking CRUD operations on various tables in our database.

// importing our database connection handler
const pool = require('./conn').pool;

// gets ids of all games in a given room
function getGamesInRoom(room_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT game_id FROM Tables WHERE room_id = $1', [room_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    resolve(results.rows)
	  	})
	})
}

// gets id's of members of room
function getRoomMembers(room_id) {
	return new Promise(function (resolve, reject) {
		pool.query('SELECT user_id FROM Members WHERE room_id = $1', [room_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    if (results.rows.length != 1) {
		    	resolve(-1)
		    }

		    resolve(results.rows)
	  	})
	})
}

// gets publicly exposed metadata of rooms exposed by API
function getUserRooms(room_id) {
	return new Promise(function (resolve, reject) {
		String parameterized_query = ```SELECT Rooms.*
						  FROM Rooms INNER JOIN Members
						  ON (Rooms.id = Members.room_id AND Rooms.id = $1;```;

		pool.query(parameterized_query, [room_id], (err, results) => {
		    if (err) {
		      reject(err)
		    }

		    if (results.rows.length != 1) {
		    	resolve(-1)
		    }

		    resolve(results.rows)
	  	})
	})
}

// creates a new entry in the Rooms table given a user_id and a name for the new room
function createRoom(user_id, name) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Rooms (name, created_by) VALUES ($1, $2, $3)', [name, user_id], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

// creates a new entry in the Tables table given a game_id and a room_id
function createTable(game_id, room_id) {
	return new Promise(function (resolve, reject) {
		pool.query('INSERT INTO Tables (game_id, room_id) VALUES ($1, $2)', [game_id, room_id], (err, result) => {
		    if (err) {
		      reject(error)
		    }
		    resolve(result.insertId)
  		})
	})
}

module.exports = {
	insertUser: insertUser,
	getUserId: getUserId,
	deleteEntry: deleteEntry,
}