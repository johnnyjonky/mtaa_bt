const port = 8080;
const fs = require('fs');
const mysql = require('mysql');
var express = require('express');
var app = express();
var cors = require('cors')

app.use(cors())

let connectionMade = false;
let connection = null;
function connectDB() {//pripojenie na db
    if(connectionMade===false) {
        connection = mysql.createConnection({
            host     : process.env.HOST,
            user     : process.env.USER,
            password : process.env.PASSWORD,
            database : process.env.NAME
        });
        connection.connect(err =>{
            if(!err) {
                connectionMade = true;
            }
        });
    }
}

//get request na zobrazenie zoznamu miest
app.get('/places/type/:placetype', function (req, res) {
    console.log('/places/type/'+req.params.placetype);
        connectDB();
        res.statusCode = 200; // HTTP OK
        res.setHeader('Content-Type', 'application/json');
        res.setHeader("Access-Control-Allow-Origin", "*");
        connection.query('SELECT name,shortDescription,photo,uniqueID FROM Places WHERE placeType ='+ req.params.placetype, function (error, results, fields) {
            if (error) {
                res.statusCode = 500;
                res.end('DB ERROR');
            }
            console.log(results);
            res.end(JSON.stringify({
                placetype: req.params.placetype,
                places: results
            }));
        });
});

//get request na zobrazenie konkretneho miesta
app.get('/places/data/:placeid', function (req, res) {
    console.log('/places/data/'+req.params.placeid);
        connectDB();
        res.statusCode = 200; // HTTP OK
        res.setHeader('Content-Type', 'application/json');
        res.setHeader("Access-Control-Allow-Origin", "*");
        connection.query('SELECT name,shortDescription,longDescription,photo,location,uniqueID FROM Places WHERE uniqueID ='+req.params.placeid, function (error, results, fields) {
            if (error) {
                res.statusCode = 500;
                res.end('DB ERROR');
            }
            console.log(results);
            res.end(JSON.stringify({
                place: results[0]
            }));
        });
});

//get request na zobrazenie recenzií
app.get('/places/reviews/:placeid', function (req, res) {
    console.log('/places/reviews/'+req.params.placeid);
        connectDB();
        res.statusCode = 200; // HTTP OK
        res.setHeader('Content-Type', 'application/json');
        res.setHeader("Access-Control-Allow-Origin", "*");
        connection.query('SELECT userUsername,reviewText,revPhoto,rating,reviewID,userUID FROM Reviews WHERE placeUID ='+req.params.placeid, function (error, results, fields) {
            if (error) {
                res.statusCode = 500;
                res.end('DB ERROR');
            }
            console.log(results);
            res.end(JSON.stringify({
                placeID: req.params.placeid,
                reviews: results
            }));
        });
});

app.listen(port, function () {
    console.log('Example app listening on port 3000.');
});
