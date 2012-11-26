<?php
include_once 'KrCustomConceptsWSClient.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';

//TODO move to kr-main.php
$configuration = new KrEndpointConfiguration();
$custom_concepts_rest_client = new KrCustomConceptsWSClient($configuration);
$custom_concepts_list = $custom_concepts_rest_client
                        ->get_custom_concepts($knowledge_template->get_user_id());

?>

<div class="kr-section-name">
    Custom Concepts
</div>

<div class ="kr-section-description">
    List of custom concepts
	</br>
</div>

<div id="resource-container">
    <form id="create_custom_concept" 
          method=POST 
          action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
        <input type="hidden" name="module" value="<?php echo $moduleNames->get_custom_concepts(); ?>"/>
        <input type="hidden" name="action" value="custom_concept_form"/>
		</br></br>
        <button type="submit" value="Create custom concept" class="button-view-concept">
          <span>Create custom concept</span>
        </button>
    </form>
</div>

<?php
if (count($custom_concepts_list) == 0) { ?>

    <p>You didn't define any Custom Concept yet. <br/>
       Use the button below and add the concepts you want to be matched in text. 
    </p>
<?php
}?>

<table class="table" id="concepts" cellspacing="0" cellpadding="0">
	<tr class="rowa">
		<th class="col1 cell">Label</td>
		<th class="col2 cell">Description</td>
		<th class="col3 cell">Actions</td>
	</tr>
<? 

foreach ($custom_concepts_list as $key => $custom_concept) {  
    ?>
    <tr class="rowa">
		<td class="col1 cell"><?php echo $custom_concept['label']; ?></td>
		<td class="col2 cell"><?php echo $custom_concept['description']; ?></td>
		<td class="col3 cell">
			<form id="view_custom_concept_<?php //echo $custom_concepts_list[$key]['']; ?>" 
                        method=POST 
                        action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
                    <input type="hidden" name="module" 
                        value="<?php echo $moduleNames->get_custom_concepts(); ?>"/>
                    <input type="hidden" name="action" value="view"/>
                    <!-- serialize in the form all the fields needed to view concept -->
                    <?php                     
                    $custom_concept_json = htmlentities(json_encode($custom_concept), ENT_QUOTES);                    
                    ?>
                    <input type="hidden" name="custom_concept_json" 
                           value="<?php echo $custom_concept_json; ?>"/>                    
                    
                    <button type="submit" value="View" class="button-view-concept">
                        <span>View</span>
                    </button>            
            </form> 
		</td>
	</tr>
<?php
}
?> 
</table>     

