<?php
session_start();
include '../connect.php';
if (isset($_POST["operation"])) {
	if ($_POST["operation"] == "changePermission") {
		$sql    = "UPDATE api_login SET authorized=$_POST[authorized] WHERE id=$_POST[id]";
		$result = mysqli_query($conn, $sql);
	} else if ($_POST["operation"] == "deleteAccount") {
		$url     = 'https://apisocialevent.altervista.org/API/';
		$data    = array(
			'api_key' => 'hi1OrDf58decjXluKusJSYaVNGqI3IVu',
			'operation' => 'deleteAccount',
			'account_id' => '' . $_POST[id] . ''
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
	} else if ($_POST["operation"] == "deleteApiKey") {
		$sql    = "DELETE FROM api_login WHERE id=$_POST[id]";
		$result = mysqli_query($conn, $sql);
	} else if ($_POST["operation"] == "removeEvent") {
		$url     = 'https://apisocialevent.altervista.org/API/';
		$data    = array(
			'api_key' => 'hi1OrDf58decjXluKusJSYaVNGqI3IVu',
			'operation' => 'removeEvent',
			'event_id' => '' . $_POST[id] . ''
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
	}
}
?>