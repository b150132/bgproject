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
	$keycolumn=$_POST['keycolumn']; //column
	$keyvalue=$_POST['keyvalue']; //value
		
	//Query ID to record evaluation
	$selectquery = mysqli_prepare($con , 'SELECT * FROM `$page` WHERE `$keycolumn` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($selectquery , "s" , $keyvalue);
	  // Execute query
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	  // Bind result variables
	mysqli_stmt_bind_result($selectquery ,$ID, $Shopid, $Shopname, $Locationone, $Locationtwo, $Area, $Type , $Coverrate, $Memberid, $d);
		
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
			$user[Coverrate] =$Coverrate;
	}
	
	$resultsale = $user[Coverrate]+$value;
 
	//Update sale info
	$sql = "UPDATE `$page` SET `$column`='$resultsale' WHERE `$keycolumn`='$keyvalue'";

    if ($con->query($sql) === TRUE) {
		echo "colunm value updated successfully";
	} 
	else {
		echo "Error updating cover number: " . $con->error;
	} 

	echo json_encode($output);
	
	mysqli_close($con);	
?>