/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function startDownload(downloadFormNr){
    console.log("called startDownload voor "+downloadFormNr);

    $("#download-form-"+downloadFormNr+"-param-1").attr({        
        name: "WSSessionToken",
        value: shared_vars.socket.WSSessionToken
    });
    console.log($("#download-form-"+downloadFormNr+"-btn").text());
    $("#download-form-"+downloadFormNr+"-param-2").attr({
        name: "fileName",
        value: $("#download-form-"+downloadFormNr+"-btn").text()                
    });    
    $("#download-form-"+downloadFormNr).submit();

}
