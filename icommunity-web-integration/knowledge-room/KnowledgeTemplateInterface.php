<?php
interface KnowledgeTemplateInterface {
	
	//returns the id of the user who is accessing the knowledge room
	function get_user_id();

	//returns the user name who is accessing the knowledge room
	function get_user_name();
	
	//returns the projects of the user who is accessing the knowledge room
	function get_projects_by_user_id($user_id);
        
}

?>
