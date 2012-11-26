<?php

include_once 'KrRecommendation.class.php';
include_once 'KrFormHelper.class.php';
include_once 'KrReason.class.php';

//TODO set error level in order to not bother during local development
//include_once 'Project.class.php';

include_once $dirname.$modules_directory.'/'.'KrRestClientExtended.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';

include_once $dirname.$modules_directory.'/'.$moduleNames->get_resources_management().'/'.'KrEndpointConfiguration.class.php';

$source = '';
$destination_module;
$destination_action = '';

if (isset($_POST['recommendation_object'])) {	    
	$recommendation_json = $_POST['recommendation_object']; 
	$source = $_POST['source'];	
	$category_id = $_POST['category_id'];
	$rec_decoded   = html_entity_decode($recommendation_json, ENT_QUOTES);
	$rec_stripped  = stripslashes($rec_decoded);        
//        echo "rec_stripped<br/>";
//        var_dump($rec_stripped);
//        echo "<br/>";
	$rec_from_json = json_decode($rec_stripped, true);
//        echo "json_decoded<br/>";
//        var_dump($rec_from_json);
//        echo 'Last error: '. json_last_error();
	
	$res_id    = $rec_from_json['id'];
	$res_title = $rec_from_json['title'];
        $res_url   = $rec_from_json['url'];
        $res_description = $rec_from_json['description'];
}

if (isset($_POST['map_permanent_search_link'])) {	
	$res_id = $_POST['resource_id'];       
	$res_title = $_POST['resource_title'];
	$res_url = $_POST['resource_url'];
	$res_description = '';//$_POST['resource_id'];
	$permanent_search_id = $_POST['permanent_search_id'];
	$source = $_POST['source'];
}
                                   
$actual_rec = new KrRecommendation($res_id,$res_title,$res_url,$res_description);

//echo 'actual rec<br/>';
//var_dump($actual_rec);
//echo '<br/>';


$configuration = new KrEndpointConfiguration();
$host = $configuration->get_host();
$port = $configuration->get_port();

/* TODO remove */
//get reasons
/*
$get_reasons_url = "recommendations-services/rest/user-feedback-test/reason/list";
$headers = array('Accept: application/json');
$json_reasons = KrRestClientExtended::connect($host, $port)
					->setHeaders($headers)
					->get($get_reasons_url)
                                        ->run();

$reasons = json_decode($json_reasons[0], true);
*/

//build POSITIVE feedback form

$projects = $knowledge_template->get_projects_by_user_id($user_id);

$form_name = 'give_positive_feedback';
$form_helper = new KrFormHelper($form_name);
$form_helper->set_action($moduleNames->get_knowledge_room_name());
$form_helper->set_method('POST');
$form_helper->add_input('hidden', 'module', $moduleNames->get_user_feedback());
$form_helper->add_input('hidden', 'action', 'give_positive_feedback');
$form_helper->add_encoded_input('hidden', 'recommendation_obj', $actual_rec);
$form_helper->add_input('submit', 'submitName', 'I like it');

$project_select = 'project_id';
$form_helper->add_select($project_select);

// adds another item to represent the user itself, with projectId = 0
$form_helper->add_option_value($project_select, 0, false, 'Myself');

for($i = 0; $i < count($projects); $i++) {
    $project_as_array = get_object_vars($projects[$i]);
    $form_helper->add_option_value($project_select, 
                                   $project_as_array['id'], false, 
                                   $project_as_array['name']);	
}

?>

<div class="kr-section-name">
    Resource Recommendations
</div>

<div class ="kr-section-description">
    Recommendation details
</div>
<!--<?php var_dump($recommendation_json);?>-->
<div id="resource-container">
    <div id="resource-row">
    <div class="label_new">Title</div>
        <div class="p_new">
            <a class="a_link" href="<?php echo $actual_rec->get_url(); ?>" target="_blank">
                <?php 
					$title = $actual_rec->get_title();
					echo $title; 
				?>
            </a>
        </div>
    </div> <!-- resource-row -->

    <div id="resource-row">    
        <div class="label_new">Summary</div>   
            <div class="p_new">
                <?php 
					$content = $actual_rec->get_description();
                                        $max_chars = 700;
					$summary = substr ($content ,0,$max_chars) . " [...]";
					echo $summary;
				?>
            </div>
    </div>  <!-- resource-row -->

</div><!-- end content of resource -->

<!-- Mapping the resource to categories -->
<?php
	//include KrCategoriesManagement class that contains connections and CRUD methods to the Categories DB        
	include_once $dirname . $modules_directory . '/' . $moduleNames->get_resources_management() .'/'. 'KrCategoriesManagement.class.php';
        include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';
        
        $configuration = new KrDbConfiguration();
        //create a new category management object
        $db = new KrCategoriesManagement($configuration);        
	$db->getConnection();
	$obj = $db->getAllUserCategories($user_id);
        
	$arr_mapped_cat = array();
	$res_id = $actual_rec->get_id();
	$arr_mapped_cat = $db->getMappedCategories($res_id);
?>
<div class="label_new">Add This Resource to one or multiple Categories </div>

<div id="resource-container">
<form name="mapping_resources_link" action="" method="POST" id="mapping_resources_link">
   <input type="hidden" name="module" value="<?php echo $moduleNames->get_user_feedback(); ?>"/>
   <input type="hidden" name="action" value="add_mapping"/> 	
	<input type="hidden" name="resource_id" value="<?php echo $actual_rec->get_id(); ?>"/>
	<input type="hidden" name="resource_title" value="<?php echo $title; ?>"/>
	<input type="hidden" name="resource_url" value="<?php echo $actual_rec->get_url(); ?>"/>
	<input type="hidden" name="resource_description" value="<?php echo $content; ?>"/>
	<?php
		
		while ($line = $db->fetchNextObject($obj)){ 
			
	?>
		<div class="p_new"><input type="checkbox" name="category_id[]" value="<?php echo $line->CategoryId;?>" 
		<?php foreach($arr_mapped_cat as $cat){ if(($line->CategoryId)==$cat) echo "checked";}?> />
		<?php echo $line->CategoryTitle; ?></div>
	<?php 
		}
	?>
	</br>
	<!--<input type="submit" name="save" value="Save" />--><div class="kr-button-container">
	<button type="submit" class="button-view-concept" name="save">Save</button>
</form>
<?php
	if($source=="from_category"){
		$destination_module = $moduleNames->get_resources_management();
		$destination_action = "see_all";
		}
	else if($source=="from_recommendations"){
		$destination_module = $moduleNames->get_user_feedback();
		$destination_action = "list";
		}
	else {
		$destination_module = $moduleNames->get_permanent_search();
		$destination_action = "view";
	}
?>
<form name="user_feedback_link" action="<?php echo $moduleNames->get_knowledge_room_name(); ?>" method="POST">
        <input type="hidden" name="module" value="<?php echo $destination_module; ?>"/>
        <input type="hidden" name="action" value="<?php echo $destination_action; ?>"/> 
        <input type="hidden" name="category_id_all" value="<?php echo $category_id; ?>"/>
        <input type="hidden" name="permanent_search_id" value="<?php echo $permanent_search_id; ?>"/>
        <div>
			<button type="submit" class="button-view-concept"><span>Back</span></button>
        </div>
</form>

 </div>
</div>
<!-- Mapping section end -->
<!--
<div id="content"
     class="kr-content">   
<h4>Do you like this resource? <br/>
    Use the form below and let us know!</h4>
</div>
-->
<!-- feedback forms -->

<div id ="left-feedback-form">        
    <?php
//print positive feedback form
//echo $form_helper->print_action_method();
//echo $form_helper->print_select_section();
?>
</div>

<?php
//build NEGATIVE feedback form
$negative_feedback_label = 'Unuseful';

$form_name = 'give_negative_feedback';
$form_helper = new KrFormHelper($form_name);
$form_helper->set_action($moduleNames->get_knowledge_room_name());
$form_helper->set_method('POST');
$form_helper->add_input('hidden', 'module', $moduleNames->get_user_feedback());
$form_helper->add_input('hidden', 'action', 'give_negative_feedback');
$form_helper->add_encoded_input('hidden', 'recommendation_obj', $actual_rec);
$form_helper->add_input('submit', 'submitName', $negative_feedback_label);

$reason_select = 'reason_id';
$form_helper->add_select($reason_select);
foreach ($reasons as $key => $reason) {
	$form_helper->add_option_value($reason_select, $reason['id'], false, $reason['description']);	
}

//print negative feedback form
?>
<div id ="right-feedback-form">    
    <?php
//    echo $form_helper->print_action_method();
//    echo $form_helper->print_select_section();
    ?>
</div>

