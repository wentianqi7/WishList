// @author Rob W <http://stackoverflow.com/users/938089/rob-w>
// Demo: var serialized_html = DOMtoString(document);

chrome.runtime.sendMessage({
	action: "getSource",
	title: document.title,
	image: document.getElementById('miniATF_image').getAttribute('src')
});

