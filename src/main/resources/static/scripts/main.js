$(document).ready(function () {

    var socket = new SockJS('/stomp');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, connectCallback, errorCallback);
    var messageToSend = [];
    messageToSend[0] = "";
    var messageReceived = [];
    var secondRow = false;
    var messageContent;
    var userListUpdate;
    var $userList = $('#userListContent');
    var $textInput = $('#textInput');
    var $textbox = $('#textbox');
    var urlPattern = /(\b(https?):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/gim;
    var emojiIcons = {
        1: "bear",
        2: "whale",
        3: "fish",
        4: "mouse",
        5: "wolf",
        6: "panda",
        7: "rhino",
        8: "hamster",
        9: "frog",
        10: "penguin",
        11: "chick",
        12: "turtle",
        13: "deer",
        14: "butterfly",
        15: "fox",
        16: "squirrel",
        17: "monkey",
        18: "watermelon",
        19: "mushroom",
        20: "rose",
        21: "chili",
        22: "cactus",
        23: "pinetree",
        24: "leaf",
        25: "heart",
        26: "christmas",
        27: "halloween",
        28: "sushi",
        29: "sandwich",
        30: "kiwi",
        31: "burger",
        32: "crossed",
        33: "strawberry",
        34: "ghost",
        35: "lips",
        36: "nope",
        37: "ok",
        38: "puke",
        39: "ups",
        40: "bj",
        41: "weep",
        42: "clench",
        43: "blush",
        44: "cry",
        45: "sad",
        46: "resigned",
        47: "tongue3",
        48: "tongue2",
        49: "tongue",
        50: "kiss",
        51: "smirk",
        52: "sunglasses",
        53: "lovinit",
        54: "sleep"
    };
    
    function errorCallback() {
        $userList.empty();
        $userList.append("<li class='serverError'>SERVER DOESN'T RESPOND.</li><li class='serverError'>TRY AGAIN LATER.</li>");
    }

    function outgoingMessage(event) {
        messageToSend[2] = $textInput.val().trim();
        if (messageToSend[2] !== "") {
            stompClient.send("/chat/send", {}, JSON.stringify(messageToSend));
            $textInput.val("");
            $('#send').animate({backgroundColor: '#adff2f'}, 200).animate({backgroundColor: '#9acd32'}, 200);
        }
        event.preventDefault();
    }

    function incomingMessage(payload) {
        messageReceived = JSON.parse(payload.body);
        messageContent = messageReceived[2].replace(urlPattern, '<a href="$1" target="_blank">$1</a>');
        if (secondRow) {
            $textScrollArea.append("<div class=\"firstRow messageRow\"></div>");
            secondRow = false;
        }
        else {
            $textScrollArea.append("<div class=\"secondRow messageRow\"></div>");
            secondRow = true;
        }

        $(".messageRow").last().append("<span class=\"time\">" + messageReceived[0] + "</span>" +
            "<span class=\"nickname\">" + messageReceived[1] + "</span>" +
            "<div class=\"messageContent\">" + messageContent + "</div></div>");

        $(".messageContent").last().emojiParse({
            icons: [{
                path: "img/emoji/",
                file: ".png",
                placeholder: ":{alias}:",
                alias: emojiIcons
            }]
        });

        $textbox.mCustomScrollbar("scrollTo", "bottom", {scrollInertia: 0});
    }

    function updateUserList(payload) {
        userListUpdate = JSON.parse(payload.body);
        if (userListUpdate.connecting) {
            $userList.append("<li id=\"a" + userListUpdate.username + "\">" + userListUpdate.username + "</li>");
        } else {
            $('#a' + userListUpdate.username).remove();
        }
    }

    function connectCallback() {
        stompClient.subscribe('/users/public', updateUserList);
        stompClient.subscribe('/messages/public', incomingMessage);
    }


    $textInput.emoji({
        button: "#btn1",
        showTab: false,
        animation: 'fade',
        position: 'topRight',
        icons: [{
            path: "img/emoji/",
            maxNum: 54,
            file: ".png",
            placeholder: ":{alias}:",
            alias: emojiIcons
        }]
    });

    $('#messageBox').submit(outgoingMessage);

    $textInput.focus().hover(
        function () {
            $(this).animate({borderColor: '#ffffff'}, 300)
        }, function () {
            $(this).animate({borderColor: '#9acd32'}, 300)
        });

    $('button').hover(function () {
        $(this).animate({backgroundColor: '#adff2f'}, 300)
    }, function () {
        $(this).animate({backgroundColor: '#9acd32'}, 300)
    });

    $.get("currentUser", function (data) {
        messageToSend[1] = data;
        $userList.append("<li><i>" + data + "</i></li>");
    });

    $.get("userList", function (data) {
        $.each(data, function (index, value) {
            $userList.append("<li>" + value + "</li>");
        });
    });

    $textbox.mCustomScrollbar({
        autoHideScrollbar: true,
        theme: "rounded-dark"
    });

    var $textScrollArea = $("#mCSB_2_container");

    $("#userList").mCustomScrollbar({
        autoHideScrollbar: true,
        theme: "rounded-dark"
    });

});