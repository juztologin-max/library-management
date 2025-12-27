const FacadeMode = Object.freeze({
	EDITMODE: 0,
	MODIFYMODE: 1
});

function Facade(title, containerId, fMap) {
	this.titleText = title;
	this.containerId = containerId;
	this.fhKMap = fMap;
	const instance = this;
	document.addEventListener("DOMContentLoaded", () => { instance.createFacade(); });
	this.__eventTarget = new EventTarget();
	this.source = {};
	this.mode = FacadeMode.EDITMODE;
}

Facade.prototype.addEventListener = function(...args) {
	this.__eventTarget.addEventListener(...args);
}

Facade.prototype.removeEventListener = function(...args) {
	this.__eventTarget.removeEventListener(...args);
}

Facade.prototype.dispatchEvent = function(...args) {
	this.__eventTarget.dispatchEvent(...args);
}

Facade.prototype.createFacade = function() {
	this.container = document.getElementById(this.containerId);
	this.container.classList.add("facade-container");
	this.title = document.createElement("label");
	this.title.id = "titleid1";
	this.title.innerText = this.titleText;
	this.title.classList.add("title");
	this.container.appendChild(this.title);
	this.gridContainer = document.createElement("div");
	this.gridContainer.classList.add("facade-grid-container");
	this.container.appendChild(this.gridContainer);
	for (let [items, value] of this.fhKMap) {
		let label = document.createElement("label");
		label.innerText = items;
		let elm
		if (value[1] === "input") {
			elm = document.createElement("input");
			elm.setAttribute("type", value[1]);
			elm.value = items;
		} else {
			elm = document.createElement(value[1]);
			elm.innerText = items;
		}
		elm.id = items;

		label.classList.add("signup-element");
		elm.classList.add("signup-element");
		this.gridContainer.appendChild(label);
		this.gridContainer.appendChild(elm);
	}

	let but = document.createElement("button");
	but.id = "facadeSaveEditButton";
	this.container.appendChild(but);
	but.classList.add("facade-button");
	but.innerText = "Save";
	but.addEventListener("click", () => {

		for (let [items, value] of this.fhKMap) {
			let elm = document.getElementById(items);
			this.source[value[0]] = elm.value;
		}
		const eventName = (this.mode == FacadeMode.EDITMODE) ? "SaveFacadeValues" : "ModifyFacadeValues";

		this.dispatchEvent(new CustomEvent(eventName, { detail: this.source }));
	});
}



Facade.prototype.fillFacade = function(event) {
	this.mode = FacadeMode.MODIFYMODE;
	this.source = event.detail;
	for (let [items, value] of this.fhKMap) {
		let elm = document.getElementById(items);

		elm.value = this.source[value[0]];

	}
	let but = document.getElementById("facadeSaveEditButton");
	but.innerText = "Update"
}

