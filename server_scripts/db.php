
<?php 
	
	$user="goyalraj_barge";
	$pass="programmer";
	$db="goyalraj_bargedb";
	$server="localhost";

	if(mysql_connect($server, $user, $pass)){
		//echo "connection to server opened<br/>";
		//echo "Connected!<br/>";	
	}
	else {
		echo "connection problem!";	
	}
	
	if($db_found = mysql_select_db($db)){
		//echo "db found<br/>";	
	
	}
	else {
			echo "db not found<br/>";			
			
	}


?>


