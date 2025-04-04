// authentication_routes.js - Handlers for endpoints relating to authentication

const express = require('express');
const router = express.Router();

// importing our config module
const config = require('./../config/config');

// importing module containing util functions to validate various input formats
const inputValidator = require('./../utils/input_validation')

// importing module used to communicate with sessions service
const sessions = require('./../services/sessions_wrapper')

// importing module that handles hashing / password comparisons
const bcrypt = require('bcrypt');

// importing module that acts as a wrapper to interactions with our Users table in our database
const db = require('./../database_handling/users_wrapper')

// importing module we use to generate session id's
const crypto = require('crypto');

// importing module used to send emails for password reset/verification
const nodemailer = require('nodemailer');

const mail_transporter = nodemailer.createTransport({
  service: 'gmail',
  port: 587,
  ignoreTLS: false,
  secure: false,
  auth: {
    user: 'weflop3@gmail.com',
    pass: 'etuswknwleityywt'
  }
});

// Define the create_account route
router.post(global.gConfig.create_account_route, function(req, res) {
  // parsing out request parameters
  const { username, email, password} = req.body

  console.log("recieved request to create account with params")

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
          sessions.createSession(user_id, token); // creating/updating session (asyncronous)
            res.cookie("sessionID", token, {sameSite: "none", secure: true}); // setting cookie
            res.status(200).send({ userID: user_id }); // success
        }
		});
    }).catch(err =>
      res.status(400).send({ error: "An account already exists with that email and or username."})
    )
  });
});

// Define the login route
router.post(global.gConfig.login_route, function(req, res) {
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
  db.getUserHash(username).then(([user_id, hash]) => {

  	if (user_id === -1) {
      res.status(400).send({ error: "Invalid username/password combination." })
      return;
    }

    bcrypt.compare(password, hash, function(err, match) {
        if (err != undefined) {
          res.status(500).send({ error: "Error comparing hash of password. Please retry." });
          return;
        }
      if(match) {
        // Passwords match...generating a session token and responding with success
        crypto.randomBytes(global.gConfig.session_token_bytes, (err, buff) => { 
          if (err) { 
            res.status(500).send({ error: "Error generating session token. Try logging in.." })
            return;
          } else { 
            token = buff.toString('hex')
            sessions.createSession(user_id, token); // creating/updating session (asyncronous)
            res.cookie("sessionID", token, {sameSite: "none", secure: true}); // setting cookie
            res.status(200).send({ userID: user_id }); // success
            return;
            }
        });
      } else {
        res.status(400).send({ error: "Invalid username/password combination." })
        return;
      } 
    });
  }).catch(err =>
    res.status(400).send({ error: "Invalid username/password combination." })
  );
});


// Define the delete_account route
router.delete(global.gConfig.delete_account_route, function(req, res) {
  // parsing out request parameters
  const sessionID = req.cookies["sessionID"]

  if (sessionID === undefined || sessionID == "" || sessionID == null) {
    res.status(401).send({ error: "Missing required cookie: 'sessionID'" });
    return
  }

  sessions.getUserFromSession(sessionID, user_id => {
     // ensuring user_id was provided as a parameter
    if (user_id === undefined || user_id == "" || user_id == null) {
      res.status(401).send({error: "Invalid session id"})
      return;
    }

     db.deleteEntry(user_id).then(() => {
       res.clearCookie("sessionID")
       res.sendStatus(200);
     }).catch(err =>
     res.status(400).send({ error: err })
     )
  })
});

// Define route that returns whether a username is taken or not
router.get(global.gConfig.username_taken_route, function(req, res) {
  // parsing out request parameters
  const { username } = req.query

  // ensuring user_id was provided as a parameter
  if (username == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'username'" });
    return
  }

  db.isUsernameTaken(username).then(isTaken => {
    res.status(200).send({ isTaken: isTaken });
  }).catch(err =>
  res.status(400).send({ error: err })
  )
});

// Define route that sends a password-reset email
router.post(global.gConfig.forgot_password_route, function(req, res) {
  // parsing out request parameters
  const { email } = req.body

  if (email == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'email'" });
    return
  }

  if (!inputValidator.validateEmail(email)) {
    // invalid email format
    res.status(400).send({ error: "Invalid email formatting." });
    return
  }

  // generating new expiration date
  let expirationDate = new Date() // current time
  expirationDate.setHours(expirationDate.getHours() + global.gConfig.password_reset_expiration)

  // generating expiration token
  crypto.randomBytes(global.gConfig.session_token_bytes, (err, buff) => { 
        if (err) { 
          res.status(500).send({ error: "Error generating expiration token. Try logging in.." })
          return;
        }
        token = buff.toString('hex')

        // now that we have our token, we update the token/expiration date in DB and send our email:
        db.updatePasswordResetTokenInformation(email, token, expirationDate).then(exists => {
          if (!exists) {
            res.status(400).send({ error: "Invalid email." })
            return;
          }

          // sending email
          const text = "Please click on the following link to reset your password: http://localhost:3000/reset/reset-password?token=" + token + "&email=" + email;
          const html = "Please click on the following link to reset your password: <a href='http://localhost:3000/reset/reset-password?token=" + token + "&email=" + email + "'>Reset Password</a>"
          const mailOptions = {
            from: 'weflop3@gmail.com',
            to: email,
            subject: 'WeFlop Password Reset',
            text: text,
            html: html
          };

         mail_transporter.sendMail(mailOptions, function(error, info){
            if (error) {
              res.status(500).send({ error: "Unable to send password reset email. Please try again later.", err: error })
              return;
            }
            res.sendStatus(200);
            return;
          });
        }).catch(err =>
          res.status(500).send({ error: "Unable to send password reset email. Please try again later.", err: err })
        )
  });
});

// Define route that changes a password given reset token credentials are present and valid
router.post(global.gConfig.change_password_route, function(req, res) {
  // parsing out request parameters
  const { email, reset_token, new_password } = req.body

  if (email == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'email'" });
    return
  }

  if (reset_token == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'reset_token'" });
    return
  }

  if (new_password == undefined) {
    res.status(400).send({ error: "Missing required parameter: 'new_password'" });
    return
  }

  if (!inputValidator.validatePassword(new_password)) {
    // invalid password format
    res.status(400).send({ error: "Invalid password formatting." });
    return
  }

  // we first fetch the reset token and expiration date to ensure that the provided token
  // matches the token in our DB and that the expiration term has not been violated
  db.getResetTokenInfo(email).then(([password_reset_token, reset_token_expiration_date]) => {

    if (password_reset_token === null || password_reset_token !== reset_token) {
      res.status(400).send({ error: "Invalid password token." })
      return;
    }

    if (reset_token_expiration_date === null || reset_token_expiration_date < new Date()) {
      res.status(400).send({ error: "Token is expired." })
      return;
    }

    // token is valid, so we reset user password by generating new hash
    bcrypt.hash(new_password, global.gConfig.salt_rounds, function(err, hash) {
      if (err != undefined) {
        res.status(500).send({ error: "Error generating hash of password. Please retry." });
      }

      // we now store a user with email, username, and hashed-password (with salt) in our DB
      db.resetPassword(email, hash).then(exists => {
        if (!exists) {
          res.status(400).send({ error: "No user exists with provided email address." })
          return;
        }
          res.sendStatus(200); // success
          return;
      }).catch(err =>
        res.status(500).send({ error: "Error resetting password. Please retry." })
      )
    });

  }).catch(err =>
      res.status(400).send({ error: err })
  )
});

// Define the route to fetch a username from session id
router.get(global.gConfig.get_username_route, function(req, res) {
  const sessionID = req.cookies["sessionID"]

  if (sessionID === undefined || sessionID == "" || sessionID == null) {
    res.status(400).send({ error: "Missing required cookie: 'sessionID'" });
    return
  }

  sessions.getUserFromSession(sessionID, user_id => {
     // ensuring user_id was provided as a parameter
    if (user_id === undefined || user_id == "" || user_id == null) {
      res.status(401).send({error: "Invalid session id"})
      return;
    }

     db.getUsernameFromId(user_id).then((username) => {
       res.status(200).send({username: username});
     }).catch(err =>
       res.status(400).send({ error: err })
     )
  })
});

module.exports = router;