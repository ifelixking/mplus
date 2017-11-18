const uuid = require('uuid/v1');

var Utils = {

	success: 'success',
	error: 'error',

	isSuccess: function (status) {
		return status == this.success;
	},

	newGuid: function () {
		return uuid();
	},

	isNullOrEmpty: function (text) {
		return text == null || text == '';
	},

	middleware: function (request, response, next) {
		response.sendSuccess = function (detail) {
			this.send({ status: Utils.success, detail });
		}.bind(response)
		response.sendError = function (detail) {
			this.send({ status: Utils.error, detail });
		}.bind(response)
		response.sendStatus = function (status, detail) {
			this.send({ status, detail });
		}.bind(response)
		next();
	},

};

module.exports = Utils;