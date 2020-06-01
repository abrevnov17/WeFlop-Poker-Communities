// input_validation.js - File containing useful utility functions

// emails must be of form somestring@somestring.somestring and must be between 4-254 characters
function validateEmail(email) {
	if (email.length < 5 || email.length > 254) {
		return false
	}

	const re = /\S+@\S+\.\S+/;
	return re.test(email);
}

// usernames must be alphanumeric and must be between 5-24 characters (inclusive)
function validateUsername(username) {
	const re = /^[a-zA-Z0-9]{5,24}$/;
	return re.test(username);
}

// passwords must contain at least one lower case letter, at least one upper case letter, have at
// least one 0-9 digit, and have at least one special character. They also must be between 8-24 characters
// (inclusive)
function validatePassword(password) {
	if (password.length < 8 || password.length > 24) {
		return false
	}
	const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])/;
	return re.test(password);
}

module.exports = {
	validateEmail: validateEmail,
	validateUsername, validateUsername
	validatePassword: validatePassword,
}