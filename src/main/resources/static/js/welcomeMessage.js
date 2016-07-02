$(function () {

    var messages = ["Broke it again?", "It was probably the new guy...", "Have you tried turning it off and on again?",
    "Having that brown feeling?"];

    var number = 0 + Math.floor(Math.random() * messages.length);

    $("#welcomeMessage").html(messages[number]);
    console.log(number);
})