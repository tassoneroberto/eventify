<?php 
session_start();
$errorSettings=false;
$newEmailMessage="";
$newPasswordMessage="";
$newEmail=$_POST["newEmail"];
$passwordCheck=$_POST["passwordCheck"];
$passwordCheck2=$_POST["passwordCheck2"];
if($newEmail!="" && $passwordCheck!=""){
	$sql="SELECT * FROM api_login WHERE email='".$_SESSION["email"]."' AND password='".$passwordCheck."'";
	$result = mysqli_query($conn, $sql);
	if ($result->num_rows > 0) {
		$sql="UPDATE api_login SET email='".$newEmail."' WHERE email='".$_SESSION["email"]."' ";	
		$result = mysqli_query($conn, $sql);
		$newEmailMessage="Email changed successfully!";
		$_SESSION["email"]=$newEmail;
	}else{
		$newEmailMessage="Wrong password!";
		$errorSettings=true;
	}
}

$oldPassword=$_POST["oldPassword"];
$newPassword1=$_POST["newPassword1"];
$newPassword2=$_POST["newPassword2"];
if($oldPassword!="" && $newPassword1!="" && $newPassword2!=""){
	$sql="SELECT * FROM api_login WHERE email='".$_SESSION["email"]."' AND password='".$oldPassword."'";
	$result = mysqli_query($conn, $sql);
	if ($result->num_rows > 0) {
		if($newPassword1!=$newPassword2){
			$newPasswordMessage="New password doesn't match!";
			$errorSettings=true;
		}
		else{
			$sql="UPDATE api_login SET password='".$newPassword1."' WHERE email='".$_SESSION["email"]."' ";
			$result = mysqli_query($conn, $sql);
			$newPasswordMessage="Password changed successfully!";
		}
	}else{
		$newPasswordMessage="Wrong old password!";
		$errorSettings=true;
	}
}

if($passwordCheck2!=""){
	$sql="SELECT * FROM api_login WHERE email='".$_SESSION["email"]."' AND password='".$passwordCheck2."'";
	$result = mysqli_query($conn, $sql);
	if ($result->num_rows > 0) {
		$sql="DELETE FROM api_login WHERE email='".$_SESSION["email"]."' ";
		$result = mysqli_query($conn, $sql);
		session_destroy();
		echo "<script>location.href='/';</script>";
		exit;
	}else{
		$deleteAccountMessage="Wrong password!";
		$errorSettings=true;
	}
}
?>
<style>
.demo-card-wide.mdl-card {
	width: 100%;
	min-height: inherit;
	padding: 15px;
}
.demo-card-wide > .mdl-card__menu {
	color: #fff;
}
</style>
<main class="mdl-layout__content mdl-color--grey-100">
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Change Email</h3>
    <form action="managepanel.php?page=settings" method="post">
      <div class="row margin">
        <input id="newEmail" name="newEmail" type="text" placeholder="New Email" onKeyUp="if(document.getElementById('newEmail').value=='' || document.getElementById('passwordCheck').value=='') document.getElementById('changeEmailButton').disabled = true; else document.getElementById('changeEmailButton').disabled = false;">
        <br />
        <input id="passwordCheck" name="passwordCheck" type="password" placeholder="Password" onKeyUp="if(document.getElementById('newEmail').value=='' || document.getElementById('passwordCheck').value=='') document.getElementById('changeEmailButton').disabled = true; else document.getElementById('changeEmailButton').disabled = false;">
        <br />
        <button id="changeEmailButton" class="btn waves-effect waves-light col s12" disabled>Confirm</button>
        <p style=" <?php if($errorSettings) echo 'color:red;'; else echo 'color:green;'; ?> font-size:12px;"><?php echo $newEmailMessage;?></p>
      </div>
    </form>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Change Password</h3>
    <form action="managepanel.php?page=settings" method="post">
      <div class="row margin">
        <input id="oldPassword" name="oldPassword" type="password" placeholder="Old Password" onKeyUp="if(document.getElementById('oldPassword').value=='' || document.getElementById('newPassword1').value=='' || document.getElementById('newPassword2').value=='') document.getElementById('changePasswordButton').disabled = true; else document.getElementById('changePasswordButton').disabled = false;">
        <br />
        <input id="newPassword1" name="newPassword1" type="password" placeholder="New Password" onKeyUp="if(document.getElementById('oldPassword').value=='' || document.getElementById('newPassword1').value=='' || document.getElementById('newPassword2').value=='') document.getElementById('changePasswordButton').disabled = true; else document.getElementById('changePasswordButton').disabled = false;">
        <br />
        <input id="newPassword2" name="newPassword2" type="password" placeholder="Confirm Password" onKeyUp="if(document.getElementById('oldPassword').value=='' || document.getElementById('newPassword1').value=='' || document.getElementById('newPassword2').value=='') document.getElementById('changePasswordButton').disabled = true; else document.getElementById('changePasswordButton').disabled = false;">
        <br />
        <button id="changePasswordButton" class="btn waves-effect waves-light col s12" disabled>Confirm</button>
        <p style=" <?php if($errorSettings) echo 'color:red;'; else echo 'color:green;'; ?> font-size:12px;"><?php echo $newPasswordMessage;?></p>
      </div>
    </form>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Delete Account</h3>
    <form action="managepanel.php?page=settings" method="post">
      <div class="row margin">
        <input id="passwordCheck2" name="passwordCheck2" type="password" placeholder="Password" onKeyUp="if(document.getElementById('passwordCheck2').value=='') document.getElementById('deleteAccountButton').disabled = true; else document.getElementById('deleteAccountButton').disabled = false;">
        <br />
        <button id="deleteAccountButton" class="btn waves-effect waves-light col s12" disabled>Confirm</button>
        <p style=" <?php if($errorSettings) echo 'color:red;'; else echo 'color:green;'; ?> font-size:12px;"><?php echo $deleteAccountMessage;?></p>
      </div>
    </form>
  </div>
</div>
</main>
