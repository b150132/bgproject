<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET NAMES `UTF8`");
	mysqli_query($con,"SET CHARACTER_SET_CLIENT=utf8");
	mysqli_query($con,"SET CHARACTER_SET_RESULTS=utf8");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	

	$sendby = $_POST["sendby"];
	$area = $_POST["area"];
	$text = $_POST["text"];
	$time = $_POST["time"];
	
	
	$insertquery = mysqli_prepare($con, 'INSERT INTO `marquee` (Sendby , Area , Text , Time ) VALUES (?,?,?,?) ');
    mysqli_stmt_bind_param($insertquery , "ssss" , $sendby , $area , $text , $time);
	mysqli_stmt_execute($insertquery);
	
	mysqli_stmt_close($insertquery);
	
	mysqli_close($con);	
	
?>