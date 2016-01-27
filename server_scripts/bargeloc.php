<?php

include 'db.php';

//$name=$_REQUEST['name'];
//echo $name."<br/>";
$query="select * from bargetest";
$result = mysql_query($query);
while($row=mysql_fetch_assoc($result)){
	$output[]=$row;
}
print(json_encode($output));
mysql_close();

?>
