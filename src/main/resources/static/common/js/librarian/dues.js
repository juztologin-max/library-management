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
	customHeaders = {
		[document.querySelector('meta[name="crsf_header"]').getAttribute("content")]: document.querySelector('meta[name="crsf_value"]').getAttribute("content"),
		"Content-Type": "application/json"
	};



	const resp2 = await fetch("api/manage-dues/pdf/create", {
		method: "GET",
		headers: customHeaders
	}).then(response => response.blob())
		.then(blob => {
			console.log(blob);
			const url = window.URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = url;
			a.download = "dues.pdf";
			a.click();
			window.URL.revokeObjectURL(url);
			a.remove();
		}).catch(e => console.error(e));

}




document.addEventListener('DOMContentLoaded', manageBookInit);