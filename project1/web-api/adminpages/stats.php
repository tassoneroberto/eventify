<?php
$num_login = mysqli_query($conn, "SELECT * FROM login")->num_rows;
$num_users = mysqli_query($conn, "SELECT * FROM user")->num_rows;
$num_organizers = mysqli_query($conn, "SELECT * FROM organizer")->num_rows;
$num_apikey = mysqli_query($conn, "SELECT * FROM api_login")->num_rows-1;
$queryUsed = mysqli_query($conn, "SELECT * FROM api_login WHERE type!='RESERVED'");
$total_num_query=0;
$result_total_num_query=mysqli_query($conn, "SELECT * FROM api_login");
while($row =$result_total_num_query->fetch_assoc()) {$total_num_query+=$row["query"];}
$num_events = mysqli_query($conn, "SELECT * FROM event")->num_rows;
$today = date('Y-m-d H:i:s');
$num_currentevents = mysqli_query($conn, 'SELECT * FROM event WHERE opening<=\'' . $today . '\' AND ending>=\'' . $today . '\'')->num_rows;
$num_incomingevents = mysqli_query($conn, 'SELECT * FROM event WHERE opening>=\'' . $today . '\'')->num_rows;
$num_expiredevents = mysqli_query($conn, 'SELECT * FROM event WHERE ending<=\'' . $today . '\'')->num_rows;

$num_cinema = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Cinema\'')->num_rows;
$num_theatre = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Theatre\'')->num_rows;
$num_outdoor = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Outdoor\'')->num_rows;
$num_foodanddrink = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Food & Drink\'')->num_rows;
$num_livemusic = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Live Music\'')->num_rows;
$num_festival = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Festival\'')->num_rows;
$num_disco = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Disco\'')->num_rows;
$num_museum = mysqli_query($conn, 'SELECT * FROM event WHERE category=\'Museum\'')->num_rows;


$num_eventscalendar = mysqli_query($conn, "SELECT * FROM calendar")->num_rows;

?>
<script type="text/javascript">
window.onload = function () {
	
	var chartQuery = new CanvasJS.Chart("queryChart",
	{
		theme: "theme2",	
		data: [
		{       
			type: "pie",
			showInLegend: true,
			toolTipContent: "{y} - #percent %",
			yValueFormatString: "#,##0",
			legendText: "{indexLabel}",
			dataPoints: [
			
			<?php 
			while($row =$queryUsed->fetch_assoc()) {
				echo '{y:'.$row["query"].', indexLabel:"'.$row["email"].'"},';
				}
			?>
			]
		}
		]
	});
	
	var chartAccounts = new CanvasJS.Chart("accountsChart",
	{
		theme: "theme2",	
		data: [
		{       
			type: "pie",
			showInLegend: true,
			toolTipContent: "{y} - #percent %",
			yValueFormatString: "#,##0",
			legendText: "{indexLabel}",
			dataPoints: [
				{  y: <?php echo $num_users ?>, indexLabel: "Users" },
				{  y: <?php echo $num_organizers ?>, indexLabel: "Organizers" },
			]
		}
		]
	});
	
	var chartEventsStatus = new CanvasJS.Chart("eventStatusChart",
	{
		theme: "theme2",	
		data: [
		{       
			type: "pie",
			showInLegend: true,
			toolTipContent: "{y} - #percent %",
			yValueFormatString: "#,##0",
			legendText: "{indexLabel}",
			dataPoints: [
				{  y: <?php echo $num_currentevents ?>, indexLabel: "Current Events" },
				{  y: <?php echo $num_incomingevents ?>, indexLabel: "Incoming Events" },
				{  y: <?php echo $num_expiredevents ?>, indexLabel: "Expired Events" },
			]
		}
		]
	});
	
	var chartEventsCategory = new CanvasJS.Chart("eventCategoryChart",
	{
		theme: "theme2",	
		data: [
		{       
			type: "pie",
			showInLegend: true,
			toolTipContent: "{y} - #percent %",
			yValueFormatString: "#,##0",
			legendText: "{indexLabel}",
			dataPoints: [
				{  y: <?php echo $num_cinema ?>, indexLabel: "Cinema events" },
				{  y: <?php echo $num_theatre ?>, indexLabel: "Theatre events" },
				{  y: <?php echo $num_outdoor ?>, indexLabel: "Outdoor events" },
				{  y: <?php echo $num_foodanddrink ?>, indexLabel: "Food & Drink events" },
				{  y: <?php echo $num_livemusic ?>, indexLabel: "Live Music events" },
				{  y: <?php echo $num_festival ?>, indexLabel: "Festival events" },
				{  y: <?php echo $num_disco ?>, indexLabel: "Disco events" },
				{  y: <?php echo $num_museum ?>, indexLabel: "Museum events" },
			]
		}
		]
	});
	
	chartQuery.render();
	chartAccounts.render();
	chartEventsStatus.render();
	chartEventsCategory.render();
}
</script>
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
    <h3>API keys and queries</h3>
    <div id="queryChart" style="height: 300px; width: 100%;"></div>
    <table>
      <tr>
        <td>Total released API keys:</td>
        <td><?php echo $num_apikey; ?></td>
      </tr>
      <tr>
        <td>Total executed queries:</td>
        <td><?php echo $total_num_query; ?></td>
      </tr>
    </table>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Accounts</h3>
    <div id="accountsChart" style="height: 300px; width: 100%;"></div>
    <table>
      <tr>
        <td>Total accounts:</td>
        <td><?php echo $num_login; ?></td>
      </tr>
      <tr>
        <td>Total users:</td>
        <td><?php echo $num_users; ?></td>
      </tr>
      <tr>
        <td>Total organizers:</td>
        <td><?php echo $num_organizers; ?></td>
      </tr>
    </table>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Events Status</h3>
    <div id="eventStatusChart" style="height: 300px; width: 100%;"></div>
    <table>
      <tr>
        <td>Total events:</td>
        <td><?php echo $num_events; ?></td>
      </tr>
      <tr>
        <td>Total current events:</td>
        <td><?php echo $num_currentevents; ?></td>
      </tr>
      <tr>
        <td>Total incoming events:</td>
        <td><?php echo $num_incomingevents; ?></td>
      </tr>
      <tr>
        <td>Total expired events:</td>
        <td><?php echo $num_expiredevents; ?></td>
      </tr>
    </table>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Events Category</h3>
    <div id="eventCategoryChart" style="height: 300px; width: 100%;"></div>
    <table>
      <tr>
        <td>Cinema events:</td>
        <td><?php echo $num_cinema; ?></td>
      </tr>
      <tr>
        <td>Theatre events:</td>
        <td><?php echo $num_theatre; ?></td>
      </tr>
      <tr>
        <td>Outdoor events:</td>
        <td><?php echo $num_outdoor; ?></td>
      </tr>
      <tr>
        <td>Food &amp; Drink events:</td>
        <td><?php echo $num_foodanddrink; ?></td>
      </tr>
      <tr>
        <td>Live Music events:</td>
        <td><?php echo $num_livemusic; ?></td>
      </tr>
      <tr>
        <td>Festival events:</td>
        <td><?php echo $num_festival; ?></td>
      </tr>
      <tr>
        <td>Disco events:</td>
        <td><?php echo $num_disco; ?></td>
      </tr>
      <tr>
        <td>Museum events:</td>
        <td><?php echo $num_museum; ?></td>
      </tr>
    </table>
  </div>
</div>
</main>
<script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>