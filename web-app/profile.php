<?php
$url     = 'https://apisocialevent.altervista.org/API/';
$data    = array(
	'api_key' => 'hi1OrDf58decjXluKusJSYaVNGqI3IVu',
	'operation' => 'getAccountInfo',
	'account_id' => '' . $_GET[id] . ''
);
$options = array(
	'http' => array(
		'header' => "Content-type: application/x-www-form-urlencoded\r\n",
		'method' => 'POST',
		'content' => json_encode($data)
	)
);
$context = stream_context_create($options);
$result  = file_get_contents($url, false, $context);
$orgInfo = json_decode($result, true);
$email = $orgInfo["email"];
$account_id = $orgInfo["account_id"];
$organizer_name = $orgInfo["organizer_name"];
$phone = $orgInfo["phone"];
$location = $orgInfo["location"];
$latitude = $orgInfo["latitude"];
$longitude = $orgInfo["longitude"];
$organizer_account_id = $_GET[id];
?>

<script>
	var redirect = true;
</script>
<script type="text/javascript" src="js/organizerManager.js"></script>
<script type="text/javascript" src="js/buildCard.js"></script>
<script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
<div id="contentwrapper">
	<div id="contentcolumn" style="overflow-y: scroll">
		<div id="card-info-organizer" class="pmd-card pmd-card-default card-info-organizer-visitors" style="height: 281px;overflow: hidden; min-width: 400px; width: 100%;padding: 6px;">
			<div style="float: right;width: 100%;height: 281px;">
				<div id="card-info-organizer-map" style="margin-right:388px; width: 388px;height: 269px;border-radius: 6px;z-index:1!important;">
					<div style="width: 388px;height: 269px;border-radius: 6px" id="map"></div>
				</div>
			</div>
			<div id="card-info-organizer-panel" style="background: rgba(81,71,255,.85);border-radius: 6px;float: right;width: 388px;height: 269px;margin-right: -100%;position: relative;top: 0;left: 0; z-index : 2 !important;">
				<div class="pmd-card-title">
					<div style="margin: 9px 0px 18px 0px;">
						<h1 class="pmd-card-title-text" style="font-size: 42px">
							<?php echo $organizer_name; ?>
						</h1>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">email</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Email: &emsp;&emsp;</span><?php echo $email; ?></h3>
						</div>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">location_on</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Address: &emsp;</span><?php echo $location; ?></h3>
						</div>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">phone</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Phone: &emsp;&emsp;</span><?php echo $phone; ?></h3>
						</div>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">perm_identity</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Account ID: </span><?php echo $organizer_account_id; ?></h3>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="border-radius: 6px;padding: 6px;background-color: #5143ff;" id='calendar'></div>
		<div id="topEventsChartContainer" style="height: 300px; width: 100%;margin-top: 32px;">
		</div>
		<div id="chartContainer" style="height: 300px; width: 100%;margin-top: 32px;">
		</div>
	</div>
</div>
<div id="leftcolumn">

	<div id="cardsContainer" class="innertube">
	</div>
</div>

<script>
	var latitude = "<?php echo $latitude; ?>";
	var longitude = "<?php echo $longitude; ?>";

	function initMap() {
		var uluru = {
			lat: parseFloat(latitude),
			lng: parseFloat(longitude)
		};
		var map = new google.maps.Map(document.getElementById('map'), {
			zoom: 14,
			center: uluru
		});
		var marker = new google.maps.Marker({
			position: uluru,
			map: map
		});
		var input = document.getElementById('editEventLocation');
		var autocomplete = new google.maps.places.Autocomplete(input);
		autocomplete.addListener('place_changed', function() {
			var place = autocomplete.getPlace();
			if (!place.geometry) {
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
					document.getElementById('editEventLatitude').value = lat;
					document.getElementById('editEventLongitude').value = lng;
				} else {
					alert("Google Maps not found address!");
				}
			});
		});
		var input2 = document.getElementById('createEventLocation');
		var autocomplete2 = new google.maps.places.Autocomplete(input2);
		autocomplete2.addListener('place_changed', function() {
			var place2 = autocomplete2.getPlace();
			if (!place2.geometry) {
				window.alert("No details available for input: '" + place2.name + "'");
				return;
			}
			var address2 = '';
			if (place2.address_components) {
				address2 = [
					(place2.address_components[0] && place2.address_components[0].short_name || ''),
					(place2.address_components[1] && place2.address_components[1].short_name || ''),
					(place2.address_components[2] && place2.address_components[2].short_name || '')
				].join(' ');
			}
			var geocoder2 = new google.maps.Geocoder();
			geocoder2.geocode({
				address: address2
			}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					var lat2 = results[0].geometry.location.lat();
					var lng2 = results[0].geometry.location.lng();
					document.getElementById('createEventLatitude').value = lat2;
					document.getElementById('createEventLongitude').value = lng2;
				} else {
					alert("Google Maps not found address!");
				}
			});
		});
	}
</script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAOHy-pnGspEEYO_T7mW9GV-wyO54FubC4&libraries=places&callback=initMap">
</script>

<script>
	getOwnedEvents("<?php echo $organizer_account_id; ?>", true);
</script>