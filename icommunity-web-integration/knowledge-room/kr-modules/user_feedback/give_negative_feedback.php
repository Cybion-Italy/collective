<?php
include_once 'KrRecommendation.class.php';
include_once 'KrUserFeedback.class.php';
include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';
include_once $dirname.$modules_directory.'/'.'KrRestClientExtended.class.php';

if (isset($_POST['reason_id'])) {
	$reason_id = $_POST['reason_id'];
}

if (isset($_POST['recommendation_obj'])) {
	$recommendation_obj = $_POST['recommendation_obj'];
}

$rec_from_json = json_decode(stripslashes(htmlspecialchars_decode($recommendation_obj)), true);

$actual_rec = new KrRecommendation($rec_from_json['id'],
                                 $rec_from_json['title'],
                                 $rec_from_json['url'],
                                 $rec_from_json['description']);


$configuration = new KrEndpointConfiguration();
$host = $configuration->get_host();
$port = $configuration->get_port();

$post_feedback_for_user_url = "$host:$port/recommendations-services/rest/user-feedback-test/";

$user_id = $knowledge_template->get_user_id();
//TODO remove, just for testing purposes
//$user_id = 21;

//serialize in json a UserFeedback object
$user_feedback = new KrUserFeedback();
$user_feedback->set_user_id($user_id);
//not set, since this is negative feedback
//$user_feedback->set_project_id();
$user_feedback->set_url_resource($actual_rec->get_url());
//negative feedback
$user_feedback->set_like(false);
$user_feedback->set_reason_id($reason_id);

$json_user_feedback = json_encode($user_feedback);


$headers = array('Accept: application/json', 'Content-Type: application/json');
$out = KrRestClientExtended::connect($host, $port)
				->setHeaders($headers)
				->post('recommendations-services/rest/user-feedback-test/userfeedback', 
                                        $json_user_feedback)
                                ->run();

//TODO manage errors
//var_dump($out);
?>

<div class="kr-section-name">
    User Feedback
</div>

<div class ="kr-section-action">
    Recommendation feedback
</div>

<p>Thank you. <br/> You gave a positive feedback for the resource: </p>
<a href="<?php echo $actual_rec->get_url();?>"><?php echo $actual_rec->get_title();?></a>
