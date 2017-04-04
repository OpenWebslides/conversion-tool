/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function startDownload(){
    console.log("called startDownload");

    $("#download-form-param-1").attr({        
        name: "WSSessionToken",
        value: shared_vars.socket.WSSessionToken
    });
    $("#download-form-param-2").attr({
        name: "fileName",
        value: shared_vars.filesReady.shift()
    });    
    $("#download-form").submit();

}
