/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global shared_vars */

shared_vars.socket.onopen = function (event) {
    shared_vars.socket.send("This is a client registering");
};

shared_vars.socket.onmessage = function (event) {
    console.log(event);
    var msg = JSON.parse(event.data);
    console.log(msg);
    // dynamically add property to socket
    shared_vars.socket.WSSessionToken = msg.WSSessionToken;
    console.log("message action = " + msg.action);
    var targetId; // = btnFormDownloadToShow
    console.log("looping looking for " + msg.fileName);
    $.each(shared_vars.filesInProgress, function (k, v) {
        console.log("**************");
        console.log(k, v);
        console.log(msg.fileName,shared_vars.filesInProgress[k]);
        console.log("--------------");
        if (msg.fileName === shared_vars.filesInProgress[k]) {
            targetId = k;
            console.log("loading to remove " + targetId);
            $("#download-loading-anim-" + targetId).hide(500, function () {
                $("#download-form-" + targetId + "-btn").show(1000);
            });
        }
    }
    );


    if (msg.action === "download-ready") {
        //shared_vars.filesReady.push(msg.fileName);
        //console.log(shared_vars.filesReady);
        //.css({opacity: 1.0, visibility: "visible"});
        //.animate({opacity: 1.0}, 200);
    }
};

shared_vars.socket.onerror = function (e) {
    alert("An error occured while connecting... " + e.data);
};


$("#btn-convert").click(function () {
    shared_vars.socket.send("file-upload started");
    console.log("file-upload started");
    console.log(shared_vars.socket);
});