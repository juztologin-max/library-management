function showAlert(type, message, placeholder) {
    const alertPlaceholder = document.getElementById(placeholder);

    const divContainer = document.createElement('div')
    divContainer.innerHTML = `
		<div class="alert alert-${type} alert-dismissible  fade show" role="alert">
		   <div>${message}</div>
		   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>`;

    alertPlaceholder.append(divContainer)
}

function initialMessage() {

    const url = new URL(window.location.href);

    const message = url.searchParams.get("message");
    const messageType = url.searchParams.get("message-type");
    url.searchParams.delete("message");
    url.searchParams.delete("message-type");
    window.history.replaceState(null, "", url);

    if (message != null) {
        if (messageType === "error")
            showAlert("danger", message, "alert-initial-placeholder");
        else
            showAlert("success", message, "alert-initial-placeholder");
    }


}