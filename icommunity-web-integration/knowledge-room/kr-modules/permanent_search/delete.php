<?php

$permanent_search_id;
$permanent_search_title;
if (isset($_POST['permanent_search_id'])) {
	$permanent_search_id = $_POST['permanent_search_id'];
}

if (isset($_POST['permanent_search_title'])) {
	$permanent_search_title = $_POST['permanent_search_title'];
}

?>

<div class="kr-section-name">
    Permanent Search
</div>

<div class ="kr-section-description">
    Deleting permanent Search
</div>

<p>Deleting permanent search with title: <?php echo $permanent_search_title;?></p>

<p>Are you sure?</p>

 <!-- delete search -->
     <form id="delete_permanent_search" 
        method=POST 
        action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    <input type="hidden" name="module" 
        value="<?php echo $moduleNames->get_permanent_search(); ?>"/>
    <input type="hidden" name="action" value="delete_permanent_search"/>
    <input type="hidden" name="permanent_search_id" 
           value="<?php echo $permanent_search_id; ?>"/>
    
    <button type="submit" value="Delete permanent search" class="button-view-concept">
        <span>Delete permanent search</span>
    </button>    
    </form>
 