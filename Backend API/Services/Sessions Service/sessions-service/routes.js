// routes.js

const express = require('express');
const router = express.Router();

// importing our config module
const config = require('./config/config');

// setting up redis client
const redis = require("redis");

const client = redis.createClient({
    port: global.gConfig.redis_port,
    host: global.gConfig.redis_host
});

const TTL_SECONDS = 60 * 60 * 24; // expiration time for keys

// Define the session creation route
router.post(global.gConfig.create_session_route, function(req, res) {

  // parsing out request parameters
  const { session_id, user_id } = req.body

  if (session_id == undefined) {
   res.status(400).send({ error: "Missing required parameter: 'session_id'" });
   return
  }

  if (user_id == undefined) {
   res.status(400).send({ error: "Missing required parameter: 'user_id'" });
   return
  }

  client.hmset(session_id, { "user_id": user_id }, function(err, reply) {
      if (err !== null) {
        res.status(500).send({ error: "Unable to save to redis." })
        return;
      }

      res.sendStatus(200);
  });
});

// Define the session retrieval route
router.get(global.gConfig.fetch_user_route, function(req, res) {

  // parsing out request parameters
  const { session_id } = req.body

  if (session_id == undefined) {
   res.status(400).send({ error: "Missing required parameter: 'session_id'" });
   return
  }

  client.hgetall(session_id, function(err, object) {
      if (err !== null) {
        res.status(500).send({ error: "Unable to fetch from redis." })
        return;
      }

      res.status(200).send(object);
  });  
});

// Define the game creation route
router.post(global.gConfig.create_game_route, function(req, res) {

  // parsing out request parameters
  const { game_id, address } = req.body

  if (game_id == undefined) {
   res.status(400).send({ error: "Missing required parameter: 'game_id'" });
   return
  }

  if (address == undefined) {
   res.status(400).send({ error: "Missing required parameter: 'address'" });
   return
  }

  client.hmset(game_id, { "address": address }, function(err, reply) {
      if (err !== null) {
        res.status(500).send({ error: "Unable to save to redis." })
        return;
      }

      res.sendStatus(200);
  });
});

// Define the game retrieval route
router.get(global.gConfig.fetch_game_route, function(req, res) {

  // parsing out request parameters
  const { game_id } = req.body

  if (game_id == undefined) {
   res.status(400).send({ error: "Missing required parameter: 'game_id'" });
   return
  }

  client.hgetall(game_id, function(err, object) {
      if (err !== null) {
        res.status(500).send({ error: "Unable to fetch from redis." })
        return;
      }

      res.status(200).send(object);
  });  
});

module.exports = router;