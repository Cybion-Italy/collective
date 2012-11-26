<?php
include_once 'KrPermanentSearchWSClient.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';
?>

<!-- <h4>Listing all the saved permanent searches for user: 
    <?php //echo $knowledge_template->get_user_id(); ?> </h4>
-->

<?php
//get all searches from REST call

$configuration = new KrEndpointConfiguration();
$permanentSearchRestClient = new KrPermanentSearchWSClient($configuration);
$permanent_search_list = $permanentSearchRestClient
                ->get_all_permanent_searches($knowledge_template->get_user_id());

//var_dump($permanent_search_list);
?>
<div class="kr-section-name">
    Permanent Search
</div>

<div class ="kr-section-description">
    List of permanent searches</br>
</div>

<div id="resource-container">
    <form id="create_permanent_search" 
          method=POST 
          action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    <input type="hidden" name="module" value="<?php echo $moduleNames->get_permanent_search(); ?>"/>
    <input type="hidden" name="action" value="permanent_search_form"/>
	</br></br>
    <button type="submit" value="Create permanent search" class="button-view-concept">
      <span>Create permanent search</span>
    </button>
    </form>
</div>
</br>

<table class="table" id="concepts" cellspacing="0" cellpadding="0">
	<tr class="rowa">
		<th class="col1 cell">Search Title</td>
		<th class="col2 cell">Actions</td>
	</tr>
<?php
foreach ($permanent_search_list as $key => $permanent_search) {     
    ?>
	<tr class="rowa">
		<td class="col1 cell"><?php echo $permanent_search_list[$key]['title'];?></td>
		<td class="col2 cell">
			<!-- view search details -->
             <form id="view_permanent_search_<?php echo $permanent_search_list[$key]['id']; ?>" 
                        method=POST 
                        action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
                    <input type="hidden" name="module" 
                        value="<?php echo $moduleNames->get_permanent_search(); ?>"/>
                    <input type="hidden" name="action" value="view"/>
                    <input type="hidden" name="permanent_search_id" 
                           value="<?php echo $permanent_search_list[$key]['id']; ?>"/>

                    <button type="submit" value="View" class="button-view-concept">
                        <span>View</span>
                    </button>            
             </form>

             <!-- delete search -->
            <form id="delete_permanent_search_<?php echo $permanent_search_list[$key]['id']; ?>" 
                        method=POST 
                        action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
                    <input type="hidden" name="module" 
                        value="<?php echo $moduleNames->get_permanent_search(); ?>"/>
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="permanent_search_id" 
                           value="<?php echo $permanent_search_list[$key]['id']; ?>"/>
                    <input type="hidden" name="permanent_search_title" 
                           value="<?php echo $permanent_search_list[$key]['title']; ?>"/>

                    <button type="submit" value="Delete" class="button-view-concept">
                        <span>Delete</span>
                    </button>          
            </form>	
		
		</td>
	</tr>
	
      
    <?php
}
?> 
  </table>     

