<?php
session_start();
include 'connect.php';
if(isset($_SESSION["adminlogged"])){
	if(!$_SESSION["adminlogged"]){
		if($_POST["email"]!="" && $_POST["password"]!=""){
			$email=$_POST["email"];
			$password=$_POST["password"];
			$sql="SELECT * FROM admin WHERE email='".$email."' AND password='".$password."'";
			$result = mysqli_query($conn, $sql);
			if ($result->num_rows > 0) {
				$_SESSION["adminlogged"]=1;
				$_SESSION["error_message"]="";
				$_SESSION["email"]=$email;
			}else{
				$_SESSION["error_message"]="Wrong input!";
				header('Location: adminlogin.php');
			}
		}else{
			$_SESSION["error_message"]="Empty fields!";
			header('Location: adminlogin.php');
		}
	}
}else{
	$_SESSION["error_message"]="Not logged!";
	header('Location: adminlogin.php');
}


$page=$_GET["page"];
if($page=="")
	$page="home";
?>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<meta name="description" content="API Social Event">
<meta name="keywords" content="api, social, event">
<title>API Social Event</title>
<!-- Favicons-->
<link rel="apple-touch-icon" sizes="57x57" href="favicon/apple-icon-57x57.png">
<link rel="apple-touch-icon" sizes="60x60" href="favicon/apple-icon-60x60.png">
<link rel="apple-touch-icon" sizes="72x72" href="favicon/apple-icon-72x72.png">
<link rel="apple-touch-icon" sizes="76x76" href="favicon/apple-icon-76x76.png">
<link rel="apple-touch-icon" sizes="114x114" href="favicon/apple-icon-114x114.png">
<link rel="apple-touch-icon" sizes="120x120" href="favicon/apple-icon-120x120.png">
<link rel="apple-touch-icon" sizes="144x144" href="favicon/apple-icon-144x144.png">
<link rel="apple-touch-icon" sizes="152x152" href="favicon/apple-icon-152x152.png">
<link rel="apple-touch-icon" sizes="180x180" href="favicon/apple-icon-180x180.png">
<link rel="icon" type="image/png" sizes="192x192"  href="favicon/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32" href="favicon/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="96x96" href="favicon/favicon-96x96.png">
<link rel="icon" type="image/png" sizes="16x16" href="favicon/favicon-16x16.png">
<link rel="manifest" href="favicon/manifest.json">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage" content="favicon/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">

<!-- Add to homescreen for Chrome on Android -->
<meta name="mobile-web-app-capable" content="yes">
<link rel="icon" sizes="192x192" href="images/android-desktop.png">

<!-- Add to homescreen for Safari on iOS -->
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-title" content="Material Design Lite">
<link rel="apple-touch-icon-precomposed" href="images/ios-desktop.png">

<!-- Tile icon for Win8 (144x144 + tile color) -->
<meta name="msapplication-TileImage" content="images/touch/ms-touch-icon-144x144-precomposed.png">
<meta name="msapplication-TileColor" content="#3372DF">
<link rel="shortcut icon" href="images/favicon.png">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.cyan-light_blue.min.css">
<link rel="stylesheet" href="css/styles.css">

<!-- MaterializeCSS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.1/css/materialize.min.css">
<style>
#view-source {
	position: fixed;
	display: block;
	right: 0;
	bottom: 0;
	margin-right: 40px;
	margin-bottom: 40px;
	z-index: 900;
}
</style>
</head>
<body>
<div class="demo-layout mdl-layout mdl-js-layout mdl-layout--fixed-drawer mdl-layout--fixed-header">
  <header class="demo-header mdl-layout__header mdl-color--grey-100 mdl-color-text--grey-600">
    <div class="mdl-layout__header-row">
    <span class="mdl-layout-title"><?php echo ucwords($page);?></span> </header>
  <div class="demo-drawer mdl-layout__drawer mdl-color--blue-grey-900 mdl-color-text--blue-grey-50">
    <header class="demo-drawer-header"> <img src="images/admin.png" class="demo-avatar">
      <div class="demo-avatar-dropdown"> <span><?php echo $_SESSION["email"]; ?></span> </div>
    </header>
    <nav class="demo-navigation mdl-navigation mdl-color--blue-grey-800"> <a class="mdl-navigation__link <?php if($page=="home") echo "actualPage"; ?>" href="adminpanel.php?page=home"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation">home</i>Home</a> <a class="mdl-navigation__link <?php if($page=="permissions") echo "actualPage"; ?>" href="adminpanel.php?page=permissions"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation">vpn_key</i>Permissions</a> <a class="mdl-navigation__link <?php if($page=="accounts") echo "actualPage"; ?>" href="adminpanel.php?page=accounts"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation">supervisor_account</i>Accounts</a> <a class="mdl-navigation__link <?php if($page=="events") echo "actualPage"; ?>" href="adminpanel.php?page=events"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation">place</i>Events</a> <a class="mdl-navigation__link <?php if($page=="stats") echo "actualPage"; ?>" href="adminpanel.php?page=stats"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation">pie_chart</i>Stats</a> <a class="mdl-navigation__link" href="logout.php"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation">exit_to_app</i>Logout</a>
      <div class="mdl-layout-spacer"></div>
    </nav>
  </div>
  <?php include "adminpages/".$page.".php";?>
</div>
<script src="https://code.getmdl.io/1.3.0/material.min.js"></script>
</body>
</html>