<?php
        include_once $dirname . $modules_directory . '/' . $moduleNames->get_resources_management() .'/'. 'KrCategoriesManagement.class.php';
        include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';
			
        $configuration = new KrDbConfiguration();
	//create a new category management object
	$db = new KrCategoriesManagement($configuration);
                
	$category_id;
	$resource_id;
	
	if(isset($_POST['category_id'])){
		$category_id=$_POST['category_id'];
		$resource_id=$_POST['resource_id'];
	}
	
	//Connect to the DB
	$db->getConnection();
	$message = $db->deleteMapping($category_id,$resource_id);
?>
</br>
<div class="kr-section-name">
    Categories and Resources Management Space
</div>
<div class ="kr-section-description">
    Delete Mapping
	<br/><hr noshade size=1 width=300>
</div>
<?php echo '<div class="p_new">'.$message.'</div>'; ?>
</br>
						<form id="see_all_category_<?php echo $line->CategoryId;?>" method=POST 
							action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
							<input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
							<input type="hidden" name="action" value="see_all"/>
							<input type="hidden" name="category_id_all" 
							value="<?php echo $category_id; ?>"/>
							<!--<input type="submit" value="See All" />-->
							<button type="submit" class="button-see-all"><span>Back</span></button>
						</form>