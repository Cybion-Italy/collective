<?php

$kr_categories_management_file = 'KrCategoriesManagement.class.php';
$kr_db_configuration_file = 'KrDbConfiguration.class.php';

include_once $kr_categories_management_file;
include_once $kr_db_configuration_file;

$configuration = new KrDbConfiguration();

$db = new KrCategoriesManagement($configuration);
$db->getConnection();

$title = $_GET['page_title'];
$url = $_GET['page_url'];
$cats = $_GET['cats'];

$categories = explode("and", $cats);
$d = '';
$message = "";
$resource_id = $db->addResource($title, $url, $d);

foreach ($categories as $category) {
    if ($category != null) {
        $db->addMapping($category, $resource_id);
    }
}

if ($resource_id != -1) {
    $message = "Ok!";
} else {
    $message = "Failed to map resource";
}

echo $message;
?>
