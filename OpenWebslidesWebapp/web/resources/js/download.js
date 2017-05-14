/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function startDownload(downloadFormNr) {
    if (downloadFormNr === -1) {
        for (i=shared_vars.downloadAllPtr; i< shared_vars.filesInProgress.length; i++) {
                if(!document.getElementById("download-form-"+i+"-btn").disabled){                    
                    setTimeout("startDownload("+i+")",16.667*i);
                }                
        }
        shared_vars.downloadAllPtr = shared_vars.filesInProgress.length;
        
    } else {
        $("#download-form-" + downloadFormNr + "-param-1").attr({
            name: "WSSessionToken",
            value: shared_vars.socket.WSSessionToken
        });        
        $("#download-form-" + downloadFormNr + "-param-2").attr({
            name: "fileName",
            value: $("#download-form-" + downloadFormNr + "-btn").text()
        });
        $("#download-form-" + downloadFormNr).submit();
    }   
}
