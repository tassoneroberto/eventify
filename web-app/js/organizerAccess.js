function checkLogin() {
	var data = {
		"login_email": document.getElementById("organizerLoginEmail").value,
		"login_password": document.getElementById("organizerLoginPassword").value
	}
	document.getElementById("loadingSpinner").style.display = 'inherit';
	$.ajax({
		url: 'checklogin.php',
		type: 'POST',
		data: JSON.stringify(data),
		contentType: 'application/json',
		dataType: 'json',
		async: true,
		success: function (response) {
			document.getElementById("loadingSpinner").style.display = 'none';
			if (response.is_organizer) {
				$('#form-dialog').modal('hide');
				document.getElementById("loginMessage").innerHTML = "";
				document.getElementById("organizerLoginSignin").style.display = 'none';
				document.getElementById("organizerSidebar").style.display = 'inherit';
				document.getElementById("filterSidebar").style.display = 'none';
				document.getElementById("organizerNameSidebar").innerHTML = response.organizer_name;
				window.location.replace("/?page=organizer");
			}
			else {
				document.getElementById("loginMessage").innerHTML = "Wrong credentials!";
			}
		}
	});
}

function logout() {
	document.getElementById("organizerLoginSignin").style.display = 'inherit';
	document.getElementById("organizerSidebar").style.display = 'none';
	document.getElementById("filterSidebar").style.display = 'inherit';

	$.ajax({
		url: 'logout.php',
		async: true,
		success: function (response) {
			if (redirect)
				window.location.replace("/");
		}
	});

}

function register() {
	if (document.getElementById("organizerRegisterPassword1").value != document.getElementById("organizerRegisterPassword2").value) {
		document.getElementById("registerMessage").innerHTML = "Password doesn't match!";
		return "failure";
	}
	var data = {
		"email": document.getElementById("organizerRegisterEmail").value,
		"password": document.getElementById("organizerRegisterPassword1").value,
		"name": document.getElementById("organizerRegisterName").value,
		"location": document.getElementById("organizerRegisterLocation").value,
		"latitude": document.getElementById("organizerRegisterLatitude").value,
		"longitude": document.getElementById("organizerRegisterLongitude").value,
		"phone": document.getElementById("organizerRegisterPhone").value,
	}
	document.getElementById("loadingSpinner").style.display = 'inherit';
	$.ajax({
		url: 'register.php',
		type: 'POST',
		data: JSON.stringify(data),
		contentType: 'application/json',
		dataType: 'json',
		async: true,
		success: function (response) {
			document.getElementById("loadingSpinner").style.display = 'none';

			if (response.result == "success") {
				$('#form-signin').modal('hide');
				document.getElementById("registerMessage").innerHTML = "";
				document.getElementById("organizerLoginSignin").style.display = 'none';
				document.getElementById("organizerSidebar").style.display = 'inherit';
				document.getElementById("filterSidebar").style.display = 'none';
				document.getElementById("organizerNameSidebar").innerHTML = response.organizer_name;
			}
			else {
				document.getElementById("registerMessage").innerHTML = "Registration failed!";
			}
		}
	});
}