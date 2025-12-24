/**
  Shows a message bar which reveals itself from the point where the
  <div> element "message-bar-container" is defined in the html and hides
  itself after a timeout.
  
  
  @author Ashwin
  @version 0.1
 **/
let mouseOverMessageBar = false;

function hideMessageBar(wait) {
	const msgBar = document.getElementById("msgBarId");
	const messageContainer = document.getElementById("message-bar-container");
	setTimeout(() => {
		if (msgBar != null && msgBar.classList.contains("show")) {
			msgBar.classList.remove("show");
			msgBar.addEventListener("transitionend", () => {
				messageContainer.replaceChildren();
			});
		}
	}, wait);
}

function showError(message){
	showMessage("error",message);
}
function showInfo(message){
	showMessage("info",message);
}

function showMessage(type, message) {
	const messageContainer = document.getElementById("message-bar-container");
	const msgBar = document.createElement("div");
	msgBar.id = "msgBarId"
	mouseOverMessageBar = false;
	msgBar.textContent = message;
	msgBar.classList.add("message-bar", type);
	messageContainer.replaceChildren(msgBar);


	setTimeout(() => {
		msgBar.classList.add("show");
		setTimeout(
			() => {
				if (!mouseOverMessageBar) {
					hideMessageBar(2000);
				}
			}, 3100


		);
		msgBar.addEventListener("mouseover", () => {
			mouseOverMessageBar = true;
			console.log("mouseover");
			msgBar.addEventListener("mouseleave", () => {
				console.log("mouseleave");
				hideMessageBar(3000);

			});

		});
		msgBar.addEventListener("click", () => {
			hideMessageBar(0);

		})
	}, 20);
}




function initialMessage() {

	const url=new URL(window.location.href);
	
	const message = url.searchParams.get("message");
	const messageType = url.searchParams.get("message-type");
	url.searchParams.delete("message");
	url.searchParams.delete("message-type");
	window.history.replaceState(null,"",url);
	
	if (message != null) {
		if (messageType === "error")
			showError(message);
		else
			showInfo(message);
	}


}
document.addEventListener("DOMContentLoaded", initialMessage);