/**
 * 
 */
var manageRequest;
var url = "api/manage-user/save";
var successText = "Inserted User";
var failureText = "Could not insert User";
var method = "POST";
var simpleTable;

function saveLibrarian() {
	password = document.getElementsByName("password")[0];

	if (!usernameAvailable || (password.hasAttribute("required") && !validatePassword())) return;


	const passwordInput = document.getElementsByName("password")[0];
	const usernameInput = document.getElementsByName("username")[0];
	const enabled = document.getElementsByName("enabled-checkbox")[0];
	const legalName = document.getElementById("legalName");
	const email = document.getElementById("email");
	const phone = document.getElementById("phone");
	const address = document.getElementById("address");
	usernameAvailable = false;

	if (window.XMLHttpRequest) {
		manageRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		manageRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		manageRequest.onreadystatechange = saveAdminCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		payload = JSON.stringify({
			name: usernameInput.value,
			password: passwordInput.value,
			enabled: enabled.checked,
			legalName: legalName.value,
			email: email.value,
			phone: phone.value,
			address: address.value

		});
		manageRequest.open(method, url, true);
		manageRequest.setRequestHeader("Content-Type", "application/json")

		manageRequest.setRequestHeader(csrfHeader, csrfValue);

		manageRequest.responseType = 'json';
		manageRequest.send(payload);
	} catch (e) {
		console.log(e);
	}

	usernameInput.value = "";
	passwordInput.value = "";
	enabled.checked = true;
	legalName.value = "";
	email.value = "";
	phone.value = "";
	address.value = "";

}

function deleteLibrarianCallback() {
	if (manageRequest.readyState == 4) {
		var resp = manageRequest.response;
		simpleTable.showFirstPage();
		if (resp != null && resp['successfull'] == true) {

			showAlert("success", "Successfully deleted Admin", 'alert-placeholder');

		} else {
			showAlert("danger", "Could not delete Admin", 'alert-placeholder');
		}

	}
}

function deleteHandler(event) {
	data = event.detail;
	if (window.XMLHttpRequest) {
		manageRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		manageRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		manageRequest.onreadystatechange = deleteLibrarianCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		manageRequest.open("DELETE", "api/manage-user/" + data.id, true);


		manageRequest.setRequestHeader(csrfHeader, csrfValue);

		manageRequest.responseType = 'json';
		manageRequest.send();
	} catch (e) {
		console.log(e);
	}

}


function saveAdminCallback() {
	if (manageRequest.readyState == 4) {
		var resp = manageRequest.response;
		simpleTable.resetAndShowFirstPage();
		if (resp != null && resp['successfull'] == true) {
			showAlert("success", successText, 'alert-placeholder');

		} else {
			showAlert("danger", failureText, 'alert-placeholder');
		}

		username = document.getElementsByName("username")[0];
		password = document.getElementsByName("password")[0];
		enabled = document.getElementsByName("enabled-checkbox")[0];
		card = document.getElementsByName("input-card-title")[0];
		submit = document.getElementsByName("submit-button")[0];

		username.classList.remove("is-valid");
		username.classList.remove("is-invalid");
		password.classList.remove("is-valid");
		password.classList.remove("is-invalid");

		url = "api/manage-user/save";
		method = "POST";
		card.innerText = "Add User";
		successText = "Inserted User";
		failureText = "Could not insert User";
		setInitialName("");
		password.setAttribute("required", true);
	}


}



function editHandler(event) {
	const data = event.detail;
	const username = document.getElementsByName("username")[0];
	const password = document.getElementsByName("password")[0];
	const enabled = document.getElementsByName("enabled-checkbox")[0];
	const card = document.getElementsByName("input-card-title")[0];
	const submit = document.getElementsByName("submit-button")[0];
	const legalName = document.getElementById("legalName");
	const email = document.getElementById("email");
	const phone = document.getElementById("phone");
	const address = document.getElementById("address");

	console.log(data);
	username.value = data.loginUser.name;
	enabled.checked = data.loginUser.enabled;
	password.value = "";
	legalName.value = data.legalName;
	email.value = data.email;
	phone.value = data.phoneNo;
	address.value = data.address;
	username.classList.remove("is-valid");
	username.classList.remove("is-invalid");
	password.classList.remove("is-valid");
	password.classList.remove("is-invalid");
	submit.removeAttribute("disabled");
	url = "api/manage-user/" + data.id;
	method = "PUT";
	card.innerText = "Update User";
	successText = "Successfully updated User";
	failureText = "Could not update User";
	setInitialName(username.value);
	password.removeAttribute("required");
	validateUsername();

}

function deleteAdminCallback() {
	if (manageRequest.readyState == 4) {
		var resp = manageRequest.response;
		simpleTable.showFirstPage();
		if (resp != null && resp['successfull'] == true) {

			showAlert("success", "Successfully deleted Admin", 'alert-placeholder');

		} else {
			showAlert("danger", "Could not delete Admin", 'alert-placeholder');
		}

	}
}



function setInitialName(iName) {
	initialName = iName;
	usernameAvailable = true;
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
	var container = document.getElementById("password-container");
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
		element.classList.add("is-invalid");
		container.classList.add("is-invalid");
	} else {
		element.classList.remove("is-invalid");
		element.classList.add("is-valid");
		container.classList.add("is-valid");
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


function manageUserInit() {
	const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
	const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
	const hKMap = new Map([
		['USERNAME', 'loginUser.name'],
		['FULL NAME', 'legalName'],
		['PHONE NO', 'phoneNo'],
		['Address', 'address'],
		['EMAIL', 'email'],
		['ENABLED', 'loginUser.enabled'],
		['CREATOR', 'createdBy.name'],
		['UPDATER', 'updatedBy.name'],
		['CREATED ON', 'createdAt'],
		['UPDATED ON', 'updatedAt']

	]);
	const inputTypes = new Map([
		['ENABLED', 'checkbox'],
		['UPDATED ON', 'datetime']
	]);

	simpleTable = new SimpleTable("table-container1", "Existing Librarians", hKMap, "api/manage-user/list", csrfHeader, csrfValue, 0, 10, "FULL NAME", "DSC", inputTypes);
	//simpleTable.addSortableColumn("NAME", "ASC");
	simpleTable.addEventListener("TakeFromTable", editHandler);
	simpleTable.addEventListener("RemoveFromTable", deleteHandler);
	simpleTable.setSearchUrl("api/manage-user/search");
	simpleTable.setCurrentColumns(["USERNAME", "FULL NAME", "UPDATED ON", "UPDATER"]);
}

document.addEventListener('DOMContentLoaded', manageUserInit);