<?php

include_once 'KrPermanentSearchWSClient.class.php';
include_once 'KrPermanentSearch.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';

$permanent_search_id;
if (isset($_POST['permanent_search_id'])) {
	$permanent_search_id = $_POST['permanent_search_id'];
}

$configuration = new KrEndpointConfiguration();

$kr_permanent_search_client = new KrPermanentSearchWSClient($configuration);
$response = $kr_permanent_search_client
                            ->delete_permanent_search($permanent_search_id);

//var_dump($response);

$permanent_search_link = $moduleNames->get_knowledge_room_name();
$success_message = '';
$link_text = 'Return to the list of permanent searches';

if ($response['status'] == 'OK') {
    $success_message = 'Permanent search successfully deleted. ';    
} else {
   $success_message = 'Error: can\'t save permanent search. Please retry. ';     
}

?>

<div class="kr-section-name">
    Permanent Search
</div>

<div class ="kr-section-action">
    Delete Permanent Search
</div>

<p><?php echo $success_message; ?></p>
