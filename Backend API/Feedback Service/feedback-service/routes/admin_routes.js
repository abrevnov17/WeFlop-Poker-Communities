// admin_routes.js - Handlers for admin-only routes

// Note: Authentication should be done in middleware

const express = require('express');
const router = express.Router();

// importing our config module
const config = require('./../config/config');

// import module that acts as a wrapper to interactions with our database
const db = require('./../database_handling/db_wrapper')

// creates a new announcement
router.post(global.gConfig.create_announcement_route, function(req, res, next) {
  const { body } = req.body;

  if (body == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'body'" });
    return
  }

 db.insertAnnouncement(body).then(announcement_id => {
   res.status(200).send({ announcement_id: announcement_id});
 }).catch(err =>
   res.status(400).send({error: err})
 )
});

// creates a new poll with given options and description
router.post(global.gConfig.create_poll_route, function(req, res) {
  const { options, description } = req.body;

  if (options == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'options'" });
    return
  }

  if (description == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'description'" });
    return
  }

  db.insertPoll(description).then(poll_id => {
    db.appendOptionsToPoll(poll_id, options).then(() => {
      res.status(200).send({ poll_id: poll_id});
     }).catch(err =>
      res.status(400).send({ error: err })
    )
   }).catch(err =>
    res.status(400).send({ error: err })
  )
});

// deletes an announcement
router.delete(global.gConfig.delete_announcement_route, function(req, res) {
    const { announcement_id } = req.body;

    if (announcement_id == undefined) {
      res.status(400).send({ error: "Missing required parameter: 'announcement_id'" });
      return
    }

   db.deleteAnnouncement(announcement_id).then(() => {
    res.sendStatus(200);
   }).catch(err =>
    res.status(400).send({ error: err })
  )
});

// deletes a poll
router.delete(global.gConfig.delete_poll_route, function(req, res) {
    const { poll_id } = req.body;

    if (poll_id == undefined) {
      res.status(400).send({ error: "Missing required parameter: 'poll_id'" });
      return
    }

   db.deletePoll(poll_id).then(() => {
    res.sendStatus(200);
   }).catch(err =>
    res.status(400).send({ error: err })
  )
});

module.exports = router;