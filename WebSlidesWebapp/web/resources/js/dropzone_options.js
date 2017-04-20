/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



var dropzone = Dropzone.options.myAwesomeDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 10000, // MB
    acceptedFiles: "application/vnd.openxmlformats-officedocument.presentationml.presentation,application/pdf",
    addRemoveLinks: true,
    autoProcessQueue: false,
    parallelUploads: 8,
    maxFiles: 8,
    accept: function (file, done) {
        //console.log("Accept");
        $('.dz-progress').hide();
        return done();
    },
    init: function () {

        setLanguage();

        var Dropzone = this;
        $("#btn-convert").click(function () {
            //console.log("Clicked btn convert");
            $('.dz-progress').show();
            Dropzone.processQueue();
            //console.log("Process Queue started");
        });
        $("#btn-cancel").click(function () {
            //console.log("Clicked btn cancel");
            Dropzone.removeAllFiles(true);
        });
        Dropzone.on("sending", function (file, xhr, formData) {
            //console.log(xhr);
            formData.append("output-type", $('input[name="output_type"]:checked').val());
            //console.log("Sending");
        });
        Dropzone.on("error", function (file, message) {
            //console.log("Error");
            if (!file.accepted)
                this.removeFile(file);
            $("#message_alert").text(message);
            $("#alert").attr("style", "");
            setTimeout(function () {
                $("#alert").fadeOut(700);
            }, 3000);
        });
        
        Dropzone.on("success",function(file){
           shared_vars.socket.send("File successfully uploaded"+file.toString());           
           shared_vars.socket.send(JSON.stringify({"msgType":"FILE","name":file.name,"timestamp":file.lastModified,"fileType":file.type,"outputType":$("input[name='outputType']:checked").val()}));
           
           
           $("#field-download").append(
                $("<form></form>", {action: "download",id: "download-form-"+shared_vars.filesInProgress.length}).removeClass().addClass("col-xs-3").prop({'target':'_blank'})
                .append($("<img />").attr({src:"resources/images/loading_blue.gif",alt:"loading animation", id:"download-loading-anim-"+shared_vars.filesInProgress.length}))
                .append($("<input />").attr({type:"hidden", id:"download-form-"+shared_vars.filesInProgress.length+"-param-1"}))
                .append($("<input />").attr({type:"hidden", id:"download-form-"+shared_vars.filesInProgress.length+"-param-2"}))
                .append($("<button >"+file.name+"</button>").attr({type:"button",onclick:"startDownload("+shared_vars.filesInProgress.length+")",id:"download-form-"+shared_vars.filesInProgress.length+"-btn"}).addClass("btn btn-primary").hide())           
           );          
   
           shared_vars.filesInProgress.push(file.name);
           console.log(file);
           console.log("success");
        });
    
    }
};


$(document).ready(function () {
    $('.language').click(function (e) {
        //console.log($(e.target).html());
        localStorage["language"] = $(e.target).html();
        setLanguage();
    });
});


function setLanguage() {
    $.ajax({
        method: "GET",
        url: "LanguageServlet",
        contentType: 'json',
        dataType: "text",
        data: {language: localStorage["language"]},
        success: function (result, status, xhr) {
            //console.log(result);
            var dict = JSON.parse(result);
            $(".dz-default, .dz-message").html(dict["dictDefaultMessage"]);
            Dropzone.options.dictDefaultMessage = dict["dictDefaultMessage"];
            Dropzone.options.dictFallbackMessage = dict["dictFallbackMessage"];
            Dropzone.options.dictFallbackText = dict["dictFallbackText"];
            Dropzone.options.dictFileTooBig = dict["dictFileTooBig"];
            Dropzone.options.dictInvalidFileType = dict["dictInvalidFileType"];
            Dropzone.options.dictResponseError = dict["dictResponseError"];
            Dropzone.options.dictCancelUpload = dict["dictCancelUpload"];
            Dropzone.options.dictCancelUploadConfirmation = dict["dictCancelUploadConfirmation"];
            Dropzone.options.dictRemoveFile = dict["dictRemoveFile"];
            Dropzone.options.dictMaxFilesExceeded = dict["dictMaxFilesExceeded"];

            $(".header-title").html(dict["header-title"]);
            $(".header-subtext").html(dict["header-subtext"]);
            $("#btn-convert").html(dict["btn-convert"]);
            $("#btn-cancel").html(dict["btn-cancel"]);
            $("#btn-options").html(dict["btn-options"]);
            $("#logo-ugent").attr("alt", dict["logo-alt"]);
            $("#logo-ugent").attr("src", dict["logo-src"]);

            $("#output-type").html(dict["output-type"] + ": ");

            $(".language").removeClass("language-selected");
            $("#lang-" + dict["lang"]).addClass("language-selected");
        },
        error: function (xhr, status, error) {
            console.log(error);
        }
    });
}

