/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


Dropzone.options.myAwesomeDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 10000, // MB
    acceptedFiles: "application/vnd.openxmlformats-officedocument.presentationml.presentation",
    addRemoveLinks: true,
    autoProcessQueue: false,
    parallelUploads: 8,
    maxFiles: 8,
    accept: function (file, done) {
        console.log("Accept");
        $('.dz-progress').hide();
        return done();
    },
    init: function () {
        var Dropzone = this;
        $("#btn-convert").click(function () {
            console.log("Clicked btn convert");
            $('.dz-progress').show();
            Dropzone.processQueue();
            console.log("Process Queue started");
        });
        $("#btn-cancel").click(function () {
            console.log("Clicked btn cancel");
            Dropzone.removeAllFiles(true);
        });
        Dropzone.on("sending", function (file, xhr, formData) {
            formData.append("output-type", $('input[name="output_type"]:checked').val());
            console.log("Sending");
        });
        Dropzone.on("error", function (file, message) {
            console.log("Error");
            if (!file.accepted)
                this.removeFile(file);
            $("#message_alert").text(message);
            $("#alert").attr("style", "");
            setTimeout(function () {
                $("#alert").fadeOut(700);
            }, 3000);
        });
    }
};


