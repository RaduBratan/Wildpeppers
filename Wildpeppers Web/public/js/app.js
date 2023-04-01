const config = {
  apiKey: "ABCDE",
  authDomain: "???.firebaseapp.com",
  databaseURL: "???.firebaseio.com/",
  projectId: "???",
  storageBucket: "???.appspot.com",
  messagingSenderId: "ABCDE",
  appId: "ABCDE",
  measurementId: "ABCDE"
};

firebase.initializeApp(config);
firebase.analytics();
firebase.auth.Auth.Persistence.LOCAL;

/*
----------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------index.html------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
*/
$("#btn-login").click(function() {

  var email = $("#email").val();
  var password = $("#password").val();

  firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
    var errorCode = error.code;
    var errorMessage = error.message;

    console.log(errorCode);
    console.log(errorMessage);
  });
});

$(".show_password").click(function() {
  $(this).toggleClass("fa-eye fa-eye-slash");
  var input = $($(this).attr("toggle"));
  if (input.attr("type") == "password") {
    input.attr("type", "text");
  } else {
    input.attr("type", "password");
  }
});

$(function() {
  var x = 0;
  var y = 0;
  setInterval(function() {
    x += 1;
    y -= 1;
    $(".diagonal_scroll").css("background-position", x + "px " + y + "px");
  }, 10);
});

/*
----------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------admin.html------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
*/
$("#btn-logout").click(function() {
  firebase.auth().signOut();
});

function switchView(view) {
  $.get({
    url: view,
    cache: false,
  }).then(function(data) {
    $("#container").html(data);
  });
}
document.getElementById("default").click();

const indicator = document.querySelector(".navigation_indicator");
const items = document.querySelectorAll(".navigation_items");

function handleIndicator(el) {
  items.forEach((item) => {
    item.classList.remove("is-active");
    item.removeAttribute("style");
  });

  indicator.style.height = `${el.offsetHeight}px`;
  indicator.style.top = `${el.offsetTop}px`;
  indicator.style.backgroundColor = "#ecb8a5";

  el.classList.add("is-active");
  el.style.color = "#fff";
}

items.forEach((item, index) => {
  item.addEventListener("click", (e) => {
    handleIndicator(e.target);
  });
  if (item.classList.contains("is-active") && handleIndicator(item)) {
    item.classList.add(!"is-active");
  }
});