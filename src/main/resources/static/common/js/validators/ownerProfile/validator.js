

var emailAvailable = false;
var request;
var initialLegalName = "";
function checkAvailability(column,name) {

	columnAvailable = false;

	var element = document.getElementsByName(name)[0];
	if (initialName === element.value) {
		emailAvailable = true;
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

			const url = getPath() + "/common/insertabilityCheckers/login/checkUsernameInsertability.jsp"
			request.open("GET", url + "?username=" + element.value, true);
			request.responseType = 'json';
			request.send();
		} catch (e) {
			console.log(e);
		}
	}

}

function usernameCheckCallback() {
	var span = document.getElementsByName("username-span")[0];
	span.style.fontWeight = "bold";
	usernameAvailable = false;
	if (request.readyState == 4) {
		var resp = request.response;
		if (resp != null && resp.usernameAvailable === true) {
			usernameAvailable = true;
			span.style.color = "green";
			span.innerText = "🗸";
		} else {
			span.style.color = "red";
			span.innerText = "Unavailable";
		}
	}
}





function validatePassword() {
	let status = true;
	var text = "🗸";
	var span = document.getElementsByName("password-span")[0];

	//var span = document.login_form.password_span;
	var element = document.getElementsByName("password")[0];
	//var element = document.login_form.pasword;

	span.style.fontWeight = "bold";
	let rep = /^.{7,}$/;
	text = rep.test(element.value) ? text : (status = false, " Needs to be >6");
	rep = /^(?=.*[0-9]).*$/;
	text = rep.test(element.value) ? text : (status = false, " Needs a number");
	rep = /^(?=.*[#@$%^&!]).*$/;
	text = rep.test(element.value) ? text : (status = false, " Needs a special character");
	if (text === "🗸") {
		span.style.color = "green";
	} else {
		span.style.color = "red";
	}
	span.innerText = text;
	return status
}




function validateUsername() {
	let status = true;
	var text = "🗸";
	var span = document.getElementsByName("username-span")[0];
	//var span = document.login_formusername_span;
	//var element = document.login_formusername;
	var element = document.getElementsByName("username")[0];



	span.style.fontWeight = "bold";
	let rep = /^.{4,}$/;
	text = rep.test(element.value) ? text : (status = false, " Needs to be >3");
	rep = /^(?=.*[#@$%^&!]).*$/;
	text = rep.test(element.value) ? (status = false, " must not contains special characters") : text;
	rep = /^[^a-zA-Z].*$/;
	text = rep.test(element.value) ? (status = false, " start with an alphabet") : text;
	if (text === "🗸") {
		span.style.color = "green";
	} else {
		span.style.color = "red";
	}

	span.innerText = text;
	return status;
}

