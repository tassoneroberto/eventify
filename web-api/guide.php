<style>
.demo-card-wide.mdl-card {
	width: 100%;
	min-height: inherit;
	padding: 15px;
}
.demo-card-wide > .mdl-card__menu {
	color: #fff;
}
table.innerborder td {
	vertical-align: top;
	border: 1px solid #999;
	text-align: center;
}
.jsonstructure {
	width: 100%;
	height: 170px;
	font-family: 'Courier New', Courier, monospace;
	font-size: 14px;
}
</style>
<main class="mdl-layout__content mdl-color--grey-100">
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>How to use it</h3>
    The APIs are based on the POST/GET request methods, supported by the HTTP protocol. The informations, or parameters, to send must be gathered in JSON packages. In order to work you need also to send your API key to ensure you are allowed to use these API. Below there are some basic instructions to begin.
    <h4>Instructions</h4>
    <ul>
      <li>Choose the function you are interested to use (See below for the complete list of all operations).</li>
      <li>Make the JSON package with all the parameters needed. The parameters include the personal API key. Below you can see a sample JSON package for the user registration function:<br />
        <textarea class="jsonstructure" readonly="readonly">
[  
    {  
        "api_key":"<?php echo $_SESSION["api_key"]; ?>",
        "operation":"registerUser",
        "firstname":"FIRSTNAME",
        "lastname":"LASTNAME",
        "email":"SAMPLE@MAIL.COM",
        "password":"PASSWORD"
    }
]</textarea>
        The "api_key" parameter specify your secret API key. It is needed to execute all function. The "operation" parameter specify the function you are interested to use. All other parameters are function related. In the "Interface" section are listed all function with each method name, description, input parameters ("api_key" and "operation" are omitted) and output parameters ("result" and "message" are omitted). </li>
      <li>Send a HTTP POST request to the URL <i>apisocialevent.altervista.org/API/</i> using as data the encoded JSON package.</li>
      <li>You'll receive an encoded JSON. You need to decode the package before use it. See below for the returned JSON package structure after a "registerUser" operation called:<br />
        <textarea class="jsonstructure" readonly="readonly">
[
    {  
        "result":"success/failure",
        "message":"ADDITIONAL_INFORMATION"
        "account_id":"ACCOUNT_ID",
    }
]</textarea>
      </li>
    </ul>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>User preferences</h3>
    <h4>Categories and Tags</h4>
    <p> To save user preferences you need to use the method "savePref". The "tags" parameter must be a list of category/tag of type "category1=tag1;category2=tag2;". For example a corrent string is "Cinema=Horror;Cinema=Tragedy;Food & Drink=Japan Food;Outdoor=Golf". Here the complete list of all categories and tags:
    <ul>
      <li>Cinema
        <ul>
          <li>Docufiction</li>
          <li>Tragedy</li>
          <li>Melodrama</li>
          <li>Comedy drama</li>
          <li>Psychodrama</li>
          <li>Legal drama</li>
          <li>Docudrama</li>
          <li>Historical drama</li>
          <li>Crime drama</li>
          <li>Teen movie</li>
          <li>Romantic comedy drama</li>
          <li>Comedy horror</li>
          <li>Black comedy</li>
          <li>Comedy of manners</li>
          <li>Superhero film</li>
          <li>Action thriller</li>
          <li>Spy film</li>
        </ul>
      </li>
      <li>Disco
        <ul>
          <li>Cocktail</li>
          <li>Dance Floor</li>
          <li>Special Guest</li>
          <li>Vip</li>
          <li>DJ</li>
          <li>Hardcore</li>
          <li>Progressive</li>
          <li>Dub</li>
          <li>House</li>
          <li>Latino</li>
          <li>Dance</li>
        </ul>
      </li>
      <li>Festival
        <ul>
          <li>Music</li>
          <li>Cinema</li>
          <li>Literature</li>
          <li>Theatre</li>
          <li>Art</li>
          <li>Religion</li>
          <li>Food and Drink</li>
          <li>Beer</li>
          <li>Seasonal Festival</li>
          <li>Hippy</li>
        </ul>
      </li>
      <li>Food &amp; Drink
        <ul>
          <li>Indian Food</li>
          <li>Greek Food</li>
          <li>Mexican Food</li>
          <li>Japan Food</li>
          <li>Chinese Food</li>
          <li>Italian Food</li>
          <li>Pizza</li>
          <li>Hamburger</li>
          <li>Wine</li>
          <li>Beer</li>
          <li>Cocktail</li>
          <li>Happy Hour</li>
          <li>Steackhouse</li>
          <li>Restaurant</li>
          <li>Fast Food</li>
        </ul>
      </li>
      <li>Live Music
        <ul>
          <li>Live Coding</li>
          <li>Music Festivals</li>
          <li>Concerts</li>
          <li>Live Singles</li>
          <li>Live Albums</li>
          <li>Cover Band</li>
          <li>Music Venues</li>
        </ul>
      </li>
      <li>Museum
        <ul>
          <li>Archaeology</li>
          <li>Anthropology</li>
          <li>Ethnology</li>
          <li>Military history</li>
          <li>Cultural history</li>
          <li>Science</li>
          <li>Technology</li>
          <li>Children's museums</li>
          <li>Natural history</li>
          <li>Numismatics</li>
          <li>Botanical gardens</li>
          <li>Zoological gardens</li>
          <li>Philately</li>
        </ul>
      </li>
      <li>Outdoor
        <ul>
          <li>Party</li>
          <li>Flash Mob</li>
          <li>Convention</li>
          <li>Conference</li>
          <li>Celebration</li>
          <li>Car Boot Sale</li>
          <li>Cabaret</li>
          <li>Awards</li>
          <li>Auction</li>
          <li>Concert</li>
          <li>Golf</li>
          <li>Beverage</li>
          <li>Food</li>
          <li>Charity Fundraiser</li>
        </ul>
      </li>
      <li>Theatre
        <ul>
          <li>Improvisation</li>
          <li>Tragedy</li>
          <li>Comedy</li>
          <li>Musical</li>
          <li>Drama</li>
        </ul>
      </li>
    </ul>
    </p>
    <hr />
    <h4>Range Time and Range Distance</h4>
    <p>The "rangeTime" and the "rangeDistance" parameters are used to specify the max limit of time and distance to get events with the method "getCustomEvents". This parameters need to be non-negative integers between 0 and 5. Below the table with all values (d=day, w=week, m=month):
    <table class="innerborder">
      <tr>
        <td style="border:none;"></td>
        <td>0</td>
        <td>1</td>
        <td>2</td>
        <td>3</td>
        <td>4</td>
        <td>5</td>
      </tr>
      <tr>
        <td>rangeTime</td>
        <td>1 d</td>
        <td>3 d</td>
        <td>1 w</td>
        <td>2 w</td>
        <td>1 m</td>
        <td>2 m</td>
      </tr>
      <tr>
        <td>rangeDistance</td>
        <td>1 km</td>
        <td>2 km</td>
        <td>10 km</td>
        <td>25 km</td>
        <td>50 km</td>
        <td>100 km</td>
      </tr>
    </table>
    </p>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Events structure</h3>
    <h4>Create event fields</h4>
    <p>An event may have all fields filled in the creation/modification phase. The "description" parameter is optional but we suggest you to fill also this field. Here a table of all event's informations:</p>
    <table class="innerborder">
      <tr>
        <td><b>Info</b></td>
        <td><b>Description</b></td>
        <td><b>Required</b></td>
      </tr>
      <tr>
        <td>account_id</td>
        <td>The id of the organizer account who created this event</td>
        <td>*</td>
      </tr>
      <tr>
        <td>organizer_name</td>
        <td>The name of the organizer account who created this event</td>
        <td></td>
      </tr>
      <tr>
        <td>title</td>
        <td>The title of the event. It should be a short string.</td>
        <td>*</td>
      </tr>
      <tr>
        <td>description</td>
        <td>The description of the event. There are no limits on the length.</td>
        <td></td>
      </tr>
      <tr>
        <td>location</td>
        <td>The address of the event.</td>
        <td>*</td>
      </tr>
      <tr>
        <td>latitude</td>
        <td>The latitude of the event's address.</td>
        <td>*</td>
      </tr>
      <tr>
        <td>longitude</td>
        <td>The longitude of the event's address.</td>
        <td>*</td>
      </tr>
      <tr>
        <td>phone</td>
        <td>The phone number related to the event.</td>
        <td>*</td>
      </tr>
      <tr>
        <td>opening</td>
        <td>The data of the event's opening. The accepted format is "Y-m-d".</td>
        <td>*</td>
      </tr>
      <tr>
        <td>openingTime</td>
        <td>The time of the event's opening. The accepted format is "H:i:s".</td>
        <td>*</td>
      </tr>
      <tr>
        <td>ending</td>
        <td>The data of the event's ending. The accepted format is "Y-m-d".</td>
        <td>*</td>
      </tr>
      <tr>
        <td>endingTime</td>
        <td>The time of the event's ending. The accepted format is "H:i:s".</td>
        <td>*</td>
      </tr>
      <tr>
        <td>category</td>
        <td>The category of the event. Each event has only one category.</td>
        <td>*</td>
      </tr>
      <tr>
        <td>tags</td>
        <td>The tags of the event. This parameter need to be in the form "tag1-tag2-tag3". There are no limit on the number of tags, but all tags need to be of the same category of event.</td>
        <td>*</td>
      </tr>
    </table>
    <h4>Get event fields</h4>
    <p>An event received after a get method will have the following structure;</p>
    <table class="innerborder">
      <tr>
        <td><b>Info</b></td>
        <td><b>Description</b></td>
      </tr>
      <tr>
        <td>id</td>
        <td>The id of the event</td>
      </tr>
      <tr><tr>
        <td>owner</td>
        <td>The id of the organizer account who created this event</td>
      </tr>
      <tr>
        <td>organizer_name</td>
        <td>The name of the organizer account who created this event</td>
      </tr>
      <tr>
        <td>title</td>
        <td>The title of the event.</td>
      </tr>
      <tr>
        <td>description</td>
        <td>The description of the event.</td>
      </tr>
      <tr>
        <td>location</td>
        <td>The address of the event.</td>
      </tr>
      <tr>
        <td>latitude</td>
        <td>The latitude of the event's address.</td>
      </tr>
      <tr>
        <td>longitude</td>
        <td>The longitude of the event's address.</td>
      </tr>
      <tr>
        <td>phone</td>
        <td>The phone number related to the event.</td>
      </tr>
      <tr>
        <td>opening</td>
        <td>The data of the event's opening in the format "Y-m-d H:i:s".</td>
      </tr>
      <tr>
        <td>ending</td>
        <td>The data of the event's ending in the format "Y-m-d H:i:s".</td>
      </tr>
      <tr>
        <td>category</td>
        <td>The category of the event. Each event has only one category.</td>
      </tr>
      <tr>
        <td>tags</td>
        <td>The tags of the event. This parameter is in the form "tag1-tag2-tag3".</td>
      </tr>
    </table>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <div id="interface">
      <h3>Interface</h3>
      <h4><u>System operations</u></h4>
      <h5>User registration</h5>
      <p>Method name: <code>registerUser</code></p>
      <p>Description: Allow to register a new user account. You must specify the complete name (firstname and lastname), a valid email and a password. It will return the assigned id for that user. You must use the user id as a parameter for other function like "addToCalendar", "savePref", etc.</p>
      <p>Input: <code>firstname, lastname, email, password</code></p>
      <p>Output: <code>account_id</code></p>
      <hr />
      <h5>Organizer registration</h5>
      <p>Method name: <code>registerOrganizer</code></p>
      <p>Description: Allow to register a new organizer account. You must specify the name, a valid email, a password, a phone number, a default location/address with associated latitude and longitude. It will return the assigned id for that organizer. You must use the organizer id as a parameter for other function like "insertEvent", "getOwnedEvents", etc.</p>
      <p>Input: <code>organizer_name, email, password, phone, location, latitude, longitude</code></p>
      <p>Output: <code>account_id</code></p>
      <hr />
      <h5>User/organizer login</h5>
      <p>Method name: <code>login</code></p>
      <p>Description: Standard login function for both user and organizer accounts. You must specify email and password. The parameter "is_organizer" is 1 or 0 if the account is an organizer or a user respectively. In the case of an organizer, the fields "firstname", "lastname" are empty. In the other case (user account) the fields "organizer_name", "location", "latitude", "longitude" and "phone" are empty.</p>
      <p>Input: <code>email, password</code></p>
      <p>Output: <code>account_id, email, firstname, lastname, organizer_name, is_organizer, location, latitude, longitude, phone</code></p>
      <hr />
      <h5>Get account ID by email</h5>
      <p>Method name: <code>getId</code></p>
      <p>Description: Allow to get the account ID, both user and organizer, associated to an email.</p>
      <p>Input: <code>email</code></p>
      <p>Output: <code>account_id</code></p>
      <hr />
      <h5>Get account info by ID</h5>
      <p>Method name: <code>getAccountInfo</code></p>
      <p>Description: Allow to get all account info by specifing the account id.</p>
      <p>Input: <code>account_id</code></p>
      <p>Output: <code>is_organizer, firstname, lastname, organizer_name, location, latitude, longitude, phone, email</code></p>
      <hr />
      <h5>Change password</h5>
      <p>Method name: <code>changePassword</code></p>
      <p>Description: Allow to change user/organizer password. You should implement a client side check password system to prevent users to insert an undesired new password.</p>
      <p>Input: <code>email, old_password, new_password</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Delete user/organizer account</h5>
      <p>Method name: <code>deleteAccount</code></p>
      <p>Description: Allow to delete a user/organizer account.</p>
      <p>Input: <code>account_id</code></p>
      <p>Output: <code>-</code></p>
      <h4><u>User operations</u></h4>
      <h5>Save preferences</h5>
      <p>Method name: <code>savePref</code></p>
      <p>Description: Allow a user to save his preferences. All user preferences are explained above.</p>
      <p>Input: <code>account_id, tags, rangeTime, rangeDistance</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Get saved preferences</h5>
      <p>Method name: <code>getPref</code></p>
      <p>Description: Allow a user to get his saved preferences. All user preferences are explained above.</p>
      <p>Input: <code>account_id</code></p>
      <p>Output: <code>tags, rangeTime, rangeDistance</code></p>
      <hr />
      <h5>Get account custom events</h5>
      <p>Method name: <code>getAccountCustomEvents</code></p>
      <p>Description: Allow a user account to get events based on his preferences. This method needs the user position (latitude and longitude). The structure "events" received from server is explained above.</p>
      <p>Input: <code>account_id, latitude, longitude</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Get custom events</h5>
      <p>Method name: <code>getCustomEvents</code></p>
      <p>Description: Allow to get events based on coordinates, tags, range time and range distance. The structure "events" received from server is explained above.</p>
      <p>Input: <code>latitude, longitude, tags, rangeDistance, rangeTime</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Get user near events</h5>
      <p>Method name: <code>getUserNearEvents</code></p>
      <p>Description: Allow a user to get all near events. This method needs the user position (latitude and longitude) and user's max distance preference. The structure "events" received from server is explained above.</p>
      <p>Input: <code>account_id, latitude, longitude</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Get near events</h5>
      <p>Method name: <code>getNearEvents</code></p>
      <p>Description: Allow to get all near events. This method needs a position (latitude and longitude) and a distance (in km). The structure "events" received from server is explained above.</p>
      <p>Input: <code>latitude, longitude, distance</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Get all events</h5>
      <p>Method name: <code>getAllEvents</code></p>
      <p>Description: Allow to get all events in the system.</p>
      <p>Input: <code>-</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Account search for events</h5>
      <p>Method name: <code>searchEvents</code></p>
      <p>Description: Allow a user to search for events. The query sent will be matched with both events' title and description. The structure "events" received from server is explained above. It will also return a "added to calendar" value for that account.</p>
      <p>Input: <code>account_id, query</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Search for events</h5>
      <p>Method name: <code>generalSearchEvents</code></p>
      <p>Description: Allow a to search for events. The query sent will be matched with both events' title and description. The structure "events" received from server is explained above.</p>
      <p>Input: <code>query</code></p>
      <p>Output: <code>events</code></p>
      <hr />
      <h5>Add an event to calendar</h5>
      <p>Method name: <code>addToCalendar</code></p>
      <p>Description: Allow a user to save an event into his calendar.</p>
      <p>Input: <code>account_id, event_id</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Remove an event from the calendar</h5>
      <p>Method name: <code>removeFromCalendar</code></p>
      <p>Description: Allow a user to remove an event from his calendar.</p>
      <p>Input: <code>account_id, event_id</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Get events in calendar</h5>
      <p>Method name: <code>getCalendarEvents</code></p>
      <p>Description: Allow a user to all saved events in his calendar. The structure "events" received from server is explained above.</p>
      <p>Input: <code>account_id</code></p>
      <p>Output: <code>events</code></p>
      <h4><u>Organizer operations</u></h4>
      <h5>Insert an event</h5>
      <p>Method name: <code>insertEvent</code></p>
      <p>Description: Allow an organizer to create a new event. Some of these fields are required. See above for more informations.</p>
      <p>Input: <code>account_id, event</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Remove an event</h5>
      <p>Method name: <code>removeEvent</code></p>
      <p>Description: Allow an organizer to delete an owned event.</p>
      <p>Input: <code>event_id</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Modify an event</h5>
      <p>Method name: <code>modifyEvent</code></p>
      <p>Description: Allow an organizer to modify an owned event. Some of these fields are required. See above for more informations.</p>
      <p>Input: <code>account_id, event</code></p>
      <p>Output: <code>-</code></p>
      <hr />
      <h5>Get created events</h5>
      <p>Method name: <code>getOwnedEvents</code></p>
      <p>Description: Allow an organizer to get all owned events.</p>
      <p>Input: <code>account_id</code></p>
      <p>Output: <code>events</code></p>
    </div>
  </div>
</div>
</main>
