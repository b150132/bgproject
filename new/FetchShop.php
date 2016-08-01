<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	
	// Check connection
	if ($con->connect_error) {
		die("Connection failed: " . $con->connect_error);
	} 
	
	mysqli_query($con,"SET CHARACTER SET UTF8");
	  // array for JSON response
	$response = array();

	$area = $_POST["area"];	
		
//	
	$sql="SELECT * FROM `shoplist` WHERE `Area` = '$area'";
	$result=mysqli_query($con,$sql);
	
	while($row=mysqli_fetch_assoc($result))
	{
		$output[] = $row;
	}
	echo json_encode($output);
	
	mysqli_close($con);	
?>
