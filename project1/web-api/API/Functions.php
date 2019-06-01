<?php
require_once 'DBOperations.php';
class Functions
{
	public $db;
	public function __construct()
	{
		$this->db = new DBOperations();
	}
	public function registerLogin($id, $email, $password, $organizer)
	{
		$db = $this->db;
		if (!empty($email) && !empty($password)) {
			if ($db->checkUserExist($email)) {
				$response["result"]  = "failure";
				$response["message"] = "Email already used!";
				return json_encode($response);
			} else {
				$result = $db->insertDataLogin($id, $email, $password, $organizer);
				if ($result) {
					$response["result"]     = "success";
					$response["account_id"] = $id;
					$response["message"]    = "Registered Successfully !";
					return json_encode($response);
				} else {
					$response["result"]  = "failure";
					$response["message"] = "Registration Failure !";
					return json_encode($response);
				}
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function registerUser($id, $firstname, $lastname)
	{
		$db = $this->db;
		if (!empty($firstname) && !empty($lastname)) {
			$result = $db->insertDataUser($id, $firstname, $lastname);
			if ($result) {
				$response["result"]     = "success";
				$response["account_id"] = $id;
				$response["message"]    = "Registered Successfully !";
				return json_encode($response);
			} else {
				$response["result"]  = "failure";
				$response["message"] = "Registration Failure !";
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function insertEvent($id, $owner, $title, $description, $phone, $location, $latitude, $longitude, $opening, $ending, $category, $tags)
	{
		$db     = $this->db;
		$result = $db->insertDataEvent($id, $owner, $title, $description, $phone, $location, $latitude, $longitude, $opening, $ending, $category, $tags);
		if ($result) {
			$response["result"]  = "success";
			$response["message"] = "Event Added!";
			return json_encode($response);
		} else {
			$response["result"]  = "failure";
			$response["message"] = "Event not added!";
			return json_encode($response);
		}
	}
	public function removeEvent($id, $editFunction)
	{
		$db     = $this->db;
		$result = $db->removeEvent($id);
		if (!$editFunction) {
			if ($result) {
				$response["result"]  = "success";
				$response["message"] = "Event removed!";
				return json_encode($response);
			} else {
				$response["result"]  = "failure";
				$response["message"] = "Event not removed!";
				return json_encode($response);
			}
		}
	}
	public function registerOrganizer($id, $name, $location, $latitude, $longitude, $phone)
	{
		$db = $this->db;
		if (!empty($name) && !empty($location) && !empty($phone)) {
			$result = $db->insertDataOrganizer($id, $name, $location, $latitude, $longitude, $phone);
			if ($result) {
				$response["result"]     = "success";
				$response["account_id"] = $id;
				$response["message"]    = "Registered Successfully !";
				return json_encode($response);
			} else {
				$response["result"]  = "failure";
				$response["message"] = "Registration Failure !";
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function login($email, $password)
	{
		$db = $this->db;
		if (!empty($email) && !empty($password)) {
			if ($db->checkUserExist($email)) {
				$result = $db->checkLogin($email, $password);
				if (!$result) {
					$response["result"]  = "failure";
					$response["message"] = "Invaild Login Credentials";
					return json_encode($response);
				} else {
					$response["result"] = "success";
					if ($result["isOrganizer"] == 0) {
						$response["message"]      = "User Login Successful";
						$response["account_id"]   = $result["id"];
						$response["is_organizer"] = false;
						$response["email"]        = $email;
						$response["firstname"]    = $result["firstname"];
						$response["lastname"]     = $result["lastname"];
						return json_encode($response);
					} else if ($result["isOrganizer"] == 1) {
						$response["message"]        = "Organizer Login Successful";
						$response["account_id"]     = $result["id"];
						$response["is_organizer"]   = true;
						$response["email"]          = $email;
						$response["organizer_name"] = $result["name"];
						$response["location"]       = $result["location"];
						$response["latitude"]       = $result["latitude"];
						$response["longitude"]      = $result["longitude"];
						$response["phone"]          = $result["phone"];
						return json_encode($response);
					}
				}
			} else {
				$response["result"]  = "failure";
				$response["message"] = "Invaild Login Credentials";
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getId($email)
	{
		$db = $this->db;
		if (!empty($email)) {
			if ($db->checkUserExist($email)) {
				$user                   = $db->getId($email);
				$response["result"]     = "success";
				$response["message"]    = "Id obtained!";
				$response["account_id"] = $user["id"];
				return json_encode($response);
			} else {
				$response["result"]  = "failure";
				$response["message"] = "Email doesn't exists!";
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getOwnedEvents($id)
	{
		$db = $this->db;
		if (!empty($id)) {
			$allEvents = $db->getOwnedEvents($id);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Got Owned Events";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getCalendarEvents($id)
	{
		$db = $this->db;
		if (!empty($id)) {
			$allEvents = $db->getCalendarEvents($id);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "Calendar empty!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Got calendar Events";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getAccountInfo($id)
	{
		$db = $this->db;
		if (!empty($id)) {
			$account = $db->getAccountInfo($id);
			if (empty($account)) {
				$response["result"]  = "failure";
				$response["message"] = "Account info not loaded!";
				return json_encode($response);
			} else {
				$response["result"]        = "success";
				$response["message"]       = "Account info loaded!";
				$response["is_organizer"]  = $account["is_organizer"];
				$response["firstname"]     = $account["firstname"];
				$response["lastname"]      = $account["lastname"];
				$response["organizer_name"]= $account["name"];
				$response["location"] 	   = $account["location"];
				$response["latitude"] 	   = $account["latitude"];
				$response["longitude"] 	   = $account["longitude"];
				$response["phone"] 		   = $account["phone"];
				$response["email"]  	   = $account["email"];
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function savePref($id, $tags, $rangeTime, $rangeDistance)
	{
		$db = $this->db;
		if (!empty($id) && !(empty($tags))) {
			$operation = $db->savePref($id, $tags, $rangeTime, $rangeDistance);
			if (!$operation) {
				$response["result"]  = "failure";
				$response["message"] = "Preferences not saved!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Preferences saved!";
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getPref($id)
	{
		$db = $this->db;
		if (!empty($id)) {
			$user = $db->getPref($id);
			if (empty($user)) {
				$response["result"]  = "failure";
				$response["message"] = "Preferences not loaded!";
				return json_encode($response);
			} else {
				$response["result"]        = "success";
				$response["message"]       = "Preferences loaded!";
				$response["tags"]          = $user["tags"];
				$response["rangeTime"]     = $user["rangeTime"];
				$response["rangeDistance"] = $user["rangeDistance"];
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function addToCalendar($user, $event)
	{
		$db = $this->db;
		if (!empty($user) && !empty($event)) {
			$operation = $db->addToCalendar($user, $event);
			if (!$operation) {
				$response["result"]  = "failure";
				$response["message"] = "Event not added!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Event added!";
				return json_encode($response);
			}
		}
	}
	public function removeFromCalendar($user, $event)
	{
		$db = $this->db;
		if (!empty($user) && !empty($event)) {
			$operation = $db->removeFromCalendar($user, $event);
			if (!$operation) {
				$response["result"]  = "failure";
				$response["message"] = "Event not removed!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Event removed!";
				return json_encode($response);
			}
		}
	}
	public function getAccountCustomEvents($id, $latitude, $longitude)
	{
		$db = $this->db;
		if (!empty($id) && !empty($latitude) && !empty($longitude)) {
			$allEvents = $db->getAccountCustomEvents($id, $latitude, $longitude);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Got account custom Events";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getCustomEvents($latitude, $longitude, $category, $tags, $rangeDistance, $rangeTime)
	{
		$db = $this->db;
		if (!empty($latitude) && !empty($longitude) && !empty($category) && !empty($tags) && !empty($rangeDistance) && !empty($rangeTime)) {
			$allEvents = $db->getCustomEvents($latitude, $longitude, $category, $tags, $rangeDistance, $rangeTime);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Got custom Events";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function searchEvents($id, $query)
	{
		$db = $this->db;
		if (!empty($id) && !empty($query)) {
			$allEvents = $db->searchEvents($id, $query);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Some Events found";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function generalSearchEvents($query)
	{
		$db = $this->db;
		if (!empty($query)) {
			$allEvents = $db->generalSearchEvents($query);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Some Events found";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getUserNearEvents($userId, $latitude, $longitude)
	{
		$db = $this->db;
		if (!empty($latitude) && !empty($longitude)) {
			$allEvents = $db->getUserNearEvents($userId, $latitude, $longitude);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Got User Near Events";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getNearEvents($latitude, $longitude, $distance)
	{
		$db = $this->db;
		if (!empty($latitude) && !empty($longitude) && !empty($distance)) {
			$allEvents = $db->getNearEvents($latitude, $longitude, $distance);
			if (!$allEvents) {
				$response["result"]  = "failure";
				$response["message"] = "No events found!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Got Near Events";
				$response["events"]  = $allEvents;
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function getAllEvents()
	{
		$db        = $this->db;
		$allEvents = $db->getAllEvents();
		if (!$allEvents) {
			$response["result"]  = "failure";
			$response["message"] = "No events found!";
			return json_encode($response);
		} else {
			$response["result"]  = "success";
			$response["message"] = "Got all Events";
			$response["events"]  = $allEvents;
			return json_encode($response);
		}
	}
	public function changePassword($email, $old_password, $new_password)
	{
		$db = $this->db;
		if (!empty($email) && !empty($old_password) && !empty($new_password)) {
			if (!$db->checkLogin($email, $old_password)) {
				$response["result"]  = "failure";
				$response["message"] = 'Invalid Old Password';
				return json_encode($response);
			} else {
				$result = $db->changePassword($email, $new_password);
				if ($result) {
					$response["result"]  = "success";
					$response["message"] = "Password Changed Successfully";
					return json_encode($response);
				} else {
					$response["result"]  = "failure";
					$response["message"] = 'Error Updating Password';
					return json_encode($response);
				}
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function deleteAccount($id)
	{
		$db = $this->db;
		if (!empty($id)) {
			$operation = $db->deleteAccount($id);
			if (!$operation) {
				$response["result"]  = "failure";
				$response["message"] = "Account not deleted!";
				return json_encode($response);
			} else {
				$response["result"]  = "success";
				$response["message"] = "Account deleted!";
				return json_encode($response);
			}
		} else {
			return $this->getMsgParamNotEmpty();
		}
	}
	public function isEmailValid($email)
	{
		return filter_var($email, FILTER_VALIDATE_EMAIL);
	}
	public function getMsgParamNotEmpty()
	{
		$response["result"]  = "failure";
		$response["message"] = "Parameters should not be empty !";
		return json_encode($response);
	}
	public function getMsgInvalidParam()
	{
		$response["result"]  = "failure";
		$response["message"] = "Invalid Parameters";
		return json_encode($response);
	}
	public function getMsgInvalidEmail()
	{
		$response["result"]  = "failure";
		$response["message"] = "Invalid Email";
		return json_encode($response);
	}
}
