/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// hide the download all button on page load
$("#download-form-all-btn").hide();

$("#collapse-options").hide();

$("#btn-options").click(function(){    
   if($("#collapse-options").is(":hidden")) $("#collapse-options").show(1000)    
   else $("#collapse-options:visible").hide(1000);
});

// hide the content of the manual
$("#field-manual-content").hide();

$("#field-manual-btn").click(function(){    
   if($("#field-manual-content").is(":hidden")) $("#field-manual-content").show(500)    
   else $("#field-manual-content:visible").hide(500);
});


var dropzone = Dropzone.options.myAwesomeDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 10000, // MB
    acceptedFiles: "application/vnd.openxmlformats-officedocument.presentationml.presentation,application/pdf",
    addRemoveLinks: true,
    autoProcessQueue: false,
    parallelUploads: 8,
    maxFiles: 8,
    accept: function (file, done) {        
        $('.dz-progress').hide();
        return done();
    },
    init: function () {
        dropzone = this;
        if (localStorage["language"] !== "en" || localStorage["language"] === null) {
            setLanguage();
        }

        $("#btn-convert").click(function () {
            //console.log("Clicked btn convert");
            $('.dz-progress').show();
            dropzone.processQueue();
            //console.log("Process Queue started");
        });
        $("#btn-cancel").click(function () {
            //console.log("Clicked btn cancel");
            dropzone.removeAllFiles(true);
        });
        dropzone.on("sending", function (file, xhr, formData) {
            //console.log(xhr);
            formData.append("output-type", $('input[name="output_type"]:checked').val());
            //console.log("Sending");
        });
        dropzone.on("error", function (file, message) {
            //console.log("Error");
            if (!file.accepted)
                this.removeFile(file);
            $("#message_alert").text(message);
            $("#alert").attr("style", "");
            setTimeout(function () {
                $("#alert").fadeOut(700);
            }, 3000);
        });
        
        dropzone.on("addedfile",function(file){
            console.log("added file to DROPZONE "+file);
            $("#btn-convert").removeAttr('disabled');
        });
        
        
        
        dropzone.on("success", function (file) {
            shared_vars.socket.send("File successfully uploaded" + file.toString());
            shared_vars.socket.send(JSON.stringify({"msgType": "FILE", "name": file.name, "timestamp": file.lastModified, "fileType": file.type, "outputType": $("input[name='outputType']:checked").val()}));           
            $("#downloads-placeholder").remove();

            $("#field-download").append(
                    $("<form></form>", {action: "download", id: "download-form-" + shared_vars.filesInProgress.length}).removeClass().addClass("col-xs-3").prop({'target': '_blank'+shared_vars.filesInProgress.length,'rel':'noopener noreferrer'})            
                    .append($("<img />").attr({src: "resources/images/loading_blue.gif", alt: "loading animation", id: "download-loading-anim-" + shared_vars.filesInProgress.length}))
                    .append($("<input />").attr({type: "hidden", id: "download-form-" + shared_vars.filesInProgress.length + "-param-1"}))
                    .append($("<input />").attr({type: "hidden", id: "download-form-" + shared_vars.filesInProgress.length + "-param-2"}))
                    .append($("<button >" + file.name + "</button>").attr({type: "button", onclick: "startDownload(" + shared_vars.filesInProgress.length + ")", id: "download-form-" + shared_vars.filesInProgress.length + "-btn"}).addClass("btn btn-primary btn-roomy").hide())
                    );            
            shared_vars.filesInProgress.push(file.name);
            console.log(file);
            console.log("current state of filesInProgress "+shared_vars.filesInProgress);
            console.log("success");
        });
    }
};


$(document).ready(function () {
    $('.language').click(function (e) {        
        if (localStorage["language"] !== $(e.target).html()) {
            setLanguage($(e.target).html());
        }
    });
});


function setLanguage(language) {
    $.ajax({
        method: "GET",
        url: "LanguageServlet",
        contentType: 'json',
        dataType: "text",
        data: {language: language, bundle: "Index"},
        success: function (result, status, xhr) {
            //console.log(result);
            var dict = JSON.parse(result);
            $(".dz-default, .dz-message").html(dict["dictDefaultMessage"]);
            $(".dz-remove").html(dict["dictRemoveFile"]);
            $(".dz-processing > .dz-remove").not($(".dz-complete > .dz-remove")).html(dict["dictCancelUpload"]);
            $("#alert").hide();
            dropzone.options.dictDefaultMessage = dict["dictDefaultMessage"];
            dropzone.options.dictFallbackMessage = dict["dictFallbackMessage"];
            dropzone.options.dictFallbackText = dict["dictFallbackText"];
            dropzone.options.dictFileTooBig = dict["dictFileTooBig"];
            dropzone.options.dictInvalidFileType = dict["dictInvalidFileType"];
            dropzone.options.dictResponseError = dict["dictResponseError"];
            dropzone.options.dictCancelUpload = dict["dictCancelUpload"];
            dropzone.options.dictCancelUploadConfirmation = dict["dictCancelUploadConfirmation"];
            dropzone.options.dictRemoveFile = dict["dictRemoveFile"];
            dropzone.options.dictMaxFilesExceeded = dict["dictMaxFilesExceeded"];

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
            localStorage["language"] = dict["lang"];

        },
        error: function (xhr, status, error) {
            console.log(error);
        }
    });
}

