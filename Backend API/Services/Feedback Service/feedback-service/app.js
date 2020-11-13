// app.js - Main entrypoint to our server

// setting up express app
const express = require('express')
const app = express();

const https = require('https')
const fs = require('fs')

// configuring request body parsing
const bodyParser = require('body-parser')
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }))

// configuring cookie parsing
const cookieParser = require('cookie-parser')
app.use(cookieParser()); 

// importing our config module
const config = require('./config/config');

// setting up middleware
middleware = require("./routes/middleware");
app.use(global.gConfig.admin_base_url, middleware.adminVerification);

// setting up our routes
app.use(global.gConfig.public_base_url, require('./routes/public_routes'));
app.use(global.gConfig.admin_base_url, require('./routes/admin_routes'));

// listening for requests to port defined in our config
https.createServer({
  key: fs.readFileSync('perm/server.key'),
  cert: fs.readFileSync('perm/server.cert')
}, app).listen(global.gConfig.port, () => {
    console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.port}`);
});