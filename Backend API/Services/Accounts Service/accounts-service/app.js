// app.js - Main entrypoint to our server

// setting up express app
const express = require('express')
const app = express();
const fs = require('fs')
const https = require('https')

// configuring request body parsing
const bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

// configuring cookie parsing
const cookieParser = require('cookie-parser')
app.use(cookieParser()); 

// importing our config module
const config = require('./config/config');

// setting up our routes
app.use(global.gConfig.base_url, require('./routes/authentication_api_routes'));

// listening for requests to port defined in our config
https.createServer({
  key: fs.readFileSync('perm/server.key'),
  cert: fs.readFileSync('perm/server.cert')
}, app).listen(global.gConfig.port, () => {
    console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.port}`);
});