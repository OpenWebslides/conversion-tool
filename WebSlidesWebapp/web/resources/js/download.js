/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function startDownload(downloadFormNr) {
    console.log("called startDownload voor " + downloadFormNr);

    if (downloadFormNr === -1) {
        for (i = 0; i < shared_vars.filesInProgress.length; i++) {
            //AJAX
//            $.ajax({url:"/WebSlidesWebapp/download?WSSessionToken="+shared_vars.socket.WSSessionToken+"&fileName="+shared_vars.filesInProgress[i],
//                type:"GET",
//                successs: function() {
//                    window.location = "/WebSlidesWebapp/download?WSSessionToken="+shared_vars.socket.WSSessionToken+"&fileName="+shared_vars.filesInProgress[i];
//                }            
//            });
//          //JS global window object
            //window.location.href="/WebSlidesWebapp/download?WSSessionToken="+shared_vars.socket.WSSessionToken+"&fileName="+shared_vars.filesInProgress[i];
            
            $("#download-form-"+i+"-btn").click();
        }
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
