var buttonRequest;

var url = "";
var successText = "Accept Successful";
var failureText = "Accept Unsuccessful";
var method = "";
var simpleTable;

var bookId;





function acceptCallback() {
	if (buttonRequest.readyState == 4) {
		var resp = buttonRequest.response;

		if (resp != null && resp['successfull'] == true) {

			showAlert("success", successText, 'alert-placeholder');

		} else {
			showAlert("danger", failureText, 'alert-placeholder');
		}

	simpleTable.resetAndShowFirstPage();
	}

}




function acceptHandler(payload) {
	var data=payload.detail;
	if (window.XMLHttpRequest) {
		buttonRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		buttonRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		buttonRequest.onreadystatechange = acceptCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		
		buttonRequest.open("GET", "/librarian/api/manage-borrowing/accept/" + data.id, true);
		buttonRequest.setRequestHeader("Content-Type", "application/json")

		buttonRequest.setRequestHeader(csrfHeader, csrfValue);

		buttonRequest.responseType = 'json';
		buttonRequest.send();
	} catch (e) {
		console.log(e);
	}
}


function deleteCallback() {
	if (buttonRequest.readyState == 4) {
		var resp = buttonRequest.response;

		if (resp != null && resp['successfull'] == true) {

			showAlert("success", "Successfully Deleted Borrowing", 'alert-placeholder');

		} else {
			showAlert("danger", "Could not delete Borrowing", 'alert-placeholder');
		}

	simpleTable.resetAndShowFirstPage();
	}

}

function deleteHandler(payload) {
	var data=payload.detail;
	if (window.XMLHttpRequest) {
		buttonRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		buttonRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		buttonRequest.onreadystatechange = deleteCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		
		buttonRequest.open("GET", "/librarian/api/manage-borrowing/delete/" + data.id, true);
		buttonRequest.setRequestHeader("Content-Type", "application/json")

		buttonRequest.setRequestHeader(csrfHeader, csrfValue);

		buttonRequest.responseType = 'json';
		buttonRequest.send();
	} catch (e) {
		console.log(e);
	}
}

function buttonPredicate(payload){
	var data=payload.detail;
	
	if(data.status=="BORROW"||data.status=="RETURN"){
		return true;
	}
	return false;
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

	simpleTable = new SimpleTable("table-container1", "Books", hKMap, "api/manage-borrowing/list-borrowings", csrfHeader, csrfValue, 0, 10, "BORROW DATE", "ASC", inputTypes, true);
	//simpleTable.addSortableColumn("NAME", "ASC");
	//simpleTable.addEventListener("TakeFromTable", editHandler);
	
	
	simpleTable.setSearchUrl("api/manage-borrowing/search-borrowings");
	simpleTable.setCurrentColumns(["BOOK","BORROWER","BORROW DATE","STATUS"]);
	simpleTable.setShowEdit(false);
	simpleTable.addEventListener("RemoveFromTable", deleteHandler);
	//simpleTable.setShowDelete(false);
	simpleTable.setAlternateButton("Accept",acceptHandler,"ACTION",buttonPredicate);
}













document.addEventListener('DOMContentLoaded', manageBookInit);