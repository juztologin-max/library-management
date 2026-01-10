function SimpleTable(tableContainerName, title, hKMap, sourceURL, crsfKey, crsfValue, start, limit, sortColumn, sortDirection, inputTypes, rowClick, hProjectionMap) {
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
	this.currentColumns = Array.from(this.hKMap.keys());
	for (column of this.currentColumns) {
		//console.log('column ' + this.inputTypes[column]);
		if (this.inputTypes.get(column) == null) {
			this.inputTypes.set(column, 'text');
			console.log(column + ' ' + this.inputTypes.get(column));
		}
	}


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

	this.previousSelected = 1;
	this.showEdit = true;
	this.showDelete = true;
	this.rowClick = rowClick;
	this.hProjectionMap = hProjectionMap;
}

SimpleTable.prototype.setAlternateButton = function(name, callback, headerName, predicate) {
	this.alternateButton = {
		name: name,
		callback: callback,
		headerName: headerName,
		predicate: predicate
	};
}

SimpleTable.prototype.setCurrentColumns = function(columns) {
	this.currentColumns = columns;
}

SimpleTable.prototype.getRowCount = function() {
	return this.source.length;
}



SimpleTable.prototype.setShowEdit = function(show) {
	this.showEdit = show;
}

SimpleTable.prototype.setShowDelete = function(show) {
	this.showDelete = show;
}

SimpleTable.prototype.selectRow = function(index) {
	const table = document.getElementById(this.tableId);
	var row = table.rows[this.previousSelected];
	row.classList.remove("table-primary");
	row = table.rows[index];
	row.classList.add("table-primary");
	this.previousSelected = index;
	this.dispatchEvent(new CustomEvent("TakeFromTable", { detail: this.source[index - 1] }));
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



SimpleTable.prototype.addSortableColumn = function(column, order) {
	this.sortables.set(column, order);
}

SimpleTable.prototype.setSearchUrl = function(url) {
	this.searchURL = url;
}



SimpleTable.prototype._createForwardNavButton = function() {
	this.fbut = document.createElement("button");
	this.fbut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-arrow-left");
	this.fbut.addEventListener("click", async () => {
		this.pageNo -= 1;
		await this.fetchDataAndCreateOrUpdateTable();
	});

	this.fbut.disabled = (this.pageNo == 0);
	return this.fbut;
}

SimpleTable.prototype._createBackwardNavButton = function() {
	this.pBut = document.createElement("button");
	this.pBut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-arrow-right");
	this.pBut.addEventListener("click", async () => {
		this.pageNo++;
		await this.fetchDataAndCreateOrUpdateTable();
	});
	this.pBut.disabled = (this.pageNo + 1 >= this.totalPages);
	return this.pBut;
}


SimpleTable.prototype._createTextInputHeaderElement = function(columnHeader, tableId) {
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
	but.setAttribute("data-bs-popper-config", '{"strategy":"fixed"}');
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


SimpleTable.prototype._createDateTimeInputHeaderElement = function(columnHeader, tableId) {
	var headerContainer = document.createElement("div");
	if (this.mode === "VIEW") {
		headerContainer.classList.add("input-group", "input-group-sm", "d-none");
	} else {
		headerContainer.classList.add("input-group", "input-group-sm");
	}
	headerContainer.setAttribute("id", tableId + columnHeader + "headerContainer")
	var dropDownContainer = document.createElement("div");
	dropDownContainer.classList.add("dropdown");
	var but = document.createElement("button");
	but.classList.add("btn", "btn-sm", "btn-primary");
	but.type = "button";
	but.setAttribute("id", tableId + columnHeader + "button");
	but.setAttribute("data-bs-toggle", "dropdown");
	but.setAttribute("data-bs-popper-config", '{"strategy":"fixed"}');
	var butIcon = document.createElement("i");
	butIcon.classList.add("bi", "bi-wrench-adjustable-circle");
	but.appendChild(butIcon);
	but.innerText = "None";
	var ul = document.createElement("ul");
	ul.setAttribute("id", tableId + columnHeader + "searchDropdown");
	ul.classList.add("dropdown-menu");

	var saved = this.saveStore.get(columnHeader);

	var li = document.createElement("li");
	li.setAttribute("id", tableId + columnHeader + "liEqual");
	var container = document.createElement("div");
	li.classList.add("dropdown-item");
	li.appendChild(container);
	var header = document.createElement("h6");
	header.classList.add("small");
	header.innerText = "Equal";
	var input = document.createElement("input");
	input.type = "date";
	input.classList.add("form-control");
	input.setAttribute("step", 1);
	input.setAttribute("id", tableId + columnHeader + "EqualInput");

	if (saved != null && saved.type == "Equal") {
		input.value = saved.value;
	}

	input.addEventListener("change", (e) => {
		var equalLi = document.getElementById(tableId + columnHeader + "liEqual");
		var betweenLi = document.getElementById(tableId + columnHeader + "liBetween");
		var NoneLi = document.getElementById(tableId + columnHeader + "liNone");
		var but = document.getElementById(tableId + columnHeader + "button");
		equalLi.classList.add("active");
		betweenLi.classList.remove("active");
		NoneLi.classList.remove("active");
		but.innerText = "Equal";
		var save = {
			type: "Equal",
			value: e.target.value
		};
		var betweenStartInput = document.getElementById(tableId + columnHeader + "BetweenStartInput");
		var betweenEndInput = document.getElementById(tableId + columnHeader + "BetweenEndInput");
		betweenStartInput.value = "";
		betweenEndInput.value = "";
		this.saveStore.set(columnHeader, save);
		this.updateTable();
	});
	container.appendChild(header);
	container.appendChild(input);
	ul.appendChild(li);

	var li = document.createElement("li");
	var container = document.createElement("hr");
	container.classList.add("dropdown-divide");
	li.appendChild(container);
	ul.appendChild(li);

	var li = document.createElement("li");
	li.setAttribute("id", tableId + columnHeader + "liBetween");
	var container = document.createElement("div");
	li.classList.add("dropdown-item");
	li.appendChild(container);
	var header = document.createElement("h6");
	header.classList.add("small");
	header.innerText = "Between";
	var input = document.createElement("input");
	input.type = "datetime";
	input.classList.add("form-control");
	input.setAttribute("id", tableId + columnHeader + "BetweenStartInput");
	if (saved != null && saved.type == "Between") {
		input.value = saved.start;
	} else {
		saved = {};
		saved.type = "Between";
		saved.start = "";
		saved.end = ""
	}

	input.addEventListener("change", (e) => {
		var equalLi = document.getElementById(tableId + columnHeader + "liEqual");
		var betweenLi = document.getElementById(tableId + columnHeader + "liBetween");
		var NoneLi = document.getElementById(tableId + columnHeader + "liNone");
		var but = document.getElementById(tableId + columnHeader + "button");
		equalLi.classList.remove("active");
		betweenLi.classList.add("active");
		NoneLi.classList.remove("active");
		but.innerText = "Between";
		console.log("####################");
		var save = this.saveStore.get(columnHeader);
		var currentEndValue = "";
		if (save != null) {
			currentEndValue = save.end;
		}
		save = {
			type: "Between",
			start: e.target.value,
			end: currentEndValue
		};

		console.log(save);

		var likeInput = document.getElementById(tableId + columnHeader + "EqualInput");
		likeInput.value = "";
		this.saveStore.set(columnHeader, save);
		if (save.end != "") {
			console.log("start");
			console.log(save);
			this.updateTable();
		}
		console.log("#########END###########");
	});

	container.appendChild(header);
	container.appendChild(input);
	input = document.createElement("input");
	input.type = "datetime";
	input.classList.add("form-control");
	input.setAttribute("id", tableId + columnHeader + "BetweenEndInput");
	if (saved != null && saved.type == "Between") {
		input.value = saved.end;
	}

	input.addEventListener("change", (e) => {
		var equalLi = document.getElementById(tableId + columnHeader + "liEqual");
		var betweenLi = document.getElementById(tableId + columnHeader + "liBetween");
		var NoneLi = document.getElementById(tableId + columnHeader + "liNone");
		var but = document.getElementById(tableId + columnHeader + "button");
		equalLi.classList.remove("active");
		betweenLi.classList.add("active");
		NoneLi.classList.remove("active");
		but.innerText = "Between";
		var save = this.saveStore.get(columnHeader);
		var currentStartValue = "";
		if (save != null) {
			currentStartValue = save.start;
		}
		save = {
			type: "Between",
			start: currentStartValue,
			end: e.target.value
		};



		var likeInput = document.getElementById(tableId + columnHeader + "EqualInput");
		likeInput.value = "";
		this.saveStore.set(columnHeader, save);
		if (save.start != "") {
			console.log("end");
			this.updateTable();
		}
	});
	container.appendChild(input);
	ul.appendChild(li);


	var li = document.createElement("li");
	var container = document.createElement("hr");
	container.classList.add("dropdown-divide");
	li.appendChild(container);
	ul.appendChild(li);

	var li = document.createElement("li");
	li.setAttribute("id", tableId + columnHeader + "liNone");
	li.classList.add("dropdown-item", "active");
	li.innerText = "None";
	ul.appendChild(li);

	dropDownContainer.appendChild(but);
	dropDownContainer.appendChild(ul);
	headerContainer.appendChild(dropDownContainer);

	return headerContainer;
}

SimpleTable.prototype._createDateInputHeaderElement = function(columnHeader, tableId) {
	var headerContainer = document.createElement("div");
	if (this.mode === "VIEW") {
		headerContainer.classList.add("input-group", "input-group-sm", "d-none");
	} else {
		headerContainer.classList.add("input-group", "input-group-sm");
	}
	headerContainer.setAttribute("id", tableId + columnHeader + "headerContainer")
	var dropDownContainer = document.createElement("div");
	dropDownContainer.classList.add("dropdown");
	var but = document.createElement("button");
	but.classList.add("btn", "btn-sm", "btn-primary");
	but.type = "button";
	but.setAttribute("id", tableId + columnHeader + "button");
	but.setAttribute("data-bs-toggle", "dropdown");
	but.setAttribute("data-bs-popper-config", '{"strategy":"fixed"}');
	var butIcon = document.createElement("i");
	butIcon.classList.add("bi", "bi-wrench-adjustable-circle");
	but.appendChild(butIcon);
	but.innerText = "None";
	var ul = document.createElement("ul");
	ul.setAttribute("id", tableId + columnHeader + "searchDropdown");
	ul.classList.add("dropdown-menu");

	var saved = this.saveStore.get(columnHeader);

	var li = document.createElement("li");
	li.setAttribute("id", tableId + columnHeader + "liEqual");
	var container = document.createElement("div");
	li.classList.add("dropdown-item");
	li.appendChild(container);
	var header = document.createElement("h6");
	header.classList.add("small");
	header.innerText = "Equal";
	var input = document.createElement("input");
	input.type = "date";
	input.classList.add("form-control");
	input.setAttribute("step", 1);
	input.setAttribute("id", tableId + columnHeader + "EqualInput");

	if (saved != null && saved.type == "Equal") {
		input.value = saved.value;
	}

	input.addEventListener("change", (e) => {
		var equalLi = document.getElementById(tableId + columnHeader + "liEqual");
		var betweenLi = document.getElementById(tableId + columnHeader + "liBetween");
		var NoneLi = document.getElementById(tableId + columnHeader + "liNone");
		var but = document.getElementById(tableId + columnHeader + "button");
		equalLi.classList.add("active");
		betweenLi.classList.remove("active");
		NoneLi.classList.remove("active");
		but.innerText = "Equal";
		var save = {
			type: "Equal",
			value: e.target.value
		};
		var betweenStartInput = document.getElementById(tableId + columnHeader + "BetweenStartInput");
		var betweenEndInput = document.getElementById(tableId + columnHeader + "BetweenEndInput");
		betweenStartInput.value = "";
		betweenEndInput.value = "";
		this.saveStore.set(columnHeader, save);
		this.updateTable();
	});
	container.appendChild(header);
	container.appendChild(input);
	ul.appendChild(li);

	var li = document.createElement("li");
	var container = document.createElement("hr");
	container.classList.add("dropdown-divide");
	li.appendChild(container);
	ul.appendChild(li);

	var li = document.createElement("li");
	li.setAttribute("id", tableId + columnHeader + "liBetween");
	var container = document.createElement("div");
	li.classList.add("dropdown-item");
	li.appendChild(container);
	var header = document.createElement("h6");
	header.classList.add("small");
	header.innerText = "Between";
	var input = document.createElement("input");
	input.type = "date";
	input.classList.add("form-control");
	input.setAttribute("id", tableId + columnHeader + "BetweenStartInput");
	if (saved != null && saved.type == "Between") {
		input.value = saved.start;
	} else {
		saved = {};
		saved.type = "Between";
		saved.start = "";
		saved.end = ""
	}

	input.addEventListener("change", (e) => {
		var equalLi = document.getElementById(tableId + columnHeader + "liEqual");
		var betweenLi = document.getElementById(tableId + columnHeader + "liBetween");
		var NoneLi = document.getElementById(tableId + columnHeader + "liNone");
		var but = document.getElementById(tableId + columnHeader + "button");
		equalLi.classList.remove("active");
		betweenLi.classList.add("active");
		NoneLi.classList.remove("active");
		but.innerText = "Between";
		console.log("####################");
		var save = this.saveStore.get(columnHeader);
		var currentEndValue = "";
		if (save != null) {
			currentEndValue = save.end;
		}
		save = {
			type: "Between",
			start: e.target.value,
			end: currentEndValue
		};

		console.log(save);

		var likeInput = document.getElementById(tableId + columnHeader + "EqualInput");
		likeInput.value = "";
		this.saveStore.set(columnHeader, save);
		if (save.end != "") {
			console.log("start");
			console.log(save);
			this.updateTable();
		}
		console.log("#########END###########");
	});

	container.appendChild(header);
	container.appendChild(input);
	input = document.createElement("input");
	input.type = "date";
	input.classList.add("form-control");
	input.setAttribute("id", tableId + columnHeader + "BetweenEndInput");
	if (saved != null && saved.type == "Between") {
		input.value = saved.end;
	}

	input.addEventListener("change", (e) => {
		var equalLi = document.getElementById(tableId + columnHeader + "liEqual");
		var betweenLi = document.getElementById(tableId + columnHeader + "liBetween");
		var NoneLi = document.getElementById(tableId + columnHeader + "liNone");
		var but = document.getElementById(tableId + columnHeader + "button");
		equalLi.classList.remove("active");
		betweenLi.classList.add("active");
		NoneLi.classList.remove("active");
		but.innerText = "Between";
		var save = this.saveStore.get(columnHeader);
		var currentStartValue = "";
		if (save != null) {
			currentStartValue = save.start;
		}
		save = {
			type: "Between",
			start: currentStartValue,
			end: e.target.value
		};



		var likeInput = document.getElementById(tableId + columnHeader + "EqualInput");
		likeInput.value = "";
		this.saveStore.set(columnHeader, save);
		if (save.start != "") {
			console.log("end");
			this.updateTable();
		}
	});
	container.appendChild(input);
	ul.appendChild(li);


	var li = document.createElement("li");
	var container = document.createElement("hr");
	container.classList.add("dropdown-divide");
	li.appendChild(container);
	ul.appendChild(li);

	var li = document.createElement("li");
	li.setAttribute("id", tableId + columnHeader + "liNone");
	li.classList.add("dropdown-item", "active");
	li.innerText = "None";
	li.addEventListener("click", () => {
		this.saveStore.set(columnHeader, () => {
			this.resetAndShowFirstPage();
		});
	});
	ul.appendChild(li);

	dropDownContainer.appendChild(but);
	dropDownContainer.appendChild(ul);
	headerContainer.appendChild(dropDownContainer);

	return headerContainer;
}

SimpleTable.prototype._createCheckboxInputHeaderElement = function(columnHeader, tableId, saveStore) {
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

SimpleTable.prototype._createTableHead = function() {
	var thead = document.createElement("thead");
	var headRow = document.createElement("tr");
	var header = document.createElement("th")
	header.textContent = "#";
	headRow.appendChild(header);

	for (let columnHeader of this.currentColumns) {

		header = document.createElement("th")
		var headerContainer;

		if (!this.ignoreSearchColumns.includes(columnHeader)) {
			if (this.inputTypes.get(columnHeader) === 'checkbox') {

				headerContainer = this._createCheckboxInputHeaderElement(columnHeader, this.tableId, this.saveStore);
			} else if (this.inputTypes.get(columnHeader) === 'datetime') {
				headerContainer = this._createDateTimeInputHeaderElement(columnHeader, this.tableId, this.saveStore);
			} else if (this.inputTypes.get(columnHeader) === 'date') {
				headerContainer = this._createDateInputHeaderElement(columnHeader, this.tableId, this.saveStore);
			}
			else {
				//this.inputTypes.get(columnHeader) === 'text'
				headerContainer = this._createTextInputHeaderElement(columnHeader, this.tableId);
			}
			header.appendChild(headerContainer);
		}

		var tempTh;
		if (this.currentColumns.includes(columnHeader) && this.sortables.has(columnHeader)) {
			//header.classList.add("text-bg-primary", "text-nowrap");
			header.classList.add("text-bg-primary");
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
	if (this.alternateButton != null) {
		header = document.createElement("th")
		header.textContent = this.alternateButton.headerName;
		headRow.appendChild(header);
	}
	if (this.showEdit) {
		header = document.createElement("th")
		header.textContent = "EDIT";
		headRow.appendChild(header);
	}
	if (this.showDelete) {
		header = document.createElement("th")
		header.textContent = "DELETE";
		headRow.appendChild(header);
	}

	thead.appendChild(headRow);
	return thead;

}

SimpleTable.prototype._createTableBody = function() {
	var tbody = document.createElement("tbody");


	for (let i = 0; i < this.source.length; i++) {

		var row = this.source[i];
		var bodyRow = document.createElement("tr");
		if (this.rowClick) {
			bodyRow.addEventListener("click", () => {
				this.selectRow(i + 1);
			});
		}
		var cell = document.createElement("td");
		cell.textContent = (this.pageNo * this.limit) + i + 1;
		//cell.classList.add("text-nowrap");
		bodyRow.appendChild(cell);

		for (columnHeader of this.currentColumns) {
			cell = document.createElement("td");
			var keys = this.hKMap.get(columnHeader).split(".");
			var temp = row[keys[0]];
			for (innerKey of keys.slice(1)) {
				temp = temp[innerKey];
			}
			if (this.inputTypes.get(columnHeader) === 'datetime') {
				var dateTime = document.createElement("time");
				var dateJS = new Date(temp);
				dateTime.innerText = dateJS.toLocaleString()
				cell.appendChild(dateTime);
			} else
				cell.textContent = temp;

			bodyRow.appendChild(cell);
		}
		if (this.alternateButton) {
			button = document.createElement("button");
			button.classList.add("btn", "btn-sm", "btn-primary");
			button.textContent = this.alternateButton.name;
			if (this.alternateButton.predicate({ detail: this.source[i] })) {
				button.removeAttribute("disabled");
			} else {
				button.setAttribute("disabled", true);
			}
			button.addEventListener("click", () => {
				this.alternateButton.callback({ detail: this.source[i] });
			});

			cell = document.createElement("td");
			cell.appendChild(button);
			bodyRow.appendChild(cell);
		}
		if (this.showEdit) {

			var button = document.createElement("button");
			button.classList.add("btn", "btn-sm", "btn-primary");
			button.textContent = "Edit";

			button.addEventListener("click", () => {
				this.dispatchEvent(new CustomEvent("TakeFromTable", { detail: this.source[i] }));

			});
			cell = document.createElement("td");
			cell.appendChild(button);
			bodyRow.appendChild(cell);
		}

		if (this.showDelete) {
			button = document.createElement("button");
			button.classList.add("btn", "btn-sm", "btn-danger");
			button.textContent = "Delete";
			button.addEventListener("click", () => {
				this.dispatchEvent(new CustomEvent("RemoveFromTable", { detail: this.source[i] }));

			});

			cell = document.createElement("td");
			cell.appendChild(button);
			bodyRow.appendChild(cell);
		}



		tbody.appendChild(bodyRow);

	}
	return tbody;
}

SimpleTable.prototype._createSearchBut = function() {
	var searchBut = document.createElement("button");
	searchBut.classList.add("btn", "btn-sm", "btn-outline-primary", "bi", "bi-search");
	searchBut.addEventListener("click", () => {
		for (var hName of this.currentColumns) {
			var headerContainer = document.getElementById(this.tableId + hName + "headerContainer");
			this.pageNo = 0;
			if (headerContainer.classList.contains("d-none")) {
				headerContainer.classList.remove("d-none");
				this.mode = "SEARCH";
			} else {
				headerContainer.classList.add("d-none");
				this.mode = "VIEW";
			}

		}
		this.fetchDataAndCreateOrUpdateTable();
	});
	return searchBut;
}

SimpleTable.prototype._createColumnsShownSelector = function() {
	var dropDownContainer = document.createElement("div");
	dropDownContainer.classList.add("dropdown");
	var but = document.createElement("button");
	but.setAttribute("data-bs-auto-close", "outside");
	but.classList.add("btn", "btn-sm", "btn-outline-primary");
	but.type = "button";
	but.setAttribute("data-bs-toggle", "dropdown");
	var butIcon = document.createElement("i");
	butIcon.classList.add("bi", "bi-list-columns");
	but.appendChild(butIcon);
	var ul = document.createElement("ul");
	//ul.setAttribute("id", tableId + columnHeader + "colDropdown");

	ul.classList.add("dropdown-menu");
	for (var dropElm of this.hKMap.keys()) {
		var li = document.createElement("li");
		var container = document.createElement("div");
		container.classList.add("form-check");
		var check = document.createElement("input");
		check.type = "checkbox";
		check.setAttribute("id", this.tableId + "columnSelector" + dropElm);
		check.setAttribute("column", dropElm);
		check.classList.add("form-check-input");
		if (this.currentColumns.includes(dropElm)) {
			check.checked = true;
		}
		var label = document.createElement("label");
		label.classList.add("form-check-label");
		label.setAttribute("for", this.tableId + "columnSelector" + dropElm);
		container.appendChild(check);
		container.appendChild(label);
		li.appendChild(container);
		ul.appendChild(li);
		label.textContent = dropElm;
		check.addEventListener("change", (e) => {
			var elm = e.target;
			if (elm.checked) {
				if (!this.currentColumns.includes(elm.getAttribute("column"))) {
					this.currentColumns.push(elm.getAttribute("column"));
					console.log("inserting: " + elm.getAttribute("column"));
				}
			} else {

				const index = this.currentColumns.indexOf(elm.getAttribute("column"));
				if (index > -1) {
					this.currentColumns.splice(index, 1);
				}
				console.log("removing: " + elm.getAttribute("column"));
			}
			this.fetchDataAndCreateOrUpdateTable();
		});

	}
	dropDownContainer.appendChild(but);
	dropDownContainer.appendChild(ul);
	return dropDownContainer;
}

SimpleTable.prototype.createTable = function() {
	this.previousSelected = 1;
	this.container = document.getElementById(this.tableContainerName);
	this.container.innerHTML = "";
	this.container.classList.add("table-container", "row", "justify-content-center", "mt-4");
	var inner = document.createElement("div");
	if (this.hKMap.size <= 2) {
		inner.classList.add("col-md-6", "col-lg-4");
	} else if (this.hKMap.size > 2 && this.hKMap.size < 4) {
		inner.classList.add("col-md-10", "col-lg-6");
	} else if (this.hKMap.size >= 4) {
		inner.classList.add("col-md-12", "col-lg-12");
	}
	this.container.appendChild(inner);

	var card = document.createElement("div");
	card.classList.add("card", "d-flex");


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
	var columnBut = this._createColumnsShownSelector();
	searchNavContainer.appendChild(columnBut);
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

	inner.appendChild(card);
	this.dispatchEvent(new CustomEvent("NewTable"));
}

SimpleTable.prototype.updateTable = async function() {
	await this.fetchSearchData();
	const newTbody = this._createTableBody();
	this.tbody.replaceWith(newTbody);
	this.tbody = newTbody;
	this.fbut.disabled = (this.pageNo == 0);
	this.pBut.disabled = (this.pageNo + 1 >= this.totalPages);
	//table.appendChild(tbody);

}

SimpleTable.prototype.showFirstPage = function() {
	this.pageNo = 0;
	this.fetchDataAndCreateOrUpdateTable();
}

SimpleTable.prototype.resetAndShowFirstPage = async function() {
	this.pageNo = 0;
	this.saveStore = new Map();
	await this.fetchData();
	this.createTable();
}

SimpleTable.prototype.fetchDataAndCreateOrUpdateTable = async function() {

	if (this.mode === "VIEW") {
		await this.fetchData();
		this.createTable();
	}
	else if (this.mode === "SEARCH") {
		await this.fetchSearchData();
		this.createTable();
	}
}

SimpleTable.prototype.fetchData = async function() {

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




SimpleTable.prototype.fetchSearchData = async function() {
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
			if (this.currentColumns.includes(key)) {
				var current = this.saveStore.get(key);
				if (current != null && current.value != "") {
					var columnName = this.hKMap.get(key);
					if (this.hProjectionMap != null) {
						if (this.hProjectionMap.get(key) != null) {
							columnName = this.hProjectionMap.get(key);
						}
					}
					if (this.inputTypes.get(key) === 'text' || this.inputTypes.get(key) === 'email') {
						searchables.set(columnName, { [current.option]: { "var": current.value } });
					} else if (this.inputTypes.get(key) === 'checkbox') {
						if (!current.value.indeterminate) {
							searchables.set(columnName, { [current.option]: { "var": current.value.value } });
						}
					} else if (this.inputTypes.get(key) === 'datetime' || this.inputTypes.get(key) === 'date') {
						if (current.type == "Equal") {
							searchables.set(columnName, { "Equal": { "var": current.value } });
						} else if (current.type == "Between") {
							searchables.set(columnName, { "Between": { "var1": current.start, "var2": current.end } });
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

SimpleTable.prototype.fetchSearchDataAndCreateTable = async function() {
	await this.fetchData();
	this.createTable();

}

SimpleTable.prototype.fetchSearchDataAndUpdateTable = async function() {
	await this.fetchSearchData();
	this.createTable();

}