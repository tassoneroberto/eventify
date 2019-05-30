 function printMap(position,data) {
 	var fenway = {
 		lat: position.coords.latitude,
 		lng: position.coords.longitude
 	};
	lat=position.coords.latitude;
	lng=position.coords.longitude;
 	inlatitude = position.coords.latitude;
 	inlongitude = position.coords.longitude;
	 zoomvalue=12;
	 if(position.zoom!=null)
	 zoomvalue=position.zoom;
 	map = new google.maps.Map(document.getElementById('map'), {
 		center: fenway,
 		zoom: zoomvalue
 	});
	 distance=50;
	 if(data.distance!=null)distance=data.distance;
 	markersOnMap(map, inlatitude, inlongitude, distance, data);
 	mapzoom = map.getZoom();
 	//add marker when zoom change
 	google.maps.event.addListener(map, 'zoom_changed', function() {
 		var zoomLevel = map.getZoom();
 		//zoom out
 		if (zoomLevel < mapzoom) {
 			zoom += 50;
 			if (zoom > previouszoom) {
 				markersOnMap(map, inlatitude, inlongitude, zoom,data);
 				previouszoom += 50;
 			}
 		} else {
 			zoom -= 50;
 		}
		if(zoom<=0)
			zoom=10;
 		mapzoom = zoomLevel;
 	});
 	google.maps.event.addListener(map, 'dragend', function() {
 		// This method returns the position of the click on the map
 		lat = map.getCenter().lat();
 		lng = map.getCenter().lng();
		data.operation="getNearEvents";
 		markersOnMap(map, lat, lng, zoom,data);
 	});
 	//close infowindow when click on maps
 	google.maps.event.addListener(map, 'click', function() {
 		infowindow.close();
 	});
 }
 // Function for adding a marker to the page.
 function addMarker(location, maps, data, cardid) {
 	marker = new google.maps.Marker({
 		position: location,
 		//map: maps,
 		icon: 'images/marker/' + data.category.replace(/ |&|&/g, "").toLowerCase() + '.png',
 		id: data.id,
 		title: data.title,
 		animation: google.maps.Animation.DROP
 	});
 	if (!markersEquals(marker)) {
 		marker.setMap(maps);
 		markers.push(marker);
 		// InfoWindow content
 		var content = '<div id="iw-container">' + '<div class="iw-title">' + data.title + '</div>' + '<div class="iw-content">' + '<img src="images/category/' + data.category.replace(/ |&amp;|&/g, "").toLowerCase() + '.png" alt="' + data.category + '" height="100" width="320">' + '<div class="iw-subTitle">Details</div>' + '<p>' + data.description + '</p>' + '<div class="iw-subTitle">Contacts</div>' + '<p>Location: ' + data.location + '</p>' + '<p>Phone: ' + data.phone + '</p>' + '</div>' + '<div class="iw-bottom-gradient"></div>' + '</div>';
 		google.maps.event.addListener(marker, 'click', function() {
 			infowindow.setContent(content);
 			infowindow.open(maps, this);
 			var pos = document.getElementById("cardnumber" + cardid).offsetTop - document.getElementById("cardsContainer").offsetTop;
 			document.getElementById("cardsContainer").scrollTop = pos;
 		});
 		//cardid++;
 		// *
 		// START INFOWINDOW CUSTOMIZE.
 		// The google.maps.event.addListener() event expects
 		// the creation of the infowindow HTML structure 'domready'
 		// and before the opening of the infowindow, defined styles are applied.
 		// *
 		google.maps.event.addListener(infowindow, 'domready', function() {
 			// Reference to the DIV that wraps the bottom of infowindow
 			var iwOuter = $('.gm-style-iw');
 			/* Since this div is in a position prior to .gm-div style-iw.
 			 * We use jQuery and create a iwBackground variable,
 			 * and took advantage of the existing reference .gm-style-iw for the previous div with .prev().
 			 */
 			var iwBackground = iwOuter.prev();
 			// Removes background shadow DIV
 			iwBackground.children(':nth-child(2)').css({
 				'display': 'none'
 			});
 			// Removes white background DIV
 			iwBackground.children(':nth-child(4)').css({
 				'display': 'none'
 			});
 			// Moves the infowindow 115px to the right.
 			iwOuter.parent().parent().css({
 				left: '115px'
 			});
 			// Moves the shadow of the arrow 76px to the left margin.
 			iwBackground.children(':nth-child(1)').attr('style', function(i, s) {
 				return s + 'left: 76px !important;'
 			});
 			// Moves the arrow 76px to the left margin.
 			iwBackground.children(':nth-child(3)').attr('style', function(i, s) {
 				return s + 'left: 76px !important;'
 			});
 			// Changes the desired tail shadow color.
 			iwBackground.children(':nth-child(3)').find('div').children().css({
 				'box-shadow': 'rgba(72, 181, 233, 0.6) 0px 1px 6px',
 				'z-index': '1'
 			});
 			// Reference to the div that groups the close button elements.
 			var iwCloseBtn = iwOuter.next();
 			// Apply the desired effect to the close button
 			iwCloseBtn.css({
 				opacity: '1',
 				right: '38px',
 				top: '3px',
 				border: '7px solid #48b5e9',
 				'border-radius': '13px',
 				'box-shadow': '0 0 5px #3990B9'
 			});
 			// If the content of infowindow not exceed the set maximum height, then the gradient is removed.
 			if ($('.iw-content').height() < 140) {
 				$('.iw-bottom-gradient').css({
 					display: 'none'
 				});
 			}
 			// The API automatically applies 0.7 opacity to the button after the mouseout event. This function reverses this event to the desired value.
 			iwCloseBtn.mouseout(function() {
 				$(this).css({
 					opacity: '1'
 				});
 			});
 		});
 	}
 }

 function markersOnMap(maps, latitude, longitude, distance, data) {
 	document.getElementById("loadingSpinner").style.display = 'inherit';
 	var data;
	if(data.operation=="getNearEvents"){
		data = {
			"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
			"operation": data.operation,
			"latitude": latitude,
			"longitude": longitude,
			"distance": distance
		}
	}else if(data.operation=="getCustomEvents"){
		data = {
			"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
			"operation": data.operation,
			"latitude": latitude,
			"longitude": longitude,
			"category": data.category,
			"tags": data.tags,
			"rangeDistance":data.rangeDistance,
			"rangeTime":data.rangeTime
		}
	}else if(data.operation=="generalSearchEvents"){
		data = {
			"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
			"operation": data.operation,
			"query": data.query,
		}
	}
 	$.ajax({
 		url: 'https://apisocialevent.altervista.org/API/',
 		type: 'POST',
 		data: JSON.stringify(data),
 		contentType: 'application/json',
 		dataType: 'json',
 		async: true,
 		success: function(response) {
			document.getElementById("cardsContainer").innerHTML = "";
 			document.getElementById("loadingSpinner").style.display = 'none';
 			eventList = response.events;
			if(response.events!=null){
				document.getElementById("noEventsMessage").innerHTML = "";
				var id = 0;
				for (var i = 0; i < eventList.length; i++) {
					var location = new google.maps.LatLng(eventList[i].latitude, eventList[i].longitude);
					addMarker(location, maps, eventList[i], i);
					buildUserCard(eventList[i], i);
				}
				var clusterOptions = {
					imagePath: 'images/m'
				}
				var markerCluster = new MarkerClusterer(maps, markers, clusterOptions);
				google.maps.event.addListener(markerCluster, 'clusterclick', function(cluster) {
					var content = '';
					// Convert the coordinates to an MVCObject
					var info = new google.maps.MVCObject;
					info.set('position', cluster.center_);
					//Get markers
					var marks_in_cluster = cluster.getMarkers();
					infowindow.close();
					infowindowCluster.close(); // closes previous open ifowindows
				});
			}
			else{
				document.getElementById("noEventsMessage").innerHTML = 'No events found. Try to zoom out!';
			}
 		}
 	});
 }

 function markersEquals(value) {
 	for (var i = 0; i < markers.length; i++) {
 		//alert(value.id+' '+markers[i].id);
 		if (value.id == markers[i].id) {
 			return true;
 		}
 	}
 	return false;
 }