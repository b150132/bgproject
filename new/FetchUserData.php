<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	

	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$selectquery = mysqli_prepare($con , 'SELECT * FROM `contacts` WHERE `Username` = ? AND `Password` = ? ');
	mysqli_stmt_bind_param($selectquery , "ss" , $username , $password);
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	mysqli_stmt_bind_result($selectquery , $ID , $name , $email , $username , $password , $phone , $address ,$assoiation, $job , $introperson , $lineid , $locantionone,$locantiontwo,$service,$countnum,$giftcount,$carcount,$evaluation,$performance,$level);
	
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
			$user[name] = $name;
			$user[email] = $email;
			$user[username] = $username;
			$user[password] = $password;
			$user[phone]= $phone;
			
			$user[address] = $address;
			$user[job] = $job;
			$user[introperson] = $introperson;
			$user[lineid] = $lineid;
			
			$user[id] = $ID;
			$user[countnum] = $countnum;
			$user[giftcount] = $giftcount;
			$user[carcount] = $carcount;
			$user[evaluation] = $evaluation;
			$user[performance] = $performance;
			$user[level] = $level;
	}
	
	echo json_encode($user);
	
	mysqli_stmt_close($selectquery);
	
	mysqli_close($con);	
?>