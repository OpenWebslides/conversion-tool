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
    error: function (file, message) {
        if (!file.accepted)
            this.removeFile(file);
        $("#message_alert").text(message);
        $("#alert").attr("style", "");
        setTimeout(function () {
            $("#alert").fadeOut(700);
        }, 3000);
    },
    accept: function (file, done) {
        $('.dz-progress').hide();
    },
    init: function () {
        var myDropzone = this;
        $("#btn-convert").click(function () {
            myDropzone.on("sending", function (file, xhr, formData) {
                formData.append("output-type", $('input[name="output_type"]:checked').val());
            });
            $('.dz-progress').show();
            myDropzone.processQueue();
        });
        $("#btn-cancel").click(function () {
            myDropzone.removeAllFiles(true);
        });
    }
};


