// public_routes.js - Handlers for publicly-exposed endpoints

const express = require('express');
const router = express.Router();

// importing our config module
const config = require('./../config/config');

// import module that acts as a wrapper to interactions with our database
const db = require('./../database_handling/db_wrapper')

// importing module used to communicate with sessions service
const sessions = require('./../services/sessions_wrapper')

// route that retrieves all updates (announcements and polls)
// in order of time (recent first)
router.get(global.gConfig.updates_route, async function(req, res) {
  let polls = []
  let announcements = []
  // getting polls and announcements
  const [poll_rows, announcement_rows] = await Promise.all([ db.getPolls(), db.getAnnouncements()])

  // for each poll, we need to get the rest of the associated options
  // we construct a list of promises to be executed simulatenously
  const option_promises = []

  for (const row in poll_rows) {
    const poll_row = poll_rows[row];

    option_promises.push(db.getPollOptions(poll_row.id))


    let poll = new Object();
    poll.id = poll_row.id
    poll.description = poll_row.description
    poll.timestamp = poll_row.date_created
    poll.options = []

    polls.push(poll)
  }

  const poll_options = await Promise.all(option_promises)

  for (let index = 0; index < poll_options.length; index++) {
    const options = poll_options[index];

    for (let o = 0; o < options.length; o++) {
      const option_r = options[o];

      let option = new Object();
      option.id = option_r.id;
      option.description = option_r.description;
      option.vote_count = option_r.vote_total;
      polls[index].options.push(option);
    }
  }

  for (const row in announcement_rows) {
    const announcement_row = announcement_rows[row];

    let announcement = new Object();
    announcement.id = announcement_row.id;
    announcement.body = announcement_row.body;
    announcement.timestamp = announcement_row.date_created;

    announcements.push(announcement)
  }

  res.status(200).send({announcements: announcements, polls: polls})
});

// casts a vote
router.post(global.gConfig.vote_route, function(req, res) {
  const { option_id } = req.body;
  const sessionID = req.cookies["session_id"]

  if (sessionID == undefined) {
    res.status(400).send({ error: "Missing required cookie: 'session_id'" });
    return
  }

  if (option_id == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'option_id'" });
    return
  }

  sessions.getUserFromSession(sessionID, user_id => {
    if (user_id === undefined || user_id == "" || user_id == null) {
      res.status(401).send({error: "Invalid session id"})
      return;
    }
    db.getPollIdFromOptionId(option_id).then(poll_id => {
     db.createVote(user_id, option_id, poll_id).then(() => {
      res.sendStatus(200);
    }).catch(err =>
    res.status(400).send({ error: "Unable to create vote. Please try again." })
    )
  }).catch(err =>
  res.status(400).send({ error: "Unable to create vote. Please try again." })
  )
})
});

// sends new feedback
router.post(global.gConfig.send_feedback_route, function(req, res) {
  const { body } = req.body;

  const sessionID = req.cookies["session_id"]

  if (sessionID == undefined) {
    res.status(400).send({ error: "Missing required cookie: 'session_id'" });
    return
  }

  if (body == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'body'" });
    return
  }

  sessions.getUserFromSession(sessionID, user_id => {
    if (user_id === undefined || user_id == "" || user_id == null) {
      res.status(401).send({error: "Invalid session id"})
      return;
    }
   db.insertFeedback(user_id, body).then(() => {
    res.sendStatus(200);
  }).catch(err =>
  res.status(400).send({ error: err })
  )
})
});

// gets all posts the user has voted for
router.get(global.gConfig.get_votes_route, function(req, res) {
  const sessionID = req.cookies["session_id"]

  if (sessionID == undefined) {
    res.status(400).send({ error: "Missing required cookie: 'session_id'" });
    return
  }

  sessions.getUserFromSession(sessionID, user_id => {
    if (user_id === undefined || user_id == "" || user_id == null) {
      res.status(401).send({error: "Invalid session id"})
      return;
    }
   db.getUserVotes(user_id).then(votes => {
    res.status(200).send({votes: votes});
  }).catch(err =>
  res.status(400).send({ error: err })
  )
})
});

module.exports = router;