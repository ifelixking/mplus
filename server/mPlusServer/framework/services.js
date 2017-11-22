var db = require("../framework/db-mysql.js")
var utils = require("../framework/utils.js")

var Services = {

	login: function (mobile, device, password, func) {
		if (utils.isNullOrEmpty(mobile)) { func(utils.error, 'mobile is invalid'); return; }
		if (utils.isNullOrEmpty(device)) { func(utils.error, 'device is invalid'); return; }
		db.query("select * from user where mobile='" + mobile + "'", (err, values, fields) => {
			if (err) { func(utils.error, err); return; }
			if (values.length == 1) {
				var user = values[0];
				if (user.password != null) {			// 有密码登录
					if (user.password == password) {
						this.startSession(user.id, func);		// 登录成功 创建 session	
					} else {
						func(utils.error, 'user or password error');
					}
				} else {								// 无密码登录
					if (user.device == device) {
						this.startSession(user.id, func);		// 登录成功 创建 session
					} else {
						func(utils.error, 'device not match');
					}
				}
			} else if (values.length == 0) {
				this.create(mobile, device, func);			// 用户未注册，创建用户
			} else {
				func(utils.error, 'match multi mobile');
			}
		});
	},

	create: function (mobile, device, func) {
		db.query("insert into user(mobile, device) values('" + mobile + "', '" + device + "')", (err, result) => {
			if (err) { func(utils.error, err); return; }
			var userID = result.insertId
			this.startSession(userID, func);			// 创建完成，直接创建 session
		})
	},

	startSession: function (userID, func) {
		var token = utils.newGuid();
		db.query("update user set token='" + token + "', expire=date_add(now(), interval 1 day) where id='" + userID + "'", (err, result) => {
			if (err) { func(utils.error, err); return; }
			func(utils.success, token);
		});
	},

	hasPwd: function (mobile, func) {
		db.query("select password from user where mobile='" + mobile + "'", (err, values, fields) => {
			if (err) { func(utils.error, err); return; }
			if (values.length == 0) {
				func(utils.success, "new user");
			} else if (values.length == 1) {
				if (values[0].password != null) {
					func(utils.success, "had pwd");
				} else {
					func(utils.success, "no pwd");
				}
			} else {
				func(utils.error, "match multi records");
			}
		});
	},

	search: function (func) {
		db.query("select id, if(isnull(nikeName), mobile, nikeName) as name from user", (err, values, fields) => {
			if (err) { func(utils.error, err); return; }
			func(utils.success, values);
		})
	},

	getUserByToken: function (token, func) {
		db.query("select id, mobile, nikeName, device from user where token='" + token + "' and expire>=now()", (err, values, fields) => {
			if (err) { func(utils.error, err); return; }
			if (values.length == 1) {
				func(utils.success, values[0]);
			} else if (values.length == 0) {
				func(utils.success, null);
			} else {
				func(utils.error, "reduplicated user access token");
			}
		});
	},

	addFriend: function (userID, friendUserID, func) {
		db.query("select id from rel_user where userID1='" + userID + "' and userID2='" + friendUserID + "' and idDelete=false", (err, values, fields) => {
			if (err) { func(utils.error, err); return; }
			if (values.length == 0) {
				db.query("insert into rel_user (userID1, userID2, createTime) values('" + userID + "', '" + friendUserID + "', now())", (err, result) => {
					if (err) { func(utils.error, err); return; }
					func(utils.success);
				});
			} else if (values.length == 1) {
				func(utils.error, "already friend");
			} else {
				func(utils.error, "reduplicated friend");
			}
		});
	},

	getFriends: function (userID, func) {
		db.query("select user.id, if(isnull(user.nikeName), user.mobile, user.nikeName) as name from user left join rel_user on user.id=rel_user.userID2 where rel_user.userID1='" + userID + "'", (err, values, fields) => {
			if (err) { func(utils.error, err); return; }
			func(utils.success, values);
		})
	},

	getMessages: function (userID, friendID, func) {
		db.query("select id, content, senderID, receiverID, createTime from msg where \
			(senderID='"+ userID + "' AND receiverID='" + friendID + "' AND isSenderDelete=0)\
			or (senderID='"+ friendID + "' AND receiverID='" + userID + "' AND isReceiverDelete=0)\
			order by createTime", (err, values, fields) => {
				if (err) { conn.rollback(() => { throw err; }); }
				func(utils.success, values);
			});
	},

	sendMessage: function (senderID, receiverID, msg, func) {
		db.query("insert into msg(content, senderID, receiverID, createTime) values('" + msg + "', '" + senderID + "', '" + receiverID + "', now())", (err, result) => {
			if (err) { conn.rollback(() => { throw err; }); }
			func(utils.success);
		})
	},
};

module.exports = Services;