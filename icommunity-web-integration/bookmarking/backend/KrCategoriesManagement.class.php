<?php
$kr_db_configuration_file = 'KrDbConfiguration.class.php';
include_once $kr_db_configuration_file;

	class KrCategoriesManagement {
	
	/*************.:: Database Credentials ::.*************/
		var $server;
		var $user;
		var $pwd;
		var $db;
                //isnt port needed?
                
                function __construct($configuration){
                    $this->server = $configuration->get_host();
                    $this->user   = $configuration->get_user();
                    $this->pwd    = $configuration->get_password();
                    $this->db     = $configuration->get_db();
                }
		
		/** Connect to a MySQL database to be able to use the methods below.
		*/
		function getConnection(){
			$db = mysql_connect($this->server, $this->user, $this->pwd) or die ('Server connection not possible, Try later');
			mysql_select_db($this->db, $db) or die ('Database connection not possible, Try later');
			/*var_dump($conn);	 */	
		}
		
	/*********** User Authentication **************/
	function getAuthentication($username,$password){
			$id = -1;
			//$auth=False;
			$sql='SELECT * FROM users WHERE UserPseudo="'.$username.'" and UserPwd="'.$password.'"';
			$res=mysql_query($sql) or die(mysql_error());
			if(mysql_num_rows($res) != 0){
				$data = mysql_fetch_object($res);
				$id = $data->id;
			}			
			
			return $id;
	
	}
	
	/*********** End of Authentication ************/
	/*************.:: End of Database Credentials ::.*************/
		
	/*************.:: CRUD methods on Categories Table ::.*************/
				
		/** Create a new Category.
			* @param $title The title.
			* @param $description The description.
			* @param $thumb The thumbnail image for the category.
			* @return The result of the method, a success message to be displayed using echo or print.
		*/
		function createCategory($title, $description, $thumb){
			$success_message='';
			//$sql='INSERT INTO categories_beta (CategoryId,CategoryTitle,CategoryDescription,CategoryThumb) VALUES ("","'.mysql_real_escape_string($title).'","'.mysql_real_escape_string($description).'","'.$thumb.'")';
			$sql='INSERT INTO categories (CategoryId,CategoryTitle,CategoryDescription,CategoryThumb) VALUES ("","'.$title.'","'.$description.'","'.$thumb.'")';
			$req=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			if ($req!=false) {
				$success_message = 'You created a new category with title: \''. $title.'\''; ;
			}
			else{
				$success_message = 'Error: can\'t save new category. Please retry. '; 
			}
			return $success_message;
		}
                
                /** Create a new Category.
			* @param $title The title.
			* @param $description The description.
			* @param $thumb The thumbnail image for the category.
                        * @param $user_id The user id of the category owner
			* @return The result of the method, a success message to be displayed using echo or print.
		*/
		function createUserCategory($title, $description, $thumb, $user_id){
			$success_message='';
			$sql='INSERT INTO categories (CategoryId,CategoryTitle,CategoryDescription,CategoryThumb, UserId) VALUES ("","'.$title.'","'.$description.'","'.$thumb.'","'. $user_id.'")';
			$req=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			if ($req!=false) {
				$success_message = 'You created a new category with title: \''. $title.'\''; ;
			}
			else{
				$success_message = 'Error: can\'t save new category. Please retry. '; 
			}
			return $success_message;
		}
		
		/** Get All user defined categories.
			* @return The result of the query, to use with fetchNextObject().
		*/
		function getAllCategories(){
			$sql='SELECT * FROM categories';
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
//			header('Content-type: image/jpg/png');
			return $res;
		}
                
                /** Get All user defined categories by user id.
			* @return The result of the query, to use with fetchNextObject().
		*/
		function getAllUserCategories($user_id){
			$sql='SELECT * FROM categories WHERE UserId = '.$user_id;
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
//			header('Content-type: image/jpg/png');
			return $res;
		}
		
		/** Get an Array of All user defined categories.
			* @return The result of the query, as an array of category Ids.
		*/
		function getArrayCategories(){
			$arr = array();
			$sql='SELECT CategoryId FROM categories';
			$res=mysql_query($sql) or die ($sql." ==> ".mysql_error());
			while($data = mysql_fetch_object($res)){
				array_push($arr, $data->CategoryId);
			}
			return $arr;
		}
		
		function getArrayCategoriesById($userid){
                        $arr = array();
                        $sql='SELECT /*CategoryId,CategoryTitle*/ * FROM categories where UserId = '.$userid;
                        $res=mysql_query($sql) or die ($sql." ==> ".mysql_error());
                        while($data = mysql_fetch_object($res)){
                                array_push($arr, $data->CategoryId);
                                array_push($arr, $data->CategoryTitle);
                        }
                        return $arr;
                }

		/** Get A catgory by a specified Id.
			* @param $categoryId The Id of the category to be retrieved.
			* @return The result of the query, to use with fetchNextObject().
		*/
		function getCategory($categoryId){
			$sql='SELECT * FROM categories WHERE CategoryId=\''.$categoryId.'\'';
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			return $res;
		}		
		
		/** Update a Category.
			* @param $id The ID.
			* @param $title The title.
			* @param $description The description.
			* @return The result of the method, a success message to be displayed using echo or print.
		*/
		function updateCategory($id,$title,$description){
			$sql='UPDATE categories set CategoryTitle=\''.$title.'\', CategoryDescription= \''.$description.'\' WHERE CategoryId=\''.$id.'\'';
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			if ($res!=false) {
				$success_message = 'Category updated sccessfuly.';
			}
			else{
				$success_message = 'Error: can\'t save changes. Please retry. '; 
			}
			return $success_message;
			
		}
                
         /** Update a Category.
			* @param $id The ID.
			* @param $title The title.
			* @param $description The description.
                        * @param $user_id the user_id
			* @return The result of the method, a success message to be displayed using echo or print.
		*/
		function updateUserCategory($id,$title,$description, $user_id){
			$sql='UPDATE categories set CategoryTitle=\''.$title.'\', CategoryDescription= \''.$description.'\', UserId= \''. $user_id .'\' WHERE CategoryId=\''.$id.'\'';
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			if ($res!=false) {
				$success_message = 'Category updated sccessfuly.';
			}
			else{
				$success_message = 'Error: can\'t save changes. Please retry. '; 
			}
			return $success_message;
			
		}
		
		/** Delete a Category.
			* @param $id The ID.
			* @return The result of the method, a success message to be displayed using echo or print.
		*/
		function deleteCategory($id){
			$message='';
			$sql='DELETE FROM categories WHERE CategoryID=\''.mysql_real_escape_string($id).'\'';
			$req=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			if($req!=false){
				$message = 'Category and all related mappings are successfuly deleted';
			}
			else{
				$message = 'Deletion Failed. Try Again';
			}
			return $message;
		}
		
		function getUserCategoriesJSON($userId){
                        $cat_json;
                        $sql='SELECT CategoryId,CategoryTitle FROM categories WHERE UserId=\''.$userId.'\'';
                        $res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
                        $categories = array();
                        //$categories = array('userId'=>$userId);
                        while($r = mysql_fetch_assoc($res)) {
                        $categories[] = array('category'=>$r);
                        }
                        header('Content-type: application/json');
                        $cat_json = json_encode(array('categories'=>$categories));
                        return $cat_json;
                }

		
	/*************.:: End of CRUD on Categories Table ::.*************/		
		
		
	/*************.:: CRUD methods on Resources Table ::.*************/
		
		/** Add a resource to the Resources Table.
			* @param $id The ID.
			* @param $title The title.
			* @param $url The Url.
			* @param $description The description.
			* @return The result of the method, a success message to be displayed using echo or print.
		*/
		function addResource($title,$url,$description) {
			$sql='INSERT INTO resources (ResourceTitle, ResourceUrl, ResourceDescription) VALUES ("'.$title.'","'.$url.'","'.$description.'")';
			$res = mysql_query($sql) or die ('Error '.$sql.' '.mysql_error());
                        $id = -1;
			if ($res != false) {
                                $id = mysql_insert_id();                                
			}			
			return $id;
		}
		
		/** Determine if a resource already exists in the Resource Table.
			* @param $resourceId The ID of the resource to be verified.
			* @return The result of the method, a Boolean set to True if the resource exists.
		*/
		/*function isResourceRedundant($resourceId){
			$red=False;
			$sql='SELECT * FROM resources WHERE ResourceId='.$resourceId;
			$res=mysql_query($sql) or die(mysql_error());
			if(mysql_num_rows($res) != 0){
				$red = True;
			}
			return $red;
		}*/
		
		function isResourceRedundant($resourceUrl){
			$red=False;
			$sql='SELECT * FROM resources WHERE ResourceUrl="'.$resourceUrl.'"';
			$res=mysql_query($sql) or die(mysql_error());
			if(mysql_num_rows($res) != 0){
				$red = True;
			}
			return $red;
		}
		
		function getResourceById($resourceId) {
			$sql='SELECT * FROM resources WHERE ResourceId=\''.$resourceId.'\'';
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			return $res;
		}
                /*
		function getResourceIdByUrl($resourceUrl){
			$sql='SELECT ResourceId FROM resources WHERE ResourceUrl=\''.$resourceUrl.'\'';
			$res=mysql_query($sql) or die ('ERROR '.$sql.' '.mysql_error());
			$data = mysql_fetch_object($res);
			$id = $data->ResourceId;
			
			return $id;
		}                 
                 */
		
		/** Get All resources mapped to a specified category.
			* @param $categoryId The Id of the category.
			* @return The result of the query, to use with fetchNextObject().
		*/
		function getResourcesByCategory($categoryId){
			$sql = "SELECT * FROM resources WHERE ResourceId= ANY(SELECT ResourceId FROM mapping WHERE CategoryId=".$categoryId." ORDER BY Timestamp ASC)";
			//$sql="select distinct resources_beta.ResourceId,resources_beta.ResourceTitle,resources_beta.ResourceUrl,resources_beta.ResourceDescription from resources_beta,mapping_beta where resources_beta.ResourceId=any(select ResourceId from mapping_beta where CategoryId=".$categoryId.") ORDER BY mapping_beta.Timestamp desc");
			$res=mysql_query($sql) or die(mysql_error());
			return $res;
		
		}
		
		function getArraySortedResourcesByCategory($categoryId){
			$arr = array();
			$sql="SELECT distinct resources.ResourceId FROM resources,mapping WHERE resources.ResourceId= ANY(SELECT distinct mapping.ResourceId FROM mapping WHERE mapping.CategoryId=".$categoryId.")order by mapping.Timestamp desc";
			$res=mysql_query($sql) or die ($sql." ==> ".mysql_error());
			while($data = mysql_fetch_object($res)){
				array_push($arr, $data->ResourceId);
			}
			return $arr;
		}
		
		function getArrayResourceIdSortedByCategory($categoryId){
			$arr = array();
			$sql="select ResourceId from mapping where CategoryId=".$categoryId." order by Timestamp desc";
			$res=mysql_query($sql) or die ($sql." ==> ".mysql_error());
			while($data = mysql_fetch_object($res)){
				array_push($arr, $data->ResourceId);
			}
			return $arr;
		}
		
		/** Get a specified Resource and encode it to json format to be consumed by the user_feedback/view.php module.
			* @param $resourceId the Id.
		*/
		function getResourceForEncoding($resourceId){
			$sql = $sql = "SELECT * FROM resources WHERE ResourceId=".$resourceId;
			$res=mysql_query($sql) or die(mysql_error());
			/*$rows = array();
			while($r = mysql_fetch_assoc($res)) {
				$rows[] = $r;
			}
			return $rows;
			$sql = mysql_query("SELECT * FROM item_details WHERE posting_id='$item_number'");*/
			$results = array();
			while($row = mysql_fetch_array($res))
			{	
				$results[] = array(
				'id' => $row["ResourceId"],
				'title' => $row["ResourceTitle"],
				'url' => $row["ResourceUrl"],
				'description' => $row["ResourceDescription"],
				);
			}
			$json = json_encode($results);
		}		
		
		function getLastResourceId(){
			$res;
			$sql = "SELECT ResourceId from resources Order By ResourceId DESC Limit 1";
			$res = mysql_query($sql) or die ('Error '.$sql.' '.mysql_error());
			
			return $res;
		}
	/*************.:: End of CRUD methods on Resources Table ::.*************/
		
	/*************.:: CRUD methods on Mapping(Category-Resource) Table ::.*************/
		function addMapping($categoryId, $resourceId){
			$message = '';
			$sql = 'INSERT INTO mapping (CategoryId, ResourceId) VALUES ("'.$categoryId.'","'.$resourceId.'")';
			$res = mysql_query($sql) or die('Error '.$sql.' '.mysql_error());
			if($res!=false){
				$message = 'Mapping added.';
			}
			else{
				$message = 'Failed. Try Again';
			}
			return $message;
		}
		
		function isMappingRedundant($categoryId,$resourceId){
			$red=False;
			$sql='SELECT * FROM mapping WHERE CategoryId='.$categoryId.' AND ResourceId='.$resourceId;
			$res=mysql_query($sql) or die(mysql_error());
			if(mysql_num_rows($res) != 0){
				$red = True;
			}
			return $red;
		}
		
		function deleteMapping($categoryId,$resourceId){
			$message = '';
			$sql='DELETE FROM mapping WHERE CategoryId='.$categoryId.' AND ResourceId='.$resourceId;
			$res=mysql_query($sql) or die ($sql.' ==> '.mysql_error());
			if($res!=false){
				$message = 'You successfuly deleted the mapping.';//<i>'..' </i>from the category <i>'..' </i>';
			}
			else{
				$message = 'Failed. Try Again';
			}
			return $message;
		}		
		
		function getMappedCategories($resourceId){
			$mappedCategories=array();
			$sql="SELECT * from mapping WHERE ResourceId=".$resourceId;
			$req=mysql_query($sql) or die($sql." ==> ".mysql_error());
			while($data = mysql_fetch_array($req,MYSQL_ASSOC)){
				array_push($mappedCategories, $data["CategoryId"]);
			}
			return $mappedCategories;
		}
		
		function getMappedResources(){
			$arr = array();
			$sql='SELECT DISTINCT ResourceId FROM mapping';
			$res=mysql_query($sql) or die ($sql." ==> ".mysql_error());
			while($data = mysql_fetch_object($res)){
				array_push($arr, $data->ResourceId);
			}
			return $arr;
		}
		
		function purgeMappings($resourceId){
			$sql='DELETE FROM mapping WHERE ResourceId='.$resourceId;
			$res=mysql_query($sql) or die(mysql_error());
		}
	/*************.:: End of CRUD methods on Mapping(Category-Resource) Table ::.*************/
		
		/** Determines if a cateogy checkbox should be checked or not.
			* @param $catId is the Id of a category of the complete list of categories
			* @param $checked is an array of element names that should be checked
		*/ 	
		function isChecked($catId,$checked){
			$isChecked = "";
				foreach ($checked as $ele) {
					if ($catId == $ele) { 
						$isChecked = "checked";
						//continue;
					}
				}
			
			return $isChecked;
		}
						
		function fetchNextObject($result = NULL)
		{
			if ($result == NULL)
				$result = $this->lastResult;

			if ($result == NULL || mysql_num_rows($result) < 1)
				return NULL;
			else
				return mysql_fetch_object($result);
		}
		
		 /** Get the number of rows of a query.
			* @param $result The ressource returned by query(). If NULL, the last result returned by query() will be used.
			* @return The number of rows of the query (0 or more).
		*/
		function numRows($result = NULL)
		{
			if ($result == NULL)
			return mysql_num_rows($this->lastResult);
			else
			return mysql_num_rows($result);
		}
		
		function close()
		{
			mysql_close();
		}
		
	}
?>
