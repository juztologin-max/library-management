var manageRequest;
var url = "api/manage-book/save";
var successText = "Inserted Book";
var failureText = "Could not insert Book";
var method = "POST";
var simpleTable;

var coverPageData = null;
function getCoverPage() {
	const hiddenFileInput = document.getElementById("hidden-file-input");
	hiddenFileInput.click();

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

	simpleTable = new SimpleTable("table-container1", "Existing Books", hKMap, "api/manage-book/list", csrfHeader, csrfValue, 0, 10, "NAME", "DSC", inputTypes);
	//simpleTable.addSortableColumn("NAME", "ASC");
	simpleTable.addEventListener("TakeFromTable", editHandler);
	simpleTable.addEventListener("RemoveFromTable", deleteHandler);
	simpleTable.setSearchUrl("api/manage-book/search");
	simpleTable.setCurrentColumns(["NAME", "AUTHOR", "PUBLISHER", "PUBLISHED DATE"]);
}




function saveBook() {
	const name = document.getElementById("name");
	const author = document.getElementById("author");
	const publisher = document.getElementById("publisher");
	const publishedDate = document.getElementById("published-date");
	const total = document.getElementById("total");
	const description = document.getElementById("description");


	if (window.XMLHttpRequest) {
		manageRequest = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		manageRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	try {
		manageRequest.onreadystatechange = saveBookCallback;


		const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
		const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
		console.log(coverPageData);
		payload = JSON.stringify({
			name: name.value,
			author: author.value,
			publisher: publisher.value,
			publishedDate: publishedDate.value,
			total: total.value,
			description: description.value,
			content: coverPageData
		});
		manageRequest.open(method, url, true);
		manageRequest.setRequestHeader("Content-Type", "application/json")

		manageRequest.setRequestHeader(csrfHeader, csrfValue);

		manageRequest.responseType = 'json';
		manageRequest.send(payload);
	} catch (e) {
		console.log(e);
	}


	name.value = "";
	author.value = "";
	publisher.value = "";
	publishedDate.value = "";
	total.value = "";
	description.value = "";
}

function saveBookCallback() {
	if (manageRequest.readyState == 4) {
		var resp = manageRequest.response;
		var card = document.getElementById("input-card-title");
		//simpleTable.showFirstPage();
		if (resp != null && resp['successfull'] == true) {
			showAlert("success", successText, 'alert-placeholder');

		} else {
			showAlert("danger", failureText, 'alert-placeholder');
		}


		url = "api/manage-book/save";
		method = "POST";
		card.innerText = "Add Book";
		successText = "Inserted Book";
		failureText = "Could not insert book";

	}


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

function editHandler(event) {
	const data = event.detail;
	const name = document.getElementById("name");
	const author = document.getElementById("author");
	const publisher = document.getElementById("publisher");
	const publishedDate = document.getElementById("published-date");
	const total = document.getElementById("total");
	const description = document.getElementById("description");
	const submit = document.getElementById("submit-button");
	const coverpage = document.getElementById("coverpage");
	const card = document.getElementById("input-card-title");
	console.log(data);
	name.value = data.name;
	author.value = data.author;
	publisher.value = data.publisher;
	publishedDate.value = data.publishedDate;
	total.value = data.total;
	description.value = data.description;
	coverPageData = Uint8Array.fromBase64(data.content);
	coverpage.src = URL.createObjectURL(new Blob([coverPageData], { type: 'application/octet-stream' }));
	const preImage = document.getElementById("preImage");
	const coverpageContainer = document.getElementById("coverpage-container");
	preImage.classList.add('d-none');
	coverpageContainer.classList.remove('d-none');
	submit.removeAttribute("disabled");
	url = "api/manage-book/" + data.id;
	method = "PUT";
	card.innerText = "Update Book";
	successText = "Successfully updated Book";
	failureText = "Could not update Book";


}



document.addEventListener('DOMContentLoaded', manageBookInit);