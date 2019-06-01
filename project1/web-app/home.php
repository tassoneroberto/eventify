<div id="contentwrapper" style="margin-bottom: 6px!important">
	<div id="contentcolumn" class="home-map">
		<div id="map"></div>
	</div>
</div>
<div id="leftcolumn">
	<div id="noEventsMessage"></div>
	<div id="cardsContainer" class="innertube"> </div>
</div>
<script>
	var markers = [];
	var infowindow = null;
	var infowindowCluster = null;
	var inlatitude;
	var inlongitude;
	var map;
	var mapzoom;
	var previouszoom = 50;
	var zoom = 50;
	var cardId = 0;

	function openDefaultMap(data) {
		position = {
			"coords": {
				"latitude": defaultLatitude,
				"longitude": defaultLongitude
			}
		}
		printMap(position, data);
	}

	function initialize() {
		document.getElementById("loadingSpinner").style.display = 'inherit';
		var fenway = {
			lat: defaultLatitude,
			lng: defaultLongitude
		};
		map = new google.maps.Map(document.getElementById('map'), {
			center: fenway,
			zoom: 16
		});

		<?php
		if (isset($_GET['log']))
			$login = $_GET['log'];
		if (isset($_GET['lat']))
			$lat = $_GET['lat'];
		if (isset($_GET['lng']))
			$lng = $_GET['lng'];
		if ($login == 1) {
			echo '$("#form-dialog").modal("show");';
		}
		if (isset($lat) && isset($lng))
			echo 'position={
					"coords":{
					"latitude": ' . $lat . ',
					"longitude": ' . $lng . '
					},
					"zoom":20,
					
				}
				
				data={
					"operation":"getNearEvents",
					"distance":0.1
				}
				printMap(position,data);
				 infowindow = new google.maps.InfoWindow({
		      maxWidth: 350,
		      maxHeight: 300,
		  });
		   infowindowCluster = new google.maps.InfoWindow({
		      maxWidth: 350,
		      maxHeight: 300,
		  });';
		else
			echo 'if (navigator.geolocation) {
			var location_timeout = setTimeout("openDefaultMap()", 10000);
			navigator.geolocation.getCurrentPosition(function(position) {
				clearTimeout(location_timeout);	
				data={
					"operation":"getNearEvents"
				}	
				printMap(position,data);
			}, function(error) {
				data={
					"operation":"getNearEvents"
				}	
				clearTimeout(location_timeout);
				openDefaultMap(data);
			});
		} else {
			data={
					"operation":"getNearEvents"
				}	
			openDefaultMap(data);
		}
		  infowindow = new google.maps.InfoWindow({
		      maxWidth: 350,
		      maxHeight: 300,
		  });
		   infowindowCluster = new google.maps.InfoWindow({
		      maxWidth: 350,
		      maxHeight: 300,
		  });';
		?>
		//close infowindow when click on maps
		google.maps.event.addListener(map, 'click', function() {
			infowindow.close();
		});
		var input = document.getElementById('organizerRegisterLocation');
		var autocomplete = new google.maps.places.Autocomplete(input);
		//alert(autocomplete);
		autocomplete.addListener('place_changed', function() {
			var place = autocomplete.getPlace();
			if (!place.geometry) {
				// User entered the name of a Place that was not suggested and
				// pressed the Enter key, or the Place Details request failed.
				window.alert("No details available for input: '" + place.name + "'");
				return;
			}
			var address = '';
			if (place.address_components) {
				address = [
					(place.address_components[0] && place.address_components[0].short_name || ''),
					(place.address_components[1] && place.address_components[1].short_name || ''),
					(place.address_components[2] && place.address_components[2].short_name || '')
				].join(' ');
			}
			var geocoder = new google.maps.Geocoder();
			geocoder.geocode({
				address: address
			}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					var lat = results[0].geometry.location.lat();
					var lng = results[0].geometry.location.lng();
					document.getElementById('organizerRegisterLatitude').value = lat;
					document.getElementById('organizerRegisterLongitude').value = lng;
				} else {
					alert("Google Maps not found address!");
				}
			});
		});
	}
</script>
<h4></h4>
<script>
	function filterEvents() {
		document.getElementById("loadingSpinner").style.display = 'inherit';
		position = {
			"coords": {
				"latitude": lat,
				"longitude": lng
			}
		}
		var filterTags = $("#filterTags").select2("val");
		var tags = "";
		var i;
		if (filterTags == null) {
			var category = document.getElementById("filterCategory").value;
			if (category == "Festival") {
				tags = "Festival-Food &#38; Drink-Disco-Live Music-Cinema-Outdoor-Theatre-Museum";
			}
			if (category == "Theater") {
				tags = "Drama-Musical-Comedy-Tragedy-Improvisation";
			}
			if (category == "Cinema") {
				tags = "Spy film-Action thriller-Superhero film-Comedy of manners-Black comedy-Comedy horror-Romantic comedy film-Teen movie-Crime drama-Historical drama-Docudrama-Legal drama-Psychodrama-Comedy drama-Melodrama-Tragedy-Docufiction";
			}
			if (category == "Food &#38; Drink") {
				tags = "Fast Food-Restaurant- Steackhouse -Happy Hour-Cocktail-Beer-Wine- Hamburger -Pizza-Italian Food-Chinese Food-Japan Food -Mexican Food-Greek Food-Indian Food";
			}
			if (category == "Live Music") {
				tags = "Cover Band-Live Albums-Live Singles -Concerts-Music Festivals-Live Coding-Music Venues ";
			}
			if (category == "Outdoor") {
				tags = "Charity Fundraiser-Food-Beverage -Golf-Concert-Auction-Awards-Cabaret -Car Boot Sale-Celebration-Conference-Convention -Flash Mob-Party";
			}
			if (category == "Festival") {
				tags = "Music-Cinema-Literature -Theatre-Art-Religion-Food and Drink- Beer -Seasonal Festival-Hippy";
			}
			if (category == "Disco") {
				tags = "Dance-Latino-House-Dub -Progressive-Hardcore-DJ-Vip-Special Guest -Dance Floor-Cocktail";
			}
			if (category == "Museum") {
				tags = "Archaeology-Anthropology-Ethnology -Military history-Cultural history-Science- Technology -Children\'s museums-Natural history-Numismatics- Botanical gardens -Zoo<h6>log</h6>ical gardens-Philately";
			}
		} else {
			for (i = 0; i < filterTags.length; i++) {
				tags += filterTags[i] + "-";
			}
			tags = tags.slice(0, -1);
		}
		data = {
			"operation": "getCustomEvents",
			"category": document.getElementById('filterCategory').value,
			"tags": tags,
			"rangeDistance": document.getElementById('hiddenDistance').value,
			"rangeTime": document.getElementById('hiddenTime').value,
		}
		printMap(position, data);
		infowindow = new google.maps.InfoWindow({
			maxWidth: 350,
			maxHeight: 300,
		});
		infowindowCluster = new google.maps.InfoWindow({
			maxWidth: 350,
			maxHeight: 300,
		});
		document.getElementById("loadingSpinner").style.display = 'none';
	}
</script>
<script>
	function searchEvents() {
		document.getElementById("loadingSpinner").style.display = 'inherit';
		position = {
			"coords": {
				"latitude": lat,
				"longitude": lng
			}
		}
		data = {
			"operation": "generalSearchEvents",
			"query": document.getElementById('querySearchEvents').value
		}
		printMap(position, data);
		infowindow = new google.maps.InfoWindow({
			maxWidth: 350,
			maxHeight: 300,
		});
		infowindowCluster = new google.maps.InfoWindow({
			maxWidth: 350,
			maxHeight: 300,
		});
		document.getElementById("loadingSpinner").style.display = 'none';
	}
</script>
<script type="text/javascript" src="js/buildCard.js"></script>
<script type="text/javascript" src="js/map.js"></script>
<script type="text/javascript" src="js/markerclusterer.js"></script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAOHy-pnGspEEYO_T7mW9GV-wyO54FubC4&libraries=places&callback=initialize">
</script>