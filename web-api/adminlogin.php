<?php
session_start();
if($_SESSION["adminlogged"])
	header('Location: adminpanel.php');
if(!isset($_SESSION["adminlogged"]))
	$_SESSION["adminlogged"]=0;
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
<!-- CSS-->
<link href="login/css/materialize.min.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="login/css/style.min.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="login/css/page-center.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="login/css/prism.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="login/css/perfect-scrollbar.css" type="text/css" rel="stylesheet" media="screen,projection">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en">
<style>
.orContainer {
	display: -webkit-box;
	display: -webkit-flex;
	display: -ms-flexbox;
	display: flex;
	-webkit-box-orient: vertical;
	-webkit-box-direction: normal;
	-webkit-flex-direction: column;
	-ms-flex-direction: column;
	flex-direction: column;
	-webkit-box-orient: horizontal;
	-webkit-box-direction: normal;
	-webkit-flex-direction: row;
	-ms-flex-direction: row;
	flex-direction: row;
	margin: 10px 40px 10px;
}
.orLeft {
	-webkit-box-flex: 1;
	-webkit-flex-grow: 1;
	-ms-flex-positive: 1;
	flex-grow: 1;
	-webkit-flex-shrink: 1;
	-ms-flex-negative: 1;
	flex-shrink: 1;
	background-color: #c7c7c7;
	height: 1px;
	position: relative;
	top: .45em;
}
.orCenter {
	-webkit-box-flex: 0;
	-webkit-flex-grow: 0;
	-ms-flex-positive: 0;
	flex-grow: 0;
	-webkit-flex-shrink: 0;
	-ms-flex-negative: 0;
	flex-shrink: 0;
	text-transform: uppercase;
	font-size: 13px;
	line-height: 15px;
	margin: 0 10px;
}
.orRight {
	-webkit-box-flex: 1;
	-webkit-flex-grow: 1;
	-ms-flex-positive: 1;
	flex-grow: 1;
	-webkit-flex-shrink: 1;
	-ms-flex-negative: 1;
	flex-shrink: 1;
	background-color: #c7c7c7;
	height: 1px;
	position: relative;
	top: .45em;
}
</style>
<!-- FONT AWESOME -->
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" type="text/css" rel="stylesheet">
</head>

<body class="cyan">
<!-- Start Page Loading -->
<div id="loader-wrapper">
  <div id="loader"></div>
  <div class="loader-section section-left"></div>
  <div class="loader-section section-right"></div>
</div>
<!-- End Page Loading -->

<div id="login-page" class="row">
  <div class="col s12 z-depth-4 card-panel">
    <form class="login-form" action="adminpanel.php" method="post">
      <div class="row">
        <div class="input-field col s12 center"> <img src="images/logo.png" alt="" class="responsive-img valign profile-image-login">
          <p class="center login-form-text">Admin Login Panel</p>
        </div>
      </div>
      <div class="row margin">
        <div class="input-field col s12"> <i class="mdi-social-person-outline prefix fa fa-user"></i>
          <input id="email" name="email" type="text" onKeyUp="if(document.getElementById('email').value=='' || document.getElementById('password').value=='') document.getElementById('loginButton').disabled = true; else document.getElementById('loginButton').disabled = false;">
          <label for="email" class="center-align">Email</label>
        </div>
      </div>
      <div class="row margin">
        <div class="input-field col s12"> <i class="mdi-action-lock-outline prefix fa fa-key"></i>
          <input id="password" name="password" type="password" onKeyUp="if(document.getElementById('email').value=='' || document.getElementById('password').value=='') document.getElementById('loginButton').disabled = true; else document.getElementById('loginButton').disabled = false;">
          <label for="password">Password</label>
        </div>
      </div>
      <div class="row">
        <div class="input-field col s12">
          <button id="loginButton" class="btn waves-effect waves-light col s12" disabled>Login</button>
        </div>
      </div>
      <hr />
      <div class="row">
        <div class="input-field col s12"> <a class="btn waves-effect waves-light col s12" href="/">Back</a> </div>
      </div>
      <p class="margin medium-small" style="text-align:center; color:red;">
        <?php if($_SESSION["error_message"]!=""){ echo "Error: ".$_SESSION["error_message"]; $_SESSION["error_message"]=""; }?>
      </p>
    </form>
  </div>
</div>

<!-- jQuery Library --> 
<script type="text/javascript" src="login/js/jquery-1.11.2.min.js"></script> 
<!--materialize js--> 
<script type="text/javascript" src="login/js/materialize.min.js"></script> 
<!--prism--> 
<script type="text/javascript" src="login/js/prism.js"></script> 
<!--scrollbar--> 
<script type="text/javascript" src="login/js/perfect-scrollbar.min.js"></script> 

<!--plugins.js - Some Specific JS codes for Plugin Settings--> 
<script type="text/javascript" src="login/js/plugins.min.js"></script> 
</body>
</html>