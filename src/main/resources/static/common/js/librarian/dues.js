var buttonRequest;

var url = "";
var successText = "Accept Successful";
var failureText = "Accept Unsuccessful";
var method = "";


var bookId;







function manageBookInit() {
	const csrfHeader = document.querySelector('meta[name="crsf_header"]').getAttribute("content");
	const csrfValue = document.querySelector('meta[name="crsf_value"]').getAttribute("content");
	const hKMap = new Map([
		['BOOK', 'book'],
		['BORROWER', 'borrower'],
		['BORROW DATE', 'borrowDate'],
		['DUE', 'due']


	]);
	const hProjectionMap = null;
	const inputTypes = new Map([
		['BORROW DATE', 'datetime'],
	]);

	simpleTable = new SimpleTable("table-container1", "Dues", hKMap, "api/manage-dues/list-dues", csrfHeader, csrfValue, 0, 10, "BORROW DATE", "ASC", inputTypes, true, hProjectionMap, false);
	//simpleTable.addSortableColumn("NAME", "ASC");
	//simpleTable.addEventListener("TakeFromTable", editHandler);


	//simpleTable.setSearchUrl("api/manage-borrowing/search-borrowings");
	simpleTable.setCurrentColumns(["BOOK", "BORROWER", "BORROW DATE", "DUE"]);
	simpleTable.setShowEdit(false);
	simpleTable.setShowDelete(false);
	//simpleTable.setShowDelete(false);

}





async function generatePdf() {
	const hKMap = new Map([
		['BOOK', 'book'],
		['BORROWER', 'borrower'],
		['BORROW DATE', 'borrowDate'],
		['DUE', 'due']


	]);
	var source;
	customHeaders = {
		[document.querySelector('meta[name="crsf_header"]').getAttribute("content")]: document.querySelector('meta[name="crsf_value"]').getAttribute("content"),
		"Content-Type": "application/json"
	};

	try {



		sortables = new Map();
		sortables.set("borrowDate", "ASC");

		body = {
			"pageable": {
				"pageNo": 0,
				"pageSize": 10000,
				"sortable": Object.fromEntries(sortables)
			},

		};

		const resp = await fetch("api/manage-dues/list-dues", {
			method: "POST",
			headers: customHeaders,
			body: JSON.stringify(body)
		});

		if (!resp.ok) {
			throw new Error("Fetch error");
		}
		var jsonResp = await resp.json();
		totalPages = jsonResp.page.totalPages;
		source = jsonResp.content;


	} catch (e) {
		console.log(e);
	}



	const resp = await fetch(getBaseUrl() + "/api/pdf/create", {
		method: "POST",
		headers: customHeaders,
		body: JSON.stringify({
			title: "Dues",
			headers: Object.fromEntries(hKMap),
			rows: source
		})
	}).then(response=>response.blob())
	.then(blob=>{
		const url=window.URL.createObjectURL(blob);
		const a =document.createElement('a');
		a.href=url;
		a.download="dues.pdf";
		a.click();
		window.URL.revokeObjectURL(url);
		a.remove();
	}).catch(e=>console.error(e));
	
}




document.addEventListener('DOMContentLoaded', manageBookInit);