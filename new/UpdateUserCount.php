<?php
  $con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
  if ($con->connect_error) {die("Connection failed: " . $con->connect_error);}
  mysqli_query($con,"SET CHARACTER SET UTF8");
  
  // array for JSON response
  $response = array();
 

if( isset($_POST['id'] ) && isset($_POST['countnum'])&& isset($_POST['giftcount'])&& isset($_POST['carcount']) ) {
    $id=$_POST['id'];
    $countnum=$_POST['countnum'];
	$giftcount=$_POST['giftcount'];
	$carcount=$_POST['carcount'];
		
	//Update count info
	$sql = "UPDATE `contacts` SET Count='$countnum',Giftcount='$giftcount',Shopcount='$carcount' WHERE id='$id'";

    if ($con->query($sql) === TRUE) {
		echo "count updated successfully";
	} 
	else {
		echo "Error updating count: " . $con->error;
	} 
	
  // echoing JSON response
	echo json_encode($response);
}
	mysqli_close($con);	
?>