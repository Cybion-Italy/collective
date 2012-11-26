<?php
include_once 'KrCustomConceptsWSClient.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';

$company;
$owner;
$name;
$label;

if (isset($_POST['company'])) {
	$company = $_POST['company'];
} else {
    echo 'error, company is not set in POST<br/>';
}
if (isset($_POST['owner'])) {
	$owner = $_POST['owner'];
} else {
    echo 'error, owner is not set in POST<br/>';
}
if (isset($_POST['name'])) {
	$name = $_POST['name'];
} else {
    echo 'error, name is not set in POST<br/>';
}
if (isset($_POST['label'])) {
	$label = $_POST['label'];
} else {
    echo 'error, label is not set in POST<br/>';
}

/*
echo $company.'<br/>';
echo $owner.'<br/>';
echo $name.'<br/>';
echo $label.'<br/>';
*/

$configuration = new KrEndpointConfiguration();
$custom_concepts_rest_client = new KrCustomConceptsWSClient($configuration);
$response = $custom_concepts_rest_client
                            ->delete_custom_concept($company, $owner, $name, $label);

//var_dump($response);

$permanent_search_link = $moduleNames->get_knowledge_room_name();
$success_message = '';
$link_text = 'Return to the list of custom concepts';

if ($response['status'] == 'OK') {
    $success_message = '<div class="p_new">Custom concept successfully deleted.</div>';    
} else {
   $success_message = '<div class="p_new">Error: can\'t delete custom concept. Please retry.</div>';     
}

?>

<div class="kr-section-name">
    Custom Concept
</div>

<div class ="kr-section-description">
    Delete Custom Concept
</div>

<p><?php echo $success_message; ?></p>
