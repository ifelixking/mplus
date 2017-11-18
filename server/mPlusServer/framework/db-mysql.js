var mysql = require("mysql")

var pool = mysql.createPool({
	host: '127.0.0.1',
	port: 3306,
	user: 'root',
	password: 'root',
	database: 'mplus'
});

var query = function(sql, func){
	pool.getConnection(function(error, conn){
		if (error){
			func(error)
		}else{
			conn.query(sql, function(queryError, values, fields){
				conn.release();
				func(queryError, values, fields);
			})
		}
	})
};

module.exports = { query };