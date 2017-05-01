/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function startDownload(downloadFormNr) {
    console.log("called startDownload voor " + downloadFormNr);

    if (downloadFormNr === -1) {
        for(i = 0; i<shared_vars.filesInProgress.length;i++){
            $("#download-form-" + i + "-param-1").attr({
            name: "WSSessionToken",
            value: shared_vars.socket.WSSessionToken
        });
        console.log($("#download-form-" + i + "-btn").text());
        $("#download-form-" + i + "-param-2").attr({
            name: "fileName",
            value: $("#download-form-" + i + "-btn").text()
        });
            
            console.log("submitting download-form-"+i);
            $("#download-form-"+i).submit(); 
        }
    } else {
        $("#download-form-" + downloadFormNr + "-param-1").attr({
            name: "WSSessionToken",
            value: shared_vars.socket.WSSessionToken
        });
        console.log($("#download-form-" + downloadFormNr + "-btn").text());
        $("#download-form-" + downloadFormNr + "-param-2").attr({
            name: "fileName",
            value: $("#download-form-" + downloadFormNr + "-btn").text()
        });
    }
    $("#download-form-" + downloadFormNr).submit();

}
