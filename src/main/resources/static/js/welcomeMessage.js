$(function () {
    var number = 0 + Math.floor(Math.random() * 6);

    var messages = ["Broke it again?", "It was probably the new guy...", "Have you tried turning it off and on again?",
    "You will never find it!", "Numbers Numbers Numbers", "Having that brown feeling?"];

    $("#welcomeMessage").html(messages[number]);
    console.log(number);
})