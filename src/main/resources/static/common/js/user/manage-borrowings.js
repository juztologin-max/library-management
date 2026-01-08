var manageRequest;
var url = "api/manage-book/save";
var successText = "Inserted Book";
var failureText = "Could not insert Book";
var method = "POST";
var simpleTable;
var currentCol = 1;

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

	let simpleTable = new SimpleTable("table-container1", "Books", hKMap, "api/manage-borrowing/list-books", csrfHeader, csrfValue, 0, 10, "NAME", "DSC", inputTypes,true);
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
	const preImage = document.getElementById("preImage");
	const coverpageContainer = document.getElementById("coverpage-container");

	console.log(data);
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
	submit.removeAttribute("disabled");
	url = "api/manage-book/" + data.id;
	method = "PUT";
	card.innerText = "Borrow Book";
	successText = "Successfully borrowed Book";
	failureText = "Could not update Book";


}



document.addEventListener('DOMContentLoaded', manageBookInit);