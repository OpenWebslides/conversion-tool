/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


shared_vars.socket.onopen = function (event) {
    shared_vars.socket.send("This is a client registering");
    alert("a websocket was opened " + event);
}

shared_vars.socket.onmessage = function (event) {
    console.log(event);
    var msg = JSON.parse(event.data);
    console.log(msg);
    if (msg.action === "download-ready") {
        $('#field-download').css({visibility: "visible"}).animate({opacity:1.0},1500);
                //.css({opacity: 1.0, visibility: "visible"});
                //.animate({opacity: 1.0}, 200);
    }
}

shared_vars.socket.onerror = function (e) {
    alert("An error occured while connecting... " + e.data);
};


$("#btn-convert").click(function () {
    shared_vars.socket.send("file-upload started");
    console.log("file-upload started");
    console.log(shared_vars.socket);
});

$("#download-form").submit(function () {
    shared_vars.socket.send("file-download started");
    console.log("file-download started");
});






