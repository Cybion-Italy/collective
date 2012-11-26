<?php
include_once 'KnowledgeTemplateInterface.php';
include_once 'KrModuleNames.class.php';

$krNames = new KrModuleNames();
$directory_name = dirname(__FILE__).'/';
$modulesDirectoryName = $krNames->get_modules_directory();
include_once $directory_name.$modulesDirectoryName.'/'
        .$krNames->get_user_feedback().'/'.'KrProject.class.php';

class KnowledgeTemplateCybion implements KnowledgeTemplateInterface
{
	function get_user_id() {
		return 21;
	}

	function get_user_name() {
		return "Test User";
	}
              
        function get_projects_by_user_id($user_id) {
            $project_one = new KrProject(18, 'first project');
            $project_two = new KrProject(22, 'second project');
            $projects = array();
            $projects[0] = $project_one;
            $projects[1] = $project_two;
            return $projects;            
        }	
}

?>