<?php

function OpenCon()
{
 
 $dbhost = "host";
 $dbuser = "user";
 $dbpass = "password";
 $db = "database";
 

 $conn = new mysqli($dbhost, $dbuser, $dbpass,$db) or die("Connect failed: %s\n". $conn -> error);

 
 return $conn;
 }
 
function CloseCon($conn)
 {
 $conn -> close();
 }
   
?>