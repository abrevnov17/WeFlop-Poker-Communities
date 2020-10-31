// sessions_wrapper.js - Wrapper exports utility requests to communicate with sessions service

// tool to simplify http requests
const unirest = require('unirest') 

// inserts new user into Users table
function createSession(userId, sessionId) {
	const options = {
	  hostname: global.gConfig.sessions_service_host,
	  port: global.gConfig.sessions_service_port,
	  path: '/api/create-session'
	}

	unirest
	  .post(options.hostname + ":" + options.port + "/" + options.path)
	  .headers({'Accept': 'application/json'})
	  .send({"session_id": sessionId, "user_id": userId})
	  .then(function (response) {
	    console.log(response.body)
	  })
}

module.exports = {
	createSession: createSession
}