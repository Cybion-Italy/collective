<?php

//TODO remove this since it is not used
   $kr_categories_management_file = 'KrCategoriesManagement.class.php';
   $kr_db_configuration_file = 'KrDbConfiguration.class.php';        
   
   include_once $kr_categories_management_file;           
   include_once $kr_db_configuration_file;
   
   $configuration = new KrDbConfiguration();   
   $db = new KrCategoriesManagement($configuration);
   $db->getConnection();
   /* TODO this file is never called, all is done in authentication.php */
   
   $categories = array();
   $obj = $db->getAllCategories();
   while ($line = $db->fetchNextObject($obj)) {
   	//echo $line->CategoryTitle.'</br>';
   	$category[]=array('CategoryId'=>$line->CategoryId,'CategoryTitle'=>$line->CategoryTitle);   	
   }
   $db->close();
   $categories[] = array($category);
      
  /* output in necessary format */
    header('Content-type: application/json');
    echo json_encode(array('categories'=>$category));
    
?>


