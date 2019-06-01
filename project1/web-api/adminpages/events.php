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
    <h3>Events</h3>
    <table>
      <tr>
        <td><b>Title</b></td>
        <td><b>Location</b></td>
        <td><b>Opening</b></td>
        <td><b>Ending</b></td>
        <td><b>Delete</b></td>
      </tr>
      <?php 
	
	$sql="SELECT * FROM event";
	$result = mysqli_query($conn, $sql);
	while($row = $result->fetch_assoc()) {
		echo "<tr id=\"".$row[id]."\"><td>".$row[title]."</td><td>".$row[location]."</td><td>".$row[opening]."</td><td>".$row[ending]."</td><td><a class=\"waves-effect waves-light btn\" onclick=\"deleteEvent('".$row[id]."')\">Delete</a></td></tr>";
	}
	
	?>
    </table>
  </div>
</div>
</main>
<script>
function deleteEvent(id) {
  var link = document.getElementById(id);
  link.style.display = 'none';
  link.style.visibility = 'hidden';
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "adminpages/operations.php", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send("operation=removeEvent&id="+id+"");
}
</script> 
