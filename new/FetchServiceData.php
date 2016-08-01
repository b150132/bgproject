<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	
	// Check connection
	if ($con->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 
	
	mysqli_query($con,"SET CHARACTER SET UTF8");
	  // array for JSON response
	$response = array();

	$service = $_POST["service"];
		
//	$selectquery = mysqli_prepare($con , 'SELECT * FROM `contacts` WHERE `Service` = ?');
	  // Bind parameters
//	mysqli_stmt_bind_param($selectquery , "s" , $service);
	  // Execute query
//	mysqli_stmt_execute($selectquery);
	
//	mysqli_stmt_store_result($selectquery);
	  // Bind result variables
//	mysqli_stmt_bind_result($selectquery , $ID , $name , $email , $username , $password , $phone , $address , $job , $introperson , $lineid , $locantionone,$locantiontwo,$service);
		
	
	$sql="SELECT * FROM `contacts` WHERE `Service` = '$service'";
	$result=mysqli_query($con,$sql);
	
	while($row=mysqli_fetch_assoc($result))
	{
		$output[] = $row;
	}
	echo json_encode($output);
	
	//mysqli_stmt_close($selectquery);
	
	mysqli_close($con);	
?>