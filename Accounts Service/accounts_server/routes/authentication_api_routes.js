// authentication_routes.js - Handlers for endpoints relating to authentication

const express = require('express');
const router = express.Router();

// importing our config module
const config = require('./../config/config');

// importing module containing util functions to validate various input formats
const inputValidator = require('./../utils/input_validation')

// importing module that handles hashing / password comparisons
const bcrypt = require('bcrypt');

// import module that acts as a wrapper to interactions with our Users table in our database
const db = require('./../database_handling/users_wrapper')

// module we use to generate session id's
const crypto = require('crypto');

// Define the create_account route
router.post(global.gConfig.create_account_route, function(req, res) {
  // parsing out request parameters
  const { username, email, password} = req.body

  // ensuring username, email, and password are provided

  if (username == undefined) {
	res.status(400).send({ error: "Missing required parameter: 'username'" });
	return
  }

  if (email == undefined) {
	res.status(400).send({ error: "Missing required parameter: 'email'" });
	return
  }

  if (password == undefined) {
	res.status(400).send({ error: "Missing required parameter: 'password'" });
	return
  }


 // first we ensure that username, email, and password are of valid format

 if (!inputValidator.validateUsername(username)) {
 	// invalid username format
	res.status(400).send({ error: "Invalid username formatting." });
	return
 }

 if (!inputValidator.validateEmail(email)) {
 	// invalid email format
	res.status(400).send({ error: "Invalid email formatting." });
	return
 }

 if (!inputValidator.validatePassword(password)) {
 	// invalid password format
	res.status(400).send({ error: "Invalid password formatting." });
	return
 }

 // we need to compute the hashed password value to store in our database
 bcrypt.hash(password, global.gConfig.salt_rounds, function(err, hash) {
 	if (err != undefined) {
		res.status(500).send({ error: "Error generating hash of password. Please retry." });
 	}
    
    // we now store a user with email, username, and hashed-password (with salt) in our DB
    db.insertUser(username, email, hash).then(user_id => {
    	// successfully inserted user into database...generating a session token and responding with success
    	crypto.randomBytes(global.gConfig.session_token_bytes, (err, buff) => { 
    		if (err) { 
				res.status(500).send({ error: "Error generating session token. Try logging in.." })
			} else { 
				token = buff.toString('hex')
				res.status(200).send({ userID: user_id, sessionID: token }); // success
			}
		});
    }).catch(err =>
		res.status(500).send({ error: "Error generating hash of password. Please retry." })
    )
 });
});

// Define the create_account route
router.get(global.gConfig.login_route, function(req, res) {
  // parsing out request parameters
  const { username, password} = req.body

  // ensuring username and password are provided as parameters

  if (username == undefined) {
	res.status(400).send({ error: "Missing required parameter: 'username'" });
	return
  }

  if (password == undefined) {
	res.status(400).send({ error: "Missing required parameter: 'password'" });
	return
  }

  // validating the provided user credentials
  db.getUserId(username, hash).then(user_id => {
  	if (user_id == -1) {
		res.status(400).send({ error: "Invalid username/password combination." })
  	} else {
		// successfully found user in database...generating a session token and responding with success
    	crypto.randomBytes(global.gConfig.session_token_bytes, (err, buff) => { 
    		if (err) { 
				res.status(500).send({ error: "Error generating session token. Try logging in.." })
			} else { 
				token = buff.toString('hex')
				res.status(200).send({ userID: user_id, sessionID: token }); // success
			}
		});
  	}
  }).catch(err =>
		res.status(500).send({ error: "Error querying database for user. Please retry." })
   );
});


// Define the delete_account route
router.delete(global.gConfig.delete_account_route, function(req, res) {
  // parsing out request parameters
  const { user_id } = req.body

  // ensuring user_id was provided as a parameter
  if (user_id == undefined) {
	res.status(400).send({ error: "Missing required parameter: 'user_id'" });
	return
  }

  db.deleteEntry(user_id).then(() => {
	res.sendStatus(200);
  }).catch(err =>
	res.status(400).send({ error: err })
  )
});

module.exports = router;