<?php

/* show the user the concept's fields: 
 * label, description and keywords of concept */

$custom_concept;
if (isset($_POST['custom_concept_json'])) {
    $custom_concept = 
        json_decode(stripslashes(html_entity_decode($_POST['custom_concept_json'], ENT_QUOTES)), true);    
} else {
    echo 'error, custom concept not present in POST<br/>';
}
//print_r($custom_concept);
?>

<div id="resource-container">
        <div id="resource-row">
        <div class="label_new">Name</div>
            <div class="p_new">
                <?php echo $custom_concept['name']; ?>
            </div>
        </div> <!-- resource-row -->
        
        <div id="resource-row">
        <div class="label_new">Label</div>
            <div class="p_new">
                <?php echo $custom_concept['label']; ?>
            </div>
        </div> <!-- resource-row -->
        <div id="resource-row">
        <div class="label_new"><b>Description</b></div>
            <div class="p_new">
                <?php echo $custom_concept['description']; ?>
            </div>
        </div> <!-- resource-row -->
        <div id="resource-row">
        <div class="label_new">Keywords</div>
            <div class="p_new">
                <?php 
                $keywords_string = '';                                                
                foreach ($custom_concept['keywords'] as $key => $keyword) { 
                    $keywords_string .= $keyword . ', '; 
                }
                $keywords_string = substr($keywords_string, 0, -2);
                
                echo $keywords_string;
                ?>
            </div>
        </div> <!-- resource-row -->
        
        
        
        <div id="resource-row">
                <div class="label_new">Actions</div>
                <div id="resource-value">
                <!-- delete -->
                     <form id="delete_custom_concept_<?php //echo $permanent_search_list[$key]['id']; ?>" 
                        method=POST 
                        action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
                    <input type="hidden" name="module" 
                        value="<?php echo $moduleNames->get_custom_concepts(); ?>"/>
                    <input type="hidden" name="action" value="delete"/>

                    <?php                     
                    $custom_concept_json = htmlentities(json_encode($custom_concept), ENT_QUOTES);                    
                    ?>
                    <input type="hidden" name="custom_concept_json" 
                           value="<?php echo $custom_concept_json; ?>"/>                      
                    </br>
                    <button type="submit" value="Delete custom concept" class="button-view-concept">
                        <span>Delete custom concept</span>
                    </button>        
                    </form>
                </div>
        </div> <!-- resource-row -->
                    
</div>
