<?php
include_once 'KrModuleNames.class.php';

global $knowledge_template;
$user_id = $knowledge_template->get_user_id();
//echo 'this room is owned by user: '.$user_id.'<br/>';
				
/* global reference to constant modules names */

global $module;
global $modules_directory;
global $dirname;
global $moduleNames;

$moduleNames = new KrModuleNames();

/* used to set the default page, to prevent having to click and change to the
 * development section we are coding */
//$defaultModule = $moduleNames->get_user_feedback();
//$defaultModule = $moduleNames->get_permanent_search();
$defaultModule = $moduleNames->get_resources_management();
$defaultAction = 'list';

//TODO refactor to const
$modules_directory = $moduleNames->get_modules_directory();
$dirname = dirname(__FILE__).'/';

/* 
 * N.B.: This array contains the actions associated with each module. 
 * If the action IS NOT HERE, it's not possible to access the page. 
 */
$moduleActions = array(
    $moduleNames->get_user_feedback() => array('view', 'list', 'feedback', 
                            'give_negative_feedback', 'give_positive_feedback', 
                            'add_mapping'),
    $moduleNames->get_permanent_search() => array('list', 'permanent_search_form', 
                            'create_permanent_search', 'view', 'delete', 
                            'delete_permanent_search'), 
    $moduleNames->get_custom_concepts() => array('list', 'custom_concept_form', 'view', 
                            'create_custom_concept', 'delete', 
                            'delete_custom_concept'), 
    //add here the actions needed for the categories and resources management
    $moduleNames->get_resources_management() => array('list', 
                                                      'create_category_form', 
                                                      'create_category', 'view', 
                                                      'see_all','delete_cat',
                                                      'delete_res_cat')
    );

if (!isset($_POST['module'])) {
	$module = $defaultModule;
} else {
	$module = $_POST['module'];
}

if (!isset($_POST['action'])) {
	$action = $defaultAction;
} else {
	$action = $_POST['action'];
}		

$actions = $moduleActions[$module];

if (!in_array($action, $actions)) {
    $action = $defaultAction;
}

/* it's better to use exact jquery version found on dev and include it 
 * rather than relying on external files with different jquery versions */

$template_url = get_bloginfo("template_url");
$stylesheet_directory =  get_stylesheet_directory_uri();
//var_dump($stylesheet_directory);
$kr_js_dir = 'kr-js';
$kr_css_dir = 'kr-css';
$jquery_filename = 'jquery.min.js';
global $common_javascript_directory;
global $common_stylesheet_directory;

$common_stylesheet_directory = $stylesheet_directory.'/'. $moduleNames->get_knowledge_room_directory() 
        .'/'.$modules_directory.'/'.$kr_css_dir.'/';

$common_javascript_directory = $stylesheet_directory.'/'. $moduleNames->get_knowledge_room_directory() 
        .'/'.$modules_directory.'/'.$kr_js_dir.'/';

/* keep in mind that with these paths, it WON'T work on cibionte. 
 * The reason seems to be that we are not inside a knowledge room plugin, 
 * while in production we do. 
 */
?> 

<script type='text/javascript' src='<?php 
echo $stylesheet_directory.'/'. $moduleNames->get_knowledge_room_directory() 
        .'/'.$modules_directory.'/'.$kr_js_dir.'/'.$jquery_filename; 
?>'></script>

<link rel="stylesheet" href="<?php 
echo $stylesheet_directory.'/'. $moduleNames->get_knowledge_room_directory() 
        .'/'.$modules_directory.'/'.$kr_css_dir; ?>/kr-css-new.css">

<?php

//echo 'including '.$module.' / '.$action.'<br/>';
//echo 'kr-modules/'.$module.'/'.$action.'.php'.'<br/>';

//echo $dirname.$modules_directory.'/'.$module.'/'.$action.'.php<br/>';

//TODO include a menu to other knowledge room sections

?>
<div id="content">        
    <?php
    include_once $dirname.$modules_directory.'/kr-menu.php';
    ?>    
    
    <?php
    include_once $dirname.$modules_directory.'/'.$module.'/'.$action.'.php';
    ?>
</div>

