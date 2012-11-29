<?php

$permanent_search_dir = $moduleNames->get_permanent_search();

include_once 'KrFormHelper.class.php';
include_once 'KrRecommendation.class.php';
include_once $dirname . $modules_directory . '/' . $moduleNames->get_resources_management() .'/'. 'KrCategoriesManagement.class.php';
include_once $dirname . $modules_directory . '/' . 'KrDbConfiguration.class.php';


include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';
include_once $dirname.$modules_directory.'/'.$permanent_search_dir.'/'.'KrPermanentSearchWSClient.class.php';

$configuration = new KrEndpointConfiguration();

$user_id = $knowledge_template->get_user_id();

//TODO remove, just for testing purposes
//$user_id = 21;

$amount = "10";

//get all recommendations from REST call

$configuration = new KrEndpointConfiguration();
$recommendations_rest_client = new KrPermanentSearchWSClient($configuration);

//TODO print them in a different section
$recommendations_list = $recommendations_rest_client
                ->get_all_recommendations($user_id, $amount);

$short_term_recommendations_list = $recommendations_rest_client
                ->get_short_term_recommendations($user_id, $amount);


//TODO REMOVE AFTER TESTING
//$user_id = 21;
//get user short term profile
echo "getting profile - short term for " . $user_id;
$short_profile = $recommendations_rest_client->get_short_profile($user_id);
var_dump($short_profile);

$long_content = "test content test content test content test content test content " . 
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content " .
                "test content test content test content test content test content";

//build fake recommendations for testing
$fake_recs = array();
$max_recs = 10;

for ($i = 1; $i <= $max_recs; $i++) {
    
    $fake_url = "http://fakeurl.com/".$id;    
    $fake_topics = array();    
    $fake_topics[]= "http://faketopic.com/".$id;
    $other_id = $id + 1;
    $fake_topics[]= "http://faketopic.com/".$other_id;
    
    $fake_rec['id'] = $i;
    $fake_rec['title'] = "this is a title of resource id=".$i;
    $fake_rec['url'] = $fake_url;
    $fake_rec['description'] = "a fake content of resource id=". $i . " " . $long_content;
    $fake_rec['topics'] = $fake_topics;
    
    $fake_recs[]= $fake_rec;
}

//echo "recommendations_list<br/>";
//var_dump($recommendations_list);

$recommendations_objects_list = array();

/* used for testing fake resources on cibionte, where recommender is not turned on */
foreach ($recommendations_list as $rec) {    
//foreach ($fake_recs as $rec) {    

    $quotes = array();
    $quotes []= "&quot;";
    $quotes []= "&amp;";
    $quotes []= "“";
    $quotes []= "”";
    $quotes []= "’";
    $quotes []= "–";
    $quotes []= "\"";
//    echo $rec['description']."<br/><br/>";
    $cleaned_title = str_replace($quotes ,'', $rec['title']);
    $cleaned_title = htmlentities($cleaned_title);
    
    $cleaned_description = str_replace($quotes ,'', $rec['description']);
    $cleaned_description = htmlentities($cleaned_description);
    
//    echo $cleaned_description."<br/><br/>";
    $recommendation = new KrRecommendation(
                            $rec['id'], 
                            $cleaned_title, 
                            $rec['url'], 
                            $cleaned_description,
                            $rec['topics']);
//    var_dump($recommendation);
//    echo '<br/>';
    $recommendations_objects_list[$recommendation->get_id()] = $recommendation;
}
/*
Here is the code to build an array recommendations_to_show_list[] that contains the recommendations not already mapped to one/multiple categories*/

//Initialize the array
$recommendations_to_show_list = array();

$configuration = new KrDbConfiguration();
//create a new category management object
$db = new KrCategoriesManagement($configuration);			
//Get the list of Id of alreday mapped Resources from the Database
$db->getConnection();
$recs_mapped_list = array();
$recs_mapped_list = $db->getMappedResources();

//Building the $recommendations_to_show_list array that will contain only not mapped resources
foreach($recommendations_objects_list as $rec){    
        if (!(in_array($rec->get_id(), $recs_mapped_list))){
                array_push($recommendations_to_show_list, $rec);                
        }
}
		

// filter recommendations array, removing the ones the user already gave feedback

/* TODO (high) use a query to the DB of mapped resources to categories */
/*
$existing_user_feedbacks = $recommendations_rest_client
                                                  ->get_user_feedbacks($user_id);
*/

//echo 'existing user feedbacks: <br/>';
//var_dump($existing_user_feedbacks);

/*
$already_written_resources_urls = array();
foreach ($existing_user_feedbacks as $fb) {
    $already_written_resources_urls[]= $fb['urlResource'];
}
 */
 
//remove from the recommendations the urls the user already gave feedback about
/*
foreach ($recommendations_objects_list as $key => $recommendation) {
        foreach ($already_written_resources_urls as $written_url) {
            if ($recommendation->get_url() == $written_url) {
                unset($recommendations_objects_list[$key]);
            }
    }
}
*/

$recList = array();

//TODO (high) configure button to view recommendation details and map to a category
$form_name = 'viewDetailsForm';
$form_helper = new KrFormHelper($form_name);
$form_helper->set_action($moduleNames->get_knowledge_room_name());
$form_helper->set_method('POST');
$form_helper->add_input('hidden', 'module', $moduleNames->get_user_feedback());
$form_helper->add_input('hidden', 'action', 'view');
$form_helper->add_input('hidden', 'source', 'from_recommendations');
/* TODO proper button name */
$form_helper->add_input('submit', 'submitName', 'View Details or Save Resource to Category');

?>

<div class="kr-section-name">
    Resource Recommendations
</div>

<div class ="kr-section-description">
    Recommendations based on your interests
</div>

<div class="p_new">In this page the system recommends you to read some of the following resources.</div>
<!-- <p>Please, read some of them and tell us if you like them. </p> -->
<div class="p_new">You can see their details and save them to some of your categories. </div>

<?php
//echo 'to show<br/>';
//var_dump($recommendations_to_show_list);
if (count(/* $recommendations_objects_list */$recommendations_to_show_list) == 0) {	
	?>
<p><!-- You already gave feedback for all the resources the system recommended you. <br/> -->
   Please come back later, since the system will update this page with new resources soon. </p>
        <?php
} ?>

<ul id="kr-resources-list" class="item-list">
    
<?php
foreach (/* $recommendations_objects_list */$recommendations_to_show_list as $recommendation_object) {
    ?>
    <li>
		<span class="text">
            <p>
                <div class="label_new"><?php echo $recommendation_object->get_title(); ?> </div>
                <?php //echo "descr: " . $recommendation_object->get_description(); ?>
                <?php                 
                    echo $form_helper->display_view_form('hidden', 
                            'recommendation_object', $recommendation_object); 
                    ?> 
            </p>
        </span>
    </li>
<?php
} 
?>
</ul>
