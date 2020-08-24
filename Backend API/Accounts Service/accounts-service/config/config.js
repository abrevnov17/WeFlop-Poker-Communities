// config.js - Module to export configuration information loaded from config.json

// used when merging config information
const _ = require('lodash');

// module variables
const config = require('./config.json');
const defaultConfig = config.default;

const environment = process.env.NODE_ENV || 'development';
const environmentConfig = config[environment];

const apiConfig = config.api
const securityConfig = config.security
const databaseConfig = config.database

const finalConfig = _.merge(defaultConfig, environmentConfig, apiConfig, securityConfig, databaseConfig);

// defining a global config variable
global.gConfig = finalConfig;