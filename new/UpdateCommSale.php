<?php
  $con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
  if ($con->connect_error) {die("Connection failed: " . $con->connect_error);}
  mysqli_query($con,"SET CHARACTER SET UTF8");
  
  // array for JSON response
  $response = array();
 

if( isset($_POST['shop'] ) && isset($_POST['num'])&& isset($_POST['addnum']) ) {
  	$shop=$_POST['shop'];
	$num=$_POST['num'];
	$addnum=$_POST['addnum'];
	
	//Query ID to record evaluation
	$selectquery = mysqli_prepare($con , 'SELECT * FROM `commodity` WHERE `Shop` = ? AND `Num` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($selectquery , "ss" , $shop, $num);
	  // Execute query
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	  // Bind result variables
	mysqli_stmt_bind_result($selectquery ,$ID, $name, $number, $Shop, $Price, $Countnum, $Photoname , $sale, $Real, $Type);
		
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
			$user[sale] =$sale;
	}
	
	$resultsale = $user[sale]+$addnum;
		
	//Update sale info
	$sql = "UPDATE `commodity` SET Sale='$resultsale' WHERE Shop = '$shop' AND Num = '$num'";

    if ($con->query($sql) === TRUE) {
		echo "sale number updated successfully";
	} 
	else {
		echo "Error updating sale number: " . $con->error;
	} 
	
  // echoing JSON response
	echo json_encode($response);
}
	mysqli_close($con);	
?>