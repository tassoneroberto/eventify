<style>
.demo-card-wide.mdl-card {
	width: 100%;
	min-height: inherit;
	padding: 15px;
}
.demo-card-wide > .mdl-card__menu {
	color: #fff;
}
table {
	font-size: 14px;
}
</style>
<main class="mdl-layout__content mdl-color--grey-100">
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Organizer accounts</h3>
    <table>
      <tr>
        <td><b>Name</b></td>
        <td><b>Location</b></td>
        <td><b>Phone</b></td>
        <td><b>Delete</b></td>
      </tr>
      <?php 
	
	$sql="SELECT * FROM organizer";
	$result = mysqli_query($conn, $sql);
	while($row = $result->fetch_assoc()) {
		echo "<tr id=\"".$row[id_login]."\"><td>".$row[name]."</td><td>".$row[location]."</td><td>".$row[phone]."</td><td><a class=\"waves-effect waves-light btn\" onclick=\"deleteAccount('".$row[id_login]."')\">Delete</a></td></tr>";
	}
	
	?>
    </table>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>User accounts</h3>
    <table>
      <tr>
        <td><b>Firtsname</b></td>
        <td><b>Lastname</b></td>
        <td><b>Ranges</b></td>
        <td><b>Delete</b></td>
      </tr>
      <?php 
	
	$sql="SELECT * FROM user";
	$result = mysqli_query($conn, $sql);
	while($row = $result->fetch_assoc()) {
		echo "<tr id=\"".$row[id_login]."\"><td>".$row[firstname]."</td><td>".$row[lastname]."</td><td>T:".$row[rangeTime]." D:".$row[rangeDistance]."</td><td><a class=\"waves-effect waves-light btn\" onclick=\"deleteAccount('".$row[id_login]."')\">Delete</a></td></tr>";
	}
	
	?>
    </table>
  </div>
</div>
</main>
<script>
function deleteAccount(id) {
  var link = document.getElementById(id);
  link.style.display = 'none';
  link.style.visibility = 'hidden';
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "adminpages/operations.php", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send("operation=deleteAccount&id="+id+"");
}
</script> 
