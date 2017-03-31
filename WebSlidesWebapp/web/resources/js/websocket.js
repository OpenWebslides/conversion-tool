/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function onOpen(event){
    shared_vars.socket.send("This is a client registering");
    alert("a websocket was opened "+event);
}

function onMessage(event){
    console.log(event);
    var msg = JSON.parse(event.data);
    if(msg.action==="download ready"){
        $('#field-download').css({opacity: 1.0, visibility: "visible"}).animate({opacity: 0}, 200);
    }
}


$("#btn-convert").click(function(){
    shared_vars.socket.send("file-upload started");
    console.log("file-upload started");
    console.log(shared_vars.socket);
});

$("#download-form").submit(function(){
    shared_vars.socket.send("file-download started");
    console.log("file-download started");    
});






