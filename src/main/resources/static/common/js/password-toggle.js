/**
 * 
 */

function setInitialName(iName) {
	initialName = iName;
	usernameAvailable = true;
}

function passwordToggle() {
	const passwordInput = document.getElementsByName("password")[0];
	const eyeToggle = document.getElementsByName("icon-toggle")[0];
	passwordInput.setAttribute("type", passwordInput.getAttribute("type") == "text" ? "password" : "text")
	eyeToggle.classList.toggle('bi-eye-slash');
	eyeToggle.classList.toggle('bi-eye');
}



var usernameAvailable = false;
var request;
var initialName = "";
function checkUsernameAvailability(url) {

	usernameAvailable = false;

	var element = document.getElementsByName("username")[0];
	if (initialName === element.value) {
		usernameAvailable = true;
		validateUsername();
		return
	}
	if (validateUsername()) {
		if (window.XMLHttpRequest) {
			request = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			request = new ActiveXObject("Microsoft.XMLHTTP");
		}
		try {
			request.onreadystatechange = usernameCheckCallback;


			const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
			const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
			payload = JSON.stringify({
				name: element.value,
				password: ""
			});
			request.open("POST", url, true);
			request.setRequestHeader("Content-Type", "application/json")

			request.setRequestHeader(csrfHeader, csrfValue);

			request.responseType = 'json';
			request.send(payload);
		} catch (e) {
			console.log(e);
		}
	}
	var submit = document.getElementsByName("submit-button")[0];
	if (!usernameAvailable) {
		submit.setAttribute("disabled", true);
	}
	return usernameAvailable;
}

function usernameCheckCallback() {
	const username = document.getElementsByName("username")[0];
	const usernameFeedbackDiv = document.getElementById("username-feedback");
	usernameAvailable = false;
	if (request.readyState == 4) {
		var resp = request.response;
		if (resp != null && resp['is-name-available'] === true) {
			username.classList.remove("is-valid");
			username.classList.remove("is-invalid");
			username.classList.add("is-valid");
			usernameAvailable = true;
		} else {
			username.classList.remove("is-valid");
			username.classList.remove("is-invalid");
			username.classList.add("is-invalid");
			usernameFeedbackDiv.innerText = "Unavailable";
		}
		var submit = document.getElementsByName("submit-button")[0];
		var password = document.getElementsByName("password")[0];

		if (!usernameAvailable || (password.hasAttribute("required") && !validatePassword())) {
			submit.setAttribute("disabled", true);
		} else {
			submit.removeAttribute("disabled");
		}
	}
}



function validateUsername() {
	var valid = false;
	var text = "";

	var element = document.getElementsByName("username")[0];
	const usernameFeedbackDiv = document.getElementById("username-feedback");
	let rep = /^.{4,}$/;
	text = rep.test(element.value) ? text : "Too short";
	console.log(text);
	rep = /^(?=.*[#@$%^&!]).*$/;
	text = rep.test(element.value) ? " must not contains special characters" : text;
	rep = /^[^a-zA-Z].*$/;
	text = rep.test(element.value) ? " start with an alphabet" : text;
	usernameFeedbackDiv.innerText = text;
	if (text != "") {
		element.classList.remove("is-valid");
		element.classList.remove("is-invalid");
		element.classList.add("is-invalid");
	} else {
		element.classList.remove("is-valid");
		element.classList.remove("is-invalid");
		element.classList.add("is-valid");
		valid = true;
	}
	return valid;
}



function validatePassword() {
	let status = true;
	var text = "";
	const passwordFeedbackDiv = document.getElementById("password-feedback");

	//var span = document.login_form.password_span;
	var element = document.getElementsByName("password")[0];
	//var element = document.login_form.pasword;

	let rep = /^.{7,}$/;
	text = rep.test(element.value) ? text : (status = false, "Too short");
	rep = /^(?=.*[0-9]).*$/;
	text = rep.test(element.value) ? text : (status = false, " Needs a number");
	rep = /^(?=.*[#@$%^&!]).*$/;
	text = rep.test(element.value) ? text : (status = false, " Needs a special character");
	passwordFeedbackDiv.innerText = text;

	if (text != "") {
		element.classList.remove("is-valid");
		element.classList.remove("is-invalid");
		element.classList.add("is-invalid");
	} else {
		element.classList.remove("is-valid");
		element.classList.remove("is-invalid");
		element.classList.add("is-valid");
		status = true;
	}
	var submit = document.getElementsByName("submit-button")[0];
	var password = document.getElementsByName("password")[0];
	if (!usernameAvailable || (password.hasAttribute("required") && !status)) {
		submit.setAttribute("disabled", true);
	} else {
		submit.removeAttribute("disabled");
	}
	return status
}