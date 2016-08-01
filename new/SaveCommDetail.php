<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET NAMES `UTF8`");
	mysqli_query($con,"SET CHARACTER_SET_CLIENT=utf8");
	mysqli_query($con,"SET CHARACTER_SET_RESULTS=utf8");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	

	$name = $_POST["name"];
	$num = $_POST["num"];
	$shop = $_POST["shop"];
	$price = $_POST["price"];
	$goodcount = $_POST["goodcount"];
	$photoname = $_POST["photoname"];
	
	
	$insertquery = mysqli_prepare($con, 'INSERT INTO `commodity` (Name , Num , Shop , Price ,Count, Photoname ) VALUES (?,?,?,?,?,?) ');
    mysqli_stmt_bind_param($insertquery , "ssssss" , $name , $num , $shop , $price , $goodcount , $photoname);
	mysqli_stmt_execute($insertquery);
	
	mysqli_stmt_close($insertquery);
	
	mysqli_close($con);	
	
?>