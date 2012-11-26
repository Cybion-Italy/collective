<?php
	include_once 'KrFormHelper.class.php';
        include_once 'KrRecommendation.class.php';
        include_once $dirname . $modules_directory . '/' . $moduleNames->get_resources_management() .'/'. 'KrCategoriesManagement.class.php';	
        include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';
        
	$category_id;
                	
	if(isset($_POST['category_id_all'])){
		$category_id=$_POST['category_id_all'];
	}
	        			
        $configuration = new KrDbConfiguration();
        //create a new category management object
        $db = new KrCategoriesManagement($configuration);
        	
	//Connect to the DB
	$db->getConnection();
	$obj=$db->getCategory($category_id);
	$arr = array();
	
	$line = $db->fetchNextObject($obj);
	
	$fake_recs = array();
?>
</br>
<div class="kr-section-name">
    Categories and Resources Management Space
</div>
<div class ="kr-section-description">
    See All Resources
	<br/><hr noshade size=1 width=300>
</div>

<form id="see_all" method=POST action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    <input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
    <input type="hidden" name="action" value="see_all"/>
	<div class="kr-category-title"><?php echo $line->CategoryTitle;?></div>
	<table>
		<?php 
		$arr = $db->getArrayResourceIdSortedByCategory($category_id);
		foreach($arr as $resId){
			$obj1=$db->getResourceById($resId);
			while($line1 = $db->fetchNextObject($obj1)){	
			//unset($fake_recs);
			$res_id=$line1->ResourceId; 
                        
			$fake_topics= "http://faketopic.com/";
                        $fake_rec['id'] = $line1->ResourceId;
                        $fake_rec['title'] = $line1->ResourceTitle;
                        $fake_rec['url'] = $line1->ResourceUrl;
                        $fake_rec['description'] = $line1->ResourceDescription;
                        $fake_rec['topics'] = $fake_topics;

                        $fake_recs[]= $fake_rec;
                        $recommendations_objects_list = array();
                        
                        foreach ($fake_recs as $rec) {
                            
                            $recommendation = new KrRecommendation(
                                $rec['id'], 
                                $rec['title'], 
                                $rec['url'], 
                                $rec['description'],
                                $rec['topics']);
                            $recommendations_objects_list[$recommendation->get_id()] = $recommendation;
			}
							
						$json_recommendation = htmlentities(json_encode($recommendation), ENT_QUOTES);
		?>
		<tr>
			<td>
				<a class="a_link" href="<?php echo $line1->ResourceUrl; ?>" target="_blank">
				
						<?php 
							$len = strlen($line1->ResourceTitle);
							if(($len - 50)>0){
												echo substr($line1->ResourceTitle,0,50)."...";
											}
							else{
									echo $line1->ResourceTitle;
								}
						?>
				
				</a>
			</td>
			<td>
				<form name="view_res_cat_link" method="POST" action="">
					<input type="hidden" name="module" value="<?php echo $moduleNames->get_user_feedback(); ?>"/>
					<input type="hidden" name="action" value="view"/>
					<input type="hidden" name="recommendation_object" value="<?php echo $json_recommendation; ?>"/>
					<input type="hidden" name="category_id" value="<?php echo $category_id; ?>"/>
					<input type="hidden" name="source" value="from_category" />
					<button class="button-view-concept" name="view" type="submit">Details</button>
					<!--<input type="submit" name="view" value="Details" />-->
				</form>
			</td>
			<td>
				<form name="delete_res_cat_link" action="" method="POST" id="delete_res_cat_link">
					<input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
					<input type="hidden" name="action" value="delete_res_cat"/> 
					<input type="hidden" name="category_id" value="<?php echo $category_id; ?>"/>
					<input type="hidden" name="resource_id" value="<?php echo $res_id; ?>"/>
					<button class="button-view-concept" name="delete" type="submit">Delete</button>
					<!--<input type="submit" value="Delete" />-->
				</form>
			</td>
		</tr>
		<?php 
			} }
		?>
	</table>
<form/>

</br>
