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
    <h3>API key</h3>
    <textarea style="font-size:18px; font-family:'Courier New', Courier, monospace;<?php if(!$_SESSION["authorized"]) echo "user-select: none;";?>" readonly="readonly"><?php echo $_SESSION["api_key"]; ?></textarea>
  </div>
</div>
<div class="mdl-grid demo-content">
  <div class="demo-card-wide mdl-card mdl-shadow--2dp">
    <h3>Permission status</h3>
    <?php if($_SESSION["authorized"]) echo "<h4 style=\"color:green;\">Authorized</h4>"; else echo "<h4 style=\"color:red;\">Not authorized</h4>"; ?>
    <p>
      <?php if($_SESSION["authorized"]) echo "You have been authorized to use the requested API key. Find all instructions in the \"Guide\" section."; else echo "You aren't yet authorized to use the requested API key. Wait until you get the permission."; ?>
    </p>
  </div>
</div>
</main>
