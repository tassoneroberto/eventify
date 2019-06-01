<?php
session_start();
if (isset($_GET["page"]))
	$page = $_GET["page"];
else
	$page = "home";
?>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="Events based social platform. Find events near you or add some new. It's easy and totally free.">
	<meta name="keywords" content="eventify, event, events, near, create, find">
	<meta name="theme-color" content="#5147FF" />
	<link rel="apple-touch-icon" sizes="57x57" href="images/favicon/apple-icon-57x57.png">
	<link rel="apple-touch-icon" sizes="60x60" href="images/favicon/apple-icon-60x60.png">
	<link rel="apple-touch-icon" sizes="72x72" href="images/favicon/apple-icon-72x72.png">
	<link rel="apple-touch-icon" sizes="76x76" href="images/favicon/apple-icon-76x76.png">
	<link rel="apple-touch-icon" sizes="114x114" href="images/favicon/apple-icon-114x114.png">
	<link rel="apple-touch-icon" sizes="120x120" href="images/favicon/apple-icon-120x120.png">
	<link rel="apple-touch-icon" sizes="144x144" href="images/favicon/apple-icon-144x144.png">
	<link rel="apple-touch-icon" sizes="152x152" href="images/favicon/apple-icon-152x152.png">
	<link rel="apple-touch-icon" sizes="180x180" href="images/favicon/apple-icon-180x180.png">
	<link rel="icon" type="image/png" sizes="192x192" href="images/favicon/android-icon-192x192.png">
	<link rel="icon" type="image/png" sizes="32x32" href="images/favicon/favicon-32x32.png">
	<link rel="icon" type="image/png" sizes="96x96" href="images/favicon/favicon-96x96.png">
	<link rel="icon" type="image/png" sizes="16x16" href="images/favicon/favicon-16x16.png">
	<link rel="manifest" href="images/favicon/manifest.json">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/vaakash/socializer/01252dd4/css/socializer.min.css">
	<meta name="msapplication-TileColor" content="#ffffff">
	<meta name="msapplication-TileImage" content="images/favicon/ms-icon-144x144.png">
	<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" type="text/css" href="propeller/fullcalendar.min.css">
	<link rel="stylesheet" media="print" type="text/css" href="propeller/fullcalendar.print.css">
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/moment.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.5.1/fullcalendar.min.js"></script>
	<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<![endif]-->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<!-- CUSTOM CSS & JS -->
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" type="text/css" href="css/loadingSpinner.css">
	<script type="text/javascript" src="js/organizerAccess.js"></script>
	<!-- PROPELLER -->
	<link rel="stylesheet" type="text/css" href="propeller/propeller.css">
	<link rel="stylesheet" type="text/css" href="propeller/select2.min.css">
	<link rel="stylesheet" type="text/css" href="propeller/pmd-select2.css">
	<link rel="stylesheet" type="text/css" href="propeller/typography.css">
	<link rel="stylesheet" type="text/css" href="propeller/bootstrap-datetimepicker.css">
	<link rel="stylesheet" type="text/css" href="propeller/pmd-datetimepicker.css">
	<link rel="stylesheet" type="text/css" href="propeller/nouislider.min.css">
	<link rel="stylesheet" type="text/css" href="propeller/range-slider.css">
	<script type="text/javascript" src="propeller/checkbox.js"></script>
	<script type="text/javascript" src="propeller/sidebar.js"></script>
	<script type="text/javascript" src="propeller/dropdown.js"></script>
	<script type="text/javascript" src="propeller/ripple-effect.js"></script>
	<script type="text/javascript" src="propeller/radio.js"></script>
	<script type="text/javascript" src="propeller/textfield.js"></script>
	<script type="text/javascript" src="propeller/select2.full.js"></script>
	<script type="text/javascript" src="propeller/pmd-select2.js"></script>
	<script type="text/javascript" src="propeller/moment-with-locales.js"></script>
	<script type="text/javascript" src="propeller/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="propeller/nouislider.js"></script>
	<script type="text/javascript" src="propeller/wNumb.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".select-tags").select2({
				tags: false,
			})
		});
		$(document).ready(function() {
			$(".select-simple").select2({
				minimumResultsForSearch: Infinity,
			});
		});
	</script>
	<script>
		// GLOBAL JS VARIABLES
		var redirect = false;
		// Default Coordinates
		var defaultLatitude = 39.364764;
		var defaultLongitude = 16.2253649;
		var lat = defaultLatitude;
		var lng = defaultLongitude;
	</script>
	<title>Eventify</title>
	<meta property="og:type" content="website" />
	<meta property="fb:app_id" content="124351901639209" />
</head>

<body>

	<script>
		window.fbAsyncInit = function() {
			FB.init({
				appId: '124351901639209',
				autoLogAppEvents: true,
				xfbml: true,
				version: 'v2.10'
			});
			FB.AppEvents.logPageView();
		};
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id)) {
				return;
			}
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/en_US/sdk.js";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	</script>
	<!-- Nav menu with user information -->
	<nav class="navbar navbar-inverse navbar-fixed-top pmd-navbar pmd-z-depth-2" style="background:#5147FF;border: transparent;">
		<div class="container-fluid">
			<!-- Sidebar Toggle Button-->
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header"> <a href="javascript:void(0);" class="btn btn-sm pmd-btn-fab pmd-btn-flat pmd-ripple-effect white pull-left margin-r8 pmd-sidebar-toggle"><i class="material-icons">menu</i></a> <a class="navbar-brand" href="/">Eventify</a>
				<!-- Organizer login/signin -->
				<div id="loadingSpinner" class="loading" style="display:none; z-index:10;">Loading&#8230;</div>
			</div>
			<div id="organizerLoginSignin" class="dropdown pmd-dropdown pmd-user-info pull-right" style="margin:0px; padding:9px 3px 9px 0px; <?php if (isset($_SESSION["logged"]) && $_SESSION["logged"]) echo 'display:none'; ?>">
				<div class="media-right media-middle">
					<div id="search-div" style="float:left; margin: 0; padding: 0;margin-top: 9px" class="form-group pmd-textfield pmd-textfield-floating-label">
						<input id="querySearchEvents" class="form-control" type="text" placeholder="Search"><span class="pmd-textfield-focused"></span>
						<div class="search-results"></div>
					</div>
					<button onclick="searchEvents()" style="float:left; margin: 0; padding: 0;" class="btn btn-sm pmd-btn-fab pmd-btn-flat pmd-ripple-effect white ">
						<i class="material-icons md-light">search</i>
					</button>
				</div>
				<div class="media-right media-middle">
					<button type="button" data-target="#form-dialog" data-toggle="modal" class="btn pmd-btn-flat pmd-ripple-effect btn-default">Log In</button>
				</div>
				<div class="media-right media-middle">
					<button type="button" data-target="#form-signin" data-toggle="modal" class="btn pmd-btn-flat pmd-ripple-effect btn-default">Sign In</button>
				</div>
			</div>
			<!-- Brand and toggle get grouped for better mobile display -->
		</div>
	</nav>
	<!-- Sidebar -->
	<section id="pmd-main" style="height: 100%;">
		<!-- Left sidebar -->
		<aside id="sidebar" class="pmd-sidebar sidebar-custom sidebar-default pmd-sidebar-slide-push pmd-sidebar-left pmd-z-depth sidebar-hide-custom" role="navigation" style="background:#5147FF;">
			<ul id="organizerSidebar" <?php if (!isset($_SESSION["logged"]) || (isset($_SESSION["logged"]) && !$_SESSION["logged"])) echo 'style="display:none"'; ?> class="nav pmd-sidebar-nav ">
				<!-- My Account -->
				<li class="pmd-user-info">
					<a class=" media" href="/?page=organizer">
						<i class="material-icons media-left media-middle">person</i>
						<div class="media-body media-middle" id="organizerNameSidebar">
							<?php if (isset($_SESSION[name])) echo $_SESSION[name]; ?>
						</div>
					</a>
				</li>
				<li> <a class="pmd-ripple-effect" href="javascript:void(0);" tabindex="-1" onClick="logout()"><i class="material-icons media-left media-middle">history</i> <span class="media-body">Logout</span></a> </li>
				<li> <a class="pmd-ripple-effect " href="javascript:void(0);"><i class="material-icons media-left media-middle">info</i> <span class="media-body">About</span></a> </li>
			</ul>
			<ul class="nav pmd-sidebar-nav" id="filterSidebar" <?php if (isset($_SESSION["logged"]) && $_SESSION["logged"]) echo 'style="display:none"'; ?>>
				<li style="margin: 24px;">
					<h3 style="color: white;margin:30px 0px 12px 0px;"><i style="color:#ff7300;vertical-align: top;margin-right: 6px" class="material-icons md-dark pmd-xs">location_on</i>Select distance in Km</h3>
					<div id="filterRangeDistance" class="pmd-range-slider"></div><input type="text" id="hiddenDistance" style="display:none" />
					<h3 style="color: white;margin:30px 0px 12px 0px;"><i style="color:#ff7300;vertical-align: top;margin-right: 6px" class="material-icons md-dark pmd-xs">access_time</i>Select day</h3>
					<div id="filterRangeTime" class="pmd-range-slider"></div> <input type="text" id="hiddenTime" style="display:none" />
					<h3 style="color: white;margin:30px 0px 12px 0px;"><i style="color:#ff7300;vertical-align: top;margin-right: 6px" class="material-icons md-dark pmd-xs">label</i>Select category</h3>
					<select onchange="selectFIlterTags()" id="filterCategory" class="select-simple form-control pmd-select2" style="width: 100%;">
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
					<h3 style="color: white;margin:30px 0px 12px 0px;"><i style="color:#ff7300;vertical-align: top;margin-right: 6px" class="material-icons md-dark pmd-xs">label_outline</i>Select tags</h3>
					<select id="filterTags" class="select-tags form-control pmd-select2-tags" multiple style="width: 100%;">
						<option disabled>Select a category in order to insert tags</option>
					</select>
					<button style="margin-top: 30px;width:100%;" data-dismiss="" class="btn pmd-btn-flat
							pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="filterEvents()"><i style="color:#ff7300;vertical-align: text-top;" class="material-icons md-dark pmd-xs">filter_list</i>
						FILTER</button>
				</li>
				<!--
		<li> <a class="pmd-ripple-effect " href="javascript:void(0);"><i class="material-icons media-left media-middle">info</i> <span class="media-body">About</span></a> </li>
-->
				<!--<li> <a class="pmd-ripple-effect" href="javascript:void(0);"><i class="material-icons media-left media-middle">info</i> <span class="media-body">Credits</span></a> </li>
          <li> <a class="pmd-ripple-effect" href="index.php?page=organizerTest"><i class="material-icons media-left media-middle">info</i> <span class="media-body">organizerTest</span></a> </li>-->
			</ul>
		</aside>
		<div tabindex="-1" class="modal fade" id="share-dialog" style="display: none;" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header pmd-modal-bordered">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button" style="color:white">×</button>
						<h2 class="pmd-card-title-text">Share with :</h2>
					</div>
					<div class="modal-body">
						<div id="shareLink" class="socializer" data-features="48px,circle,zoom,icon-white,pad" data-sites="telegram,reddit,facebook,fbmessenger,whatsapp,googleplus,wechat,vkontakte,instagram,wechat,linkedin,vkontakte,snapchat,twitter,print,email" data-meta-link="/" data-meta-title="Eventify"></div>
					</div>
				</div>
			</div>
		</div>
		<!-- Dialog with Form Elements -->
		<div tabindex="-1" class="modal fade" id="form-dialog" style="display: none;" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header pmd-modal-bordered">
						<button aria-hidden="true" data-dismiss="modal" class="close" style="color:white" type="button">×</button>
						<h2 class="pmd-card-title-text">Organizer Login</h2>
					</div>
					<div class="modal-body">
						<form class="form-horizontal">
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Email</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">perm_identity</i> </div>
									<input type="text" id="organizerLoginEmail" class="form-control">
								</div>
							</div>
							<!-- Password with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Password</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">https</i> </div>
									<input type="password" id="organizerLoginPassword" class="form-control">
								</div>
							</div>
						</form>
					</div>
					<div class="pmd-modal-action">
						<button data-dismiss="" class="btn pmd-btn-flat
							pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="checkLogin()">LOG IN</button>
						<button data-dismiss="modal" class="btn pmd-btn-flat
							pmd-ripple-effect btn-default" type="button">CANCEL</button>
						<span id="loginMessage"></span> </div>
				</div>
			</div>
		</div>
		<div tabindex="-1" class="modal fade" id="form-signin" style="display: none;" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header pmd-modal-bordered">
						<button aria-hidden="true" data-dismiss="modal" class="close" style="color:white" type="button">×</button>
						<h2 class="pmd-card-title-text">Organizer Registration</h2>
					</div>
					<div class="modal-body">
						<form class="form-horizontal">
							<!-- User name with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Organizer name</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">domain</i> </div>
									<input type="text" class="form-control" id="organizerRegisterName">
								</div>
							</div>
							<!-- User name with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Telephone number</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">phone</i> </div>
									<input type="text" class="form-control" id="organizerRegisterPhone">
								</div>
							</div>
							<!-- User name with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Address</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">location_on</i> </div>
									<input type="text" class="form-control" id="organizerRegisterLocation">
									<input type="text" class="form-control" id="organizerRegisterLatitude" style="display:none;">
									<input type="text" class="form-control" id="organizerRegisterLongitude" style="display:none;">
								</div>
							</div>
							<!-- User name with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Email</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">email</i> </div>
									<input type="text" class="form-control" id="organizerRegisterEmail">
								</div>
							</div>
							<!-- Password with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Password</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">https</i> </div>
									<input type="password" class="form-control" id="organizerRegisterPassword1">
								</div>
							</div>
							<!-- Password with floating label -->
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Repeat password</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">https</i> </div>
									<input type="password" class="form-control" id="organizerRegisterPassword2">
								</div>
							</div>
							<label class="checkbox-inline pmd-checkbox pmd-checkbox-ripple-effect">
								<input type="checkbox" value="">
								<span class="pmd-checkbox"> Accept Terms and conditions</span> </label>
						</form>
					</div>
					<div class="pmd-modal-action">
						<button data-dismiss="" class="btn pmd-btn-flat
							pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="register()">SIGN IN</button>
						<button data-dismiss="modal" class="btn pmd-btn-flat
							pmd-ripple-effect btn-default" type="button">DISCARD</button>
						<span id="registerMessage"></span> </div>
				</div>
			</div>
		</div>
		<!--
		<div tabindex="-1" class="modal fade" id="form-dialog" style="display: none;" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header pmd-modal-bordered">
						<button aria-hidden="true" data-dismiss="modal" class="close" style="color:white" type="button">×</button>
						<h2 class="pmd-card-title-text">Organizer Login</h2>
					</div>
					<div class="modal-body">
						<form class="form-horizontal">
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Email</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">perm_identity</i> </div>
									<input type="text" id="organizerLoginEmail" class="form-control">
								</div>
							</div>
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Password</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">https</i> </div>
									<input type="password" id="organizerLoginPassword" class="form-control">
								</div>
							</div>
						</form>
					</div>
					<div class="pmd-modal-action">
						<button data-dismiss="" class="btn pmd-btn-flat
              pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="checkLogin()">LOG IN</button>
						<button data-dismiss="modal" class="btn pmd-btn-flat
              pmd-ripple-effect btn-default" type="button">CANCEL</button>
						<span id="loginMessage"></span> </div>
				</div>
			</div>
		</div>
		<div tabindex="-1" class="modal fade" id="form-signin" style="display: none;" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header pmd-modal-bordered">
						<button aria-hidden="true" data-dismiss="modal" class="close" style="color:white" type="button">×</button>
						<h2 class="pmd-card-title-text">Organizer Registration</h2>
					</div>
					<div class="modal-body">
						<form class="form-horizontal">
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Organizer name</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">domain</i> </div>
									<input type="text" class="form-control" id="organizerRegisterName">
								</div>
							</div>
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Telephone number</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">phone</i> </div>
									<input type="text" class="form-control" id="organizerRegisterPhone">
								</div>
							</div>
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Address</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">location_on</i> </div>
									<input type="text" class="form-control" id="organizerRegisterLocation">
									<input type="text" class="form-control" id="organizerRegisterLatitude" style="display:none;">
									<input type="text" class="form-control" id="organizerRegisterLongitude" style="display:none;">
								</div>
							</div>
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Email</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">email</i> </div>
									<input type="text" class="form-control" id="organizerRegisterEmail">
								</div>
							</div>
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Password</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">https</i> </div>
									<input type="password" class="form-control" id="organizerRegisterPassword1">
								</div>
							</div>
							<div class="form-group pmd-textfield pmd-textfield-floating-label">
								<label for="inputError1" class="control-label pmd-input-group-label">Repeat password</label>
								<div class="input-group">
									<div class="input-group-addon"><i class="material-icons md-light pmd-sm">https</i> </div>
									<input type="password" class="form-control" id="organizerRegisterPassword2">
								</div>
							</div>
							<label class="checkbox-inline pmd-checkbox pmd-checkbox-ripple-effect">
                  <input type="checkbox" value="">
                  <span class="pmd-checkbox"> Accept Terms and conditions</span> </label>
						</form>
					</div>
					<div class="pmd-modal-action">
						<button data-dismiss="" class="btn pmd-btn-flat
              pmd-btn-flat pmd-ripple-effect btn-primary" type="button" onClick="register()">SIGN IN</button>
						<button data-dismiss="modal" class="btn pmd-btn-flat
              pmd-ripple-effect btn-default" type="button">DISCARD</button>
						<span id="registerMessage"></span> </div>
				</div>
			</div>
		</div> -->
		<!-- Content -->
		<div class="pmd-content" id="content" style="height: 100%;">
			<?php include $page . ".php"; ?>
			<div id="footer"> Eventify ©
				<?php echo date("Y"); ?> . All Rights Reserved. </div>
		</div>
	</section>
	<!-- Maps -->
	<script>
	</script>
	<script>
		$(function() {
			$('.myarrow').click(function() {
				alert("rotate");
				$(this).toggleClass('rotate');
			});
		});
	</script>
	<script src="https://cdn.rawgit.com/vaakash/socializer/01252dd4/js/socializer.min.js"></script>
	<script>
		// single range slider with step
		var pmdSliderDistance = document.getElementById('filterRangeDistance');
		noUiSlider.create(pmdSliderDistance, {
			start: [30],
			connect: 'lower',
			tooltips: [wNumb({
				decimals: 0
			})],
			range: {
				'min': [10],
				'max': [110]
			},
			step: 20,
		});
		pmdSliderDistance.noUiSlider.on('update', function(values, handle) {
			document.getElementById('hiddenDistance').value = values[handle];
		});
	</script>
	<script>
		// single range slider with step
		var pmdSliderTime = document.getElementById('filterRangeTime');
		noUiSlider.create(pmdSliderTime, {
			start: [20],
			connect: 'lower',
			tooltips: [wNumb({
				decimals: 0
			})],
			range: {
				'min': [10],
				'max': [60]
			},
			step: 10,
		});
		pmdSliderTime.noUiSlider.on('update', function(values, handle) {
			document.getElementById('hiddenTime').value = values[handle];
		});
	</script>
	<script>
		$("#filterCategory").select2({
			initSelection: function(element, callback) {
				var data = {};
				callback(data);
			} //Then the rest of your configurations (e.g.: ajax, allowClear, etc.)
		});
		$("#filterTags").select2({
			initSelection: function(element, callback) {
				var data = []; //Array
				callback(data); //Fill'em
			}
		});
	</script>
	<script>
		function selectFIlterTags() {
			var tagsContainer = document.getElementById("filterTags");
			var category = document.getElementById("filterCategory").value;
			tagsContainer.innerHTML = "";
			$("#filterTags").empty().trigger('change');
			if (category == "Cinema") {
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
			} else if (category == "Food & Drink") {
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
			} else if (category == "Disco") {
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
			} else if (category == "Live Music") {
				tagsContainer.innerHTML += "<option>Cover Band</option>";
				tagsContainer.innerHTML += "<option>Live Albums</option>";
				tagsContainer.innerHTML += "<option>Live Singles</option>";
				tagsContainer.innerHTML += "<option>Concerts</option>";
				tagsContainer.innerHTML += "<option>Music Festivals</option>";
				tagsContainer.innerHTML += "<option>Live Coding</option>";
				tagsContainer.innerHTML += "<option>Music Venues</option>";
			} else if (category == "Festival") {
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
			} else if (category == "Outdoor") {
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
			} else if (category == "Theatre") {
				tagsContainer.innerHTML += "<option>Drama</option>";
				tagsContainer.innerHTML += "<option>Musical</option>";
				tagsContainer.innerHTML += "<option>Comedy</option>";
				tagsContainer.innerHTML += "<option>Tragedy</option>";
				tagsContainer.innerHTML += "<option>Improvisation</option>";
			} else if (category == "Museum") {
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
		$(document).ready(function() {
			// page is now ready, initialize the calendar...
			$('#calendar').fullCalendar({
				height: 540
			})
		});
	</script>
</body>

</html>