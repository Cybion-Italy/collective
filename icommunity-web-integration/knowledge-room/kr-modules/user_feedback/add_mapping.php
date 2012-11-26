<?php
        include_once $dirname . $modules_directory . '/' . $moduleNames->get_resources_management() .'/'. 'KrCategoriesManagement.class.php';
        include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';
		
                $configuration = new KrDbConfiguration();        
                //create a new category management object
                $db = new KrCategoriesManagement($configuration);        
                $db->getConnection();
                
		$resource_id=$_POST['resource_id'];	
		$resource_title=$_POST['resource_title'];	
		$resource_url=$_POST['resource_url'];		
		$resource_description=$_POST['resource_description'];
		$category_id = $_POST['category_id'];
		
	if(isset($_POST['save'])){	
		$res_exist = $db->isResourceRedundant($resource_id);
		if($res_exist==False){
			$db->addResource($resource_id,$resource_title,$resource_url,$resource_description);
		}
		$db->purgeMappings($resource_id);
		foreach($category_id as $cat){
			$map_exist = $db->isMappingRedundant($cat,$resource_id);
			if($map_exist==False){
				$db->addMapping($cat,$resource_id);
				$message = "<i>Mapping added successfuly.</i></br>";
			}
			else{
				$message = "<i>Mapping exist !</i></br>";
			}
		}
		//echo $message;
	}
	if(isset($_POST['cancel'])){
	}
	$db->close(); 
?>

<div class="kr-section-name">
    User Feedback
</div>

<div class ="kr-section-description">
    Recommendation details
</div>

<p><?php echo $message; ?></p>
<!--<meta http-equiv="refresh" content="1; URL=<?php echo $moduleNames->get_user_feedback(); ?>">-->