<?php
//create connection
//address, user, passwd, database, port
$con = mysqli_connect("localhost","air411w","411air!","wherearfthou");
//check connection
if(mysqli_connect_errno())
{
  echo "Failed Connection: " . mysqli_connect_error();
}
?>
