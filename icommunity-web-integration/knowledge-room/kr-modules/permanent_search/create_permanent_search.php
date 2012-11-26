<?php

include_once 'KrLabelledURI.class.php';
include_once 'KrPermanentSearchWSClient.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';
include_once $dirname.$modules_directory.'/'.'KrRestClientExtended.class.php';


if (isset($_POST['permanent_search_json'])) {
	$common_concepts_json = urldecode($_POST['permanent_search_json']);
} else {
    echo 'not working<br/>';
}

if (isset($_POST['permanent_search_user_id'])) {
    $permanent_search_user = $_POST['permanent_search_user_id'];
} else {    
    echo 'user id not set<br/>';
}

if (isset($_POST['permanent_search_name'])) {
    $permanent_search_name = $_POST['permanent_search_name'];
} else {    
    echo 'name not set';
}

//get custom concepts list
$custom_concepts_encoded = $_POST['custom_concepts'];
$custom_concepts = array();

if(is_array($custom_concepts_encoded)) {    
    foreach ($custom_concepts_encoded as $concept) {
        $concept_fields = explode("|", $concept);         
        $custom_concept = new KrLabelledURI($concept_fields[0], $concept_fields[1]);                
        $custom_concepts[]= $custom_concept;
    }
}

/* TODO get custom_concepts from POST and use it in REST call */

/* TODO manage errors in form fields and redirect to previous form 
with error message */

//var_dump ("common concepts: <br/>" . $common_concepts_json. "<br/>");

//decode common concepts
$decoded_common_concepts = json_decode($common_concepts_json, true);
$common_concepts = array();

//iterate on DBpedia autocompleted urls
foreach ($decoded_common_concepts as $common_concept) {
    //only for non-empty fields
    if (!empty($common_concept['url'])) {        
        $labelled_uri = new KrLabelledURI($common_concept['label'], $common_concept['url']);
        $common_concepts[] = $labelled_uri;  
    }    
}

/*
echo 'common concepts list: <br/>';
print_r($common_concepts);
echo '<br/>';

echo 'custom concepts list: <br/>';
print_r($custom_concepts);
echo '<br/>';
*/

//DO NOT merge with the rest of custom concepts
//$concepts = array_merge($concepts, $custom_concepts_objects);


$configuration = new KrEndpointConfiguration();
$kr_permanent_search_client = new KrPermanentSearchWSClient($configuration);

//TODO add custom_concepts parameter with the list
$response = "";


$response = $kr_permanent_search_client
                            ->create_permanent_search($permanent_search_user, 
                                                      $permanent_search_name, 
                                                      $common_concepts, 
                                                      $custom_concepts);

//var_dump($response);

//redirect to error/success/page

$permanent_search_link = $moduleNames->get_knowledge_room_name();
$success_message = '';
$link_text = 'Return to the list of permanent searches';

if ($response['status'] == 'OK') {
    $success_message = 'You created a new permanent search with title: \''. $permanent_search_name.'\'';    
} else {
   $success_message = 'Error: can\'t save permanent search. Please retry. ';     
}

?>
<div class="kr-section-name">
    Permanent Search
</div>

<div class ="kr-section-description">
    Permanent Search Creation
</div>

<p><?php echo $success_message; ?></p>
