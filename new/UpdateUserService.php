<?php
 $con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
  if ($con->connect_error) {die("Connection failed: " . $con->connect_error);}
  mysqli_query($con,"SET CHARACTER SET UTF8");

  
  // array for JSON response
  $response = array();

if( isset($_POST['id'] ) && isset($_POST['service']) ) {
    $id=$_POST['id'];
    $service=$_POST['service'];
	
	$sql = "UPDATE `contacts` SET Service='$service' WHERE id='$id'";

    if ($con->query($sql) === TRUE) {
		echo "Record updated successfully";
	} 
	else {
		echo "Error updating record: " . $con->error;
	} 
  // echoing JSON response
	echo json_encode($response);
}
	mysqli_close($con);	
?>