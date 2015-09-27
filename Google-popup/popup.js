var notifyId = Math.floor((Math.random() * 100) + 1);

chrome.runtime.onMessage.addListener(function(request, sender) {
	if (request.action == "getSource") {
		if ((request.title.match(/:\s(.+):\s/) || []).length > 0) {
			title.innerText = request.title.match(/:\s(.+):\s/)[1];
		} else {
			title.innerText = request.title;
		}
		var imageResult = document.getElementById('image-result');
		imageResult.src = request.image;
		imageResult.hidden = false;
		document.getElementById('add').style.visibility = 'visible';
	}
});

function getCurrentTabUrl(callback) {
	var queryInfo = {
		active: true,
		currentWindow: true
	};

	chrome.tabs.query(queryInfo, function(tabs) {
		var tab = tabs[0];
		var url = tab.url;
		console.assert(typeof url == 'string', 'tab.url should be a string');

		callback(url);
	});
}

function clickHandler(e) {
	var xhr = new XMLHttpRequest();
	xhr.open("Get", "http://ec2-52-21-9-109.compute-1.amazonaws.com:8080/mini-0.0/wishlist?title=" + document.getElementById('title').innerHTML + "&image="+document.getElementById('image-result').src+"&url="+document.getElementById('url').innerHTML, true);
	xhr.onreadystatechange = function() {
	    if (xhr.readyState == 4) {
			// innerText does not let the attacker inject HTML elements.
			/*
			0      The request is not initialized
			1      The request has been set up
			2      The request has been sent
			3      The request is in process
			4      The request is complete
			*/
			if (new String(xhr.response.trim()).valueOf() == new String('Add successful').valueOf()) {
				var opt = {
					iconUrl: document.getElementById('image-result').src,
					type: 'basic',
					title: 'Success!',
					message: 'Your item has been added to the wish list.'
				};
				chrome.notifications.create("nid"+notifyId++, opt, clearNotification);
			} else {
				var opt = {
					iconUrl: document.getElementById('image-result').src,
					type: 'basic',
					title: 'Notice',
					message: 'Your item already exists in the wish list.'
				};
				chrome.notifications.create("nid"+notifyId++, opt, clearNotification);
			}
	    }
	}
	xhr.send();
}

function clearNotification(noteId) {
	setTimeout(function () {
		chrome.notifications.clear(noteId, function(wasCleared){});
		window.close();
	}, 1500);
}

function onWindowLoad() {
	var message = document.querySelector('#title');
	document.getElementById('add').style.visibility = 'hidden';
	document.getElementById('url').style.visibility = 'hidden';
	getCurrentTabUrl(function(url) { 
		document.getElementById('url').innerHTML = url;
		chrome.tabs.executeScript(null, {
			file: "getPagesSource.js"
		}, function() {
			// If you try and inject into an extensions page or the webstore/NTP you'll get an error
			if (chrome.runtime.lastError) {
				message.innerText = 'There was an error injecting script : \n' + chrome.runtime.lastError.message;
			}
		});
	});

}

document.addEventListener('DOMContentLoaded', function () {
	document.querySelector('button').addEventListener('click', clickHandler);
});

window.onload = onWindowLoad;