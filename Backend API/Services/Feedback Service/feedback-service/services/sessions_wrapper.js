// sessions_wrapper.js - Wrapper exports utility requests to communicate with sessions service

// tool to simplify http requests
const unirest = require('unirest') 

// inserts new user into Users table
function createSession(userId, sessionId) {
	const options = {
	  hostname: global.gConfig.sessions_service_host,
	  port: global.gConfig.sessions_service_port,
	  path: '/create-session'
	}

	unirest
	  .post("http://" + options.hostname + ":" + options.port + options.path)
	  .type('json')
	  .send({session_id: sessionId, user_id: userId}).then(function (response) {
	  	if (response.body === undefined) {
	  		callback(null);
	  	}
	    else {
	    	callback(response.body["user_id"])
	    }
	  })
}

// gets user info based on session id
function getUserFromSession(sessionId, callback) {
	const options = {
	  hostname: global.gConfig.sessions_service_host,
	  port: global.gConfig.sessions_service_port,
	  path: '/user'
	}

	unirest
	  .get("http://" + options.hostname + ":" + options.port + options.path)
  	  .type('json')
      .send({session_id: sessionId})
	  .then(function (response) {
	  	if (response.body === undefined) {
	  		callback(null);
	  	}
	    else {
	    	callback(response.body["user_id"])
	    }
	  })
}

module.exports = {
	createSession: createSession,
	getUserFromSession: getUserFromSession
}