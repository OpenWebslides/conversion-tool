/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    if (localStorage["language"] !== "en") {
        setLanguage();
    }
});

function setLanguage() {
    $.ajax({
        method: "GET",
        url: "LanguageServlet",
        contentType: 'json',
        dataType: "text",
        data: {language: localStorage["language"], bundle: "404"},
        success: function (result, status, xhr) {
            //console.log(result);
            var dict = JSON.parse(result);

            $(".header-title").html(dict["header-title"]);
            $("#header-subtext").html(dict["header-subtext"]);
            $("#logo-ugent").attr("alt", dict["logo-alt"]);
            $("#logo-ugent").attr("src", dict["logo-src"]);

            $("#message-404").html(dict["message"]);
            $("#redirect").html(dict["redirect-front"]);

        },
        error: function (xhr, status, error) {
            console.log(error);
        }
    });
}