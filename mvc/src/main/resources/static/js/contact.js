var Contact = {

    addMessage:function (message) {
        var alertTemplate = Handlebars.compile($("#template-alert-message").html());
        $jq("#message-holder").html(alertTemplate({message:message}));
        $jq("#alert-message").delay(5000).fadeOut(400, function() { $(this).remove(); })
    }
};

$(document).ready(function () {
    var feedbackMessage = $(".messageblock");
    if (feedbackMessage.length > 0) {
        Contact.addMessage(feedbackMessage.text());
    }
});




