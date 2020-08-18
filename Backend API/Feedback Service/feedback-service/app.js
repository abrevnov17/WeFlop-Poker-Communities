// app.js - Main entrypoint to our server

// setting up express app
const express = require('express')
const app = express();

// importing our config module
const config = require('./config/config');

// listening for requests to port defined in our config
app.listen(global.gConfig.port, () => {
    console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.port}`);
});