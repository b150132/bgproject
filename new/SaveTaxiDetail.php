<?php
	$con = mysqli_connect("mysql9.000webhost.com","a6748659_ta","b11818","a6748659_ta");
	mysqli_query($con,"SET NAMES `UTF8`");
	mysqli_query($con,"SET CHARACTER_SET_CLIENT=utf8");
	mysqli_query($con,"SET CHARACTER_SET_RESULTS=utf8");
	mysqli_query($con,"SET CHARACTER SET UTF8");
	

	$time = $_POST["time"];
	$locatone = $_POST["locatone"];
	$locattwo = $_POST["locattwo"];
	$carid = $_POST["carid"];
	$guestid = $_POST["guestid"];
	
	$ccount = $_POST["ccount"];
	$gcount = $_POST["gcount"];
	$valuation = $_POST["valuation"];
	$cevalu = $_POST["cevalu"];
	$gevalu = $_POST["gevalu"];	
	
	$insertquery = mysqli_prepare($con, 'INSERT INTO `texi` (Time , Locatone , Locattwo , Carid , Guestid , Ccount , Gcount , Valuation , Cevalu , Gevalu ) VALUES (?,?,?,?,?,?,?,?,?,?) ');
    mysqli_stmt_bind_param($insertquery , "ssssssssss" , $time , $locatone , $locattwo , $carid , $guestid , ccount , gcount , valuation , cevalu , gevalu);
	mysqli_stmt_execute($insertquery);
	
	mysqli_stmt_close($insertquery);
	
	mysqli_close($con);	
	
?>