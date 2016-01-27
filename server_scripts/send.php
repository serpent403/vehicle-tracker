<?php

require_once('c2dm.class.php');

$google_id = 'rtc2dmtest@gmail.com';//sender ID
$google_pwd = 'c2dmservice';//sender pwd


if(isset($_GET['registrationid'])){
	$registration_id = $_GET['registrationid'];//also called the device token and keeps changing
	//$device_id = $_GET['deviceid'];//ANDROID_ID is fixed for a particular device[NOT USED IN THIS CODE]
	
}else{
	echo "Crap! Could not obtain registration id from the GET url<br/>";
	
}


/*
$registration_id = "APA91bEWIEYgxawNHTsy9f3zzdMBWnZWDfCdxjhu5b4QB7N0dD6MAT4tHKEeG0572WElegCyYs-CduIoCEnFJnYfVo4O4466XYyVqqA4DDPeOkn1CQLrx8tVilQRCuJERhLOnRnPb0s_7pCRYUkRPL7Ov7AqqUQFhw";
*/

$data = array(
    'payload' => "This is a test message!"
);

$c2dm = new c2dm($google_id, $google_pwd);

echo $c2dm->send($registration_id, $data);


?>
