<?php
		
	$uname = $_POST["uname"];
	$image = $_POST["image"];
	
	$decodeimage = base64_decode("$image");
	file_put_contents("pictures/" . $uname . ".JPG", $decodeimage)
		
?>