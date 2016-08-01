<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET NAMES `UTF8`");
	mysqli_query($con,"SET CHARACTER_SET_CLIENT=utf8");
	mysqli_query($con,"SET CHARACTER_SET_RESULTS=utf8");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	
	
	$name = $_POST["name"];
	$email = $_POST["email"];
	$username = $_POST["username"];
	$password = $_POST["password"];
	$phone = $_POST["phone"];
	
	$address = $_POST["address"];
	$assoiation = $_POST["assoiation"];
	$job = $_POST["job"];
	$introperson = $_POST["introperson"];
	$lineid = $_POST["lineid"];
	$level = $_POST["level"];
	$giftcount = $_POST["giftcount"];
	
	$insertquery = mysqli_prepare($con, 'INSERT INTO `contacts` (Name , Email , Username , Password , Phone ,  Address ,Assoiation ,Job , Introperson , Lineid,Level,Giftcount) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ');
    mysqli_stmt_bind_param($insertquery , "ssssssssssss" , $name , $email , $username , $password , $phone , $address , $assoiation,$job , $introperson , $lineid,$level,$giftcount);
	mysqli_stmt_execute($insertquery);
	
	mysqli_stmt_close($insertquery);
	
	mysqli_close($con);	
	
?>