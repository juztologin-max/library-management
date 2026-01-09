var buttonRequest;
var borrowReturnRequest;
var url = "";
var successText = "";
var failureText = "";
var method = "";
var simpleTable;
var currentCol = 1;
var bookId;

var coverPageData = null;
function getCoverPage() {
	const hiddenFileInput = document.getElementById("hidden-file-input");
	hiddenFileInput.click();

}



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
	const hiddenFileInput = document.getElementById("hidden-file-input");
	hiddenFileInput.addEventListener('change', (event) => {
		const list = event.target.files;
		const coverpage = document.getElementById("coverpage");
		const file = list[0];

		const fileReader = new FileReader();

		fileReader.onload = function(e) {


			coverPageData = e.target.result;
			coverpage.src = e.target.result;


			coverpage.onload = function() {
				const preImage = document.getElementById("preImage");
				const coverpageContainer = document.getElementById("coverpage-container");
				preImage.classList.add('d-none');
				coverpageContainer.classList.remove('d-none');
			};
		};


		fileReader.readAsDataURL(file);

	});

	const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
	const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
	const hKMap = new Map([
		['NAME', 'name'],
		['AUTHOR', 'author'],
		['PUBLISHER', 'publisher'],
		['PUBLISHED DATE', 'publishedAt'],
		['TOTAL', 'total'],
		['DESCRIPTION', 'description'],
	]);
	const inputTypes = new Map([
		['PUBLISHED DATE', 'date']


	]);

	let simpleTable = new SimpleTable("table-container1", "Books", hKMap, "api/manage-borrowing/list-books", csrfHeader, csrfValue, 0, 10, "NAME", "DSC", inputTypes, true);
	//simpleTable.addSortableColumn("NAME", "ASC");
	simpleTable.addEventListener("TakeFromTable", editHandler);
	//simpleTable.addEventListener("RemoveFromTable", deleteHandler);
	simpleTable.addEventListener("NewTable", () => {
		currentCol = 1;
		simpleTable.selectRow(1);
	});
	simpleTable.setSearchUrl("api/manage-borrowing/search-books");
	simpleTable.setCurrentColumns(["NAME", "AUTHOR", "PUBLISHER", "PUBLISHED DATE"]);
	simpleTable.setShowEdit(false);
	simpleTable.setShowDelete(false);

	document.addEventListener('keydown', (e) => {
		if (e.key === 'ArrowLeft') {
			currentCol--;
			if (currentCol < 1) {
				currentCol = simpleTable.getRowCount();
			}
			simpleTable.selectRow(currentCol);
		} else if (e.key === 'ArrowRight') {
			currentCol++;
			if (currentCol > simpleTable.getRowCount()) {
				currentCol = 1;
			}
			simpleTable.selectRow(currentCol);
		}


	});

}










function editHandler(event) {

	const data = event.detail;
	const name = document.getElementById("name");
	const author = document.getElementById("author");
	const publisher = document.getElementById("publisher");
	const publishedDate = document.getElementById("published-date");
	const total = document.getElementById("total");
	const description = document.getElementById("description");

	const coverpage = document.getElementById("coverpage");

	const preImage = document.getElementById("preImage");
	const coverpageContainer = document.getElementById("coverpage-container");

	buttonToggler(data.id);
	name.value = data.name;
	author.value = data.author;
	publisher.value = data.publisher;
	publishedDate.value = data.publishedAt;
	total.value = data.total;
	description.value = data.description;
	coverPageData = data.content;
	coverpage.src = URL.createObjectURL(new Blob([Uint8Array.fromBase64(data.content)], { type: 'application/octet-stream' }));
	preImage.classList.add('d-none');
	coverpageContainer.classList.remove('d-none');



}



document.addEventListener('DOMContentLoaded', manageBookInit);