function getPath() {
	const baseURL = (new URL(window.location.href)).origin;
	console.log("path: ",baseURL);
	const path = window.location.pathname;
	console.log("path: ",path);
	const temp = path.substring(1);
	console.log("temp: ",temp);
	const parent = temp.substring(0, temp.indexOf("/"));
	console.log("parent: ",parent);
	return baseURL + "/" + parent

}

function getBaseUrl(){
	return (new URL(window.location.href)).origin;
}