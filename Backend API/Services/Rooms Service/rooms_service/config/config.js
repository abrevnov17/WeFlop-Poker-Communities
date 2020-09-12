// config.js - Module to export configuration information loaded from config.json

// used when merging config information
const _ = require('lodash');

// module variables
const config = require('./config.json');
const defaultConfig = config.default;

const environment = process.env.NODE_ENV || 'development';
const environmentConfig = config[environment];

const apiConfig = config.api
const databaseConfig = config.database
const roomDefaultsConfig = config.room_defaults

const finalConfig = _.merge(defaultConfig, environmentConfig, apiConfig, databaseConfig, roomDefaultsConfig);

// defining a global config variable
global.gConfig = finalConfig;