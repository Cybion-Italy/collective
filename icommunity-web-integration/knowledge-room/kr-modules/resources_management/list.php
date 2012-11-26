<style>
.button{
	font-family:Verdana, arial, sans-serif;
	color:#600;
	background-color: #ffffff;
	font-size:11px;
	width:120px;
}
title{font-family: verdana,arial,sans-serif;
	font-size:12px;
	color:#333333;
	border-width: 0px;
	border-color: #ffffff;
	border-collapse: collapse;
}
.table_line {
	border-bottom:1px solid #000;
	border-top:1px solid #000;
}
</style>
<br/>
<br/>
    <div>
        <a href="http://cibionte.cybion.eu/collective/backend/kr_chrome_plugin.crx">Click here to download the iCommunity Chrome Extension!</a>
<!--        <a href="http://gaia.cybion.eu/collective/backend/kr_chrome_plugin.crx">Click here to download the iCommunity Chrome Extension!</a>-->
    </div>
</br>
<div class="kr-section-name">
    Categories and Resources Management Space
</div>
<div class ="kr-section-description">
    Manage user defined categories 
	<br/>
</div>
<div id="resource-container">
    <form id="create_category" 
          method=POST 
          action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    <input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
    <input type="hidden" name="action" value="create_category_form"/>
	</br></br>
    <button type="submit" value="Create new Category" class="button-view-concept">
      <span>Create new Category</span>
    </button>
    </form>
    <br/>    
</div>

	<?php        
        
//        ini_set('display_errors', 1);        
                
        $kr_categories_management_file = 'KrCategoriesManagement.class.php';
        include_once $kr_categories_management_file;
        
        $kr_db_configuration_file = $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';        
        include_once $kr_db_configuration_file;
        
        $configuration = new KrDbConfiguration();
        //create a new category management object
        $db = new KrCategoriesManagement($configuration);
        	
	$db->getConnection();
        
        $obj = $db->getAllUserCategories($user_id);
        //TODO remove if it's not working
	$image_dir = $dirname . $modules_directtory . 'resources_management';
	$link = str_replace("/home/pardalis","",$image_dir);
        
        ?>
	<table align="center" class="title" cellpadding="6px">
			
	<?php while ($line = $db->fetchNextObject($obj)) {
    ?>

    <div id="category-container">
        <div id="category-row">
            <div id="resource-value">
				<tr class="table_line">
					<!--<td class="table_line">
						<img src="<?php //echo $link.'/cat.png'?>" />
						</br></br>
						<form id="see_all_category_<?php //echo $line->CategoryId;?>" method=POST 
							action="<?php //echo $moduleNames->get_knowledge_room_name(); ?>">
							<input type="hidden" name="module" value="<?php //echo $moduleNames->get_resources_management(); ?>"/>
							<input type="hidden" name="action" value="see_all"/>
							<input type="hidden" name="category_id_all" 
							value="<?php //echo $line->CategoryId; ?>"/>
							<!--<input type="submit" value="See All" />
							<button type="submit" class="button-see-all"><span>See All</span></button>
						</form>
					</td>-->
					<td class="table_line">
						<!--<div style="height: 70px;width: 100px; overflow: "auto">-->
                                                <div class="kr-category-title"><?php echo $line->CategoryTitle; ?></div></br>
                                                <div class="kr-category-description"><?php echo $line->CategoryDescription; ?></div>
        					</br>
                                                </br>
							<ul class="title">
								<?php //$obj1 = $db->getResourcesByCategory($line->CategoryId);
									$arr = $db->getArrayResourceIdSortedByCategory($line->CategoryId);
									$i = 1;
									foreach($arr as $resId){
										$obj1 = $db->getResourceById($resId);
										//$i=1;
										while (($line1 = $db->fetchNextObject($obj1)) and $i<=3) {?>
								<li>
									<a href="<?php echo $line1->ResourceUrl; ?>"  target="_blank">                                                                             
										<?php 
											$len = strlen($line1->ResourceTitle);
											if(($len - 80) > 0){
												echo substr($line1->ResourceTitle,0,80)."...";
											}
											else{
												echo $line1->ResourceTitle;
											}
											$i++;
										?>
									</a>
								</li>
								</br>
								<?php } }?>
							</ul>
                                                        </br></br>
						<form id="see_all_category_<?php echo $line->CategoryId;?>" method=POST
							action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
							<input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
							<input type="hidden" name="action" value="see_all"/>
							<input type="hidden" name="category_id_all" value="<?php echo $line->CategoryId; ?>"/>
							<!--<input type="submit" value="See All" />-->
							<button type="submit" class="button-see-all"><span>See All</span></button>
						</form>
						<!--</div>-->
					</td>
					
					<td class="table_line">
						<form id="view_category_<?php echo $line->CategoryId;?>" method=POST 
							action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
							<input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
							<input type="hidden" name="action" value="view"/>
							<input type="hidden" name="category_id" 
							value="<?php echo $line->CategoryId; ?>"/>
							<!--<input type="submit" value="edit" />-->
							<button type="submit" class="button-view-concept"><span>Edit</span></button>
						</form>	
						
					</td>
				</tr>
			</div>
		</div>
    </div> <!-- resource-row -->

    <!-- end content of resource -->    
    <?php
		}
		$db->close();
	?> 
	</table>

