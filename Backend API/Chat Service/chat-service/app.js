// app.js - Main entrypoint to our server

// setting up express app
const express = require('express')
const app = express();

// importing our config module
const config = require('./config/config');

// enabling websockets
const enableWebSockets = require('express-ws');
enableWebSockets(app);

const MESSAGE_TYPE = {
    JOIN: 0, // outgoing only
    CHAT: 1,
    EXIT: 2 // outgoing only
}

var roomIdToSockets = {};

app.ws('/:room_id', function(ws, req) {
    const room_id = req.params.room_id;
    console.log("connection opened with id: " + room_id)

    if (room_id in roomIdToSockets) {
        roomIdToSockets[room_id].push(ws);
    } else {
        roomIdToSockets[room_id] = [ws];
    }
    
    propogateMessageToRoom(room_id, constructJoinMessage())

    ws.on('message', function(data) {
        const { type, payload } = JSON.parse(data);

        switch (type) {
            case MESSAGE_TYPE.CHAT:
                if (payload === undefined || !verifyUsername(payload.message)) {
                    ws.close(); // client is attempting to send an invalid message (should not be possible in standard use cases)
                } else {
                    propogateMessageToRoom(room_id, payload);
                }
                break;
        }
    });

    ws.on('close', () => {
        propogateMessageToRoom(room_id, constructDisconnectMessage())
    })
});

function verifyUsername(message) {
    if (message === undefined || message.length > 200 || message.length == 0) {
        return false;
    }

    return true;
}

function propogateMessageToRoom(room_id, msg) {
    const socketsInRoom = roomIdToSockets[room_id];
    for (socket in socketsInRoom) {
        try {
            socketsInRoom[socket].send(msg);
        } catch(err) {
            // socket must be closed or player has disconnected
            roomIdToSockets[room_id][socket] = undefined;
        }
    }

    // filtering out stale sockets
    roomIdToSockets[room_id] = roomIdToSockets[room_id].filter(function(socket) {
        return socket !== undefined;
    });
}

function constructJoinMessage(username) {
    let message = {};
    message["type"] = MESSAGE_TYPE.JOIN;
    return JSON.stringify(message)
}

function constructDisconnectMessage(username) {
    let message = {};
    message["type"] = MESSAGE_TYPE.EXIT;
    return JSON.stringify(message)
}


// listening for requests to port defined in our config
app.listen(global.gConfig.port, () => {
    console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.port}`);
});