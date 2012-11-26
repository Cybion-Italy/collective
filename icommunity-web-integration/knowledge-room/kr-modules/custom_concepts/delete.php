<?php

$custom_concept;

if (isset($_POST['custom_concept_json'])) {
    $custom_concept = 
        json_decode(stripslashes(html_entity_decode($_POST['custom_concept_json'], ENT_QUOTES)), true);    
} else {
    echo 'error, custom concept not present in POST<br/>';
}

//print_r($custom_concept);

//echo '<br/>';
//echo 'owner: '.$custom_concept['owner'].'<br/>';
//echo 'url: '.$custom_concept['url'].'<br/>';

$custom_concept_url_encoded = htmlentities($custom_concept['url'], ENT_QUOTES);      

?>
<div class="kr-section-name">
    Custom Concept
</div>

<div class ="kr-section-description">
    Deleting Custom Concept
</div>

<div class="p_new">Deleting custom concept with label: <i><?php echo $custom_concept['name'];?></i></div>

<div class="p_new">Are you sure?</div>

 <!-- delete search -->
     <form id="delete_custom_concept" 
        method=POST 
        action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    <input type="hidden" name="module" 
        value="<?php echo $moduleNames->get_custom_concepts(); ?>"/>
    <input type="hidden" name="action" value="delete_custom_concept"/>
    
    <input type="hidden" name="company" 
           value="<?php echo $custom_concept['company']; ?>"/>    
    <input type="hidden" name="owner" 
           value="<?php echo $custom_concept['owner']; ?>"/>
    <input type="hidden" name="name" 
           value="<?php echo $custom_concept['name']; ?>"/>
    <input type="hidden" name="label" 
           value="<?php echo $custom_concept['label']; ?>"/>
        
    <button type="submit" value="Delete custom concept" class="button-view-concept">
        <span>Delete custom concept</span>
    </button>    
    </form>
 
