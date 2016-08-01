<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	

	$ID = $_POST["id"];

	
	$selectquery = mysqli_prepare($con , 'SELECT `Count`,`Giftcount`,`Shopcount` FROM `contacts` WHERE `ID` = ? ');
	mysqli_stmt_bind_param($selectquery , "s" , $ID );
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	mysqli_stmt_bind_result($selectquery ,$countnum,$giftcount,$shopcount);
	
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
		$user[countnum] = $countnum;	
		$user[giftcount] = $giftcount;		
		$user[carcount] = $shopcount;		
	}
	
	echo json_encode($user);
	
	mysqli_stmt_close($selectquery);
	
	mysqli_close($con);	
?>