var buttonRequest;

var url = "";
var successText = "Accept Successful";
var failureText = "Accept Unsuccessful";
var method = "";
var simpleTable;

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

	simpleTable = new SimpleTable("table-container1", "Dues", hKMap, "api/manage-dues/list-dues", csrfHeader, csrfValue, 0, 10, "BORROW DATE", "ASC", inputTypes, true,hProjectionMap);
	//simpleTable.addSortableColumn("NAME", "ASC");
	//simpleTable.addEventListener("TakeFromTable", editHandler);


	simpleTable.setSearchUrl("api/manage-borrowing/search-borrowings");
	simpleTable.setCurrentColumns(["BOOK", "BORROWER", "BORROW DATE", "DUE"]);
	simpleTable.setShowEdit(false);
	simpleTable.setShowDelete(false);
	//simpleTable.setShowDelete(false);
	
}













document.addEventListener('DOMContentLoaded', manageBookInit);