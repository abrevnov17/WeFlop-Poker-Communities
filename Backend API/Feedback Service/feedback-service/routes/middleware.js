// middleware.js – Defines middleware that processes incoming requests

// importing our config module
const config = require('./../config/config');

function adminVerification(req, res, next){
	const { admin_password } = req.body;

	if (admin_password === undefined) {
      	res.status(400).send({ error: "Missing required parameter: 'admin_password'" });
    	return;
	}

	if (global.gConfig.admin_password === admin_password) {
		next();
	}

	// otherwise, admin password is not valid
	res.sendStatus(401);
}

module.exports = {
    adminVerification: adminVerification
}