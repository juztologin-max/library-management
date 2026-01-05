function passwordToggle() {
	const passwordInput = document.getElementsByName("password")[0];
	const eyeToggle = document.getElementsByName("icon-toggle")[0];
	passwordInput.setAttribute("type", passwordInput.getAttribute("type") == "text" ? "password" : "text")
	eyeToggle.classList.toggle('bi-eye-slash');
	eyeToggle.classList.toggle('bi-eye');
}



