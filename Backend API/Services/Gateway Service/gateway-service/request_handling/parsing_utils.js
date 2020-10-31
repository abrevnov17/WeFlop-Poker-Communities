// parsing_utils.js – Contains utility functions used to parse HTTP requests

const { parse } = require('querystring');

// Cookies:

function parseCookies(str) {
  let rx = /([^;=\s]*)=([^;]*)/g;
  let obj = { };
  for ( let m ; m = rx.exec(str) ; )
    obj[ m[1] ] = decodeURIComponent( m[2] );
  return obj;
}

function stringifyCookies(cookies) {
  return Object.entries( cookies )
    .map( ([k,v]) => k + '=' + encodeURIComponent(v) )
    .join( '; ');
}

// Request Body:

function collectRequestData(request, callback) {
    const FORM_URLENCODED = 'application/x-www-form-urlencoded';
    if(request.headers['content-type'] === FORM_URLENCODED) {
        let body = '';
        request.on('data', chunk => {
            body += chunk.toString();
        });
        request.on('end', () => {
            callback(parse(body));
        });
    }
    else {
        callback(null);
    }
}