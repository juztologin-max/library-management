/**
 * 
 */
var manageRequest;
var url = "api/manage-admin/save";
var successText = "Inserted Admin";
var failureText = "Could not insert Admin";
var method = "POST";

function saveAdmin() {
	password = document.getElementsByName("password")[0];

	if (!usernameAvailable || (password.hasAttribute("required") && !validatePassword())) return;


	const passwordInput = document.getElementsByName("password")[0];
	const usernameInput = document.getElementsByName("username")[0];
	const enabled = document.getElementsByName("enabled-checkbox")[0];
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
			enabled: enabled.checked
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
}


function showAlert(type, message, placeholder) {
	const alertPlaceholder = document.getElementById(placeholder);

	const divContainer = document.createElement('div')
	divContainer.innerHTML = `
		<div class="alert alert-${type} alert-dismissible  fade show" role="alert">
		   <div>${message}</div>
		   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>`;

	alertPlaceholder.append(divContainer)
}

function saveAdminCallback() {
	if (manageRequest.readyState == 4) {
		var resp = manageRequest.response;
		simpleTable.showFirstPage();
		if (resp != null && resp['successfull'] == true) {
			showAlert("success", successText, 'alert-placeholder');

		} else {
			showAlert("danger", failureText, 'alert-placeholder');
		}

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

	url = "api/manage-admin/save";
	method = "POST";
	card.innerText = "Add Admin";
	successText = "Inserted Admin";
	failureText = "Could not insert Admin";
	setInitialName("");
	password.setAttribute("required",true);

}

var simpleTable;

function editHandler(event) {
	data = event.detail;
	username = document.getElementsByName("username")[0];
	password = document.getElementsByName("password")[0];
	enabled = document.getElementsByName("enabled-checkbox")[0];
	card = document.getElementsByName("input-card-title")[0];
	submit = document.getElementsByName("submit-button")[0];
	username.value = data.name;
	enabled.checked = data.enabled;
	password.value = "";
	username.classList.remove("is-valid");
	username.classList.remove("is-invalid");
	password.classList.remove("is-valid");
	password.classList.remove("is-invalid");
	submit.removeAttribute("disabled");
	url = "api/manage-admin/" + data.id;
	method = "PUT";
	card.innerText = "Update Admin";
	successText = "Successfully updated Admin";
	failureText = "Could not update Admin";
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

function deleteHandler(event) {
	data = event.detail;
	if (window.XMLHttpRequest) {
		manageRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		manageRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		manageRequest.onreadystatechange = deleteAdminCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		manageRequest.open("DELETE", "api/manage-admin/" + data.id, true);


		manageRequest.setRequestHeader(csrfHeader, csrfValue);

		manageRequest.responseType = 'json';
		manageRequest.send();
	} catch (e) {
		console.log(e);
	}

}
function manageAdminInit() {
	const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
	const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
	const hKMap = new Map([
		['NAME', 'name'],
		['ENABLED', 'enabled']
	]);

	simpleTable = new SimpleTable("table-container1", "Existing Admins", hKMap, "api/manage-admin/list", csrfHeader, csrfValue, 0, 10, "ENABLED", "DSC");
	simpleTable.addSortableColumn("NAME","ASC");
	simpleTable.addEventListener("TakeFromTable", editHandler);
	simpleTable.addEventListener("RemoveFromTable", deleteHandler);

}

document.addEventListener('DOMContentLoaded', manageAdminInit);