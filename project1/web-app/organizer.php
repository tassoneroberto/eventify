<?php
if ( !isset( $_SESSION[ "logged" ] ) || ( isset( $_SESSION[ "logged" ] ) && !$_SESSION[ "logged" ] ) ) {
	echo '<script>window.location = "/index.php";
</script>';
}
$email = $_SESSION[ "email" ];
$account_id = $_SESSION[ "account_id" ];
$organizer_name = $_SESSION[ "name" ];
$phone = $_SESSION[ "phone" ];
$location = $_SESSION[ "location" ];
$latitude = $_SESSION[ "latitude" ];
$longitude = $_SESSION[ "longitude" ];


?>

<!--
$msg = array
(
    'body' => "corpo",
    'title' => "titolo",
    'vibrate' => 1,
    'sound' => 1,
);
$fields = array
(
    'to' => 'fldkgJjv0JE:APA91bGQBCXYy0qzM1V3C-L-Vs0n855TbwXtmzQrTQtrKOfieMEChTmOmKwlYl4lzdeVdiEwe0NsqYVMCQYJFLDmaTvPrRNfVkGynkXwcNkAerp0u58koOaIGA8FwNi5Hkh2ciYP2DVK',
    'notification' => $msg
);

$headers = array
(
    'Authorization: key=' . "AIzaSyBUZXdbPKAdlOnalpJkDKDtvm9_yASsgC4",
    'Content-Type: application/json'
);

$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );

$result = curl_exec($ch );
curl_close( $ch );
return $result;
-->
<script>
	// GLOBAL JS VARIABLES
	var redirect = true;
</script>
<script type="text/javascript" src="js/organizerManager.js"></script>
<script type="text/javascript" src="js/buildCard.js"></script>
<script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
<div id="contentwrapper">
	<div id="contentcolumn"  style="overflow-y: scroll">
		<div id="card-info-organizer"class="pmd-card pmd-card-default " style="height: 281px;overflow: hidden; min-width: 400px; width: 100%;padding: 6px;">
			<div style="float: right;width: 100%;height: 281px;">
				<div id="card-info-organizer-map" style="margin-right:388px; width: 388px;height: 269px;border-radius: 6px;z-index:1!important;">
					<div  style="width: 388px;height: 269px;border-radius: 6px" id="map"></div>
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
							<h3 class="pmd-card-title-text"><span>Email:  &emsp;&emsp;</span><?php echo $email; ?></h3>
						</div>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">location_on</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Address: &emsp;</span><?php echo $location; ?></h3> </div>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">phone</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Phone: &emsp;&emsp;</span><?php echo $phone; ?></h3> </div>
					</div>
					<div style="margin: 6px 0px;">
						<div class="media-left media-middle"> <i style="color:#ff7300" class="material-icons pmd-sm">perm_identity</i> </div>
						<div class="media-left media-middle">
							<h3 class="pmd-card-title-text"><span>Account ID: </span><?php echo $account_id; ?></h3> </div>
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
	<button style="position: fixed;z-index: 100;top: 83px;right: 9px;" class="btn pmd-btn-fab pmd-z-depth-3 pmd-ripple-effect orange" type="button" data-target="#form-new" data-toggle="modal"><i class="material-icons pmd-sm">add</i></button>
	<div id="cardsContainer" class="innertube">
	</div>
</div>
<!-- start EDIT EVENT -->
<div tabindex="-1" class="modal fade" id="form-edit" style="display: none;" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header pmd-modal-bordered">
				<button aria-hidden="true" data-dismiss="modal" class="close" style="color:white" type="button">×</button>
				<h2 class="pmd-card-title-text">Edit event</h2>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group pmd-textfield pmd-textfield-floating-label form-group-lg pmd-textfield-floating-label-completed">
						<label for="Large" class="control-label">Title</label>
						<input type="Large" id="editEventTitle" class="form-control input-group-lg">
						<input type="Large" id="editEventId" style="display:none;">
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
						<label style="margin-bottom: 32px;">Select Category</label>
						<select onchange="selectTags('editEvent')" id="editEventCategory" class="select-simple form-control pmd-select2 select2-hidden-accessible" style="width: 100%;">
							<option></option>
							<option id="catfestival">Festival</option>
							<option id="catfooddrink">Food &#38; Drink</option>
							<option id="catdisco">Disco</option>
							<option id="catlivemusic">Live Music</option>
							<option id="catcinema">Cinema</option>
							<option id="catoutdoor">Outdoor</option>
							<option id="cattheatre">Theatre</option>
							<option id="catmuseum">Museum</option>
						</select>
					</div>
					<div id="tagsContainer" class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
						<label class="control-label" id="editEventTagsLabel" style="margin-bottom: 32px;">Select Multiple Tags</label>
						<select id="editEventTags" class="select-simple form-control pmd-select2 select2-hidden-accessible" multiple style="width: 100%;">
							<option disabled>Select a category in order to insert tags</option>
						</select>
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
						<label for="regular1" class="control-label">Address</label>
					
						<input type="text" id="editEventLocation" class="form-control">
					</div>
					<input type="text" id="editEventLatitude" class="form-control" style="display:none;">
						<input type="text" id="editEventLongitude" class="form-control" style="display:none;">
					<div id="editEventDescriptionLabel" class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
						<label class="control-label">Description</label>
						<textarea id="editEventDescription" class="form-control"></textarea>
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label form-group-lg pmd-textfield-floating-label-completed">
						<label for="Large" class="control-label">Phone</label>
						<input type="Large" id="editEventPhone" class="form-control input-group-lg">
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
								<label class="control-label" for="regular1" id="editEventStartLabel">Start Date</label>
								<input type="text" class="form-control" id="editEventOpening">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
								<label class="control-label" for="regular1" id="editEventEndLabel">End Date</label>
								<input type="text" class="form-control" id="editEventEnding">
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="pmd-modal-action">
				<button data-dismiss="" class="btn pmd-btn-flat
              pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="editEvent()">EDIT</button>
				<button data-dismiss="modal" class="btn pmd-btn-flat
              pmd-ripple-effect btn-default" type="button">DISCARD</button>
				<span id="editEventMessage"></span>
			</div>
		</div>
	</div>
</div>
<!-- end EDIT EVENT -->
<!-- start CREATE EVENT -->
<div tabindex="-1" class="modal fade" id="form-new" style="display: none;" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header pmd-modal-bordered">
				<button aria-hidden="true" data-dismiss="modal" class="close" style="color:white" type="button">×</button>
				<h2 class="pmd-card-title-text">Create event</h2>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group pmd-textfield pmd-textfield-floating-label form-group-lg">
						<label for="Large" class="control-label">Title</label>
						<input type="Large" id="createEventTitle" class="form-control input-group-lg">
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
						<label style="margin-bottom: 32px;">Select Category</label>
						<select onchange="selectTags('createEvent')" id="createEventCategory" class="select-simple form-control pmd-select2" style="width: 100%;">
							<option></option>
							<option>Festival</option>
							<option>Food &#38; Drink</option>
							<option>Disco</option>
							<option>Live Music</option>
							<option>Cinema</option>
							<option>Outdoor</option>
							<option>Theatre</option>
							<option>Museum</option>
						</select>
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label pmd-textfield-floating-label-completed">
						<label class="control-label" style="margin-bottom: 32px;">Select Multiple Tags</label>
						<select id="createEventTags" class="select-tags form-control pmd-select2-tags" multiple style="width: 100%;">
							<option disabled>Select a category in order to insert tags</option>
						</select>
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label">
						<label for="regular1" class="control-label"> Address </label>
						<input type="text" id="createEventLocation" class="form-control" value="<?php echo $location;?>">
					</div>
					<input type="text" id="createEventLatitude" value="<?php echo $latitude;?>" class="form-control" style="display:none;">
						<input type="text" id="createEventLongitude" value="<?php echo $longitude;?>" class="form-control" style="display:none;">
					<div class="form-group pmd-textfield pmd-textfield-floating-label">
						<label class="control-label">Description</label>
						<textarea id="createEventDescription" required class="form-control"></textarea>
					</div>
					<div class="form-group pmd-textfield pmd-textfield-floating-label form-group-lg">
						<label for="Large" class="control-label">Phone</label>
						<input type="Large" id="createEventPhone" value="<?php echo $phone;?>" class="form-control input-group-lg">
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label class="control-label" for="regular1">Start Date</label>
								<input type="text" class="form-control" id="createEventOpening">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label class="control-label" for="regular1">End Date</label>
								<input type="text" class="form-control" id="createEventEnding">
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="pmd-modal-action">
				<button data-dismiss="" class="btn pmd-btn-flat
              pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="createNewEvent()">CREATE </button>
				<button data-dismiss="modal" class="btn pmd-btn-flat
              pmd-ripple-effect btn-default" type="button">DIMISS</button>
				<span id="createEventMessage"></span>
			</div>
		</div>
	</div>
</div>
<!-- end CREATE EVENT -->
</div>
<script>
		function fixes() {
		document.getElementById( 'createEventLocation' ).placeholder = "";
	}
	window.onload = fixes();

	function fillEditEventForm( event_id, title, category, opening, ending, location, latitude, longitude, description, tags, phone ) {
		document.getElementById( 'editEventId' ).value = event_id;
		document.getElementById( 'editEventTitle' ).value = title;
		document.getElementById( 'editEventDescription' ).value = description;
		document.getElementById( 'editEventLocation' ).value = location;
		document.getElementById( 'editEventLatitude' ).value = latitude;
		document.getElementById( 'editEventLongitude' ).value = longitude;
		document.getElementById( 'editEventPhone' ).value = phone;
		document.getElementById( 'editEventOpening' ).value = opening;
		document.getElementById( 'editEventEnding' ).value = ending;
		document.getElementById( 'editEventCategory' ).value = category;
		$( "#editEventCategory" ).select2( {
			initSelection: function ( element, callback ) {
					var data = {
						id: 'cat' + category.replace( / |&amp;|&/g, "" ).toLowerCase(),
						text: category
					};
					callback( data );
				} //Then the rest of your configurations (e.g.: ajax, allowClear, etc.)
		} );
		$( "#editEventTags" ).select2( {
			initSelection: function ( element, callback ) {
				var data = []; //Array
				var tagList = tags.split( "-" );
				if ( tagList.length > 0 ) {
					for ( var i = 0; i < tagList.length; i++ ) {
						tagList[ i ] = tagList[ i ].replace( /^\s*/, "" ).replace( /\s*$/, "" );
						data.push( {
							id: "tag" + tagList[ i ],
							text: tagList[ i ]
						} ); //Push values to data array
						//$("#editEventTags").trigger("change");
					}
				}
				callback( data ); //Fill'em
			}
		} );
		var tagList = tags.split( "-" );
		for ( var i = 0; i < tagList.length; i++ ) {
			tagList[ i ] = tagList[ i ].replace( /^\s*/, "" ).replace( /\s*$/, "" );
			// Add additional code here, such as:
		}
		/*
    var tagsContainer =  document.getElementById("tagsContainer");
	tagsContainer.parentNode.insertBefore("<span id=\"test\">inserito</span>", referenceNode.nextSibling);
    var txt = "";
    var i;
    for (i = 0; i < c.length; i++) {
        txt = txt + c[i].innerHTML + "-";
    }
	 */
		var tagsContainer = document.getElementById( "editEventTags" );
		tagsContainer.innerHTML = "";
		if ( category == "Cinema" ) {
			tagsContainer.innerHTML += "<option>Spy film</option>";
			tagsContainer.innerHTML += "<option>Action thriller</option>";
			tagsContainer.innerHTML += "<option>Superhero film</option>";
			tagsContainer.innerHTML += "<option>Comedy of manners</option>";
			tagsContainer.innerHTML += "<option>Black comedy</option>";
			tagsContainer.innerHTML += "<option>Romantic comedy film</option>";
			tagsContainer.innerHTML += "<option>Teen movie</option>";
			tagsContainer.innerHTML += "<option>Crime drama</option>";
			tagsContainer.innerHTML += "<option>Historical drama</option>";
			tagsContainer.innerHTML += "<option>Docudrama</option>";
			tagsContainer.innerHTML += "<option>Legal drama</option>";
			tagsContainer.innerHTML += "<option>Psychodrama</option>";
			tagsContainer.innerHTML += "<option>Comedy drama</option>";
			tagsContainer.innerHTML += "<option>Melodrama</option>";
			tagsContainer.innerHTML += "<option>Tragedy</option>";
			tagsContainer.innerHTML += "<option>Docufiction</option>";
		} else if ( category == "Food & Drink" ) {
			tagsContainer.innerHTML += "<option>Fast Food</option>";
			tagsContainer.innerHTML += "<option>Restaurant</option>";
			tagsContainer.innerHTML += "<option>Steackhouse</option>";
			tagsContainer.innerHTML += "<option>Happy Hour</option>";
			tagsContainer.innerHTML += "<option>Cocktail</option>";
			tagsContainer.innerHTML += "<option>Beer</option>";
			tagsContainer.innerHTML += "<option>Wine</option>";
			tagsContainer.innerHTML += "<option>Hamburger</option>";
			tagsContainer.innerHTML += "<option>Pizza</option>";
			tagsContainer.innerHTML += "<option>Italian Food</option>";
			tagsContainer.innerHTML += "<option>Chinese Food</option>";
			tagsContainer.innerHTML += "<option>Japan Food</option>";
			tagsContainer.innerHTML += "<option>Mexican Food</option>";
			tagsContainer.innerHTML += "<option>Greek Food</option>";
			tagsContainer.innerHTML += "<option>Indian Food</option>";
		} else if ( category == "Disco" ) {
			tagsContainer.innerHTML += "<option>Dance</option>";
			tagsContainer.innerHTML += "<option>Latino</option>";
			tagsContainer.innerHTML += "<option>House</option>";
			tagsContainer.innerHTML += "<option>Dub</option>";
			tagsContainer.innerHTML += "<option>Progressive</option>";
			tagsContainer.innerHTML += "<option>Hardcore</option>";
			tagsContainer.innerHTML += "<option>DJ</option>";
			tagsContainer.innerHTML += "<option>Vip</option>";
			tagsContainer.innerHTML += "<option>Special Guest</option>";
			tagsContainer.innerHTML += "<option>Dance Floor</option>";
			tagsContainer.innerHTML += "<option>Cocktail</option>";
		} else if ( category == "Live Music" ) {
			tagsContainer.innerHTML += "<option>Cover Band</option>";
			tagsContainer.innerHTML += "<option>Live Albums</option>";
			tagsContainer.innerHTML += "<option>Live Singles</option>";
			tagsContainer.innerHTML += "<option>Concerts</option>";
			tagsContainer.innerHTML += "<option>Music Festivals</option>";
			tagsContainer.innerHTML += "<option>Live Coding</option>";
			tagsContainer.innerHTML += "<option>Music Venues</option>";
		} else if ( category == "Festival" ) {
			tagsContainer.innerHTML += "<option>Music</option>";
			tagsContainer.innerHTML += "<option>Cinema</option>";
			tagsContainer.innerHTML += "<option>Literature</option>";
			tagsContainer.innerHTML += "<option>Theatre</option>";
			tagsContainer.innerHTML += "<option>Art</option>";
			tagsContainer.innerHTML += "<option>Religion</option>";
			tagsContainer.innerHTML += "<option>Food and Drink</option>";
			tagsContainer.innerHTML += "<option>Beer</option>";
			tagsContainer.innerHTML += "<option>Season Festival</option>";
			tagsContainer.innerHTML += "<option>Hippy</option>";
		} else if ( category == "Outdoor" ) {
			tagsContainer.innerHTML += "<option>Charity Fundraiser</option>";
			tagsContainer.innerHTML += "<option>Food</option>";
			tagsContainer.innerHTML += "<option>Beverage</option>";
			tagsContainer.innerHTML += "<option>Golf</option>";
			tagsContainer.innerHTML += "<option>Concert</option>";
			tagsContainer.innerHTML += "<option>Auction</option>";
			tagsContainer.innerHTML += "<option>Awards</option>";
			tagsContainer.innerHTML += "<option>Cabaret</option>";
			tagsContainer.innerHTML += "<option>Car Boot Sale</option>";
			tagsContainer.innerHTML += "<option>Celebration</option>";
			tagsContainer.innerHTML += "<option>Conference</option>";
			tagsContainer.innerHTML += "<option>Convention</option>";
			tagsContainer.innerHTML += "<option>Flash Mob</option>";
			tagsContainer.innerHTML += "<option>Party</option>";
		} else if ( category == "Theatre" ) {
			tagsContainer.innerHTML += "<option>Drama</option>";
			tagsContainer.innerHTML += "<option>Musical</option>";
			tagsContainer.innerHTML += "<option>Comedy</option>";
			tagsContainer.innerHTML += "<option>Tragedy</option>";
			tagsContainer.innerHTML += "<option>Improvisation</option>";
		} else if ( category == "Museum" ) {
			tagsContainer.innerHTML += "<option>Archaeology</option>";
			tagsContainer.innerHTML += "<option>Anthropology</option>";
			tagsContainer.innerHTML += "<option>Ethnology</option>";
			tagsContainer.innerHTML += "<option>Military history</option>";
			tagsContainer.innerHTML += "<option>Cultural history</option>";
			tagsContainer.innerHTML += "<option>Science</option>";
			tagsContainer.innerHTML += "<option>Technology</option>";
			tagsContainer.innerHTML += "<option>Children's museums</option>";
			tagsContainer.innerHTML += "<option>Natural history</option>";
			tagsContainer.innerHTML += "<option>Numismatics</option>";
			tagsContainer.innerHTML += "<option>Botanical gardens</option>";
			tagsContainer.innerHTML += "<option>Zoological gardens</option>";
			tagsContainer.innerHTML += "<option>Philately</option>";
			tagsContainer.innerHTML += "<option>Party</option>";
		} else {
			tagsContainer.innerHTML = "<option disabled>Select a category in order to insert tags</option>";
		}
		if ( description == "" ) {
			document.getElementById( 'editEventDescriptionLabel' ).classList.remove( "pmd-textfield-floating-label-completed" );
		}
	}

	function editEvent() {
		var event_id = document.getElementById( 'editEventId' ).value;
		var account_id = "<?php echo $account_id ?>";
		var organizer_name = "<?php echo $organizer_name ?>";
		var title = document.getElementById( 'editEventTitle' ).value;
		var description = document.getElementById( 'editEventDescription' ).value;
		var location = document.getElementById( 'editEventLocation' ).value;
		var latitude = document.getElementById( 'editEventLatitude' ).value;
		var longitude = document.getElementById( 'editEventLongitude' ).value;
		var phone = document.getElementById( 'editEventPhone' ).value;
		var opening = document.getElementById( 'editEventOpening' ).value;
		var ending = document.getElementById( 'editEventEnding' ).value;
		var category = document.getElementById( 'editEventCategory' ).value;
		$( '#editEventTags' ).trigger( 'change' );
		var newTags = $( "#editEventTags" ).select2( "val" );
		var tags = "";
		var i;
		if ( newTags.length > 0 ) {
			for ( i = 0; i < newTags.length; i++ ) {
				tags += newTags[ i ] + "-";
			}
			tags = tags.slice( 0, -1 );
		}
		if ( title == "" || location == "" || category == "" )
			document.getElementById( "editEventMessage" ).innerHTML = "Wrong input parameters!";
		else {
			document.getElementById( "loadingSpinner" ).style.display = 'inherit';
			var data = {
				"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
				"operation": "modifyEvent",
				"account_id": account_id,
				"event": {
					"id": event_id,
					"organizer_name": organizer_name,
					"title": title,
					"description": description,
					"location": location,
					"latitude": latitude,
					"longitude": longitude,
					"phone": phone,
					"opening": opening,
					"openingTime": "",
					"ending": ending,
					"endingTime": "",
					"category": category,
					"tags": tags
				}
			}
			$.ajax( {
				url: 'https://apisocialevent.altervista.org/API/',
				type: 'POST',
				data: JSON.stringify( data ),
				contentType: 'application/json',
				dataType: 'json',
				async: true,
				success: function ( response ) {
					document.getElementById( "loadingSpinner" ).style.display = 'none';
					if ( response.result == "success" ) {
						$( '#form-edit' ).modal( 'hide' );
						window.location.reload( false );
					} else {
						document.getElementById( "editEventMessage" ).innerHTML = response.message;
					}
				}
			} );
		}
	}

	function createNewEvent() {
		console.log("createNewEvent()");
		var account_id = "<?php echo $account_id ?>";
		var organizer_name = "<?php echo $organizer_name ?>";
		var title = document.getElementById( 'createEventTitle' ).value;
		var description = document.getElementById( 'createEventDescription' ).value;
		var location = document.getElementById( 'createEventLocation' ).value;
		var latitude = document.getElementById( 'createEventLatitude' ).value;
		var longitude = document.getElementById( 'createEventLongitude' ).value;
		var phone = document.getElementById( 'createEventPhone' ).value;
		var opening = document.getElementById( 'createEventOpening' ).value;
		var ending = document.getElementById( 'createEventEnding' ).value;
		var category = document.getElementById( 'createEventCategory' ).value;
		$( '#createEventTags' ).trigger( 'change' );
		var newTags = $( "#createEventTags" ).select2( "val" );
		var tags = "";
		var i;
		if ( newTags.length > 0 ) {
			for ( i = 0; i < newTags.length; i++ ) {
				tags += newTags[ i ] + "-";
			}
			tags = tags.slice( 0, -1 );
		}
		if ( title == "" || location == "" || category == "" )
			document.getElementById( "createEventMessage" ).innerHTML = "Wrong input parameters!";
		else {
			document.getElementById( "loadingSpinner" ).style.display = 'inherit';
			var data = {
				"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
				"operation": "insertEvent",
				"account_id": account_id,
				"event": {
					"organizer_name": organizer_name,
					"title": title,
					"description": description,
					"location": location,
					"latitude": latitude,
					"longitude": longitude,
					"phone": phone,
					"opening": opening,
					"openingTime": "",
					"ending": ending,
					"endingTime": "",
					"category": category,
					"tags": tags
				}
			}
			$.ajax( {
				url: 'https://apisocialevent.altervista.org/API/',
				type: 'POST',
				data: JSON.stringify( data ),
				contentType: 'application/json',
				dataType: 'json',
				async: true,
				success: function ( response ) {
					document.getElementById( "loadingSpinner" ).style.display = 'none';
					if ( response.result == "success" ) {
						$( '#form-new' ).modal( 'hide' );
						window.location.reload( false );
					} else {
						document.getElementById( "createEventMessage" ).innerHTML = response.message;
					}
				}
			} );
		}
	}
	$( '#form-edit' ).on( 'show.bs.modal', function ( e ) {
		var $modal = $( this ),
			esseyId = e.relatedTarget.id;
		//            $.ajax({
		//                cach: false,
		//                type: 'POST',
		//                url: 'backend.php',
		//                data: 'EID='+essay_id,
		//                success: function(data) 
		//                {
		$modal.find( '.edit-content' ).html( esseyId );
		//                }
		//            });
	} )
</script>
<script>
	var latitude = "<?php echo $latitude;?>";
	var longitude = "<?php echo $longitude;?>";

	function initMap() {
		var uluru = {
			lat: parseFloat( latitude ),
			lng: parseFloat( longitude )
		};
		var map = new google.maps.Map( document.getElementById( 'map' ), {
			zoom: 14,
			center: uluru
		} );
		var marker = new google.maps.Marker( {
			position: uluru,
			map: map
		} );
		var input = document.getElementById( 'editEventLocation' );
		var autocomplete = new google.maps.places.Autocomplete( input );
		//alert(autocomplete);
		autocomplete.addListener( 'place_changed', function () {
			var place = autocomplete.getPlace();
			if ( !place.geometry ) {
				// User entered the name of a Place that was not suggested and
				// pressed the Enter key, or the Place Details request failed.
				window.alert( "No details available for input: '" + place.name + "'" );
				return;
			}
			var address = '';
			if ( place.address_components ) {
				address = [
					( place.address_components[ 0 ] && place.address_components[ 0 ].short_name || '' ),
					( place.address_components[ 1 ] && place.address_components[ 1 ].short_name || '' ),
					( place.address_components[ 2 ] && place.address_components[ 2 ].short_name || '' )
				].join( ' ' );
			}
			var geocoder = new google.maps.Geocoder();
			geocoder.geocode( {
				address: address
			}, function ( results, status ) {
				if ( status == google.maps.GeocoderStatus.OK ) {
					var lat = results[ 0 ].geometry.location.lat();
					var lng = results[ 0 ].geometry.location.lng();
					document.getElementById( 'editEventLatitude' ).value = lat;
					document.getElementById( 'editEventLongitude' ).value = lng;
				} else {
					alert( "Google Maps not found address!" );
				}
			} );
		} );
		var input2 = document.getElementById( 'createEventLocation' );
		var autocomplete2 = new google.maps.places.Autocomplete( input2 );
		//alert(autocomplete);
		autocomplete2.addListener( 'place_changed', function () {
			var place2 = autocomplete2.getPlace();
			if ( !place2.geometry ) {
				// User entered the name of a Place that was not suggested and
				// pressed the Enter key, or the Place Details request failed.
				window.alert( "No details available for input: '" + place2.name + "'" );
				return;
			}
			var address2 = '';
			if ( place2.address_components ) {
				address2 = [
					( place2.address_components[ 0 ] && place2.address_components[ 0 ].short_name || '' ),
					( place2.address_components[ 1 ] && place2.address_components[ 1 ].short_name || '' ),
					( place2.address_components[ 2 ] && place2.address_components[ 2 ].short_name || '' )
				].join( ' ' );
			}
			var geocoder2 = new google.maps.Geocoder();
			geocoder2.geocode( {
				address: address2
			}, function ( results, status ) {
				if ( status == google.maps.GeocoderStatus.OK ) {
					var lat2 = results[ 0 ].geometry.location.lat();
					var lng2 = results[ 0 ].geometry.location.lng();
					document.getElementById( 'createEventLatitude' ).value = lat2;
					document.getElementById( 'createEventLongitude' ).value = lng2;
				} else {
					alert( "Google Maps not found address!" );
				}
			} );
		} );
	}
</script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAOHy-pnGspEEYO_T7mW9GV-wyO54FubC4&libraries=places&callback=initMap">
</script>
<script>
	function selectTags( form ) {
		var tagsContainer = document.getElementById( form + "Tags" );
		var category = document.getElementById( form + "Category" ).value;
		tagsContainer.innerHTML = "";
		$( '#' + form + "Tags" ).empty().trigger( 'change' );
		if ( category == "Cinema" ) {
			tagsContainer.innerHTML += "<option>Spy film</option>";
			tagsContainer.innerHTML += "<option>Action thriller</option>";
			tagsContainer.innerHTML += "<option>Superhero film</option>";
			tagsContainer.innerHTML += "<option>Comedy of manners</option>";
			tagsContainer.innerHTML += "<option>Black comedy</option>";
			tagsContainer.innerHTML += "<option>Romantic comedy film</option>";
			tagsContainer.innerHTML += "<option>Teen movie</option>";
			tagsContainer.innerHTML += "<option>Crime drama</option>";
			tagsContainer.innerHTML += "<option>Historical drama</option>";
			tagsContainer.innerHTML += "<option>Docudrama</option>";
			tagsContainer.innerHTML += "<option>Legal drama</option>";
			tagsContainer.innerHTML += "<option>Psychodrama</option>";
			tagsContainer.innerHTML += "<option>Comedy drama</option>";
			tagsContainer.innerHTML += "<option>Melodrama</option>";
			tagsContainer.innerHTML += "<option>Tragedy</option>";
			tagsContainer.innerHTML += "<option>Docufiction</option>";
		} else if ( category == "Food & Drink" ) {
			tagsContainer.innerHTML += "<option>Fast Food</option>";
			tagsContainer.innerHTML += "<option>Restaurant</option>";
			tagsContainer.innerHTML += "<option>Steackhouse</option>";
			tagsContainer.innerHTML += "<option>Happy Hour</option>";
			tagsContainer.innerHTML += "<option>Cocktail</option>";
			tagsContainer.innerHTML += "<option>Beer</option>";
			tagsContainer.innerHTML += "<option>Wine</option>";
			tagsContainer.innerHTML += "<option>Hamburger</option>";
			tagsContainer.innerHTML += "<option>Pizza</option>";
			tagsContainer.innerHTML += "<option>Italian Food</option>";
			tagsContainer.innerHTML += "<option>Chinese Food</option>";
			tagsContainer.innerHTML += "<option>Japan Food</option>";
			tagsContainer.innerHTML += "<option>Mexican Food</option>";
			tagsContainer.innerHTML += "<option>Greek Food</option>";
			tagsContainer.innerHTML += "<option>Indian Food</option>";
		} else if ( category == "Disco" ) {
			tagsContainer.innerHTML += "<option>Dance</option>";
			tagsContainer.innerHTML += "<option>Latino</option>";
			tagsContainer.innerHTML += "<option>House</option>";
			tagsContainer.innerHTML += "<option>Dub</option>";
			tagsContainer.innerHTML += "<option>Progressive</option>";
			tagsContainer.innerHTML += "<option>Hardcore</option>";
			tagsContainer.innerHTML += "<option>DJ</option>";
			tagsContainer.innerHTML += "<option>Vip</option>";
			tagsContainer.innerHTML += "<option>Special Guest</option>";
			tagsContainer.innerHTML += "<option>Dance Floor</option>";
			tagsContainer.innerHTML += "<option>Cocktail</option>";
		} else if ( category == "Live Music" ) {
			tagsContainer.innerHTML += "<option>Cover Band</option>";
			tagsContainer.innerHTML += "<option>Live Albums</option>";
			tagsContainer.innerHTML += "<option>Live Singles</option>";
			tagsContainer.innerHTML += "<option>Concerts</option>";
			tagsContainer.innerHTML += "<option>Music Festivals</option>";
			tagsContainer.innerHTML += "<option>Live Coding</option>";
			tagsContainer.innerHTML += "<option>Music Venues</option>";
		} else if ( category == "Festival" ) {
			tagsContainer.innerHTML += "<option>Music</option>";
			tagsContainer.innerHTML += "<option>Cinema</option>";
			tagsContainer.innerHTML += "<option>Literature</option>";
			tagsContainer.innerHTML += "<option>Theatre</option>";
			tagsContainer.innerHTML += "<option>Art</option>";
			tagsContainer.innerHTML += "<option>Religion</option>";
			tagsContainer.innerHTML += "<option>Food and Drink</option>";
			tagsContainer.innerHTML += "<option>Beer</option>";
			tagsContainer.innerHTML += "<option>Season Festival</option>";
			tagsContainer.innerHTML += "<option>Hippy</option>";
		} else if ( category == "Outdoor" ) {
			tagsContainer.innerHTML += "<option>Charity Fundraiser</option>";
			tagsContainer.innerHTML += "<option>Food</option>";
			tagsContainer.innerHTML += "<option>Beverage</option>";
			tagsContainer.innerHTML += "<option>Golf</option>";
			tagsContainer.innerHTML += "<option>Concert</option>";
			tagsContainer.innerHTML += "<option>Auction</option>";
			tagsContainer.innerHTML += "<option>Awards</option>";
			tagsContainer.innerHTML += "<option>Cabaret</option>";
			tagsContainer.innerHTML += "<option>Car Boot Sale</option>";
			tagsContainer.innerHTML += "<option>Celebration</option>";
			tagsContainer.innerHTML += "<option>Conference</option>";
			tagsContainer.innerHTML += "<option>Convention</option>";
			tagsContainer.innerHTML += "<option>Flash Mob</option>";
			tagsContainer.innerHTML += "<option>Party</option>";
		} else if ( category == "Theatre" ) {
			tagsContainer.innerHTML += "<option>Drama</option>";
			tagsContainer.innerHTML += "<option>Musical</option>";
			tagsContainer.innerHTML += "<option>Comedy</option>";
			tagsContainer.innerHTML += "<option>Tragedy</option>";
			tagsContainer.innerHTML += "<option>Improvisation</option>";
		} else if ( category == "Museum" ) {
			tagsContainer.innerHTML += "<option>Archaeology</option>";
			tagsContainer.innerHTML += "<option>Anthropology</option>";
			tagsContainer.innerHTML += "<option>Ethnology</option>";
			tagsContainer.innerHTML += "<option>Military history</option>";
			tagsContainer.innerHTML += "<option>Cultural history</option>";
			tagsContainer.innerHTML += "<option>Science</option>";
			tagsContainer.innerHTML += "<option>Technology</option>";
			tagsContainer.innerHTML += "<option>Children's museums</option>";
			tagsContainer.innerHTML += "<option>Natural history</option>";
			tagsContainer.innerHTML += "<option>Numismatics</option>";
			tagsContainer.innerHTML += "<option>Botanical gardens</option>";
			tagsContainer.innerHTML += "<option>Zoological gardens</option>";
			tagsContainer.innerHTML += "<option>Philately</option>";
			tagsContainer.innerHTML += "<option>Party</option>";
		} else {
			tagsContainer.innerHTML = "<option disabled>Select a category in order to insert tags</option>";
		}
	}
</script>
<script>
	// NEW EVENT
	// Linked date and time picker 
	// start date date and time picker 
	$( '#createEventOpening' ).datetimepicker();
	// End date date and time picker 
	$( '#createEventEnding' ).datetimepicker( {
		useCurrent: false
	} );
	// start date picke on chagne event [select minimun date for end date datepicker]
	$( "#createEventOpening" ).on( "dp.change", function ( e ) {
		$( '#createEventEnding' ).data( "DateTimePicker" ).minDate( e.date );
	} );
	// Start date picke on chagne event [select maxmimum date for start date datepicker]
	$( "#createEventEnding" ).on( "dp.change", function ( e ) {
		$( '#createEventOpening' ).data( "DateTimePicker" ).maxDate( e.date );
	} );
</script>
<script>
	// EDIT EVENT
	// Linked date and time picker 
	// start date date and time picker 
	$( '#editEventOpening' ).datetimepicker();
	// End date date and time picker 
	$( '#editEventEnding' ).datetimepicker( {
		useCurrent: false
	} );
	// start date picke on chagne event [select minimun date for end date datepicker]
	$( "#editEventOpening" ).on( "dp.change", function ( e ) {
		$( '#editEventEnding' ).data( "DateTimePicker" ).minDate( e.date );
	} );
	// Start date picke on chagne event [select maxmimum date for start date datepicker]
	$( "#editEventEnding" ).on( "dp.change", function ( e ) {
		$( '#editEventOpening' ).data( "DateTimePicker" ).maxDate( e.date );
	} );
</script>
<script>
	getOwnedEvents("<?php echo $account_id; ?>");
</script>
<script src="https://www.gstatic.com/firebasejs/4.3.1/firebase.js"></script>
<script>
 var key = 'AIzaSyBUZXdbPKAdlOnalpJkDKDtvm9_yASsgC4';
var to = 'YOUR-IID-TOKEN';
var notification = {
  'title': 'Portugal vs. Denmark',
  'body': '5 to 1',
  'icon': 'firebase-logo.png',
  'click_action': 'http://localhost:8081'
};

fetch('https://fcm.googleapis.com/fcm/send', {
  'method': 'POST',
  'headers': {
    'Authorization': 'key=' + key,
    'Content-Type': 'application/json'
  },
  'body': JSON.stringify({
    'notification': notification,
    'to': to
  })
}).then(function(response) {
}).catch(function(error) {
  console.error(error);
})
</script>