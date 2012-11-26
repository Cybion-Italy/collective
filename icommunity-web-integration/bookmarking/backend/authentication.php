<?php

$kr_categories_management_file = 'KrCategoriesManagement.class.php';
$kr_db_configuration_file = 'KrDbConfiguration.class.php';

include_once $kr_categories_management_file;
include_once $kr_db_configuration_file;

$configuration = new KrDbConfiguration();

$db = new KrCategoriesManagement($configuration);
$db->getConnection();

//ini_set('display_errors', 1);        

$username = $_GET['username'];
$password = $_GET['password'];

$url = 'http://94.75.243.141/collective/login';

$fields = array(
    'username' => urlencode($username),
    'password' => urlencode($password)
);

//build POST parameters
$fields_string = "";
foreach ($fields as $key => $value) {
    $fields_string .= $key . '=' . $value . '&';
}
$fields_string = rtrim($fields_string, '&');

$ch = curl_init($url);
//set the number of POST vars, POST data
curl_setopt($ch, CURLOPT_POST, count($fields));
curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      

$result = curl_exec($ch);
//close connection
curl_close($ch);

//clean reply from whitespace
$user_id = trim($result);
//var_dump($result);
$categories = array();

//TODO this is NOT the place to return categories. this file should just authenticate users. 
$categories = $db->getArrayCategoriesById($user_id);
$a = array_push($categories, $user_id);
$comma_separated = implode(",", $categories);
echo $comma_separated;
?>
