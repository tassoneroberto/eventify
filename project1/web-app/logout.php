<?php
    session_start();
    $_SESSION = array();
    session_destroy();
    return true;
?>