<?php 
session_start();
$data = json_decode(file_get_contents("php://input"));
$login_email =$data->login_email;
$login_password =$data->login_password;
$url = '/API/';
$data = array('api_key' => 'hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH', 'operation' => 'login', 'email' => ''.$login_email.'','password' => ''.$login_password.'');
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
if($response['is_organizer']){
	$_SESSION["logged"]=true;
	$_SESSION["account_id"]=$response[account_id];
	$_SESSION["email"]=$response[email];
	$_SESSION["name"]=$response[organizer_name];
	$_SESSION["location"]=$response[location];
	$_SESSION["latitude"]=$response[latitude];
	$_SESSION["longitude"]=$response[longitude];
	$_SESSION["phone"]=$response[phone];
}
echo $result;
?>