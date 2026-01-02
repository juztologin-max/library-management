function SimpleTable(tableContainerName, title, hKMap, sourceURL, crsfKey, crsfValue, start, limit, sortColumn, sortDirection, inputTypes) {
	this.tableContainerName = tableContainerName;
	this.tableId = "table1";
	this.titleText = title;
	this.sourceURL = sourceURL;
	this.searchURL;
	this.source = "{}";
	this.hKMap = hKMap;
	this.csrfKey = crsfKey;
	this.csrfValue = crsfValue;
	this.pageNo = start;
	this.limit = limit;
	this.initialSortColumn = sortColumn;
	this.initialSortDirection = sortDirection;
	this.sortables = new Map([[this.initialSortColumn, this.initialSortDirection]]);
	this.searchEnabled = false;
	this.totalPages = 0;
	this.saveStore = new Map();
	this.inputTypes = inputTypes;



	this.ignoreSearchColumns = ["EDIT", "DELETE"];
	this.ignoreSortColumns = ["EDIT", "DELETE"];

	this.mode = "VIEW";
	const instance = this;
	if (document.readyState != 'loading') {
		this.fetchDataAndCreateOrUpdateTable();
	} else {
		document.addEventListener("DOMContentLoaded", () => {
			instance.fetchDataAndCreateOrUpdateTable();
		});
	}
	this.__eventTarget = new EventTarget();



}

SimpleTable.prototype.addEventListener = function (...args) {
	this.__eventTarget.addEventListener(...args);
}

SimpleTable.prototype.removeEventListener = function (...args) {
	this.__eventTarget.removeEventListener(...args);
}

SimpleTable.prototype.dispatchEvent = function (...args) {
	this.__eventTarget.dispatchEvent(...args);
}



SimpleTable.prototype.addSortableColumn = function (column, order) {
	this.sortables.set(column, order);
}

SimpleTable.prototype.setSearchUrl = function (url) {
	this.searchURL = url;
}



SimpleTable.prototype._createForwardNavButton = function () {
	this.fbut = document.createElement("button");
	this.fbut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-arrow-left");
	this.fbut.addEventListener("click", async () => {
		this.pageNo -= 1;
		await this.fetchDataAndCreateOrUpdateTable();
	});

	this.fbut.disabled = (this.pageNo == 0);
	return this.fbut;
}

SimpleTable.prototype._createBackwardNavButton = function () {
	this.pBut = document.createElement("button");
	this.pBut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-arrow-right");
	this.pBut.addEventListener("click", async () => {
		this.pageNo++;
		await this.fetchDataAndCreateOrUpdateTable();
	});
	this.pBut.disabled = (this.pageNo + 1 >= this.totalPages);
	return this.pBut;
}


SimpleTable.prototype._createTextInputHeaderElement = function (columnHeader, tableId) {
	var headerContainer = document.createElement("div");
	if (this.mode === "VIEW") {
		headerContainer.classList.add("input-group", "input-group-sm", "d-none");
	} else {
		headerContainer.classList.add("input-group", "input-group-sm");
	}
	headerContainer.setAttribute("id", this.tableId + columnHeader + "headerContainer")
	var dropDownContainer = document.createElement("div");
	dropDownContainer.classList.add("dropdown");
	var but = document.createElement("button");
	but.classList.add("btn", "btn-sm", "btn-primary");
	but.type = "button";
	but.setAttribute("data-bs-toggle", "dropdown");
	var butIcon = document.createElement("i");
	butIcon.classList.add("bi", "bi-wrench-adjustable-circle");
	but.appendChild(butIcon);
	var ul = document.createElement("ul");
	ul.setAttribute("id", tableId + columnHeader + "searchDropdown");
	var saved = this.saveStore.get(columnHeader);
	ul.classList.add("dropdown-menu");
	ul.innerHTML = `
	<li><button class="dropdown-item ${saved?.option === "Like" || saved == null ? "active" : ""}" id=${tableId + columnHeader + "likeButton"} type=button>Like</button></li>
	<li><button class="dropdown-item ${saved?.option === "Equal" ? "active" : ""}" id=${tableId + columnHeader + "equalsButton"} type=button>Equal</button></li>
	<li><button class="dropdown-item ${saved?.option === "NotLike" ? "active" : ""}" id=${tableId + columnHeader + "notLikeButton"} type=button>Not Like</button></li>
	<li><button class="dropdown-item ${saved?.option === "NotEqual" ? "active" : ""}" id=${tableId + columnHeader + "notEqualButton"} type=button>Not Equal</button></li>
	`;
	ul.addEventListener("click", (e) => {
		var likeButton = document.getElementById(tableId + columnHeader + "likeButton");
		var equalsButton = document.getElementById(tableId + columnHeader + "equalsButton");
		var notLikeButton = document.getElementById(tableId + columnHeader + "notLikeButton");
		var notEqualButton = document.getElementById(tableId + columnHeader + "notEqualButton");
		likeButton.classList.remove("active");
		equalsButton.classList.remove("active");
		notLikeButton.classList.remove("active");
		notEqualButton.classList.remove("active");
		switch (e.target.textContent) {
			case "Like":
				likeButton.classList.add("active");
				console.log("like");
				this.saveStore.set(columnHeader, { option: "Like", value: "%" });
				break;
			case "Equal":
				equalsButton.classList.add("active");
				console.log("equals");
				this.saveStore.set(columnHeader, { option: "Equal", value: "" });
				break;
			case "Not Like":
				notLikeButton.classList.add("active");
				console.log("not like");
				this.saveStore.set(columnHeader, { option: "NotLike", value: "" });
				break;
			case "Not Equal":
				notEqualButton.classList.add("active");
				console.log("not equal");
				this.saveStore.set(columnHeader, { option: "NotEqual", value: "" });
				break;
		}
	});
	dropDownContainer.appendChild(but);
	dropDownContainer.appendChild(ul);
	var searchInput = document.createElement("input");
	searchInput.classList.add("form-control");
	searchInput.type = "text";
	if (saved != null) {
		searchInput.value = saved.value;
	}

	searchInput.setAttribute("id", tableId + columnHeader + "searchInput");
	headerContainer.appendChild(searchInput);
	headerContainer.appendChild(dropDownContainer);

	searchInput.addEventListener("input", () => {
		var current = this.saveStore.get(columnHeader);
		if (current == null) {
			this.pageNo = 0;

			current = { option: "Like", value: "%" };
		}
		current.value = searchInput.value;
		this.saveStore.set(columnHeader, current);
		console.log(columnHeader + " i");
		this.updateTable();
	});

	return headerContainer;
}

SimpleTable.prototype._createCheckboxInputHeaderElement = function (columnHeader, tableId, saveStore) {
	var headerContainer = document.createElement("div");
	if (this.mode === "VIEW") {
		headerContainer.classList.add("input-group", "input-group-sm", "d-none");
	} else {
		headerContainer.classList.add("input-group", "input-group-sm");
	}
	headerContainer.setAttribute("id", this.tableId + columnHeader + "headerContainer")
	var searchInput = document.createElement("input");
	searchInput.type = "checkbox";
	searchInput.setAttribute("id", tableId + columnHeader + "searchInput");
	headerContainer.appendChild(searchInput);
	searchInput.bootstrapToggle({
		tristate: true,
		width: 100,
		height: 1,
		onlabel: "true",
		offlabel: "false"
	});
	var saved = this.saveStore.get(columnHeader);
	if (saved != null) {
		console.log(saved);

		if (!saved.value.indeterminate) {
			console.log("not inde " + saved.value.indeterminate);
			searchInput.bootstrapToggle(saved.value.value ? "on" : "off");
		} else {
			console.log("not inde " + saved.value.indeterminate);
			searchInput.bootstrapToggle("indeterminate");
		}
	} else {
		searchInput.bootstrapToggle("indeterminate");
	}




	searchInput.addEventListener("change", (e) => {
		console.log(e.target.checked + " " + e.target.indeterminate);
		var current = saveStore.get(columnHeader);
		if (current == null) {


			current = { option: "Equal", value: { value: true, indeterminate: false } };
		}
		current.value = { value: searchInput.checked, indeterminate: searchInput.indeterminate };
		saveStore.set(columnHeader, current);
		console.log(columnHeader + " i");
		this.pageNo = 0;
		this.updateTable();
	});

	return headerContainer;
}

SimpleTable.prototype._createTableHead = function () {
	var thead = document.createElement("thead");
	var headRow = document.createElement("tr");
	var header = document.createElement("th")
	header.textContent = "#";
	headRow.appendChild(header);

	for (let columnHeader of this.hKMap.keys()) {

		header = document.createElement("th")
		var headerContainer;

		if (!this.ignoreSearchColumns.includes(columnHeader)) {
			if (this.inputTypes.get(columnHeader) === 'text') {
				headerContainer = this._createTextInputHeaderElement(columnHeader, this.tableId);
			} else if (this.inputTypes.get(columnHeader) === 'checkbox') {

				headerContainer = this._createCheckboxInputHeaderElement(columnHeader, this.tableId, this.saveStore);
			}
			header.appendChild(headerContainer);
		}

		var tempTh;
		if (this.sortables.has(columnHeader)) {
			header.classList.add("text-bg-primary", "text-nowrap");
			thContainer = document.createElement("div");
			thContainer.classList.add("d-flex", "justify-content-center", "align-items-center", "gap-2");
			thCheckBox = document.createElement("input");
			thCheckBox.classList.add("form-check-input");
			thCheckBox.type = "checkbox";
			thCheckBox.checked = true;
			thCheckBox.name = columnHeader + "checkbox";
			header.id = this.tableId + columnHeader;


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
			tempTh = document.createElement("div");
			tempTh.innerText = columnHeader;
			header.appendChild(tempTh)
			//header.textContent = columnHeader;
		}

		headRow.appendChild(header);
		tempTh.addEventListener("click", () => {
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
	return thead;

}

SimpleTable.prototype._createTableBody = function () {
	var tbody = document.createElement("tbody");


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
	return tbody;
}

SimpleTable.prototype._createSearchBut = function () {
	var searchBut = document.createElement("button");
	searchBut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-search");
	searchBut.addEventListener("click", () => {
		for (var hName of this.hKMap.keys()) {
			var headerContainer = document.getElementById(this.tableId + hName + "headerContainer");
			this.pageNo = 0;
			if (headerContainer.classList.contains("d-none")) {
				headerContainer.classList.remove("d-none");
				this.mode = "SEARCH";
			} else {
				headerContainer.classList.add("d-none");
				this.mode = "VIEW";
			}
			this.fetchDataAndCreateOrUpdateTable();
		}
	});
	return searchBut;
}

SimpleTable.prototype.createTable = function () {
	this.container = document.getElementById(this.tableContainerName);
	this.container.innerHTML = "";
	this.container.classList.add("table-container", "mt-4");


	var card = document.createElement("div");
	card.classList.add("card", "d-inline-block");


	var cardHeader = document.createElement("div");
	cardHeader.classList.add("card-header", "d-flex", "justify-content-between", "align-items-center");
	var cardTitle = document.createElement("span");
	cardTitle.innerText = this.titleText;
	cardTitle.classList.add("fw-bold");
	cardHeader.appendChild(cardTitle);

	var bContainer = document.createElement("div");
	bContainer.classList.add("btn-group");
	var fBut = this._createForwardNavButton();
	bContainer.appendChild(fBut);
	var bBut = this._createBackwardNavButton();
	bContainer.appendChild(bBut);
	var searchNavContainer = document.createElement("div");
	searchNavContainer.classList.add("d-flex", "gap-2");
	var searchBut = this._createSearchBut();
	searchNavContainer.appendChild(searchBut);
	searchNavContainer.appendChild(bContainer);
	cardHeader.appendChild(searchNavContainer);


	var cardBody = document.createElement("div");
	cardBody.classList.add("card-body", "p-1");

	var table = document.createElement("table");
	var tableResponsiveContainer = document.createElement("div");
	tableResponsiveContainer.classList.add("table-responsive");
	tableResponsiveContainer.appendChild(table);
	table.classList.add("table", "table-hover", "mb-1", "table-sm", "table-striped", "text-center", "mx-auto");
	table.id = this.tableId;
	var thead = this._createTableHead();
	table.appendChild(thead);

	card.appendChild(cardHeader);
	this.tbody = this._createTableBody();
	table.appendChild(this.tbody);


	cardBody.appendChild(tableResponsiveContainer);
	card.appendChild(cardBody);
	this.container.appendChild(card);
}

SimpleTable.prototype.updateTable = async function () {
	await this.fetchSearchData();
	const newTbody = this._createTableBody();
	this.tbody.replaceWith(newTbody);
	this.tbody = newTbody;
	this.fbut.disabled = (this.pageNo == 0);
	this.pBut.disabled = (this.pageNo + 1 >= this.totalPages);
	//table.appendChild(tbody);

}

SimpleTable.prototype.showFirstPage = function () {
	this.pageNo = 0;
	this.fetchDataAndCreateOrUpdateTable();
}

SimpleTable.prototype.fetchDataAndCreateOrUpdateTable = async function () {

	if (this.mode === "VIEW") {
		await this.fetchData();
		this.createTable();
	}
	else if (this.mode === "SEARCH") {
		await this.fetchSearchData();
		this.createTable();
	}
}

SimpleTable.prototype.fetchData = async function () {

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
			"pageable": {
				"pageNo": this.pageNo,
				"pageSize": this.limit,
				"sortable": Object.fromEntries(sortables)
			},

		};

		const resp = await fetch(this.sourceURL, {
			method: "POST",
			headers: customHeaders,
			body: JSON.stringify(body)
		});

		if (!resp.ok) {
			throw new Error("Fetch error");
		}
		var jsonResp = await resp.json();
		this.totalPages = jsonResp.page.totalPages;
		this.source = jsonResp.content;


	} catch (e) {
		console.log(e);
	}
}




SimpleTable.prototype.fetchSearchData = async function () {
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

		searchables = new Map();
		for ([key, value] of this.hKMap) {
			if (!this.ignoreSearchColumns.includes(key)) {
				var current = this.saveStore.get(key);
				if (current != null && current.value != "") {
					var columnName = this.hKMap.get(key);
					if (this.inputTypes.get(key) === 'text') {
						searchables.set(columnName, { [current.option]: { "var": current.value } });
					} else if (this.inputTypes.get(key) === 'checkbox') {
						if (!current.value.indeterminate) {
							searchables.set(columnName, { [current.option]: { "var": current.value.value } });
						}
					}
				}
			}
		}


		body = {
			"pageable": {
				"pageNo": this.pageNo,
				"pageSize": this.limit,
				"sortable": Object.fromEntries(sortables)
			},
			"searchable": Object.fromEntries(searchables)
		};

		const resp = await fetch(this.searchURL, {
			method: "POST",
			headers: customHeaders,
			body: JSON.stringify(body)
		});

		if (!resp.ok) {
			throw new Error("Fetch error");
		}
		var jsonResp = await resp.json();
		this.totalPages = jsonResp.page.totalPages;
		this.source = jsonResp.content;
	} catch (e) {
		console.log(e);
	}
}

SimpleTable.prototype.fetchSearchDataAndCreateTable = async function () {
	await this.fetchData();
	this.createTable();

}

SimpleTable.prototype.fetchSearchDataAndUpdateTable = async function () {
	await this.fetchSearchData();
	this.createTable();

}