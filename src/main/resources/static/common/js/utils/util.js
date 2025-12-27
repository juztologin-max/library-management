export function getPath() {
	const baseURL = (new URL(window.location.href)).origin;
	const path = window.location.pathname;
	const temp = path.substring(1);
	const parent = temp.substring(0, temp.indexOf("/"));
	return baseURL + "/" + parent

}