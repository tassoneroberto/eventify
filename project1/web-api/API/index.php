<?php
$origin = $_SERVER['HTTP_ORIGIN'];
$method = $_SERVER['REQUEST_METHOD'];
if (isset($_SERVER['HTTP_ORIGIN'])) {
	header('Access-Control-Allow-Origin: ' . $_SERVER['HTTP_ORIGIN']);
	header('Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS');
	header('Access-Control-Max-Age: 1000');
	header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');
}
if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
	if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD']))
		header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
	if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']))
		header("Access-Control-Allow-Headers: {$_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']}");
	$_SERVER['REQUEST_METHOD'] = 'POST';
}
require_once 'Functions.php';
$fun = new Functions();
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	$data = json_decode(file_get_contents("php://input"));
	if ($fun->db->checkApiAllowed($data->api_key)) {
		if (isset($data->operation)) {
			$operation = $data->operation;
			if (!empty($operation)) {
				if ($operation == 'registerUser') {
					if (isset($data->email) && isset($data->password) && isset($data->firstname) && isset($data->lastname)) {
						$firstname = $data->firstname;
						$lastname  = $data->lastname;
						$email     = $data->email;
						$password  = $data->password;
						if ($fun->isEmailValid($email)) {
							$id       = uniqid('', true);
							$response = $fun->registerLogin($id, $email, $password, 0);
							$decoded  = json_decode($response, true);
							if ($decoded['result'] == "success")
								echo $fun->registerUser($id, $firstname, $lastname);
							else {
								$response["result"]  = "failure";
								$response["message"] = "Registration Failure !";
								return json_encode($response);
							}
						} else {
							echo $fun->getMsgInvalidEmail();
						}
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'registerOrganizer') {
					if (isset($data->email) && isset($data->password) && isset($data->organizer_name) && isset($data->phone) && isset($data->location) && isset($data->latitude) && isset($data->longitude) && !empty($data->latitude) && !empty($data->longitude)) {
						$name      = $data->organizer_name;
						$phone     = $data->phone;
						$location  = $data->location;
						$latitude  = $data->latitude;
						$longitude = $data->longitude;
						$email     = $data->email;
						$password  = $data->password;
						if ($fun->isEmailValid($email)) {
							$id       = uniqid('', true);
							$response = $fun->registerLogin($id, $email, $password, 1);
							$decoded  = json_decode($response, true);
							if ($decoded['result'] == "success")
								echo $fun->registerOrganizer($id, $name, $location, $latitude, $longitude, $phone);
							else {
								$response["result"]  = "failure";
								$response["message"] = "Registration Failure !";
								return json_encode($response);
							}
						} else {
							echo $fun->getMsgInvalidEmail();
						}
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'insertEvent') {
					if (isset($data->account_id) && !empty($data->event) && isset($data->event->title) && isset($data->event->location) && isset($data->event->latitude) && isset($data->event->longitude) && isset($data->event->category) && isset($data->event->phone)) {
						$event       = $data->event;
						$owner       = $data->account_id;
						$title       = $event->title;
						$description = $event->description;
						$location    = $event->location;
						$phone       = $event->phone;
						if ($location != "") {
							$latitude  = $event->latitude;
							$longitude = $event->longitude;
						} else {
							$latitude  = NULL;
							$longitude = NULL;
						}
						if ($event->opening != "" && $event->openingTime != "")
							$opening = date("Y-m-d H:i:s", strtotime(($event->opening) . " " . ($event->openingTime)));
						else if ($event->opening != "" && $event->openingTime == "")
							$opening = date("Y-m-d H:i:s", strtotime($event->opening));
						else
							$opening = NULL;
						if ($event->ending != "" && $event->endingTime != "")
							$ending = date("Y-m-d H:i:s", strtotime(($event->ending) . " " . ($event->endingTime)));
						else if ($event->ending != "" && $event->endingTime == "")
							$ending = date("Y-m-d H:i:s", strtotime($event->ending));
						else
							$ending = NULL;
						$category = $event->category;
						$tags     = $event->tags;
						$id       = uniqid('', true);
						echo $fun->insertEvent($id, $owner, $title, $description, $phone, $location, $latitude, $longitude, $opening, $ending, $category, $tags);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'modifyEvent') {
					if (isset($data->account_id) && !empty($data->event) && isset($data->event->id) && isset($data->event->title) && isset($data->event->location) && isset($data->event->latitude) && isset($data->event->longitude) && isset($data->event->category) && isset($data->event->phone)) {
						$event       = $data->event;
						$owner       = $data->account_id;
						$id          = $event->id;
						$title       = $event->title;
						$description = $event->description;
						$phone       = $event->phone;
						$location    = $event->location;
						if ($location != "") {
							$latitude  = $event->latitude;
							$longitude = $event->longitude;
						} else {
							$latitude  = NULL;
							$longitude = NULL;
						}
						if ($event->opening != "" && $event->openingTime != "")
							$opening = date("Y-m-d H:i:s", strtotime(($event->opening) . " " . ($event->openingTime)));
						else if ($event->opening != "" && $event->openingTime == "")
							$opening = date("Y-m-d H:i:s", strtotime($event->opening));
						else
							$opening = NULL;
						if ($event->ending != "" && $event->endingTime != "")
							$ending = date("Y-m-d H:i:s", strtotime(($event->ending) . " " . ($event->endingTime)));
						else if ($event->ending != "" && $event->endingTime == "")
							$ending = date("Y-m-d H:i:s", strtotime($event->ending));
						else
							$ending = NULL;
						$category = $event->category;
						$tags     = $event->tags;
						echo $fun->removeEvent($id, true);
						echo $fun->insertEvent($id, $owner, $title, $description, $phone, $location, $latitude, $longitude, $opening, $ending, $category, $tags);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'removeEvent') {
					if (isset($data->event_id)) {
						$id = $data->event_id;
						echo $fun->removeEvent($id, false);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'savePref') {
					if (isset($data->account_id) && isset($data->tags) && isset($data->rangeTime) && isset($data->rangeDistance)) {
						$id            = $data->account_id;
						$tags          = $data->tags;
						$rangeTime     = $data->rangeTime;
						$rangeDistance = $data->rangeDistance;
						echo $fun->savePref($id, $tags, $rangeTime, $rangeDistance);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getPref') {
					if (isset($data->account_id)) {
						$id = $data->account_id;
						echo $fun->getPref($id);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getAccountInfo') {
					if (isset($data->account_id)) {
						$id = $data->account_id;
						echo $fun->getAccountInfo($id);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'addToCalendar') {
					if (isset($data->account_id) && isset($data->event_id)) {
						$id      = $data->account_id;
						$eventId = $data->event_id;
						echo $fun->addToCalendar($id, $eventId);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'removeFromCalendar') {
					if (isset($data->account_id) && isset($data->event_id)) {
						$id      = $data->account_id;
						$eventId = $data->event_id;
						echo $fun->removeFromCalendar($id, $eventId);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getId') {
					if (isset($data->email)) {
						$email = $data->email;
						echo $fun->getId($email);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'login') {
					if (isset($data->email) && isset($data->password)) {
						$email    = $data->email;
						$password = $data->password;
						echo $fun->login($email, $password);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getOwnedEvents') {
					if (isset($data->account_id)) {
						$id = $data->account_id;
						echo $fun->getOwnedEvents($id);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getAccountCustomEvents') {
					if (isset($data->account_id) && isset($data->latitude) && isset($data->longitude)) {
						$id        = $data->account_id;
						$latitude  = $data->latitude;
						$longitude = $data->longitude;
						echo $fun->getAccountCustomEvents($id, $latitude, $longitude);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getCustomEvents') {
					if (isset($data->latitude) && isset($data->longitude) && isset($data->category) && isset($data->tags) && isset($data->rangeDistance) && isset($data->rangeTime)) {
						$latitude      = $data->latitude;
						$longitude     = $data->longitude;
						$category      = $data->category;
						$tags          = $data->tags;
						$rangeDistance = $data->rangeDistance;
						$rangeTime     = $data->rangeTime;
						echo $fun->getCustomEvents($latitude, $longitude, $category, $tags, $rangeDistance, $rangeTime);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getUserNearEvents') {
					if (isset($data->account_id) && isset($data->latitude) && isset($data->longitude)) {
						$id        = $data->account_id;
						$latitude  = $data->latitude;
						$longitude = $data->longitude;
						echo $fun->getUserNearEvents($id, $latitude, $longitude);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getNearEvents') {
					if (isset($data->latitude) && isset($data->longitude) && isset($data->distance)) {
						$latitude  = $data->latitude;
						$longitude = $data->longitude;
						$distance  = $data->distance;
						echo $fun->getNearEvents($latitude, $longitude, $distance);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'searchEvents') {
					if (isset($data->query) && isset($data->account_id)) {
						$id    = $data->account_id;
						$query = $data->query;
						echo $fun->searchEvents($id, $query);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'generalSearchEvents') {
					if (isset($data->query)) {
						$query = $data->query;
						echo $fun->generalSearchEvents($query);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getCalendarEvents') {
					if (isset($data->account_id)) {
						$userId = $data->account_id;
						echo $fun->getCalendarEvents($userId);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'getAllEvents') {
					echo $fun->getAllEvents();
				} else if ($operation == 'changePassword') {
					if (isset($data->email) && isset($data->old_password) && isset($data->new_password)) {
						$email        = $data->email;
						$old_password = $data->old_password;
						$new_password = $data->new_password;
						echo $fun->changePassword($email, $old_password, $new_password);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else if ($operation == 'deleteAccount') {
					if (isset($data->account_id)) {
						$id = $data->account_id;
						echo $fun->deleteAccount($id);
					} else {
						echo $fun->getMsgInvalidParam();
					}
				} else {
					echo $fun->getMsgParamNotEmpty();
				}
			} else {
				echo $fun->getMsgInvalidParam();
			}
		}
	}
} else if ($_SERVER['REQUEST_METHOD'] == 'GET') {
	header('Location: /');
} else {
	header('Location: /');
}