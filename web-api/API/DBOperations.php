<?php
class DBOperations
{
	private $host = 'localhost';
	private $user = 'apisocialevent';
	private $db = 'my_apisocialevent';
	private $pass = '';
	public $conn;
	public function __construct()
	{
		$this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db, $this->user, $this->pass);
	}
	public function insertDataLogin($id, $email, $password, $organizer)
	{
		$hash               = $this->getHash($password);
		$encrypted_password = $hash["encrypted"];
		$salt               = $hash["salt"];
		$sql                = 'INSERT INTO login SET id =:id, email =:email,encrypted_password =:encrypted_password,salt =:salt,organizer =:organizer,created_at = NOW()';
		$query              = $this->conn->prepare($sql);
		$query->execute(array(
			'id' => $id,
			':email' => $email,
			':encrypted_password' => $encrypted_password,
			':salt' => $salt,
			':organizer' => $organizer
		));
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function insertDataUser($id_login, $firstname, $lastname)
	{
		$rangeTime     = 2;
		$rangeDistance = 2;
		$sql           = 'INSERT INTO user SET id_login =:id_login, firstname =:firstname,lastname =:lastname, rangeTime=:rangeTime,rangeDistance=:rangeDistance';
		$query         = $this->conn->prepare($sql);
		$query->execute(array(
			'id_login' => $id_login,
			':firstname' => $firstname,
			':lastname' => $lastname,
			':rangeTime' => $rangeTime,
			':rangeDistance' => $rangeDistance
		));
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function insertDataOrganizer($id_login, $name, $location, $latitude, $longitude, $phone)
	{
		$sql   = 'INSERT INTO organizer SET id_login =:id_login, name =:name,location =:location,latitude =:latitude,longitude =:longitude,phone =:phone';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			'id_login' => $id_login,
			':name' => $name,
			':location' => $location,
			':latitude' => $latitude,
			':longitude' => $longitude,
			':phone' => $phone
		));
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function insertDataEvent($id, $owner, $title, $description, $phone, $location, $latitude, $longitude, $opening, $ending, $category, $tags)
	{
		$sql   = 'INSERT INTO event SET id =:id, owner =:owner,title =:title,description =:description, phone=:phone, location=:location, latitude=:latitude, longitude=:longitude, opening=:opening, ending=:ending, category=:category';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			'id' => $id,
			':owner' => $owner,
			':title' => $title,
			':description' => $description,
			':phone' => $phone,
			':location' => $location,
			':latitude' => $latitude,
			':longitude' => $longitude,
			':opening' => $opening,
			':ending' => $ending,
			':category' => $category
		));
		$tagList = explode("-", $tags);
		$error   = false;
		foreach ($tagList as $tag) {
			if ($tag != "") {
				$sqltag   = 'INSERT INTO tags SET id_event =:id_event, tag =:tag';
				$querytag = $this->conn->prepare($sqltag);
				$querytag->execute(array(
					'id_event' => $id,
					':tag' => $tag
				));
				if (!$querytag)
					$error = true;
			}
		}
		if ($query && !$error) {
			return true;
		} else {
			return false;
		}
	}
	public function savePref($id, $tags, $rangeTime, $rangeDistance)
	{
		$sql   = 'DELETE FROM pref WHERE id_user =:id_user';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			'id_user' => $id
		));
		$categories = explode(";", $tags);
		foreach ($categories as $tags) {
			$split    = explode("=", $tags);
			$category = $split[0];
			$tagList  = explode(",", $split[1]);
			$error    = false;
			foreach ($tagList as $tag) {
				$tag = trim($tag);
				if ($tag != "") {
					$sqltag   = 'INSERT INTO pref SET id_user =:id_user, category=:category, tag =:tag';
					$querytag = $this->conn->prepare($sqltag);
					$querytag->execute(array(
						'id_user' => $id,
						':category' => $category,
						':tag' => $tag
					));
					if (!$querytag) {
						$error = true;
					}
				}
			}
		}
		$sql   = 'UPDATE user SET rangeTime=\'' . $rangeTime . '\' , rangeDistance=\'' . $rangeDistance . '\'  WHERE id_login =:id_login';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			'id_login' => $id
		));
		if (!$error) {
			return true;
		} else {
			return false;
		}
	}
	public function addToCalendar($user, $event)
	{
		$sqlExist   = 'SELECT * FROM calendar WHERE user =\'' . $user . '\' AND event=\'' . $event . '\' ';
		$queryExist = $this->conn->prepare($sqlExist, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$queryExist->execute();
		$exist = false;
		while ($rowExist = $queryExist->fetch(PDO::FETCH_ASSOC)) {
			$exist = true;
		}
		if (!$exist) {
			$sql   = 'INSERT INTO calendar SET user=:user, event=:event';
			$query = $this->conn->prepare($sql);
			$query->execute(array(
				'user' => $user,
				':event' => $event
			));
			if ($query) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	public function removeFromCalendar($user, $event)
	{
		$sql   = 'DELETE FROM calendar WHERE user=\'' . $user . '\' AND event=\'' . $event . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function deleteAccount($id)
	{
		$sql   = 'SELECT * FROM login WHERE id=\'' . $id . '\'';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$is_organizer = $row['organizer'];
		}
		if ($is_organizer == 1)
			$this->deleteOrganizerAccount($id);
		else
			$this->deleteUserAccount($id);
	}
	public function deleteUserAccount($id)
	{
		$sql   = 'DELETE FROM login WHERE id=\'' . $id . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		$sql   = 'DELETE FROM user WHERE id_login=\'' . $id . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		$sql   = 'DELETE FROM pref WHERE id_user=\'' . $id . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		$sql   = 'DELETE FROM calendar WHERE user=\'' . $id . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function deleteOrganizerAccount($id)
	{
		$sql   = 'DELETE FROM login WHERE id=\'' . $id . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		$sql   = 'DELETE FROM organizer WHERE id_login=\'' . $id . '\' ';
		$query = $this->conn->prepare($sql);
		$query->execute();
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function getPref($id)
	{
		$sql   = 'SELECT * FROM pref WHERE id_user=\'' . $id . '\'';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$allPref;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$allPref = $allPref . $row['category'] . "=" . $row['tag'] . ";";
		}
		$user["tags"] = $allPref;
		$sql          = 'SELECT * FROM user WHERE id_login=\'' . $id . '\'';
		$query        = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$user["rangeTime"]     = $row["rangeTime"];
			$user["rangeDistance"] = $row["rangeDistance"];
		}
		return $user;
	}
	public function getAccountInfo($id)
	{
		$sql   = 'SELECT * FROM login WHERE id = :id';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			':id' => $id
		));
		$data = $query->fetchObject();
		$result["is_organizer"]=$data->organizer;
		$result["email"]=$data->email;
		if ($data->organizer == 0) {
			$sql   = 'SELECT * FROM user WHERE id_login = :id';
			$query = $this->conn->prepare($sql);
			$query->execute(array(
				':id' => $id
			));
			$dataUser            = $query->fetchObject();
			$result["firstname"]   = $dataUser->firstname;
			$result["lastname"]    = $dataUser->lastname;
			$result["rangeDistance"]    = $dataUser->rangeDistance;
			$result["rangeTime"]    = $dataUser->rangeTime;
			return $result;
		} else {
			$sql   = 'SELECT * FROM organizer WHERE id_login = :id';
			$query = $this->conn->prepare($sql);
			$query->execute(array(
				':id' => $id
			));
			$dataOrganizer         = $query->fetchObject();
			$result["name"]        = $dataOrganizer->name;
			$result["location"]    = $dataOrganizer->location;
			$result["latitude"]    = $dataOrganizer->latitude;
			$result["longitude"]   = $dataOrganizer->longitude;
			$result["phone"]       = $dataOrganizer->phone;
			return $result;
		}
	}
	public function getId($email)
	{
		$sql   = 'SELECT id FROM login WHERE email=\'' . $email . '\'';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$user['id'] = $row['id'];
		}
		return $user;
	}
	public function removeEvent($id)
	{
		$sql   = 'DELETE FROM event WHERE id =:id';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			'id' => $id
		));
		$sqltag   = 'DELETE FROM tags WHERE id_event =:id_event';
		$querytag = $this->conn->prepare($sqltag);
		$querytag->execute(array(
			'id_event' => $id
		));
		if ($query && $querytag) {
			return true;
		} else {
			return false;
		}
	}
	public function checkLogin($email, $password)
	{
		$sql   = 'SELECT * FROM login WHERE email = :email';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			':email' => $email
		));
		$data                  = $query->fetchObject();
		$id                    = $data->id;
		$salt                  = $data->salt;
		$db_encrypted_password = $data->encrypted_password;
		if ($this->verifyHash($password . $salt, $db_encrypted_password)) {
			if ($data->organizer == 0) {
				$sql   = 'SELECT * FROM user WHERE id_login = :id';
				$query = $this->conn->prepare($sql);
				$query->execute(array(
					':id' => $id
				));
				$dataUser            = $query->fetchObject();
				$user["firstname"]   = $dataUser->firstname;
				$user["lastname"]    = $dataUser->lastname;
				$user["email"]       = $data->email;
				$user["isOrganizer"] = 0;
				$user["id"]          = $id;
				return $user;
			} else {
				$sql   = 'SELECT * FROM organizer WHERE id_login = :id';
				$query = $this->conn->prepare($sql);
				$query->execute(array(
					':id' => $data->id
				));
				$dataOrganizer            = $query->fetchObject();
				$organizer["name"]        = $dataOrganizer->name;
				$organizer["location"]    = $dataOrganizer->location;
				$organizer["latitude"]    = $dataOrganizer->latitude;
				$organizer["longitude"]   = $dataOrganizer->longitude;
				$organizer["phone"]       = $dataOrganizer->phone;
				$organizer["email"]       = $data->email;
				$organizer["isOrganizer"] = 1;
				$organizer["id"]          = $id;
				return $organizer;
			}
		} else {
			return false;
		}
	}
	public function getOwnedEvents($id)
	{
		$sql   = 'SELECT * FROM event WHERE owner = "' . $id . '"';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$event;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']          = $row['id'];
			$event['owner']       = $row['owner'];
			$event['title']       = $row['title'];
			$event['description'] = $row['description'];
			$event['phone']       = $row['phone'];
			$event['location']    = $row['location'];
			$event['latitude']    = $row['latitude'];
			$event['longitude']   = $row['longitude'];
			$event['opening']     = $row['opening'];
			$event['ending']      = $row['ending'];
			$event['category']    = $row['category'];
			$sql2                 = 'SELECT name FROM organizer WHERE id_login=:id_login';
			$query2               = $this->conn->prepare($sql2);
			$query2->execute(array(
				':id_login' => $row['owner']
			));
			$data                    = $query2->fetchObject();
			$event['organizer_name'] = $data->name;
			$sql2                    = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2                  = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function getAllEvents()
	{
		$sql   = 'SELECT * FROM event';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$event;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']          = $row['id'];
			$event['owner']       = $row['owner'];
			$event['title']       = $row['title'];
			$event['description'] = $row['description'];
			$event['phone']       = $row['phone'];
			$event['location']    = $row['location'];
			$event['latitude']    = $row['latitude'];
			$event['longitude']   = $row['longitude'];
			$event['opening']     = $row['opening'];
			$event['ending']      = $row['ending'];
			$event['category']    = $row['category'];
			$sql2                 = 'SELECT name FROM organizer WHERE id_login=:id_login';
			$query2               = $this->conn->prepare($sql2);
			$query2->execute(array(
				':id_login' => $row['owner']
			));
			$data                    = $query2->fetchObject();
			$event['organizer_name'] = $data->name;
			$sql2                    = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2                  = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function getCalendarEvents($id)
	{
		$today = date('Y-m-d H:i:s');
		$sql   = 'SELECT e.* FROM event e JOIN calendar c ON c.event=e.id WHERE user = "' . $id . '" AND c.event=e.id AND e.ending>=\'' . $today . '\'';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$event;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']    = $row['id'];
			$event['owner'] = $row['owner'];
			$sqlOrg         = 'SELECT name FROM organizer WHERE id_login =\'' . $row['owner'] . '\' ';
			$queryOrg       = $this->conn->prepare($sqlOrg, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$queryOrg->execute();
			while ($resutlOrg = $queryOrg->fetch(PDO::FETCH_ASSOC)) {
				$event['organizer_name'] = $resutlOrg['name'];
			}
			$event['title']       = $row['title'];
			$event['description'] = $row['description'];
			$event['phone']       = $row['phone'];
			$event['location']    = $row['location'];
			$event['latitude']    = $row['latitude'];
			$event['longitude']   = $row['longitude'];
			$event['opening']     = $row['opening'];
			$event['ending']      = $row['ending'];
			$event['category']    = $row['category'];
			$event['added']       = true;
			$sql2                 = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2               = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function getAccountCustomEvents($user, $latitude, $longitude)
	{
		$sql   = 'SELECT rangeDistance,rangeTime FROM user WHERE id_login=\'' . $user . '\'';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$distance;
		$time;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$distance = $row['rangeDistance'];
			$time     = $row['rangeTime'];
		}
		$kmDistance;
		switch ($distance) {
			case 0:
				$kmDistance = 1;
				break;
			case 1:
				$kmDistance = 2;
				break;
			case 2:
				$kmDistance = 10;
				break;
			case 3:
				$kmDistance = 25;
				break;
			case 4:
				$kmDistance = 50;
				break;
			case 5:
				$kmDistance = 100;
				break;
		}
		$lonLength      = 111.325 * cos($latitude * 0.0174532925199433);
		$rangeDegreeLon = $kmDistance / $lonLength;
		$rangeDegreeLat = $kmDistance / 111.132;
		$lat            = floatval($latitude);
		$lon            = floatval($longitude);
		$dayTime;
		switch ($time) {
			case 0:
				$dayTime = 1;
				break;
			case 1:
				$dayTime = 3;
				break;
			case 2:
				$dayTime = 7;
				break;
			case 3:
				$dayTime = 14;
				break;
			case 4:
				$dayTime = 30;
				break;
			case 5:
				$dayTime = 60;
				break;
		}
		$dateLimit = date('Y-m-d H:i:s', strtotime('+' . $dayTime . ' days'));
		$today     = date('Y-m-d H:i:s');
		$allPref;
		$sql   = 'SELECT category,tag FROM pref WHERE id_user = "' . $user . '"';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$allPref[$k] = array(
				$row['category'],
				$row['tag']
			);
			$k++;
		}
		$sql   = 'SELECT * FROM event WHERE latitude<\'' . ($lat + $rangeDegreeLat) . '\' AND latitude>\'' . ($lat - $rangeDegreeLat) . '\' AND longitude<\'' . ($lon + $rangeDegreeLon) . '\' AND longitude>\'' . ($lon - $rangeDegreeLon) . '\' AND ((opening<=\'' . $today . '\' AND ending>=\'' . $today . '\') OR (ending>=\'' . $today . '\' AND ending<=\'' . $dateLimit . '\'))';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$categoryEvent = $row['category'];
			$sqltag        = 'SELECT * FROM tags WHERE id_event=\'' . $row['id'] . '\'';
			$querytag      = $this->conn->prepare($sqltag, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$querytag->execute();
			$allTags;
			$j = 0;
			while ($rowtag = $querytag->fetch(PDO::FETCH_ASSOC)) {
				$allTags[$j] = ($rowtag['tag']);
				$test        = $allTags[$j];
				$j++;
			}
			$valid = false;
			foreach ($allPref as $pref) {
				if ($pref[0] == $categoryEvent) {
					foreach ($allTags as $tag) {
						if ($pref[1] == $tag) {
							$valid = true;
							break;
						}
					}
				}
			}
			if ($valid) {
				$event['id']    = $row['id'];
				$event['owner'] = $row['owner'];
				$sqlOrg         = 'SELECT name FROM organizer WHERE id_login =\'' . $row['owner'] . '\' ';
				$queryOrg       = $this->conn->prepare($sqlOrg, array(
					PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
				));
				$queryOrg->execute();
				while ($resutlOrg = $queryOrg->fetch(PDO::FETCH_ASSOC)) {
					$event['organizer_name'] = $resutlOrg['name'];
				}
				$event['title']       = $row['title'];
				$event['description'] = $row['description'];
				$event['phone']       = $row['phone'];
				$event['location']    = $row['location'];
				$event['latitude']    = $row['latitude'];
				$event['longitude']   = $row['longitude'];
				$event['opening']     = $row['opening'];
				$event['ending']      = $row['ending'];
				$event['category']    = $row['category'];
				$sqlAdded             = 'SELECT * FROM calendar WHERE user =\'' . $user . '\' AND event=\'' . $event['id'] . '\' ';
				$queryAdded           = $this->conn->prepare($sqlAdded, array(
					PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
				));
				$queryAdded->execute();
				$event['added'] = false;
				while ($rowAdded = $queryAdded->fetch(PDO::FETCH_ASSOC)) {
					$event['added'] = true;
					break;
				}
				$sql2   = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
				$query2 = $this->conn->prepare($sql2, array(
					PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
				));
				$query2->execute();
				$tags = "";
				while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
					$tags = $tags . $row2['tag'] . "-";
				}
				if ($tags != "")
					$tags = trim($tags, '-');
				$event['tags'] = $tags;
				
				$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
				$query3               = $this->conn->prepare($sql3);
				$query3->execute(array(
					':event' => $event['id'] 
				));
				$followers=$query3->fetchColumn();
				if($followers==null)$followers=0;
				$event['followers'] =$followers;
				
				$events[$k]    = $event;
				$k++;
			}
		}
		return $events;
	}
	public function getCustomEvents($latitude, $longitude, $category, $tags, $rangeDistance, $rangeTime)
	{
		$kmDistance     = $rangeDistance;
		$dayTime        = $rangeTime;
		$lonLength      = 111.325 * cos($latitude * 0.0174532925199433);
		$rangeDegreeLon = $kmDistance / $lonLength;
		$rangeDegreeLat = $kmDistance / 111.132;
		$lat            = floatval($latitude);
		$lon            = floatval($longitude);
		$dateLimit      = date('Y-m-d H:i:s', strtotime('+' . $dayTime . ' days'));
		$today          = date('Y-m-d H:i:s');
		$allPref;
		$allTag = explode("-", $tags);
		$k      = 0;
		foreach ($allTag as &$tag) {
			$allPref[$k] = array(
				$category,
				$tag
			);
			$k++;
		}
		$sql   = 'SELECT * FROM event WHERE latitude<\'' . ($lat + $rangeDegreeLat) . '\' AND latitude>\'' . ($lat - $rangeDegreeLat) . '\' AND longitude<\'' . ($lon + $rangeDegreeLon) . '\' AND longitude>\'' . ($lon - $rangeDegreeLon) . '\' AND ((opening<=\'' . $today . '\' AND ending>=\'' . $today . '\') OR (ending>=\'' . $today . '\' AND ending<=\'' . $dateLimit . '\'))';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$categoryEvent = $row['category'];
			$sqltag        = 'SELECT * FROM tags WHERE id_event=\'' . $row['id'] . '\'';
			$querytag      = $this->conn->prepare($sqltag, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$querytag->execute();
			$allTags;
			$j = 0;
			while ($rowtag = $querytag->fetch(PDO::FETCH_ASSOC)) {
				$allTags[$j] = ($rowtag['tag']);
				$test        = $allTags[$j];
				$j++;
			}
			$valid = false;
			foreach ($allPref as $pref) {
				if ($pref[0] == $categoryEvent) {
					foreach ($allTags as $tag) {
						if ($pref[1] == $tag) {
							$valid = true;
							break;
						}
					}
				}
			}
			if ($valid) {
				$event['id']    = $row['id'];
				$event['owner'] = $row['owner'];
				$sqlOrg         = 'SELECT name FROM organizer WHERE id_login =\'' . $row['owner'] . '\' ';
				$queryOrg       = $this->conn->prepare($sqlOrg, array(
					PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
				));
				$queryOrg->execute();
				while ($resutlOrg = $queryOrg->fetch(PDO::FETCH_ASSOC)) {
					$event['organizer_name'] = $resutlOrg['name'];
				}
				$event['title']       = $row['title'];
				$event['description'] = $row['description'];
				$event['phone']       = $row['phone'];
				$event['location']    = $row['location'];
				$event['latitude']    = $row['latitude'];
				$event['longitude']   = $row['longitude'];
				$event['opening']     = $row['opening'];
				$event['ending']      = $row['ending'];
				$event['category']    = $row['category'];
				$sqlAdded             = 'SELECT * FROM calendar WHERE user =\'' . $user . '\' AND event=\'' . $event['id'] . '\' ';
				$queryAdded           = $this->conn->prepare($sqlAdded, array(
					PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
				));
				$queryAdded->execute();
				$event['added'] = false;
				while ($rowAdded = $queryAdded->fetch(PDO::FETCH_ASSOC)) {
					$event['added'] = true;
					break;
				}
				$sql2   = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
				$query2 = $this->conn->prepare($sql2, array(
					PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
				));
				$query2->execute();
				$tags = "";
				while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
					$tags = $tags . $row2['tag'] . "-";
				}
				if ($tags != "")
					$tags = trim($tags, '-');
				$event['tags'] = $tags;
				
				$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
				$query3               = $this->conn->prepare($sql3);
				$query3->execute(array(
					':event' => $event['id'] 
				));
				$followers=$query3->fetchColumn();
				if($followers==null)$followers=0;
				$event['followers'] =$followers;
				
				$events[$k]    = $event;
				$k++;
			}
		}
		return $events;
	}
	public function getUserNearEvents($user, $latitude, $longitude)
	{
		$sql   = 'SELECT rangeDistance FROM user WHERE id_login=\'' . $user . '\'';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$distance;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$distance = $row['rangeDistance'];
		}
		$kmDistance;
		switch ($distance) {
			case 0:
				$kmDistance = 1;
				break;
			case 1:
				$kmDistance = 2;
				break;
			case 2:
				$kmDistance = 10;
				break;
			case 3:
				$kmDistance = 25;
				break;
			case 4:
				$kmDistance = 50;
				break;
			case 5:
				$kmDistance = 100;
				break;
		}
		$lonLength      = 111.325 * cos($latitude * 0.0174532925199433);
		$rangeDegreeLon = $kmDistance / $lonLength;
		$rangeDegreeLat = $kmDistance / 111.132;
		$lat            = floatval($latitude);
		$lon            = floatval($longitude);
		$today          = date('Y-m-d H:i:s');
		$sql            = 'SELECT * FROM event WHERE latitude<\'' . ($lat + $rangeDegreeLat) . '\' AND latitude>\'' . ($lat - $rangeDegreeLat) . '\' AND longitude<\'' . ($lon + $rangeDegreeLon) . '\' AND longitude>\'' . ($lon - $rangeDegreeLon) . '\' AND ending>=\'' . $today . '\'';
		$query          = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']    = $row['id'];
			$event['owner'] = $row['owner'];
			$sqlOrg         = 'SELECT name FROM organizer WHERE id_login =\'' . $row['owner'] . '\' ';
			$queryOrg       = $this->conn->prepare($sqlOrg, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$queryOrg->execute();
			while ($resutlOrg = $queryOrg->fetch(PDO::FETCH_ASSOC)) {
				$event['organizer_name'] = $resutlOrg['name'];
			}
			$event['title']       = $row['title'];
			$event['description'] = $row['description'];
			$event['phone']       = $row['phone'];
			$event['location']    = $row['location'];
			$event['latitude']    = $row['latitude'];
			$event['longitude']   = $row['longitude'];
			$event['opening']     = $row['opening'];
			$event['ending']      = $row['ending'];
			$event['category']    = $row['category'];
			$sqlAdded             = 'SELECT * FROM calendar WHERE user =\'' . $user . '\' AND event=\'' . $event['id'] . '\' ';
			$queryAdded           = $this->conn->prepare($sqlAdded, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$queryAdded->execute();
			$event['added'] = false;
			while ($rowAdded = $queryAdded->fetch(PDO::FETCH_ASSOC)) {
				$event['added'] = true;
				break;
			}
			$sql2   = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2 = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function getNearEvents($latitude, $longitude, $distance)
	{
		$lonLength      = 111.325 * cos($latitude * 0.0174532925199433);
		$rangeDegreeLon = $distance / $lonLength;
		$rangeDegreeLat = $distance / 111.132;
		$lat            = floatval($latitude);
		$lon            = floatval($longitude);
		$today          = date('Y-m-d H:i:s');
		$sql            = 'SELECT * FROM event WHERE latitude<\'' . ($lat + $rangeDegreeLat) . '\' AND latitude>\'' . ($lat - $rangeDegreeLat) . '\' AND longitude<\'' . ($lon + $rangeDegreeLon) . '\' AND longitude>\'' . ($lon - $rangeDegreeLon) . '\' AND ending>=\'' . $today . '\'';
		$query          = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$query->execute();
		$events;
		$k = 0;
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']    = $row['id'];
			$event['owner'] = $row['owner'];
			$sqlOrg         = 'SELECT name FROM organizer WHERE id_login =\'' . $row['owner'] . '\' ';
			$queryOrg       = $this->conn->prepare($sqlOrg, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$queryOrg->execute();
			while ($resutlOrg = $queryOrg->fetch(PDO::FETCH_ASSOC)) {
				$event['organizer_name'] = $resutlOrg['name'];
			}
			$event['title']       = $row['title'];
			$event['description'] = $row['description'];
			$event['phone']       = $row['phone'];
			$event['location']    = $row['location'];
			$event['latitude']    = $row['latitude'];
			$event['longitude']   = $row['longitude'];
			$event['opening']     = $row['opening'];
			$event['ending']      = $row['ending'];
			$event['category']    = $row['category'];
			$sqlAdded             = 'SELECT * FROM calendar WHERE user =\'' . $user . '\' AND event=\'' . $event['id'] . '\' ';
			$queryAdded           = $this->conn->prepare($sqlAdded, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$queryAdded->execute();
			$event['added'] = false;
			while ($rowAdded = $queryAdded->fetch(PDO::FETCH_ASSOC)) {
				$event['added'] = true;
				break;
			}
			$sql2   = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2 = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function searchEvents($user, $searchQuery)
	{
		$today = date('Y-m-d H:i:s');
		$sql   = 'SELECT e.id, e.owner, o.name, e.title, e.description, e.phone, e.location, e.latitude, e.longitude, e.opening, e.ending, e.category FROM organizer o,event e WHERE  o.id_login=e.owner AND e.ending>=\'' . $today . '\' AND (o.name LIKE \'%' . $searchQuery . '%\' OR e.description LIKE \'%' . $searchQuery . '%\' OR e.title LIKE \'%' . $searchQuery . '%\')';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$k     = 0;
		$events;
		$query->execute();
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']             = $row['id'];
			$event['owner']          = $row['owner'];
			$event['organizer_name'] = $row['name'];
			$event['title']          = $row['title'];
			$event['description']    = $row['description'];
			$event['phone']          = $row['phone'];
			$event['location']       = $row['location'];
			$event['latitude']       = $row['latitude'];
			$event['longitude']      = $row['longitude'];
			$event['opening']        = $row['opening'];
			$event['ending']         = $row['ending'];
			$event['category']       = $row['category'];
			$sqlAdded                = 'SELECT * FROM calendar WHERE user =\'' . $user . '\' AND event=\'' . $row['id'] . '\' ';
			$queryAdded              = $this->conn->prepare($sqlAdded, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$queryAdded->execute();
			$event['added'] = false;
			while ($rowAdded = $queryAdded->fetch(PDO::FETCH_ASSOC)) {
				$event['added'] = true;
				break;
			}
			$sql2   = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2 = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function generalSearchEvents($searchQuery)
	{
		$today = date('Y-m-d H:i:s');
		$sql   = 'SELECT e.id, e.owner, o.name, e.title, e.description, e.phone, e.location, e.latitude, e.longitude, e.opening, e.ending, e.category FROM organizer o,event e WHERE  o.id_login=e.owner AND e.ending>=\'' . $today . '\' AND (o.name LIKE \'%' . $searchQuery . '%\' OR e.description LIKE \'%' . $searchQuery . '%\' OR e.title LIKE \'%' . $searchQuery . '%\')';
		$query = $this->conn->prepare($sql, array(
			PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
		));
		$k     = 0;
		$events;
		$query->execute();
		while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
			$event['id']             = $row['id'];
			$event['owner']          = $row['owner'];
			$event['organizer_name'] = $row['name'];
			$event['title']          = $row['title'];
			$event['description']    = $row['description'];
			$event['phone']          = $row['phone'];
			$event['location']       = $row['location'];
			$event['latitude']       = $row['latitude'];
			$event['longitude']      = $row['longitude'];
			$event['opening']        = $row['opening'];
			$event['ending']         = $row['ending'];
			$event['category']       = $row['category'];
			$sql2                    = 'SELECT tag FROM tags WHERE id_event = "' . $event['id'] . '"';
			$query2                  = $this->conn->prepare($sql2, array(
				PDO::ATTR_CURSOR => PDO::CURSOR_SCROLL
			));
			$query2->execute();
			$tags = "";
			while ($row2 = $query2->fetch(PDO::FETCH_ASSOC)) {
				$tags = $tags . $row2['tag'] . "-";
			}
			if ($tags != "")
				$tags = trim($tags, '-');
			$event['tags'] = $tags;
			
			$sql3                 = 'SELECT count(*) FROM calendar WHERE event=:event';
			$query3               = $this->conn->prepare($sql3);
			$query3->execute(array(
				':event' => $event['id'] 
			));
			$followers=$query3->fetchColumn();
			if($followers==null)$followers=0;
			$event['followers'] =$followers;
			
			$events[$k]    = $event;
			$k++;
		}
		return $events;
	}
	public function changePassword($email, $password)
	{
		$hash               = $this->getHash($password);
		$encrypted_password = $hash["encrypted"];
		$salt               = $hash["salt"];
		$sql                = 'UPDATE login SET encrypted_password = :encrypted_password, salt = :salt WHERE email = :email';
		$query              = $this->conn->prepare($sql);
		$query->execute(array(
			':email' => $email,
			':encrypted_password' => $encrypted_password,
			':salt' => $salt
		));
		if ($query) {
			return true;
		} else {
			return false;
		}
	}
	public function checkUserExist($email)
	{
		$sql   = 'SELECT COUNT(*) FROM login WHERE email =:email';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			':email' => $email
		));
		if ($query) {
			$row_count = $query->fetchColumn();
			if ($row_count == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	public function checkApiAllowed($api_key)
	{
		$sql   = 'SELECT COUNT(*) FROM api_login WHERE api_key =:api_key AND authorized=1';
		$query = $this->conn->prepare($sql);
		$query->execute(array(
			':api_key' => $api_key
		));
		if ($query) {
			$row_count = $query->fetchColumn();
			if ($row_count == 0) {
				return false;
			} else {
				$sql2   = 'UPDATE api_login SET query = query+1 WHERE api_key=:api_key';
				$query2 = $this->conn->prepare($sql2);
				$query2->execute(array(
					':api_key' => $api_key
				));
				return true;
			}
		} else {
			return false;
		}
	}
	public function getHash($password)
	{
		$salt      = sha1(rand());
		$salt      = substr($salt, 0, 10);
		$encrypted = password_hash($password . $salt, PASSWORD_DEFAULT);
		$hash      = array(
			"salt" => $salt,
			"encrypted" => $encrypted
		);
		return $hash;
	}
	public function verifyHash($password, $hash)
	{
		return password_verify($password, $hash);
	}
	public function distance($lat1, $lon1, $lat2, $lon2)
	{
		$theta = $lon1 - $lon2;
		$dist  = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
		$dist  = acos($dist);
		$dist  = rad2deg($dist);
		$miles = $dist * 60 * 1.1515;
		$unit  = strtoupper($unit);
		return ($miles * 1.609344);
	}
}