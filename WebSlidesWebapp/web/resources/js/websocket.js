/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var socket = new WebSocket("ws://localhost:8080/WebSlidesWebapp/pipe");

function onOpen(event){
    socket.send("This is a client registering");
}

function onMessage(event){
    console.log(event);
    var msg = JSON.parse(event.data);
    if(msg.action==="download ready"){
        $('#field-download').css({opacity: 1.0, visibility: "visible"}).animate({opacity: 0}, 200);
    }
}


$("#btn-convert").click(function(){
    socket.send("file-upload started");
    console.log("file-upload started");
});

$("#download-form").submit(function(){
    socket.send("file-download started");
    console.log("file-download started");    
});








