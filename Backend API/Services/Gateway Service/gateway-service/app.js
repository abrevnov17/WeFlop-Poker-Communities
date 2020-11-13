// app.js - Main entrypoint to our server

// importing proxy-related modules
const https = require('https'),
    httpProxy = require('http-proxy');

const fs = require('fs')

// importing our config module
const config = require('./config/config');

const accountsProxy = new httpProxy.createProxyServer({
  target: {
    protocol: 'https:',
    host: global.gConfig.accounts_service_host,
    port: global.gConfig.accounts_service_port,
  },
  ssl: {
    key: fs.readFileSync('perm/server.key'),
    cert: fs.readFileSync('perm/server.cert'),
  },
  secure: false
});

const feedbackProxy = new httpProxy.createProxyServer({
  target: {
    protocol: 'https:',
    host: global.gConfig.feedback_service_host,
    port: global.gConfig.feedback_service_port
  },
  ssl: {
    key: fs.readFileSync('perm/server.key'),
    cert: fs.readFileSync('perm/server.cert'),
  },
  secure: false
});

const chatProxy = new httpProxy.createProxyServer({
  target: {
    protocol: 'https:',
    host: global.gConfig.chat_service_host,
    port: global.gConfig.chat_service_port
  },
  ssl: {
    key: fs.readFileSync('perm/server.key'),
    cert: fs.readFileSync('perm/server.cert'),
  },
  secure: false
});

const gameProxy = new httpProxy.createProxyServer({
  target: {
    protocol: 'http:',
    host: global.gConfig.game_service_host,
    port: global.gConfig.game_service_port
  }
});

const server = https.createServer({
  key: fs.readFileSync('perm/server.key'),
  cert: fs.readFileSync('perm/server.cert')
}, function (req, res) {
  const splitUrl = req.url.split("/");

  // getting service name
  const service = splitUrl[1];

  console.log("recieved request to service: " + service)

  // removing service name from path
  delete splitUrl[1]
  req.url = splitUrl.join("/");

  switch(service) {
      case global.gConfig.accounts_service_endpoint:
        accountsProxy.web(req, res);
        break;
      case global.gConfig.feedback_service_endpoint:
        feedbackProxy.web(req, res);
        break;
      case global.gConfig.chat_service_endpoint:
        chatProxy.web(req, res);
        break;
      case global.gConfig.game_service_endpoint:
        gameProxy.web(req, res);
        break;
      default:
        // service does not support this url
        res.sendStatus(400);
  }
});

server.on('upgrade', function (req, socket, head) {
  const splitUrl = req.url.split("/");

  // getting service name
  const service = splitUrl[1];

  // removing service name from path
  delete splitUrl[1]
  req.url = Array.prototype.join.call(splitUrl, "/");

  switch(service) {
      case global.gConfig.chat_service_endpoint:
        chatProxy.ws(req, socket, head);
        break;
      case global.gConfig.game_service_endpoint:
        gameProxy.ws(req, socket, head);
        break;
      default:
        // service does not support websockets
        socket.end()
  }
});

// listening for requests to port defined in our config
server.listen(global.gConfig.port, () => {
    console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.port}`);
});