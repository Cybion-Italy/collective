<?php

include_once $dirname . $modules_directory . '/' . $moduleNames->get_resources_management() .'/'. 'KrCategoriesManagement.class.php';
include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';

include_once 'KrPermanentSearchWSClient.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';

//var_dump($dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php'.'<br/>');

$permanent_search_id;
if (isset($_POST['permanent_search_id'])) {
	$permanent_search_id = $_POST['permanent_search_id'];
}

$configuration = new KrEndpointConfiguration();
$permanentSearchRestClient = new KrPermanentSearchWSClient($configuration);

/* call web service to get details of permanent search metadata */
//var_dump($permanent_search_id.'<br/>');
$permanent_search = $permanentSearchRestClient
                ->get_permanent_search($permanent_search_id);

//print_r($permanent_search);

/* call web service to get 10 most recent results */
$limit = 10;
$resources = $permanentSearchRestClient
                ->get_permanent_search_results($permanent_search_id, $limit);
//var_dump($resources);

//Connections to cibionte machine
$configuration = new KrDbConfiguration();        
              //create a new category management object
                $db = new KrCategoriesManagement($configuration);        
                $db->getConnection();
?>

<div class="kr-section-name">
    Permanent Search
</div>

<div class ="kr-section-description">
    Permanent Search Details
</div>

<!-- <p>Viewing details of permanent search id: <?php //echo $permanent_search_id; ?></p> -->

 <div id="resource-container">
        <div id="resource-row">
        <div class="label_new">Search title</div>
            <div class="p_new">
                <?php echo $permanent_search['title']; ?>
            </div>
        </div> <!-- resource-row -->

        <div id="resource-row">    
            <div class="label_new">Common Concepts</div>   
                <div class="p_new">
                    <?php 
                        foreach ($permanent_search['commonUris'] as $key => $labelled_url) {    
                            echo $permanent_search['commonUris'][$key]['label'].'<br/>';    
                        }
                    ?>
                </div>
        </div>  <!-- resource-row -->
        
        <div id="resource-row">    
            <div class="label_new">Custom Concepts</div>   
                <div class="p_new">
                    <?php 
                    if (count($permanent_search['customUris']) == 0)  {
                        echo "no custom concepts";
                    } else {
                        foreach ($permanent_search['customUris'] as $key => $labelled_url) {    
                            echo $permanent_search['customUris'][$key]['label'].'<br/>';    
                        }
                    }
                    ?>
                </div>
        </div>  <!-- resource-row -->
        
        <div id="resource-row">    
            <div class="label_new">Search Results</div>   
                <div class="p_new">
                    <?php 
                        $length = count($resources);

                        if ($length == 0) {
                            echo 'No results yet. <br/>';
                        } 
                    ?>
                   
                   	
                    <ul id="kr-resources-list" class="item-list">
                        <?php
                        foreach ($resources as $key => $resource) {    
                            ?> 
                          
                            <li>
                            	
                                <span class="text">
                                    <p>&middot;
                                    <a class="a_link" href="<?php echo $resources[$key]['url']; ?>" 
                                       target="_blank">
                                        <?php echo $resources[$key]['title']; ?>
                                       </a>
                                      </p>
                                </span>                               
								<!-- Code of button that leads to mapping resource to category goes here -->								
									<form align="right" create_category" method=POST action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    									<input type="hidden" name="module" value="<?php echo $moduleNames->get_user_feedback(); ?>"/>
    									<input type="hidden" name="action" value="view"/>
    									<input type="hidden" name="resource_id" value="<?php $last_resource_id = $db->getLastResourceId(); echo $last_resource_id+1;?>"/>
    									<input type="hidden" name="resource_title" value="<?php echo $resources[$key]['title']; ?>"/>
    									<input type="hidden" name="resource_url" value="<?php echo $resources[$key]['url']; ?>"/>
    									<input type="hidden" name="permanent_search_id" value="<?php echo $permanent_search_id; ?>"/>
    									<input type="hidden" name="source" value="from_permanent_search" />
										</br></br>						
									
    									<button type="submit" class="button-view-concept" name="map_permanent_search_link">
      										<span>Add to Category</span>
    									</button>
    								</form>
    							
                            </li>
                             
                            <?php        
                        }
                        ?>
                    </ul>
                    	
                </div>
        </div>  <!-- resource-row -->
    </div><!-- end content of resource -->    

