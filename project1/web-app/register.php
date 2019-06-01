<?php 
session_start();
$data = json_decode(file_get_contents("php://input"));
$email =$data->email;
$password =$data->password;
$name =$data->name;
$phone =$data->phone;
$location =$data->location;
$latitude =$data->latitude;
$longitude =$data->longitude;
$url = 'https://apisocialevent.altervista.org/API/';
$data = array('api_key' => 'hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH', 'operation' => 'registerOrganizer', 'email' => ''.$email.'','password' => ''.$password.'','organizer_name' => ''.$name.'','phone' => ''.$phone.'','location' => ''.$location.'','latitude' => ''.$latitude.'','longitude' => ''.$longitude.'');
$data_json = json_encode($data);
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS,$data_json);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result  = curl_exec($ch);
$response  = json_decode($result,true);
curl_close($ch);
if($response[result]=="success"){
	$_SESSION[logged]=true;
	$_SESSION[account_id]=$response[account_id];
	$_SESSION[email]=$email;
	$_SESSION[name]=$name;
	$_SESSION[location]=$location;
	$_SESSION[latitude]=$latitude;
	$_SESSION[longitude]=$longitude;
	$_SESSION[phone]=$phone;
	$response[organizer_name]=$name;
	$response[account_id];
}
echo json_encode($response,true);
