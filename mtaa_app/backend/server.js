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

//post na vlozenie noveho miesta
app.post('/places/create/:placeid/:userid', function (req, res) {
    connectDB();
    console.log('POST received');
    req.on('data', function(data){
        let input = JSON.parse(data);
        res.setHeader('Content-Type', 'application/json');
        connection.query("INSERT INTO Places (name, shortDescription, longDescription, photo, placeType, location) VALUES \
        ('"+input.name+"','"+input.shortDescription+"','"+input.longDescription+"','"+input.photo+"','"+req.params.placetype+"','"+input.location+"');", 
        function (error, results) {
            if (error) {
                res.statusCode = 500;
                console.log(error)
                res.end(JSON.stringify({
                    'status': 'error',
                    'json': input
                }));
            }
            else{
                res.statusCode = 200;
                res.end(JSON.stringify({'status': 'ok'}));
            }
        });
    });
});

//post na vlozenie recenzií
app.post('/places/reviews/create/:placeid/:userid', function (req, res) {
    connectDB();
    console.log('POST received');
    req.on('data', function(data){
        let input = JSON.parse(data);
        res.setHeader('Content-Type', 'application/json');
        connection.query("INSERT INTO Reviews (placeUID, userUsername, reviewText, revPhoto, rating, userUID) VALUES \
        ('"+req.params.placeid+"','"+input.userUsername+"','"+input.reviewText+"','"+input.revPhoto+"','"+input.rating+"' ,'"+req.params.userid+"');", 
        function (error, results) {
            if (error) {
                res.statusCode = 500;
                console.log(error)
                res.end(JSON.stringify({
                    'status': 'error',
                    'json': input
                }));
            }
            else{
                res.statusCode = 200;
                res.end(JSON.stringify({'status': 'ok'}));
            }
        });
    });
});

//delete na vlozenie recenzií
app.delete('/places/reviews/delete/:reviewid/:userid', function (req, res) {
    connectDB();
    console.log('DELETE received');
    req.on('data', function(data){
        let input = JSON.parse(data);
        res.setHeader('Content-Type', 'application/json');
        connection.query('DELETE FROM Reviews WHERE reviewID = ' +req.params.reviewid, function (error, results) {
            if (error) {
                res.statusCode = 500;
                console.log(error)
                res.end(JSON.stringify({
                    'status': 'error',
                    'json': input
                }));
            }
            else{
                res.statusCode = 200;
                res.end(JSON.stringify({'status': 'ok'}));
            }
        });
    });
});

app.delete('/places/delete/:placeid/:userid', function (req, res) {
    connectDB();
    console.log('DELETE received');
    req.on('data', function(data){
        let input = JSON.parse(data);
        res.setHeader('Content-Type', 'application/json');
        res.setHeader("Access-Control-Allow-Origin", "*");
        connection.query('DELETE FROM Places WHERE uniqueID = ' +req.params.placeid, function (error, results) {
            if (error) {
                res.statusCode = 500;
                console.log(error)
                res.end(JSON.stringify({
                    'status': 'error',
                    'json': input
                }));
            }
            else{
                res.statusCode = 200;
                res.end(JSON.stringify({'status': 'ok'}));
            }
        });
    });
});

app.listen(port, function () {
    console.log('Example app listening on port 3000.');
});
