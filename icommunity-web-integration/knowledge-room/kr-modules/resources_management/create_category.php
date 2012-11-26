<?php
include_once $dirname . $modules_directory . '/' . $moduleNames -> get_resources_management() . '/' . 'KrCategoriesManagement.class.php';
include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';

$configuration = new KrDbConfiguration();

//create a new category management object
$db = new KrCategoriesManagement($configuration);

//Connect to the DB
$db -> getConnection();

if (isset($_POST['send'])) {

	$title = $_POST["category_title"];
	$description = $_POST["category_description"];
	$img = $_POST['element_3'];
	copy($img, "./tmp/");
	$img_name = $_FILES["element_3"]["name"];
	$img_type = $_FILES["element_3"]["type"];
	$img_size = $_FILES["element_3"]["size"];
	$img_temp = $_FILES["element_3"]["tmp"];
	$img_error = $_FILES["element_3"]["error"];
	
	//$image_dir=$dirname . $modules_directtory . 'resources_management';
	
        /* removing this since it never worked */
        /* START image copy */
        
        /*
	$image_dir = $dirname . $modules_directory . '/' . $moduleNames -> get_resources_management() . '/' . 'thumbnails';
	$link=str_replace("/home/pardalis","",$image_dir);
	if($img_error > 0)
		die ("Error uploading the thumbnail image $img_error.");
	else {
		move_uploaded_file($img_temp,$link. '/' .$img_name);
	}         
         */
        
        /* END image copy */
        
	$message = $db -> createUserCategory($title, $description, $img_name, $user_id);
        $db -> close();
        
        /* TODO remove this since it never worked */
	/*
	//$thumb = "";
	//$element_3
	//from here onwards, we are copying the file to the directory you made earlier, so it can then be moved
	//into the database. The image is named after the persons IP address until it gets moved into the database

	//get users IP
	$ip = $REMOTE_ADDR;
	//TODO -- Code to retrieve thumbnail image and upload it to the database*/
	//$img = $_POST['element_3'];
	//	if (!empty($img)){

	//copy the image to directory

	//copy($img, "./tmp/" . $ip . "");
/*
	//open the copied image, ready to encode into text to go into the database
	$filename1 = "./tmp/" . $REMOTE_ADDR;
	$fp1 = fopen($filename1, "r");

	//record the image contents into a variable
	$contents1 = fread($fp1, filesize($filename1));

	//close the file
	fclose($fp1);

	//encode the image into text
	$encoded = chunk_split(base64_encode($contents1));

	//insert information into the database
	$message = $db -> createUserCategory($title, $description, $encoded, $user_id);
	//delete the temporary file we made
	unlink($filename1);
	//}

	/*$thumb=$_FILES["category_thumb"]["name"];
	 mkdir("./thumbs", 0777);
	 if(is_uploaded_file($_FILES["category_thumb"]["tmp_name"])==true){
	 move_uploaded_file($_FILES["category_thumb"]["tmp_name"],"thumbs/".$_FILES["category_thumb"]["name"]);
	 }
	 else{
	 die ("problem in transfert");
	 }

	//$message = $db->createUserCategory($title,$description,$thumb, $user_id);*/
        
	
}
?>
<div class="kr-section-name">
	Categories and Resources Management Space
</div>
<div class ="kr-section-action">
	New Category creation
</div>

<p>
	<?php //var_dump($img_temp);?></br>
	<?php 
        echo $message;
//        echo '</br></br>'.$link; 
        ?>
</p>
