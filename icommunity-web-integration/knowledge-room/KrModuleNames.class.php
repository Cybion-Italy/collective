<?php
/* these constants represent the names (directories) that refer to modules */
class KrModuleNames {
	
		const KNOWLEDGE_ROOM_NAME      = 'knowledge';
                const KNOWLEDGE_ROOM_DIRECTORY = 'knowledge-room';
		const MODULES_DIRECTORY        = 'kr-modules';
                
		const USER_FEEDBACK            = 'user_feedback'; 
		const CUSTOM_CONCEPTS          = 'custom_concepts';
		const PERMANENT_SEARCH         = 'permanent_search';
                const RESOURCES_MANAGEMENT     = 'resources_management';
		
		function get_modules_directory () {
			return self::MODULES_DIRECTORY;
		}
		
		function get_knowledge_room_name() {
			return self::KNOWLEDGE_ROOM_NAME;
		}
                
                function get_knowledge_room_directory() {
                    return self::KNOWLEDGE_ROOM_DIRECTORY;
                }
		
		function get_user_feedback() {
	  	return self::USER_FEEDBACK;
		}		

		function get_custom_concepts() {
			return self::CUSTOM_CONCEPTS;
		}
		function get_permanent_search() {
			return self::PERMANENT_SEARCH;
		}
                function get_resources_management() {
                    return self::RESOURCES_MANAGEMENT;
                }
}

?>
