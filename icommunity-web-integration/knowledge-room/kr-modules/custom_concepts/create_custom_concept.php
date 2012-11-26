<?php

include_once 'KrCustomConceptsWSClient.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';


if (isset($_POST['label'])) {
    $label = $_POST['label'];
} else {    
    echo 'label not set<br/>';
}

if (isset($_POST['description'])) {
    $description = $_POST['description'];
} else {    
    echo 'description not set<br/>';
}

if (isset($_POST['keywords'])) {
    $keywords = $_POST['keywords'];
} else {    
    echo 'keywords not set<br/>';
}


/*
var_dump($label);
echo '<br/>';
var_dump($description);
echo '<br/>';
var_dump($keywords);
echo '<br/>';
var_dump($company);
echo '<br/>';
var_dump($owner);
echo '<br/>';
var_dump($user_id);
echo '<br/>';
*/
$owner   = $knowledge_template->get_user_id();

//set as the same
$name = $label;

/*
echo $owner;
echo '<br/>';

echo $name;
echo '<br/>';

echo $label;
echo '<br/>';

echo $user;
echo '<br/>';

echo $description;
echo '<br/>';

echo $keywords;
echo '<br/>';
*/

$user = $knowledge_template->get_user_id();

//TODO move to kr-main.php
$configuration = new KrEndpointConfiguration();
$custom_concepts_rest_client = new KrCustomConceptsWSClient($configuration);
$response = $custom_concepts_rest_client->create_custom_concept(
                                                null, $owner, $name, 
                                                $label, $user, $description, 
                                                $keywords);
/*
echo 'response<br/>';
print_r($response);
echo '<br/>';
 */

$permanent_search_link = $moduleNames->get_knowledge_room_name();
$success_message = '';

if ($response['status'] == 'OK') {
    $success_message = 'You created a new custom concept with label: \''. $label .'\'';    
} else {
   $success_message = 'Error: can\'t save custom concept. Please retry. ';     
}

?>
<div class="kr-section-name">
    Custom Concept
</div>

<div class ="kr-section-action">
    Custom Concept Creation
</div>

<p><?php echo $success_message; ?></p>
