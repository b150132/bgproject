<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	
	// Check connection
	if ($con->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 
	
	mysqli_query($con,"SET CHARACTER SET UTF8");
	
    // array for JSON response
    $response = array();

	$page=$_POST['page']; //page
	$column=$_POST['column']; //column
	$value=$_POST['value']; //value
 
	$sql="SELECT * FROM `$page` WHERE `$column` < '$value'";
	//$sql="SELECT * FROM `commodity` WHERE `Shop` ='$shop'";
	$result=mysqli_query($con,$sql);

	while($row=mysqli_fetch_assoc($result))
	{
		$output[] = $row;
	}
	echo json_encode($output);
	
	mysqli_close($con);	
?>