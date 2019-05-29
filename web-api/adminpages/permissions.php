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
    <h3>API key permissions</h3>
    <table>
      <tr>
        <td><b>Email</b></td>
        <td><b>App type</b></td>
        <td><b>Queries</b></td>
        <td><b>Allowed</b></td>
        <td><b>Delete</b></td>
      </tr>
      <?php 
	
	$sql="SELECT * FROM api_login WHERE type!='RESERVED'";
	$result = mysqli_query($conn, $sql);
	while($row = $result->fetch_assoc()) {
		$checked="";
		if($row[authorized]) $checked="checked=\"checked\"";
		echo "<tr id=\"".$row[id]."\"><td>".$row[email]."</td><td>".$row[type]."</td><td>".$row[query]."</td><td><div class=\"switch\"><label><input id=\"".$row[id]."\" onchange=\"changePermission(this)\" type=\"checkbox\" $checked><span class=\"lever\"></span></label></div></td><td><a class=\"waves-effect waves-light btn\" onclick=\"deleteApiKey('".$row[id]."')\">Delete</a></td></tr>";
	}
	
	?>
    </table>
  </div>
</div>
</main>
<script>
function changePermission(e) {
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "adminpages/operations.php", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  if(e.checked)
	    xhttp.send("operation=changePermission&id="+e.id+"&authorized=1");
  else
  		xhttp.send("operation=changePermission&id="+e.id+"&authorized=0");
}
</script>
<script>
function deleteApiKey(id) {
  var link = document.getElementById(id);
  link.style.display = 'none';
  link.style.visibility = 'hidden';
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "adminpages/operations.php", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send("operation=deleteApiKey&id="+id+"");
}
</script> 
