<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET NAMES `UTF8`");
	mysqli_query($con,"SET CHARACTER_SET_CLIENT=utf8");
	mysqli_query($con,"SET CHARACTER_SET_RESULTS=utf8");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	

	$type = $_POST["type"];
	$fromid = $_POST["fromid"];
	$toid = $_POST["toid"];
	$fromcount = $_POST["fromcount"];
	$tocount = $_POST["tocount"];
	$fromgcount = $_POST["fromgcount"];
	$togcount = $_POST["togcount"];
	$time = $_POST["time"];
	
	$insertquery = mysqli_prepare($con, 'INSERT INTO `counts` (Type , Fromid , Toid , Fromcount , Tocount ,Fromgcount , Togcount ,  Time ) VALUES (?,?,?,?,?,?,?,?) ');
    mysqli_stmt_bind_param($insertquery , "ssssssss" , $type , $fromid , $toid , $fromcount , $tocount, $fromgcount , $togcount , $time);
	mysqli_stmt_execute($insertquery);
	
	mysqli_stmt_close($insertquery);
	
	mysqli_close($con);	
	
?>