// authentication_routes.js - Handlers for endpoints relating to authentication

const express = require('express');
const router = express.Router();

// importing our config module
const config = require('./../config/config');

// import module that acts as a wrapper to interactions with our Users table in our database
const db = require('./../database_handling/db_wrapper')

// Define the endpoint used ot get game ids in a given room
router.get(global.gConfig.get_games_in_room_route, function(req, res) {
  // parsing out request parameters
  const { user_id, room_id } = req.body

  // ensuring that user and room id's are provided
  if (user_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'user_id'" });
    return
  }

  if (room_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'room_id'" });
    return
  }

  // getting games in room from db
  db.getGamesInRoom(user_id, room_id).then(ids => {
    res.status(200).send({ games: ids}); // success
  }).catch(err =>
    res.status(400).send({ error: err })
  );
});

// Define the endpoint that handles fetching members given a room id
router.get(global.gConfig.get_members_of_room_route, function(req, res) {
  // parsing out request parameters
  const { user_id, room_id } = req.body

  // ensuring that user and room id's are provided
  if (user_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'user_id'" });
    return
  }

  if (room_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'room_id'" });
    return
  }

  // getting members in room from db
  db.getRoomMembers(user_id, room_id).then(ids => {
    res.status(200).send({ members: ids}); // success
  }).catch(err =>
    res.status(400).send({ error: err })
  );
});


// Define the endpoint that handles getting rooms (as id's) that contain a given user
router.get(global.gConfig.get_rooms_of_user_route, function(req, res) {
  // parsing out request parameters
  const { user_id } = req.body

  // ensuring that user_id is provided
  if (user_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'user_id'" });
    return
  }

  // getting rooms that contain user
  db.getUserRooms(user_id).then(room_objects => {
    res.status(200).send({ rooms: room_objects}); // success
  }).catch(err =>
    res.status(400).send({ error: err })
  );
});

// Define the endpoint that handles room creation
router.post(global.gConfig.create_room_route, function(req, res) {
  // parsing out request parameters
  const { user_id, room_name } = req.body

  // ensuring that user_d and room_name are provided
  if (user_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'user_id'" });
    return
  }

  if (room_name == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'room_name'" });
    return
  }

  // creating a room
  db.createRoom(user_id, room_name).then(() => {
    res.sendStatus(200);
  }).catch(err =>
    res.status(400).send({ error: err })
  );
});

// Define the endpoint that handles table creation (given an existing game id)
router.post(global.gConfig.add_table_to_room_route, function(req, res) {
  // parsing out request parameters
  const { user_id, room_id, game_id } = req.body

  // ensuring that user_d and room_name are provided
  if (user_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'user_id'" });
    return
  }

  if (room_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'room_id'" });
    return
  }

  if (game_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'game_id'" });
    return
  }

  // creating a table
  db.createRoom(user_id, game_id, room_id).then(() => {
    res.sendStatus(200);
  }).catch(err =>
    res.status(400).send({ error: err })
  );
});

module.exports = router;