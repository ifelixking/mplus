var mysql = require("mysql")

var pool = mysql.createPool({
	host: '127.0.0.1',
	port: 3306,
	user: 'root',
	password: '000000',
	database: 'mplus'
});

var query = function (sql, func) {
	pool.getConnection((error, conn) => {
		if (error) {
			func(error)
		} else {
			conn.query(sql, function (queryError, values, fields) {
				conn.release();
				func(queryError, values, fields);
			})
		}
	})
};

var start = function (func) {
	pool.getConnection((error, conn) => {
		if (error) { throw error; }
		conn.beginTransaction((error) => {
			if (error) { throw error; }
			func(null, conn);
		});

	})
}

module.exports = { query, start };