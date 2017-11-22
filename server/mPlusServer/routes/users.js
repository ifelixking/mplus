var express = require('express');
var router = express.Router();
var service = require('../framework/services')
var utils = require('../framework/utils')

router.post('/login', (req, res, next) => {
	var mobile = req.body.mobile;
	var device = req.body.device;
	var password = req.body.password;
	service.login(mobile, device, password, res.sendStatus);

})

router.get('/hasPwd/:mobile', (req, res, next) => {
	var mobile = req.params.mobile;
	service.hasPwd(mobile, res.sendStatus);
})

router.get('/search', (req, res, next) => {
	service.search(res.sendStatus);
})

router.post('/addFriend', (req, res, next) => {
	var friendUserID = req.query.userID;
	service.getUserByToken(req.headers['access-token'], (status, user) => {
		if (!utils.isSuccess(status)) { res.sendError(user); return; }
		if (user == null) { res.sendError("not login"); return; }
		service.addFriend(user.id, friendUserID, res.sendStatus);
	})
})

router.get('/friends', (req, res, next) => {
	service.getUserByToken(req.headers['access-token'], (status, user) => {
		if (!utils.isSuccess(status)) { res.sendError(user); return; }
		if (user == null) { res.sendError("not login"); return; }
		service.getFriends(user.id, res.sendStatus);
	})
})

router.get('/messages/:friendID', (req, res, next) => {
	var friendID = req.params.friendID;
	service.getUserByToken(req.headers['access-token'], (status, user) => {
		if (!utils.isSuccess(status)) { res.sendError(user); return; }
		if (user == null) { res.sendError("not login"); return; }
		service.getMessages(user.id, friendID);
	})	
})


module.exports = router;