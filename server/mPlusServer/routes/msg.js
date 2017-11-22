var express = require('express');
var router = express.Router();
var service = require('../framework/services')
var utils = require('../framework/utils')

router.post('/:friendID', (req, res, next) => {
	var msg = req.body.msg;
	service.getUserByToken(req.headers['access-token'], (status, user) => {
		if (!utils.isSuccess(status)) { res.sendError(user); return; }
		if (user == null) { res.sendError("not login"); return; }
		service.sendMessage(user.id, req.params.friendID, msg, () => {
			res.sendSuccess();
		});
	})
})

router.get('/:friendID', (req, res, next) => {
	service.getUserByToken(req.headers['access-token'], (status, user) => {
		if (!utils.isSuccess(status)) { res.sendError(user); return; }
		if (user == null) { res.sendError("not login"); return; }
		service.getMessages(user.id, req.params.friendID, (status, msgs) => {
			res.sendSuccess(msgs)
		});
	})
})

module.exports = router;