function SimpleTable(tableContainerName, title, hKMap, sourceURL, crsfKey, crsfValue, start, limit, sortColumn, sortDirection) {
	this.tableContainerName = tableContainerName;
	this.tableId = "table1";
	this.titleId = "title1";
	this.titleText = title;
	this.sourceURL = sourceURL;
	this.source = "{}";
	this.hKMap = hKMap;
	this.csrfKey = crsfKey;
	this.csrfValue = crsfValue;
	this.pageNo = start;
	this.limit = limit;
	this.initialSortColumn = sortColumn;
	this.initialSortDirection = sortDirection;
	this.sortables = new Map([[this.initialSortColumn, this.initialSortDirection]]);
	this.sortables.set("NAME", "DSC");
	this.ignoreSortColumns = ["EDIT", "DELETE"];
	const instance = this;
	if (document.readyState != 'loading') {
		this.fetchDataAndCreateTable();
	} else {
		document.addEventListener("DOMContentLoaded", () => {
			instance.fetchDataAndCreateTable();
		});
	}
	this.__eventTarget = new EventTarget();



}

SimpleTable.prototype.addEventListener = function(...args) {
	this.__eventTarget.addEventListener(...args);
}

SimpleTable.prototype.removeEventListener = function(...args) {
	this.__eventTarget.removeEventListener(...args);
}

SimpleTable.prototype.dispatchEvent = function(...args) {
	this.__eventTarget.dispatchEvent(...args);
}





SimpleTable.prototype.createTable = function() {

	this.container = document.getElementById(this.tableContainerName);
	this.container.innerHTML = "";
	this.container.classList.add("table-container", "mt-4");

	this.card = document.createElement("div");
	this.card.classList.add("card", "d-inline-block");

	responsiveContainer = document.createElement("div");
	responsiveContainer.classList.add("table-responsive");

	this.header = document.createElement("div");
	this.header.classList.add("card-header", "d-flex", "justify-content-between", "align-items-center");
	title = document.createElement("span");
	title.innerText = this.titleText;
	title.classList.add("fw-bold");
	this.header.appendChild(title);
	bContainer = document.createElement("div");
	bContainer.classList.add("btn-group");
	fBut = document.createElement("button");
	fBut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-arrow-left");
	fBut.addEventListener("click", () => {
		this.pageNo -= 1;
		this.fetchDataAndCreateTable();
	});
	if (this.pageNo == 0) {
		fBut.disabled = true;
	} else {
		fBut.disabled = false;
	}

	bContainer.appendChild(fBut);
	pBut = document.createElement("button");
	pBut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-arrow-right");
	pBut.addEventListener("click", () => {
		this.pageNo++;
		this.fetchDataAndCreateTable();

	});
	bContainer.appendChild(pBut);
	this.header.appendChild(bContainer);
	if (this.source.length < this.limit) {
		pBut.disabled = true;
	} else {
		pBut.disabled = false;
	}

	this.body = document.createElement("div");
	this.body.classList.add("card-body", "p-1");

	this.table = document.createElement("table");
	this.tableResponsiveContainer = document.createElement("div");
	this.tableResponsiveContainer.classList.add("table-responsive");
	this.tableResponsiveContainer.appendChild(this.table);
	this.table.classList.add("table", "table-hover", "mb-1", "table-sm", "table-striped", "text-center", "mx-auto");
	this.table.id = this.tableId;

	this.container.appendChild(this.card);
	this.card.appendChild(this.header);
	this.card.appendChild(this.body);
	this.body.appendChild(responsiveContainer);
	responsiveContainer.appendChild(this.table);

	thead = document.createElement("thead");
	headRow = document.createElement("tr");
	header = document.createElement("th")
	header.textContent = "#";
	headRow.appendChild(header);

	for (let columnHeader of this.hKMap.keys()) {

		header = document.createElement("th")
		if (this.sortables.has(columnHeader)) {
			header.classList.add("text-bg-primary", "text-nowrap");
			thContainer = document.createElement("div");
			thContainer.classList.add("d-flex", "justify-content-center", "align-items-center", "gap-2");
			thCheckBox = document.createElement("input");
			thCheckBox.classList.add("form-check-input");
			thCheckBox.type = "checkbox";
			thCheckBox.checked = true;
			thCheckBox.name = columnHeader + "checkbox";
			header.name = this.tableId + columnHeader;


			tempTh = document.createElement("span");
			//tempTh.classList.add("me-2");
			let checkContainer = document.createElement("div");
			checkContainer.classList.add("form-check", "mb-0");
			checkContainer.appendChild(thCheckBox);
			thContainer.appendChild(checkContainer);

			thCheckBox.addEventListener("change", (e) => {
				e.stopPropagation();
				this.sortables.delete(columnHeader);
				if (this.sortables.size == 0) {
					this.sortables.set(this.initialSortColumn, this.initialSortDirection);

				}
				
				checkContainer.remove();
				this.showFirstPage();
				

			});

			tempTh.textContent = columnHeader;
			thContainer.appendChild(tempTh);
			ic = document.createElement("i");
			if (this.sortables.get(columnHeader) == "ASC") {
				ic.classList.add("bi", "bi-arrow-up");
			} else {
				ic.classList.add("bi", "bi-arrow-down");
			}
			thContainer.appendChild(ic);
			header.appendChild(thContainer);
		}
		else {
			header.classList.remove("text-bg-primary");
			header.textContent = columnHeader;
		}

		headRow.appendChild(header);
		header.addEventListener("click", () => {
			if (this.ignoreSortColumns.includes(columnHeader)) {
				return;
			}
			let sortorder = "ASC";
			if (this.sortables.has(columnHeader)) {

				if (this.sortables.get(columnHeader) == "ASC") {
					sortorder = "DSC"
				}


			}
			this.sortables.set(columnHeader, sortorder);
			this.showFirstPage();
		});
	}
	header = document.createElement("th")
	header.textContent = "EDIT";
	headRow.appendChild(header);
	header = document.createElement("th")
	header.textContent = "DELETE";
	headRow.appendChild(header);
	thead.appendChild(headRow);

	this.table.appendChild(thead);

	tbody = document.createElement("tbody");


	for (let i = 0; i < this.source.length; i++) {

		row = this.source[i];
		bodyRow = document.createElement("tr");
		cell = document.createElement("td");
		cell.textContent = (this.pageNo * this.limit) + i + 1;
		cell.classList.add("text-nowrap");
		bodyRow.appendChild(cell);

		for (columnHeader of this.hKMap.keys()) {
			cell = document.createElement("td");
			cell.textContent = row[this.hKMap.get(columnHeader)];

			bodyRow.appendChild(cell);
		}

		var button = document.createElement("button");
		button.classList.add("btn", "btn-sm", "btn-primary");
		button.textContent = "Edit";

		button.addEventListener("click", () => {
			this.dispatchEvent(new CustomEvent("TakeFromTable", { detail: this.source[i] }));

		});
		cell = document.createElement("td");
		cell.appendChild(button);
		bodyRow.appendChild(cell);
		button = document.createElement("button");
		button.classList.add("btn", "btn-sm", "btn-danger");
		button.textContent = "Delete";
		button.addEventListener("click", () => {
			this.dispatchEvent(new CustomEvent("RemoveFromTable", { detail: this.source[i] }));

		});

		cell = document.createElement("td");
		cell.appendChild(button);
		bodyRow.appendChild(cell);
		tbody.appendChild(bodyRow);

	}
	this.table.appendChild(tbody);

}



SimpleTable.prototype.showFirstPage = function() {
	this.pageNo = 0;
	this.fetchDataAndCreateTable();
}


SimpleTable.prototype.fetchDataAndCreateTable = async function() {

	customHeaders = {};
	try {
		if (this.csrfKey != null) {
			customHeaders = {
				[this.csrfKey]: this.csrfValue,
				"Content-Type": "application/json"
			};
		}
		sortables = new Map();
		for ([key, value] of this.sortables) {
			sortables.set(this.hKMap.get(key), value);
		}
		console.log(sortables);
		body = {
			"start": this.pageNo,
			"limit": this.limit,
			"sortables": Object.fromEntries(sortables),

		};

		const resp = await fetch(this.sourceURL, {
			method: "POST",
			headers: customHeaders,
			body: JSON.stringify(body)
		});

		if (!resp.ok) {
			throw new Error("Fetch error");
		}
		this.source = await resp.json();

		this.createTable();
	} catch (e) {
		console.log(e);
	}
}



