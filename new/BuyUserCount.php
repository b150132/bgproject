<?php
  $con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
  if ($con->connect_error) {die("Connection failed: " . $con->connect_error);}
  mysqli_query($con,"SET CHARACTER SET UTF8");

  
  // array for JSON response
  $response = array();
  
 

if( isset($_POST['id'] ) && isset($_POST['count'])&& isset($_POST['giftcount']) ) {
    $id=$_POST['id'];
    $count=$_POST['count'];
	$giftcount=$_POST['giftcount'];
	
	//Query ID to record evaluation
	$selectquery = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($selectquery , "s" , $id);
	  // Execute query
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	  // Bind result variables
	mysqli_stmt_bind_result($selectquery , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $introperson1 , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$shopcount,$evaluation,$performance,$level);
		
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
			$user[count] =$countnum;
			$user[evaluation] = $evaluation;
	}
	
	$user[evaluation]=$user[evaluation]+$count-$user[count];
	
	//Update count info
	$sql = "UPDATE `contacts` SET Count='$count',Giftcount='$giftcount',Evaluation='$user[evaluation]' WHERE id='$id'";

    if ($con->query($sql) === TRUE) {
		echo "count updated successfully";
	} 
	else {
		echo "Error updating count: " . $con->error;
	} 
	
	//Query Introperson performance
	$introquery1 = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($introquery1 , "s" , $introperson1);
	  // Execute query
	mysqli_stmt_execute($introquery1);
	
	mysqli_stmt_store_result($introquery1);
	  // Bind result variables
	mysqli_stmt_bind_result($introquery1 , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $introperson2 , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$shopcount,$evaluation,$performance1,$level);
	
	while(mysqli_stmt_fetch($introquery1))
	{
			$user[performance1]=$performance1;
			$user[introperson2]=$introperson2;
	}
	
	//calculate
	$introperformance1 = $user[performance1] + ($count-$user[count])/10*2; //10=2+2+2+2+2
	//updated
	$Commissionsql =  "UPDATE `contacts` SET Performance='$introperformance1' WHERE id='$introperson1'";
	
	if ($con->query($Commissionsql) === TRUE) {
		echo "performance updated successfully";
	} 
	else {
		echo "Error updating performance: " . $con->error;
	} 
	
	
	/*
	//Query feedbackmoney performance
	$all = 2;
	$introquerybs = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($introquerybs , "s" , $all);
	  // Execute query
	mysqli_stmt_execute($introquerybs);
	
	mysqli_stmt_store_result($introquerybs);
	  // Bind result variables
	mysqli_stmt_bind_result($introquerybs , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $introperson4 , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$carcount,$evaluation,$performancebs,$level);
	while(mysqli_stmt_fetch($introquerybs))
	{
			$user[performancebs] =$performancebs;
	}
	//calculate
	$introperformancebs = $user[performancebs] + ($count-$user[count])/10*2; //10=2+2+2+2+2
	//updated
	
	$Commissionsqbs =  "UPDATE `contacts` SET Performance='$introperformancebs' WHERE id='$all'";
	
	if ($con->query($Commissionsqbs) === TRUE) {
		echo "performance updated successfully";
	} 
	else {
		echo "Error updating performance: " . $con->error;
	}  
	*/
	//===================================================================================================================
	//Query Introperson2 performance
	$introquery2 = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($introquery2 , "s" , $user[introperson2]);
	  // Execute query
	mysqli_stmt_execute($introquery2);
	
	mysqli_stmt_store_result($introquery2);
	  // Bind result variables
	mysqli_stmt_bind_result($introquery2 , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $introperson3 , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$carcount,$evaluation,$performance2,$level);
	while(mysqli_stmt_fetch($introquery2))
	{
			$user[performance2] =$performance2;
			$user[introperson3]=$introperson3;
	}
	//calculate
	$introperformance2 = $user[performance2] + ($count-$user[count])/10*2; //10=2+2+2+2+2
	//updated
	$Commissionsq2 =  "UPDATE `contacts` SET Performance='$introperformance2' WHERE id='$introperson2'";
	
	if ($con->query($Commissionsq2) === TRUE) {
		echo "performance updated successfully";
	} 
	else {
		echo "Error updating performance: " . $con->error;
	}  
	
	//Query Introperson3 performance
	$introquery3 = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($introquery3 , "s" , $user[introperson3]);
	  // Execute query
	mysqli_stmt_execute($introquery3);
	
	mysqli_stmt_store_result($introquery3);
	  // Bind result variables
	mysqli_stmt_bind_result($introquery3 , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $introperson4 , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$carcount,$evaluation,$performance3,$level);
	while(mysqli_stmt_fetch($introquery3))
	{
			$user[performance3] =$performance3;
			$user[introperson4]=$introperson4;
	}
	//calculate
	$introperformance3 = $user[performance3] + ($count-$user[count])/10*2; //10=2+2+2+2+2
	//updated
	$Commissionsq3 =  "UPDATE `contacts` SET Performance='$introperformance3' WHERE id='$introperson3'";
	
	if ($con->query($Commissionsq3) === TRUE) {
		echo "performance updated successfully";
	} 
	else {
		echo "Error updating performance: " . $con->error;
	}  
	
	//Query Introperson4 performance
	$introquery4 = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($introquery4 , "s" , $user[introperson4]);
	  // Execute query
	mysqli_stmt_execute($introquery4);
	
	mysqli_stmt_store_result($introquery4);
	  // Bind result variables
	mysqli_stmt_bind_result($introquery4 , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $intropersonend , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$carcount,$evaluation,$performance4,$level);
	while(mysqli_stmt_fetch($introquery4))
	{
			$user[performance4] =$performance4;
			$user[intropersonend]=$intropersonend;
	}
	//calculate
	$introperformance4 = $user[performance4] + ($count-$user[count])/10*2; //10=2+2+2+2+2
	//updated
	$Commissionsq4 =  "UPDATE `contacts` SET Performance='$introperformance4' WHERE id='$introperson4'";
	
	if ($con->query($Commissionsq4) === TRUE) {
		echo "performance updated successfully";
	} 
	else {
		echo "Error updating performance: " . $con->error;
	}  
	
	//Query feedbackmoney performance
	$all = 2;
	$introquerybs = mysqli_prepare($con , 'SELECT *  FROM `contacts` WHERE `id` = ?');
	  // Bind parameters
	mysqli_stmt_bind_param($introquerybs , "s" , $all);
	  // Execute query
	mysqli_stmt_execute($introquerybs);
	
	mysqli_stmt_store_result($introquerybs);
	  // Bind result variables
	mysqli_stmt_bind_result($introquerybs , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $intropersonbs , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$carcount,$evaluation,$performancebs,$level);
	while(mysqli_stmt_fetch($introquerybs))
	{
			$user[performancebs] =$performancebs;
	}
	//calculate
	$introperformancebs = $user[performancebs] + ($count-$user[count])/10*2; //10=2+2+2+2+2
	//updated
	
	$Commissionsqbs =  "UPDATE `contacts` SET Performance='$introperformancebs' WHERE id='$all'";
	
	if ($con->query($Commissionsqbs) === TRUE) {
		echo "performance updated successfully";
	} 
	else {
		echo "Error updating performance: " . $con->error;
	}  
	
  // echoing JSON response
	echo json_encode($response);
}
	mysqli_close($con);	
?>