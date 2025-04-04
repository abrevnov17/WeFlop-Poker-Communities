// app.js - Main entrypoint to our server

// setting up express app
const express = require('express')
const app = express();

// configuring request body parsing
const bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

// importing our config module
const config = require('./config/config');

// setting up our routes
app.use(global.gConfig.base_url, require('./routes'));

// listening for requests to port defined in our config
app.listen(global.gConfig.port, () => {
    console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.port}`);
});