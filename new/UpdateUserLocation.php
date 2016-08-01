<?php
  $con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
  if ($con->connect_error) {die("Connection failed: " . $con->connect_error);}
  mysqli_query($con,"SET CHARACTER SET UTF8");

  
  // array for JSON response
  $response = array();

if( isset($_POST['id'] ) && isset($_POST['location1'])&& isset($_POST['location2']) ) {
    $id=$_POST['id'];
    $location1=$_POST['location1'];
	 $location2=$_POST['location2'];

	$sql = "UPDATE `contacts` SET Locationone='$location1',Locationtwo='$location2' WHERE id='$id'";

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