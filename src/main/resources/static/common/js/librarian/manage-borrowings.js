var buttonRequest;
var borrowReturnRequest;
var url = "";
var successText = "";
var failureText = "";
var method = "";
var simpleTable;

var bookId;





function buttonClickCallback() {
	if (borrowReturnRequest.readyState == 4) {
		var resp = borrowReturnRequest.response;

		if (resp != null && resp['successfull'] == true) {

			showAlert("success", successText, 'alert-placeholder');

		} else {
			showAlert("danger", failureText, 'alert-placeholder');
		}

	}

}

function buttonClickHandler() {
	var button = document.getElementById("borrow-return-button");
	if (window.XMLHttpRequest) {
		borrowReturnRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		borrowReturnRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		borrowReturnRequest.onreadystatechange = buttonClickCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		console.log(coverPageData);

		borrowReturnRequest.open(method, url, true);
		borrowReturnRequest.setRequestHeader("Content-Type", "application/json")

		borrowReturnRequest.setRequestHeader(csrfHeader, csrfValue);

		borrowReturnRequest.responseType = 'json';
		borrowReturnRequest.send();
		button.setAttribute("disabled", true);
	} catch (e) {
		console.log(e);
	}

}

function buttonStateCallback() {
	if (buttonRequest.readyState == 4) {
		var button = document.getElementById("borrow-return-button");
		const card = document.getElementById("input-card-title");
		var resp = buttonRequest.response;

		if (resp != null && resp['successfull']) {
			button.classList.remove("d-none");
			button.removeAttribute("disabled");

			if (resp['isreturnable']) {
				card.innerText = "Return Book";
				button.innerText = "Return"
				url = "/user/api/manage-borrowing/return/" + bookId;
				successText = "Successfully added return Request";
				failureText = "Could not Add return Request";
				method = "GET";
			} else if (resp['isborrowable']) {
				card.innerText = "Borrow Book";
				button.innerText = "Borrow"
				url = "/user/api/manage-borrowing/borrow/" + bookId;
				successText = "Successfully added borrow Request";
				failureText = "Could not Add borrow Request";
				method = "GET";

			} else {
				bookId = "";
				card.innerText = "Borrow Book";
				button.innerText = "Borrow"
				button.setAttribute("disabled", true);
			}

		} else {

		}

	}

}

function buttonToggler(id) {
	bookId = "";
	if (window.XMLHttpRequest) {
		buttonRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		buttonRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		buttonRequest.onreadystatechange = buttonStateCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		console.log(coverPageData);
		bookId = id;
		buttonRequest.open("GET", "/user/api/manage-borrowing/is-borrowable-and-returnable/" + id, true);
		buttonRequest.setRequestHeader("Content-Type", "application/json")

		buttonRequest.setRequestHeader(csrfHeader, csrfValue);

		buttonRequest.responseType = 'json';
		buttonRequest.send();
	} catch (e) {
		console.log(e);
	}


}


function manageBookInit() {
	const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
	const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
	const hKMap = new Map([
		['BOOK','book.name'],
		['BORROWER','user.legalName'],
		['BORROW DATE', 'borrowDate'],
		['STATUS', 'status']
		
		
	]);
	const inputTypes = new Map([
		['BORROW DATE', 'datetime'],
	]);

	let simpleTable = new SimpleTable("table-container1", "Books", hKMap, "api/manage-borrowing/list-borrowings", csrfHeader, csrfValue, 0, 10, "STATUS", "DSC", inputTypes, true);
	//simpleTable.addSortableColumn("NAME", "ASC");
	//simpleTable.addEventListener("TakeFromTable", editHandler);
	//simpleTable.addEventListener("RemoveFromTable", deleteHandler);
	
	simpleTable.setSearchUrl("api/manage-borrowing/search-borrowings");
	simpleTable.setCurrentColumns(["BOOK","BORROWER","BORROW DATE","STATUS"]);
	simpleTable.setShowEdit(false);
	simpleTable.setShowDelete(false);
}













document.addEventListener('DOMContentLoaded', manageBookInit);