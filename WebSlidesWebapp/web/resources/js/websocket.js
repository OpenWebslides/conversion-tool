/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var socket = new WebSocket("ws://localhost:8080/WebSlidesWebapp/pipe");

socket.binaryType="arraybuffer";

function onOpen(event){
    socket.send("This is a client registering");
}

function onMessage(event){
    console.log(event);
}


$("#btn-convert").click(function(){
    socket.send("file-upload started");
    console.log("file-upload started");
});

$("#")






