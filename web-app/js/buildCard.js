function buildOrganizerCard(e, id) {
	var color = getColorByCategory(e.category);
	e.description = e.description.replace('\'', '\\\'');
	document.getElementById("cardsContainer").innerHTML += '<div id="cardnumber' + id + '"  class="card-small pmd-card pmd-card-default pmd-z-depth-2" style="background-color:' + color + '!important"> <!-- Card media --> <div class="pmd-card-media"> <img src="./images/category/' + e.category.replace(/ |&amp;|&/g, "").toLowerCase() + '.png" class="img-card" style="height:150px;width:400px"> </div> <!-- Card body --> <div class="pmd-card-title" style="padding-bottom:0px;margin-bottom:0px;margin-top:0px;"><a onclick="setShareLink(\'' + e.latitude + '\',\'' + e.longitude + '\',\'' + e.title + '\')" style="cursor: pointer;cursor: hand;" data-target="#share-dialog" data-toggle="modal"><i class="material-icons pmd-sm" style="font-size:24px;float: right;color: white">share</i></a><h2 class="pmd-card-title-text" style="margin-bottom: 6px">' + e.title + '</h2><div class="media-left media-middle"> <i class="material-icons pmd-sm">domain</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.organizer_name + '</h4> </div> <div id="collapse' + id + '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne" style="padding: 0px;margin-top: 5px;"><div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">face</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.followers + '</h4> </div> </div> <div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">date_range</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.opening + ' | ' + e.ending + '</h4> </div> </div> <div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">location_on</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.location + '</h4> </div> </div> <div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">phone</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.phone + '</h4> </div> </div> <div class="panel-body" style="color:white;padding:0px; margin: 0px;">' + e.description + '</div> </div> </div> <hr style="color: white;margin: 0px; margin-left: 6px;margin-right: 6px;"> <div class="pmd-card-actions" role="tab" id="headingOne"><button class="media-left btn pmd-btn-flat pmd-ripple-effect btn-primary" type="button" href="#form-edit" data-toggle="modal" id="edit' + id + '" data-target="#form-edit" onclick="fillEditEventForm(\'' + e.id + '\',\'' + e.title + '\',\'' + e.category + '\',\'' + e.opening + '\',\'' + e.ending + '\',\'' + e.location + '\',\'' + e.latitude + '\',\'' + e.longitude + '\',\'' + e.description + '\',\'' + e.tags + '\',\'' + e.phone + '\')">EDIT</button> <button type="button" class="btn pmd-btn-flat pmd-ripple-effect btn-default" onclick="removeEvent(\'' + e.id + '\',\'cardnumber' + id + '\')">DELETE</button> <button id="arrow' + id + '" onclick="rotateelement(' + id + ')" class="btn btn-sm pmd-btn-fab pmd-btn-flat pmd-ripple-effect pull-right media-middle " type="button" aria-expanded="false" aria-controls="collapse' + id + '" data-expandable="false" data-parent="#accordion5" data-toggle="collapse"  href="#collapse' + id + '" style="color: white;background-color: transparent "><i class="material-icons pmd-sm " >keyboard_arrow_down</i></button> </div> <!-- Accordion with No Space example end --> <!--Default card ends --> </div>';
}

function buildUserCard(e, id) {
	var color = getColorByCategory(e.category);
	e.description = e.description.replace('\'', '\\\'');
	document.getElementById("cardsContainer").innerHTML += '<div  id="cardnumber' + id + '" class="card-small pmd-card pmd-card-default pmd-z-depth-2" style="background-color:' + color + '!important"> <!-- Card media --> <div class="pmd-card-media"> <img src="./images/category/' + e.category.replace(/ |&amp;|&/g, "").toLowerCase() + '.png" class="img-card" style="height:150px;width:400px"> </div> <!-- Card body --> <div class="pmd-card-title" style="padding-bottom:0px;margin-bottom:0px;margin-top:0px;"><h2 class="pmd-card-title-text">' + e.title + '</h2> <div class="media-left media-middle"> <i class="material-icons pmd-sm">domain</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text"><a style="color:white" href = "/index.php?page=profile&id=' + e.owner + '" > ' + e.organizer_name + ' </a> </h4> </div> <div id="collapse' + id + '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne" style="padding: 0px;margin-top: 5px;"> <div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">date_range</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.opening + ' | ' + e.ending + '</h4> </div> </div> <div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">location_on</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.location + '</h4> </div> </div> <div style="padding: 0px;margin: 0px;"> <div class="media-left media-middle"> <i class="material-icons pmd-sm">phone</i> </div> <div class="media-left media-middle"> <h4 class="pmd-card-title-text">' + e.phone + '</h4> </div> </div> <div class="panel-body" style="color:white;padding:0px; margin: 0px;">' + e.description + '</div> </div> </div> <hr style="color: white;margin: 0px; margin-left: 6px;margin-right: 6px;"> <div onclick="setShareLink(\'' + e.latitude + '\',\'' + e.longitude + '\',\'' + e.title + '\')" class="pmd-card-actions" role="tab" id="headingOne" style="height:50px;"><button data-target="#share-dialog" data-toggle="modal" class="btn btn-sm pmd-btn-fab pmd-btn-flat pmd-ripple-effect white" type="button"><i class="material-icons pmd-sm">share</i></button><button id="arrow' + id + '" onclick="rotateelement(' + id + ')" class="btn btn-sm pmd-btn-fab pmd-btn-flat pmd-ripple-effect pull-right media-middle " type="button" aria-expanded="false" aria-controls="collapse' + id + '" data-expandable="false" data-parent="#accordion5" data-toggle="collapse"  href="#collapse' + id + '" style="color: white;background-color: transparent "><i class="material-icons pmd-sm " >keyboard_arrow_down</i></button> </div> <!-- Accordion with No Space example end --> <!--Default card ends --> </div>';
}

function getColorByCategory(category) {
	if (category == "Festival") { return '#0A6DBB'; }
	else if (category == "Outdoor") { return '#2E7D32'; }
	else if (category == "Food & Drink") { return '#B71C1C'; }
	else if (category == "Theatre") { return '#827717'; }
	else if (category == "Museum") { return '#795548'; }
	else if (category == "Live Music") { return '#3F51B5'; }
	else if (category == "Disco") { return '#AD1457'; }
	else return '#000000';
}

function fbShare(title, category, descr, lat, lng) {
	//getContentByMetaTagName(title,descr);
	FB.ui({
		method: 'share_open_graph',
		action_type: 'og.shares',
		action_properties: JSON.stringify({
			object: {
				'og:url': 'https://eventifyserver.altervista.org/index.php?lat=' + lat + '&lng=' + lng,
				'og:title': title,
				'og:description': descr,
				'og:image': 'https://eventifyserver.altervista.org/images/category/' + category + '.png',
				'og:image:width': '1200',
				'og:image:height': '630'
			}
		})

	}, function (response) {
		if (response && !response.error_message) {
			alert('Posting completed.');
		} else {
			alert('Error while posting.');
		}
	});
}

function rotateelement(e) {
	var cl = document.getElementById("arrow" + e).classList;
	if (cl.contains("arrowrotate")) { cl.remove("arrowrotate"); }
	else {
		cl.add("arrowrotate");
	}
}

function setShareLink(latitude, longitude, title) {
	document.getElementById("shareLink").setAttribute("data-meta-link", "https://eventifyserver.altervista.org/?lat=" + latitude + "&lng=" + longitude);
	document.getElementById("shareLink").setAttribute("data-meta-title", title);

	(function () {
		socializer('.socializer');
	}());
}