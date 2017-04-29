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
    
    if(downloadFormNr === -1){        
        var names;
        $.each([shared_vars.filesInProgress],function(index,value){
            names += $("#download-form-"+index+"-btn").text().append(';');
        });
        console.log("filling the hidden field download-form-all-param-2 with "+names);
     $("#download-form-all").attr({
        name: "fileNames",
        value: names
     });
    }    
    else{
    $("#download-form-"+downloadFormNr+"-param-2").attr({
        name: "fileName",
        value: $("#download-form-"+downloadFormNr+"-btn").text()                
    });
    }
    $("#download-form-"+downloadFormNr).submit();

}
