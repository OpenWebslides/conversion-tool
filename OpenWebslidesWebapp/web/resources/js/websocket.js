/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global shared_vars */

var shared_vars = {
                socket: new WebSocket("ws://localhost:8080/OpenWebslidesWebapp/pipe"),
                filesInProgress: [],
                downloadAllPtr: 0
            };
        

shared_vars.socket.onopen = function (event) {
    shared_vars.socket.send("This is a client registering");
};

shared_vars.socket.onmessage = function (event) {
    var msg = JSON.parse(event.data);
    // dynamically add property to socket
    shared_vars.socket.WSSessionToken = msg.WSSessionToken;        
    var targetId; // = btnFormDownloadToShow
    $.each(shared_vars.filesInProgress, function (k, v) {
        if (msg.fileName === shared_vars.filesInProgress[k]) {
            targetId = k;
            $("#download-loading-anim-" + targetId).hide(500, function () {
                if(msg.action === "download-ready")  {
                    $("#download-form-" + targetId + "-btn").show(1000);                    
                    if($("#download-form-all-btn").not(':visible')) $("#download-form-all-btn").attr({onclick:"startDownload(-1)"}).show(1000);
                }                
                else if(msg.action ==="download-not-ready") $("#download-form-"+targetId+"-btn").removeClass('btn-primary').addClass('btn-danger').prop('disabled',true).show(1000);
            });
        }
    }
    ); 
};

shared_vars.socket.onerror = function (e) {
    alert("An error occurred while opening the websocket, no functionality will be available");
};


$("#btn-convert").click(function () {
    shared_vars.socket.send("file-upload started");
});
