<?php

   $kr_categories_management_file = 'KrCategoriesManagement.class.php';
   $kr_db_configuration_file = 'KrDbConfiguration.class.php';        
   
   include_once $kr_categories_management_file;           
   include_once $kr_db_configuration_file;
   
   $configuration = new KrDbConfiguration();
   
   $db = new KrCategoriesManagement($configuration);
   $db->getConnection();
   
   $userid = $_GET['userid'];
   $response = $db->getArrayCategoriesById($userid);
   //TODO put correct headers building the right http response maybe? 
   echo $response;
?>
