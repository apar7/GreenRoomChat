var socket = new SockJS('/stomp');
var stompClient = Stomp.over(socket);
stompClient.connect({}, connectCallback);
var messageToSend = [];
var messageReceived = [];
var secondRow = false;
var messageContent;
var textbox = $('#textbox');
messageToSend[2] = "";

$.get("currentUser", function (data) {
    messageToSend[0] = data;
});

function sendMessage(event) {
    messageToSend[1] = $("input").val().trim();
    if (!(messageToSend[1] === "")) {
        ;
        stompClient.send("/app/send", {}, JSON.stringify(messageToSend));
        $("input").val("");
        $('button').animate({backgroundColor: '#adff2f'}, 200).animate({backgroundColor: '#9acd32'}, 200);
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    messageReceived = JSON.parse(payload.body);
    messageContent = $(document.createTextNode(messageReceived[1]));
    if (secondRow) {
        $("#textbox").append("<div class=\"firstRow messageRow\"></div>");
        secondRow = false;
    }
    else {
        $("#textbox").append("<div class=\"secondRow messageRow\"></div>");
        secondRow = true;
    }

    $(".messageRow").last().append("<span class=\"time\">" + messageReceived[2] + "</span>" +
        "<span class=\"nickname\">" + messageReceived[0] + "</span>" +
        "<span class=\"messageContent\"> </span></div>");

    messageContent.appendTo($(".messageContent").last());
    textbox.scrollTop(textbox[0].scrollHeight);
}

function connectCallback(event) {
    stompClient.subscribe('/topic/public', onMessageReceived);
    event.preventDefault();
}

document.querySelector('#messageBox').addEventListener('submit', sendMessage, true);

$('input').focus().hover(
    function () {
        $('#messageBoxContainer').animate({backgroundColor: '#ffffff'}, 450)
    }, function () {
        $('#messageBoxContainer').animate({backgroundColor: '#9acd32'}, 450)
    });

$('button').hover(function () {
    $(this).animate({backgroundColor: '#adff2f'}, 300)
}, function () {
    $(this).animate({backgroundColor: '#9acd32'}, 300)
});


var $scrollbar = $(".scrollbar"),
    height,
    scrollHeight,
    barHeight,
    $scrollable;

adjustScrollbar();

function adjustScrollbar() {
    $scrollable = $(".scrollable");
    height = $scrollable.outerHeight(true);
    scrollHeight = $scrollable.prop("scrollHeight");
    barHeight = height * height / scrollHeight;
}


$scrollbar.hover(function () {
    $(this).animate({backgroundColor: '#9acd32'}, 50)
}, function () {
    $(this).animate({backgroundColor: '#adff2f'}, 50)
});

$scrollbar.height(barHeight).draggable({
    axis: "y",
    containment: "parent",
    drag: function (e, ui) {
        adjustScrollbar();
        $scrollable.scrollTop(scrollHeight / height * ui.position.top);
    }
});

// Element scroll:
$scrollable.on("scroll", function () {
    adjustScrollbar();
    $scrollbar.css({top: $scrollable.scrollTop() / height * barHeight});
});